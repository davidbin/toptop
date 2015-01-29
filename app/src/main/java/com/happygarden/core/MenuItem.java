package com.happygarden.core;

import java.io.Serializable;

/**
 * Created by Administrator on 8/2/2014.
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = -7495897652017488896L;

    protected String itemID;
    protected String name;
    protected String descriptions;
    protected String image;

    public String getItemID() {
        return itemID;
    }
    public void setItemID(final String itemID) {
        this.itemID = itemID;
    }
    public String getName(){
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getDescriptions(){
        return descriptions;
    }
    public void setDescriptions(final String descriptions){
        this.descriptions = descriptions;
    }
    public String getImage(){
        return (Constants.Http.URL_FOODIMAGE + image);
    }
    public void setImage(final String imgfile){
        this.image = imgfile;
    }


}
