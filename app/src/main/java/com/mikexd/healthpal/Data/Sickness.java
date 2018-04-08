package com.mikexd.healthpal.Data;

/**
 * Created by xunda on 8/4/18.
 */

public class Sickness {
    private String id;
    private String issue;

    public Sickness(String id, String issue){
        this.id = id;
        this.issue = issue;
    }

    public String getIssue(){
        return issue;
    }

    public String getID(){
        return id;
    }
}
