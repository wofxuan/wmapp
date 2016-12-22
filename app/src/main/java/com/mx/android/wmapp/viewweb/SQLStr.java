package com.mx.android.wmapp.viewweb;

/**
 * Created by Administrator on 2016-12-13.
 */
public class SQLStr {
    public static final String CREATE_DATABASE =
            "CREATE DATABASE com_webbrowser_xq";
    public static final String CREATE_TABLE_FAVORITES =
            "CREATE TABLE favorite (id INTEGER PRIMARY KEY, name TEXT NOT NULL, url TEXT NOT NULL)";
    public static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE history (id INTEGER PRIMARY KEY, name TEXT NOT NULL, url TEXT NOT NULL, date LONG NOT NULL)";
}
