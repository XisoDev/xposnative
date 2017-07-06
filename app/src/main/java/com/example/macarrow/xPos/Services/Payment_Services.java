package com.example.macarrow.xPos.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment_Services extends SQLiteOpenHelper {

    public Payment_Services(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * lookup_idx (garage_idx, month_idx)
    * lookup_type (garage, month)
    * pay_type (cash, card)
    * 총액 = total_amount
    * 결제 금액 = pay_amount
    * code
    * ret_code
    * success_code
    * success_date
    * regdate
    * cancel_date
    * is_cancel
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE payment (" +
                "idx INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "lookup_idx INTEGER NOT NULL, " +
                "lookup_type TEXT NOT NULL, " +
                "pay_type TEXT, " +
                "total_amount INTEGER DEFAULT 0, " +
                "pay_amount INTEGER DEFAULT 0, " +
                "code TEXT, " +
                "ret_code TEXT, " +
                "success_code TEXT, " +
                "success_date TEXT, " +
                "regdate INTEGER, " +
                "cancel_date INTEGER, " +
                "is_cancel TEXT NOT NULL DEFAULT 'N');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(int lookup_idx,
                       String lookup_type,
                       String pay_type,
                       int total_amount,
                       int pay_amount) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO payment(lookup_idx," +
                "lookup_type," +
                "pay_type," +
                "total_amount," +
                "pay_amount) VALUES(" + lookup_idx + ", " +
                "'" + lookup_type + "', " +
                "'" + pay_type + "', " +
                "" + total_amount + ", " +
                "" + pay_amount + ");");
        db.close();
    }

    public void update(String car_type_title,
                       int minute_free,
                       int basic_amount,
                       int basic_minute,
                       int amount_unit,
                       int minute_unit,
                       int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE payment SET car_type_title = '" + car_type_title + "', " +
                "minute_free = " + minute_free + ", " +
                "basic_amount = " + basic_amount + ", " +
                "basic_minute = " + basic_minute + ", " +
                "amount_unit = " + amount_unit + ", " +
                "minute_unit = " + minute_unit + " " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public Map<String, Object> getResultForUpdate(int idx) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM payment WHERE idx = " + idx, null);
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("lookup_idx", cursor.getInt(1));
            map.put("lookup_type", cursor.getString(2));
            map.put("pay_type", cursor.getString(3));
            map.put("total_amount", cursor.getInt(4));
            map.put("pay_amount", cursor.getInt(5));
            map.put("code", cursor.getString(6));
            map.put("ret_code", cursor.getString(7));
            map.put("success_code", cursor.getString(8));
            map.put("success_date", cursor.getString(9));
            map.put("regdate", cursor.getInt(10));
            map.put("cancel_date", cursor.getInt(11));
            map.put("is_cancel", cursor.getString(12));

        }

        return map;

    }

    public List<Map<String, Object>> getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM payment", null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("lookup_idx", cursor.getInt(1));
            map.put("lookup_type", cursor.getString(2));
            map.put("pay_type", cursor.getString(3));
            map.put("total_amount", cursor.getInt(4));
            map.put("pay_amount", cursor.getInt(5));
            map.put("code", cursor.getString(6));
            map.put("ret_code", cursor.getString(7));
            map.put("success_code", cursor.getString(8));
            map.put("success_date", cursor.getString(9));
            map.put("regdate", cursor.getInt(10));
            map.put("cancel_date", cursor.getInt(11));
            map.put("is_cancel", cursor.getString(12));
            list.add(map);

        }

        return list;

    }
}