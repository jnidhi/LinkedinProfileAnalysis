package com.siyu.parser;

import org.apache.xerces.impl.xpath.regex.Match;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by siyu on 2017/7/5.
 */
public class Linkedinparser {
    public static void main(String[] args) {

        FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe"));
        FirefoxProfile profile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(binary, profile);
        //设置10秒
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://www.linkedin.com/");
        WebElement id = driver.findElement(By.id("login-email"));
        WebElement pass = driver.findElement(By.id("login-password"));
        id.sendKeys("********");
        pass.sendKeys("********");
        driver.findElement(By.xpath("//input[@id='login-submit'and@type='submit'and@class='login submit-button']")).click();

        Set cookies = driver.manage().getCookies();
        Iterator i = (Iterator) cookies.iterator();
    /*  while(i.hasNext()){
         System.out.println(i.next().toString()+"\n");
      }*/
        driver.get("http://www.linkedin.com/feed/");
        driver.get("https://www.linkedin.com/in/jeff-dean-8b212555/");
        //the name of the interviewer
        WebElement name = driver.findElement(By.xpath("//h1[@class='pv-top-card-section__name Sans-26px-black-85%']"));
        //the position of the interviewer
        WebElement position = driver.findElement(By.xpath("//h2[@class='pv-top-card-section__headline Sans-19px-black-85%']"));
        WebElement school = driver.findElement(By.xpath("//h3[@class='pv-top-card-section__school pv-top-card-section__school--with-separator Sans-17px-black-70% mb1 inline-block']"));
        WebElement location = driver.findElement(By.xpath("//h3[@class='pv-top-card-section__location Sans-17px-black-70% mb1 inline-block']"));
        WebElement followerNum = driver.findElement(By.xpath("//span[@class='visually-hidden']"));
        List<WebElement> edu = new ArrayList<>();
        List<WebElement> work = new ArrayList<>();
        List<WebElement> pos = new ArrayList<>();
        List<WebElement> skills = new ArrayList<>();
        work = driver.findElements(By.xpath("//div/h4/span[@class='pv-entity__secondary-title']"));
        edu = driver.findElements(By.xpath("//h3[@class='pv-entity__school-name Sans-17px-black-85%-semibold']"));
        pos = driver.findElements(By.xpath("//h3[@class='Sans-17px-black-85%-semibold']"));
        skills = driver.findElements(By.xpath("//span[@class='pv-skill-entity__skill-name truncate Sans-15px-black-85%-semibold inline-block ']"));

        JSONObject resultJson = convertToJson(name, position, school, location, followerNum, work, edu, pos, skills);
        Map<String, Object> resultMap = converToMap(name, position, school, location, followerNum, work, edu, pos, skills);
        List<String> s = new ArrayList<>();
        s.add("JavaEE");
        s.add("Android");
        s.add("Ios");
        s.add("Algorithms");
        float scores = calculateScores(resultMap, s);
        System.out.println(resultJson.toString());
        System.out.println("The scores of the profile=" + scores);
        driver.close();
    }

    public static Map<String, Object> converToMap(WebElement name, WebElement position, WebElement school, WebElement location
            , WebElement followerNum, List<WebElement> workExp, List<WebElement> eduExp, List<WebElement> positions, List<WebElement> skills) {
        Map<String, Object> parseMap = new HashMap<String, Object>();
        parseMap.put("Name", name.getText());
        parseMap.put("Position", position.getText());
        parseMap.put("School", school.getText());
        parseMap.put("Location", location.getText());
        parseMap.put("FollowerNum", followerNum.getText());
        List<String> workExpList = new ArrayList<>();
        List<String> posList = new ArrayList<>();
        List<String> eduExpList = new ArrayList<>();
        List<String> skillsList = new ArrayList<>();
        if (workExp != null) {
            for (int i = 0; i < workExp.size(); i++) {
                workExpList.add(workExp.get(i).getText());
                posList.add(positions.get(i).getText());
            }
        }
        if (eduExp != null) {
            for (WebElement e : eduExp) {
                eduExpList.add(e.getText());
            }
        }
        if (skills != null) {
            for (WebElement skill : skills) {
                skillsList.add(skill.getText());
            }
        }
        parseMap.put("Working Experience", workExpList);
        parseMap.put("Positions ", posList);
        parseMap.put("Education Experience", eduExpList);
        parseMap.put("Skills", skillsList);
        return parseMap;
    }


    public static JSONObject convertToJson(WebElement name, WebElement position, WebElement school, WebElement location
            , WebElement followerNum, List<WebElement> workExp, List<WebElement> eduExp, List<WebElement> positions, List<WebElement> skills) {
        JSONObject resultJson = new JSONObject();
        try {
            resultJson.put("Name", name.getText());
            resultJson.put("Position", position.getText());
            resultJson.put("School", school.getText());
            resultJson.put("Location", location.getText());
            resultJson.put("FollowerNum", followerNum.getText());
            JSONArray arrayWork = new JSONArray();
            JSONArray arrayPos = new JSONArray();

            if (workExp != null) {
                for (int i = 0; i < workExp.size(); i++) {
                    arrayWork.put(workExp.get(i).getText());
                    arrayPos.put(positions.get(i).getText());
                }
            }
            resultJson.put("Working Experience", arrayWork);
            resultJson.put("Positions ", arrayPos);
            JSONArray arrayEdu = new JSONArray();
            if (eduExp != null) {
                for (WebElement e : eduExp) {
                    arrayEdu.put(e.getText());
                }
            }
            resultJson.put("Education Experience", arrayEdu);
            JSONArray arraySkills = new JSONArray();
            if (skills != null) {
                for (WebElement skill : skills) {
                    arraySkills.put(skill.getText());
                }
            }
            resultJson.put("Skills", arraySkills);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("名字：" + name.getText());
        System.out.println("现在的职位：" + position.getText());
        System.out.println("学校：" + school.getText());
        System.out.println("地区：" + location.getText());
        System.out.println("粉丝：" + followerNum.getText());
        return resultJson;

    }

    public static boolean isElementPresent(By by, WebDriver driver) {
        boolean present;
        try {
            driver.findElement(by);
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }
        return present;
    }

    public static float calculateScores(Map<String, Object> profile, List<String> skillList) {
        Map<String, Integer> university = ParseUtil.loadFromFile(new File("./src/resources/University.txt"));
        Map<String, Integer> company = ParseUtil.loadFromFile(new File("./src/resources/Company.txt"));
        boolean isFreshGraduate = false;
        //The number of skills that matches
        float skillScore = 0;
        float eduScore = 0;
        float workScore = 0;
        float scores = 0;
        Pattern p = Pattern.compile("student|学生", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher((String) profile.get("Position"));
        StringBuilder skillPattern = new StringBuilder();
        for (String skill : skillList) {
            skillPattern.append(skill + "|");
        }

        p = Pattern.compile(skillPattern.toString(), Pattern.CASE_INSENSITIVE);
        List<String> skills = (List<String>) profile.get("Skills");
        for (String skill : skills) {
            if (p.matcher(skill).find()) {
                skillScore += 25;
            }
        }
        //Check whether the employee is fresh graduates or a job-hopping
        if (matcher.find()) {
            isFreshGraduate = true;
        } else {
            isFreshGraduate = false;
        }
        //System.out.println(isFreshGraduate);
        List<String> edu = (List<String>) profile.get("Education Experience");


        Set<String> key = company.keySet();
        StringBuilder comPattern = new StringBuilder();
        for (String k : key) {
            comPattern.append(k + "|");
        }
        System.out.println(comPattern.toString());
        Pattern pCom = Pattern.compile(comPattern.toString(), Pattern.CASE_INSENSITIVE);
        List<String> com = (List<String>) profile.get("Working Experience");
        for (String c : com) {
            c = c.trim().toLowerCase();
            System.out.println(c);
            Matcher mCom = pCom.matcher(c);
            if (mCom.find()) {
                String findCom = mCom.group().toLowerCase();
                System.out.println(findCom);
                System.out.println(company);
                if (company.containsKey(findCom)) {
                    int s = company.get(findCom);
                    if (s == 1) {
                        workScore += 60;
                    } else if (s == 2) {
                        workScore += 30;
                    } else if (s == 3) {
                        workScore += 25;
                    } else if (s == 4) {
                        workScore += 20;
                    }

                }

            }
        }
        //For fresh graduate students the weight of edu experience is higher than the job-hopping
        eduScore = calcuScore(edu, university);
        //workScore = calcuScore(com, company);
        System.out.println("eduScore" + eduScore);
        System.out.println("workScore" + workScore);
        if (isFreshGraduate) {
            scores = (float) (0.4 * skillScore + 0.3 * eduScore + 0.3 * workScore);
        } else {
            scores = (float) (0.5 * skillScore + 0.4 * workScore + 0.1 * eduScore);
        }
        return scores;
    }

    public static float calcuScore(List<String> elements, Map<String, Integer> source) {
        float score = 0;
        if (source != null) {
            for (String u : elements) {
                u.trim();
                u.toLowerCase();
                if (source.containsKey(u)) {
                    int value = source.get(u);
                    if (value == 1) {
                        score += 50;
                    } else if (value == 2) {
                        score += 30;
                    } else if (value == 3) {
                        score += 25;
                    } else if (value == 4) {
                        score += 20;
                    }

                }
            }
        }
        if (score <= 100) {
            return score;
        } else {
            return 100;
        }
    }
}
