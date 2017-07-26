package com.lingoffer.model;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class Education {
    private String school;

    private String degree;

    //This can be 0 or null if not provided
    private Double grade;

    public void setSchool(String school) {
        this.school = school;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public String getDegree() {
        return degree;
    }

    public Double getGrade() {
        return grade;
    }

    public String toString(){
        return "school:"+school+
                "degree:"+degree+
                "grade:"+grade;
    }
}
