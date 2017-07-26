package com.lingoffer.model;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class Skill {
    private int NumEndorsements;

    private String name;

    public int getNumEndorsements() {
        return NumEndorsements;
    }

    public String getName() {
        return name;
    }

    public void setNumEndorsements(int numEndorsements) {
        NumEndorsements = numEndorsements;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "NumEndorsements:" + NumEndorsements +
                "name:" + name;
    }
}
