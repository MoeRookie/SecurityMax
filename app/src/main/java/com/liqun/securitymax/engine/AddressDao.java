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
        // 正则表达式, 匹配手机号码
        // 手机号码的正则表达式
        String regularExpression = "^1[3-8]\\d{9}";
        // 02. 开启数据库连接(只读的形式打开)
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, OPEN_READONLY);
        String address = "未知号码";
        if (phone.matches(regularExpression)) {
            phone = phone.substring(0, 7);
            // 03. 数据库查询
            Cursor cursor = database.query("data1", new String[]{"outkey"},
                    "id = ?", new String[]{phone}, null, null, null);
            // 04. 查到即可
            if (cursor.moveToNext()) {
                String outkey = cursor.getString(0);
                Log.e(TAG, "outkey = " + outkey);
                // 05. 通过data1查询到的结果, 作为外键查询data2
                Cursor indexCursor = database.query("data2", new String[]{"location"},
                        "id = ?", new String[]{outkey}, null, null, null);
                // 06. 获取查询到的电话归属地
                if (indexCursor.moveToNext()) {
                    address = indexCursor.getString(0);
                    Log.e(TAG, "address = " + address);
                }
            }
        }else{
            int length = phone.length();
            switch (length) {
                case 3: // 110 119 120
                    address = "报警电话";
                    break;
                case 4: // 5556
                    address = "模拟器";
                    break;
                case 5: // 10086 95536
                    address = "服务电话";
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                case 11:
                    // (3+8) 区号+座机号码(外地)[查询data2]
                    String area = phone.substring(1, 3);
                    Cursor cursor = database.query("data2", new String[]{"location"},
                            "area = ?", new String[]{area},
                            null, null, null);
                    if (cursor.moveToNext()) {
                        address = cursor.getString(0);
                    }else {
                        address = "未知号码";
                    }
                    break;
                case 12:
                    // (4+8) 区号+座机号码(外地)[查询data2]
                    area = phone.substring(1, 4);
                    cursor = database.query("data2", new String[]{"location"},
                            "area = ?", new String[]{area},
                            null, null, null);
                    if (cursor.moveToNext()) {
                        address = cursor.getString(0);
                    }else {
                        address = "未知号码";
                    }
                    break;
            }
        }
        Log.e(TAG, "getAddress: " + address);
        return address;
    }
}
