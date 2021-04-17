package com.liqun.securitymax.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.OPEN_READONLY;

public class AddressDao {
    private static final String TAG = AddressDao.class.getSimpleName();
    // 01. 指定访问数据库的路径
    private static String path = "data/data/com.liqun.securitymax/files/address.db";

    /**
     * 传递一个电话号码, 开启数据库连接, 进行访问, 返回一个归属地
     * @param phone 待查询的电话号码
     */
    public static String getAddress(String phone){
        phone = phone.substring(0, 7);
        // 02. 开启数据库连接(只读的形式打开)
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, OPEN_READONLY);
        // 03. 数据库查询
        Cursor cursor = database.query("data1", new String[]{"outkey"},
                "id = ?", new String[]{phone}, null, null, null);
        // 04. 查到即可
        if (cursor.moveToNext()) {
            String outkey = cursor.getString(0);
            Log.e(TAG, "outkey = " + outkey);
        }
        return null;
    }
}
