package com.lingoffer.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by siyu on 2017/7/23.
 */
public class ParseUniversities {
    public static void main(String[] args) {
        try {
            File f = new File("./src/resources/University.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f));
            BufferedWriter bw = new BufferedWriter(write);
            FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe"));
            FirefoxProfile profile = new FirefoxProfile();
            WebDriver driver = new FirefoxDriver(binary, profile);
            driver.get("http://www.liuxue86.com/a/3129818.html");
            List<WebElement> universities = driver.findElements(By.xpath("//tr/td"));
            StringBuilder sb;
            int i = 0;
            boolean a=true;
            Pattern pEn = Pattern.compile("university|institute|college", Pattern.CASE_INSENSITIVE);
            Pattern pCh = Pattern.compile("分校|学院|大学");
            for (WebElement u : universities) {

                String line = u.getText();
                Matcher matcherEn = pEn.matcher(line);
                Matcher matcherCh = pCh.matcher(line);
                sb = new StringBuilder();
                if (matcherEn.find()) {

                    //int begin=line.indexOf(" ");
                    //int end=line.length();
                    if (i < 20) {
                        sb.append(line.toLowerCase().trim() + "1");
                    } else if (i > 20 && i < 40) {
                        sb.append(line.toLowerCase().trim() + "2");
                    } else if (i >= 40 && i < 60) {
                        sb.append(line.toLowerCase().trim() + "3");
                    } else if (i >= 60) {
                        sb.append(line.toLowerCase().trim() + "4");
                    }
                    i++;

                } else if (matcherCh.find()) {
                    if(a==true){
                        i=0;
                        a=false;
                    }
                    sb = new StringBuilder();
                    //int begin=line.indexOf(" ");
                    //int end=line.length();
                    if (i < 20) {
                        sb.append(line.toLowerCase().trim() + "1");
                    } else if (i > 20 && i < 40) {
                        sb.append(line.toLowerCase().trim() + "2");
                    } else if (i >= 40 && i < 60) {
                        sb.append(line.toLowerCase().trim() + "3");
                    } else if (i >= 60) {
                        sb.append(line.toLowerCase().trim() + "4");
                    }
                    i++;

                }
                if (sb.toString()!=" ") {
                    //sb.append(line.substring(begin,end).toLowerCase().trim()+"1");
                    System.out.println(sb.toString());
                    bw.write(sb.toString()+"\n");
                    bw.flush();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
