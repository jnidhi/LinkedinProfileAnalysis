package com.lingoffer.model;

import java.util.List;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class Profile {
    private String LinkedinURL;

    private String name;

    private String location;

    private List<Experience> workingExperiences;

    private List<Education> educations;

    private List<Skill> skills;

    private int followers;

    public void setWorkingExperiences(List<Experience> workingExperiences) {
        this.workingExperiences = workingExperiences;
    }

    public void setLinkedinURL(String linkedinURL) {
        LinkedinURL = linkedinURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }


    public String toString() {
        return "LinkedinURL:" + LinkedinURL +
                "name:" + name +
                "location:" + location +
                "workingExperiences:" + workingExperiences.toString() +
                "educations:" + educations.toString() +
                "skills:" + skills.toString() +
                "followers:" + followers;
    }

    public boolean insertDB(boolean insert) {
        if (insert == true) {
            System.out.println(this.toString());
            return true;
        } else {
            System.out.println("Insert failure");
            return false;
        }
    }


}
