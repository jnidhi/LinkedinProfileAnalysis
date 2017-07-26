package com.lingoffer.parser;

import com.lingoffer.model.Profile;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public interface Parser {
    //Initialize the web driver from file by using the firefox
    public boolean loadWebDriverWithFireFox();

    public boolean loadWebDriverWithkernel();

    /**
     * Parser the Linkedin homepage by giving some urls
     * @return
     */
    public Profile parserLinkedin(String linkedInUrl);

}
