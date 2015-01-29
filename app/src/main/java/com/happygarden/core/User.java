package com.happygarden.core;

import android.text.TextUtils;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -7495897652017488896L;

    protected String email;
    protected String fullname;
    protected String objectId;
    protected String sessionToken;

    public String getUsername() {
        return email;
    }

    public void setUsername(final String username) {
        this.email = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }


}
