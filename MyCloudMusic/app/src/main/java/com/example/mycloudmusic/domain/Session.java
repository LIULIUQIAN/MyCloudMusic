package com.example.mycloudmusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Session {

    /**
     * 用户Id
     */
    private String user;

    /**
     * 登录后的Session
     */
    private String session;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("session", session)
                .toString();
    }
}
