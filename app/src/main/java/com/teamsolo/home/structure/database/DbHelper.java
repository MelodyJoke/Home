package com.teamsolo.home.structure.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.melody.base.util.LogUtility;
import com.teamsolo.home.constant.DatabaseConstant;

import org.jetbrains.annotations.Contract;

/**
 * description: database helper
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static final String ORDER_BY_ID_DESC = " id desc";

    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, "common.db", null, DatabaseConstant.DB_VERSION);
    }

    public DbHelper(Context context, String name) {
        super(context, TextUtils.isEmpty(name) ? "common.db" : name.contains(".db") ? name : (name + ".db"), null, DatabaseConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.beginTransaction();

        try {
            database.execSQL(generateSqlScript(DatabaseConstant.TABLE_USER, DatabaseConstant.TABLE_USER_FIELDS));

            database.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // TODO:
    }

    /**
     * generate create sql script base on tableName and fields array
     *
     * @param tableName   table name
     * @param tableFields fields array
     * @return table create script
     */
    @NonNull
    private String generateSqlScript(String tableName, String[][] tableFields) {
        StringBuilder builder = new StringBuilder("create table if not exists " + tableName + "(");

        int length = tableFields.length;
        for (int i = 0; i < length; i++) {
            builder.append(tableFields[i][0]).append(" ").append(tableFields[i][1]);

            if (i < length - 1) builder.append(", ");
            else builder.append(")");
        }

        return builder.toString();
    }

    public long insert(String tableName, ContentValues values) {
        db = this.getWritableDatabase();
        db.beginTransaction();

        long id = -1;

        try {
            id = db.insert(tableName, null, values);
            db.setTransactionSuccessful();
            LogUtility.d(TAG, tableName + " insert success");
        } catch (Exception e) {
            LogUtility.d(TAG, tableName + " insert error");
            return -1;
        } finally {
            db.endTransaction();
            close(db);
        }

        return id;
    }

    public void update(String tableName, ContentValues values, String[] whereArgs, String[] whereArgsValues) {
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            if (whereArgs == null) db.update(tableName, values, null, null);
            else {
                if (whereArgs.length == 1) {
                    if (whereArgsValues.length == 1)
                        db.update(tableName, values, whereArgs[0] + "='" + whereArgsValues[0] + "'", null);
                    else
                        db.update(tableName, values, createSQL(whereArgs, whereArgsValues, whereArgsValues.length), null);
                } else
                    db.update(tableName, values, createSQL(whereArgs, whereArgsValues, whereArgs.length), null);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
            close(db);
        }
    }

    public void delete(String tableName, String[] whereArgs, String[] whereArgsValues) {
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            if (whereArgs == null) db.delete(tableName, null, null);
            else {
                if (whereArgs.length == 1) {
                    if (whereArgsValues.length == 1)
                        db.delete(tableName, whereArgs[0] + " = ?", whereArgsValues);
                    else
                        db.execSQL(delSql(tableName, createSQL(whereArgs, whereArgsValues, whereArgsValues.length)));
                } else
                    db.execSQL(delSql(tableName, createSQL(whereArgs, whereArgsValues, whereArgs.length)));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
            close(db);
        }
    }

    public void delete(String tableName, String selection) {
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.execSQL(delSql(tableName, selection));
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
            close(db);
        }
    }

    @NonNull
    private String createSQL(String[] whereArgs, String[] whereArgsValues, int length) {
        StringBuilder sql = new StringBuilder(" ");

        if (whereArgs.length == 1) {
            for (int i = 0; i < length; i++) {
                sql.append(whereArgs[0]).append(" = '").append(whereArgsValues[i]).append("'");
                if (i < length - 1) sql.append(" or ");
            }
        } else {
            for (int i = 0; i < length; i++) {
                sql.append(whereArgs[i]).append(" = '").append(whereArgsValues[i]).append("'");
                if (i < length - 1) sql.append(" and ");
            }
        }

        return sql.toString();
    }

    @Contract(pure = true)
    private String delSql(String tableName, String strSql) {
        return "delete from " + tableName + " where " + strSql;
    }

    public Cursor query(String tableName, String selection, String orderBy) {
        Cursor cursor = null;
        db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            cursor = db.query(tableName, null, selection, null, null, null, orderBy);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public Cursor query(String tableName, String selection) {
        Cursor cursor = null;
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            cursor = db.query(tableName, null, selection, null, null, null, ORDER_BY_ID_DESC);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public Cursor rawQuery(String tableName, String sql) {
        Cursor cursor = null;
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            cursor = db.rawQuery(sql, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public void execBySql(String sql) {
        db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public Cursor query(String tableName, String[] whereArgs,
                        String[] whereArgsValues, String[] column, String orderBy) {
        Cursor cursor = null;
        db = this.getReadableDatabase();

        db.beginTransaction();
        try {
            if (whereArgs == null)
                cursor = db.query(tableName, column, null, null, null, null, ORDER_BY_ID_DESC);
            else {
                if (whereArgs.length == 1) {
                    if (whereArgsValues.length == 1)
                        cursor = db.query(tableName, column, whereArgs[0] + "= ?", whereArgsValues, null, null, ORDER_BY_ID_DESC);
                    else
                        cursor = db.query(tableName, column, createSQL(whereArgs, whereArgsValues, whereArgsValues.length), null, null, null, ORDER_BY_ID_DESC);
                } else
                    cursor = db.query(tableName, column, createSQL(whereArgs, whereArgsValues, whereArgs.length), null, null, null, ORDER_BY_ID_DESC);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public void closeDatabase() {
        try {
            close();
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        }
    }

    public void close(SQLiteDatabase db) {
        try {
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        } catch (Exception e) {
            LogUtility.e(TAG, e.getMessage());
        } finally {
            if (db != null) db.close();
        }
    }
}
