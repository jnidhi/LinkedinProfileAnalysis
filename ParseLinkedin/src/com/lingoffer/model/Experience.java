package com.lingoffer.model;


import java.util.Date;

/**
 * Created by jasonchu.zsy on 2017/7/26.
 */
public class Experience {
    private String title;

    private String company;

    private String description;

    private Date from;

    private Date to;

    private WorkingType workingType;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public void setWorkingType(WorkingType workingType) {
        this.workingType = workingType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public WorkingType getWorkingType() {
        return workingType;
    }

    public String toString(){
        return "title:"+title+
                "company:"+company+
                "description:"+description+
                "from:"+from.toString()+
                "to:"+to.toString()+
                "workingType"+workingType.toString();
    }
}
