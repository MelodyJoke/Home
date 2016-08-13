package com.teamsolo.home.structure.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.melody.base.util.LogUtility;
import com.teamsolo.home.bean.User;
import com.teamsolo.home.constant.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * description: user db helper
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public class UserDbHelper extends BaseDbHelper {

    public UserDbHelper(Context context) {
        super(context, DatabaseConstant.TABLE_USER);
        initDBHelper();
    }

    /**
     * insert user
     *
     * @param user user
     * @return true if success
     */
    public boolean insert(User user) {
        if (user == null || TextUtils.isEmpty(user.phone)) return false;

        if (getUser(user.phone) != null) {
            update(user);
            return true;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseConstant.TABLE_USER_FIELDS[1][0], user.phone);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[2][0], user.rememberPassword ? user.password : "");
        values.put(DatabaseConstant.TABLE_USER_FIELDS[3][0], user.portrait);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[4][0], user.rememberPassword ? 1 : 0);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[5][0], System.currentTimeMillis());

        boolean result = db.insert(tableName, values) != -1;
        closeDB();

        return result;
    }

    /**
     * update user
     *
     * @param user user
     */
    public void update(User user) {
        if (user == null || TextUtils.isEmpty(user.phone)) return;

        ContentValues values = new ContentValues();
        values.put(DatabaseConstant.TABLE_USER_FIELDS[1][0], user.phone);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[2][0], user.rememberPassword ? user.password : "");
        values.put(DatabaseConstant.TABLE_USER_FIELDS[3][0], user.portrait);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[4][0], user.rememberPassword ? 1 : 0);
        values.put(DatabaseConstant.TABLE_USER_FIELDS[5][0], System.currentTimeMillis());

        db.update(tableName, values, new String[]{DatabaseConstant.TABLE_USER_FIELDS[1][0]}, new String[]{user.phone});
        closeDB();
    }

    /**
     * get user
     *
     * @param phone phone number
     * @return the user
     */
    public User getUser(String phone) {
        if (TextUtils.isEmpty(phone)) return null;

        Cursor cursor = db.query(tableName, DatabaseConstant.TABLE_USER_FIELDS[1][0] + "=" + phone);

        if (cursor == null) {
            closeDB();
            return null;
        }

        User user = null;
        if (cursor.moveToFirst()) {
            do {
                try {
                    if (user == null) user = new User();
                    user.phone = cursor.getString(cursor.getColumnIndex(DatabaseConstant.TABLE_USER_FIELDS[1][0]));
                    user.password = cursor.getString(cursor.getColumnIndex(DatabaseConstant.TABLE_USER_FIELDS[2][0]));
                    user.portrait = cursor.getString(cursor.getColumnIndex(DatabaseConstant.TABLE_USER_FIELDS[3][0]));
                    user.rememberPassword = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.TABLE_USER_FIELDS[4][0])) == 1;
                } catch (Exception e) {
                    LogUtility.e(getClass().getSimpleName(), e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();

        return user;
    }

    /**
     * get phones logged before
     *
     * @param size size
     * @return the phones list
     */
    public List<String> getPhones(int size) {
        if (size <= 0) return null;

        Cursor cursor = db.rawQuery(tableName, "select " + DatabaseConstant.TABLE_USER_FIELDS[1][0] + " from " + tableName + " order by " + DatabaseConstant.TABLE_USER_FIELDS[5][0] + " desc limit 0," + size);

        if (cursor == null) {
            closeDB();
            return null;
        }

        List<String> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    String phone = cursor.getString(cursor.getColumnIndex(DatabaseConstant.TABLE_USER_FIELDS[1][0]));
                    if (!TextUtils.isEmpty(phone)) result.add(phone);
                } catch (Exception e) {
                    LogUtility.e(getClass().getSimpleName(), e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();

        return result;
    }
}
