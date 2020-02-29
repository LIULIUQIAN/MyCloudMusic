package com.example.mycloudmusic.domain;

import java.util.List;

/**
 * 搜索建议模型
 */
public class Suggest {
    /**
     * 歌单搜索建议
     */
    private List<SearchTitle> sheets;

    /**
     * 用户搜索建议
     */
    private List<SearchTitle> users;

    public List<SearchTitle> getSheets() {
        return sheets;
    }

    public void setSheets(List<SearchTitle> sheets) {
        this.sheets = sheets;
    }

    public List<SearchTitle> getUsers() {
        return users;
    }

    public void setUsers(List<SearchTitle> users) {
        this.users = users;
    }
}
