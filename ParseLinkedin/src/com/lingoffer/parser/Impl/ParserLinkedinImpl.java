package com.lingoffer.parser.Impl;

import com.lingoffer.model.*;
import com.lingoffer.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class ParserLinkedinImpl implements Parser {
    private static final String firfoxPath = "./src/firefox/firefox.exe";

    private WebDriver driver;


    @Override
    public boolean loadWebDriverWithFireFox() {
        try {
            FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe"));
            FirefoxProfile profile = new FirefoxProfile();
            driver = new FirefoxDriver(binary, profile);
            //设置5秒,避免页面还没加载完导致获取元素失败
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().window().setSize(new Dimension(0, 0));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loadWebDriverWithkernel() {
        try {
            WebDriver driver = new PhantomJSDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Profile parserLinkedin(String linkedInUrl) {
        try {
            if (driver != null) {
                Profile profile = new Profile();
                Map<String, String> loginInfo = getConfigProperties();
                String loginEmail;
                String loginPassword;
                if (loginInfo.containsKey("login-email") && loginInfo.containsKey("login-password")) {
                    loginEmail = loginInfo.get("login-email");
                    loginPassword = loginInfo.get("login-password");
                } else {
                    return null;
                }
                driver.get("http://www.linkedin.com/");
                WebElement id = driver.findElement(By.id("login-email"));
                WebElement pass = driver.findElement(By.id("login-password"));
                id.sendKeys(loginEmail);
                pass.sendKeys(loginPassword);
                driver.findElement(By.xpath("//input[@id='login-submit'and@type='submit'and@class='login submit-button']")).click();
                //设置一个中转站点,防止被检测为爬虫
                driver.get("http://www.linkedin.com/feed/");
                driver.get(linkedInUrl);
                profile.setName(getName());
                profile.setFollowers(getFollowNum());
                profile.setWorkingExperiences(getWorkingExperiences());
                profile.setLinkedinURL(getLinkedinURL(linkedInUrl));
                profile.setLocation(getLocation());
                return profile;
            } else {
                System.out.println("web driver initialize error!");
                throw new NullPointerException();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Map<String, String> getConfigProperties() {
        Properties prop = new Properties();
        Map<String, String> propMap = new HashMap<>();
        try {
            prop.load(ParserLinkedinImpl.class.getClassLoader().getResourceAsStream("config.properties"));
            propMap.put("login-email", (String) prop.get("login-email"));
            propMap.put("login-password", (String) prop.get("login-password"));
            return propMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLinkedinURL(String linkedinURL) {
        return linkedinURL;
    }


    private List<Education> getEducations() {
        List<Education> educations = new ArrayList<>();
        List<WebElement> edu;
        edu = driver.findElements(By.xpath("//h3[@class='pv-entity__school-name Sans-17px-black-85%-semibold']"));
        for (WebElement e : edu) {
            Education education = new Education();
            education.setSchool(e.getText().toString());
            education.setDegree(null);
            education.setGrade(null);
            educations.add(education);
        }
        return educations;
    }

    private List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<>();
        List<WebElement> skillsEle = driver.findElements(By.xpath("//div[@id='pv-skill-entity-name-tooltip']"));
        List<WebElement> numEndorsements = driver.findElements(By.xpath("//a[@data-control-name='edit_endorsements']/span"));
        if (numEndorsements != null && skillsEle.size() == numEndorsements.size()) {
            for (int i = 0; i < skillsEle.size(); i++) {
                Skill skill = new Skill();
                String name = skillsEle.get(i).getText();
                skill.setName(name);
                int numEndorsement = Integer.getInteger(numEndorsements.get(i).getText());
                skill.setNumEndorsements(numEndorsement);
                skills.add(skill);
            }
        } else {
            for (int i = 0; i < skillsEle.size(); i++) {
                Skill skill = new Skill();
                String name = skillsEle.get(i).getText();
                skill.setName(name);
                skill.setNumEndorsements(0);
                skills.add(skill);
            }
        }
        return skills;
    }

    private String getName() {
        return driver.findElement(By.xpath("//h1[@class='pv-top-card-section__name Sans-26px-black-85%']")).getText().toString();
    }

    private String getPosition() {
        return driver.findElement(By.xpath("//h2[@class='pv-top-card-section__headline Sans-19px-black-85%']")).getText().toString();
    }

    private String getLocation() {
        return driver.findElement(By.xpath("//h3[@class='pv-top-card-section__location Sans-17px-black-70% mb1 inline-block']")).getText().toString();
    }

    private int getFollowNum() {
        return Integer.getInteger(driver.findElement(By.xpath("//span[@class='visually-hidden']")).getText().toString());
    }

    private List<Experience> getWorkingExperiences() {
        try {
            List<Experience> workingExperiences = new ArrayList<>();
            //List<Experience> internExperiences = new ArrayList<>();
            //List<Experience> fulltimeExperiences = new ArrayList<>();
            List<WebElement> companies = driver.findElements(By.xpath("//div/h4/span[@class='pv-entity__secondary-title']"));
            List<WebElement> titles = driver.findElements(By.xpath("//h3[@class='Sans-17px-black-85%-semibold']"));
            List<WebElement> descriptions = driver.findElements(By.xpath("//div/p[@class='pv-entity__description Sans-15px-black-70% mt4']"));
            List<WebElement> time = driver.findElements(By.xpath("//h4[@class='pv-entity__date-range inline-block Sans-15px-black-70%']/span[2]"));
            Pattern patternIntern = Pattern.compile("实习|intern", Pattern.CASE_INSENSITIVE);
            Pattern patternAsssistant = Pattern.compile("助理|assist", Pattern.CASE_INSENSITIVE);
            if (companies.size() != titles.size()) {
                System.out.println("职位和公司个数不符，请管理员调试");
            } else {
                for (int i = 0; i < companies.size(); i++) {
                    Experience e = new Experience();
                    String title = titles.get(i).getText().toString();
                    String company = companies.get(i).getText().toString();
                    String description = descriptions.get(i).getText().toString();
                    String t = time.get(i).getText();
                    //对于获取的一个时间段进行处理,
                    Date from = null;
                    StringTokenizer tokenizer = new StringTokenizer(t, "-");
                    if (tokenizer.hasMoreTokens()) {
                        String start = tokenizer.nextToken();
                        DateFormat format = new SimpleDateFormat("yyyy 年 MM 月 ");
                        from = format.parse(start);
                    }
                    Date to = null;
                    String end = tokenizer.nextToken();
                    if (end.equals("至今")) {
                        to = new Date();
                    } else {
                        DateFormat format = new SimpleDateFormat("yyyy 年 MM 月 ");
                        to = format.parse(end);
                    }
                    e.setCompany(company);
                    e.setDescription(description);
                    e.setFrom(from);
                    e.setTo(to);
                    e.setTitle(title);
                    Matcher m = patternIntern.matcher(title);
                    //如果是实习生
                    if (m.find()) {
                        WorkingType workingType = WorkingType.INTERNSHIP;
                        e.setWorkingType(workingType);
                    }
                    m = patternAsssistant.matcher(title);
                    if (m.find()) {
                        WorkingType workingType = WorkingType.ASSISTANTSHIP;
                        e.setWorkingType(workingType);
                    }
                    //说明前两种都不是，则默认为fulltime
                    if (e.getWorkingType() == null) {
                        WorkingType workingType = WorkingType.FULLTIME;
                        e.setWorkingType(workingType);
                    }
                    workingExperiences.add(e);
                }
            }
            return workingExperiences;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
