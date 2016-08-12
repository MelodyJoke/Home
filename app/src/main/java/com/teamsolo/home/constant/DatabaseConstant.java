package com.teamsolo.home.constant;

/**
 * description: database constant values
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public interface DatabaseConstant {

    /**
     * database version
     * 0: user db {@link #TABLE_USER,#TABLE_USER_FIELDS}
     */
    int DB_VERSION = 1;

    String TABLE_USER = "table_user";

    String[][] TABLE_USER_FIELDS = {
            {"id", "integer primary key autoincrement"},
            {"phone", "text not null"},
            {"password", "text"},
            {"portrait", "text"},
            {"remember", "integer default 0"}
    };
}
