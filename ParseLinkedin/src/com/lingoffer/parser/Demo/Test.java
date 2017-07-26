package com.lingoffer.parser.Demo;

import com.lingoffer.model.Profile;
import com.lingoffer.parser.Impl.ParserLinkedinImpl;
import com.lingoffer.parser.Parser;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class Test {
    public static void main(String[] args) {
        Parser p=new ParserLinkedinImpl();
        p.loadWebDriverWithkernel();
       // p.loadWebDriverWithFireFox();
        Profile profile=p.parserLinkedin("https://www.linkedin.com/in/jeff-dean-8b212555/");
    }
}
