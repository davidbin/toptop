package com.happygarden.core;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = -6641292855569752036L;

    private String id;
    private int name;
    private int image;
    private int description;

    public int getImage(){return image;}
    public int getName() {
        return name;
    }

    public String getID() {
        return id;
    }
    public void setTitle(final int name) {
        this.name = name;
    }

    public Category(final String id,final int name,final int image,final int description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }
}
