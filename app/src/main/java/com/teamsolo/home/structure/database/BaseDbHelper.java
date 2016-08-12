package com.teamsolo.home.structure.database;

import android.content.Context;
import android.text.TextUtils;

/**
 * description: base database helper
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public abstract class BaseDbHelper {

    protected Context mContext;

    protected String dbName;

    protected String tableName;

    protected DbHelper db;

    protected BaseDbHelper(Context context, String dbName, String tableName) {
        mContext = context;
        this.dbName = dbName;
        this.tableName = tableName;
    }

    protected BaseDbHelper(Context context, String tableName) {
        mContext = context;
        this.tableName = tableName;
    }

    protected void initDBHelper() {
        if (db == null) {
            if (mContext == null) android.os.Process.killProcess(android.os.Process.myPid());

            if (!TextUtils.isEmpty(dbName)) db = new DbHelper(mContext, dbName);
            else db = new DbHelper(mContext);
        }
    }

    public void closeDB() {
        if (db != null) db.closeDatabase();
    }
}
