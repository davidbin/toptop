package com.happygarden.core;

import java.io.Serializable;

/**
 * Created by Administrator on 8/2/2014.
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = -8495897652017488896L;

    protected String comment;
    protected String user;
    protected String rating;
    protected String date;

    public String getComment() {
        return comment;
    }
    public void setComment(final String comment) {
        this.comment = comment;
    }
    public String getUser(){
        return user;
    }
    public void setUser(final String user) {
        this.user = user;
    }
    public String getRating(){
        return rating;
    }
    public void setRating(final String rating){
        this.rating = rating;
    }
    public String getDate(){
        return (date);
    }
    public void setDate(final String date){
        this.date = date;
    }


}
