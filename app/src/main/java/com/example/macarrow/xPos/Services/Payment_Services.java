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
    * 할인액 = cooper_amount
    * 결제 금액 = pay_amount
    * pay_year
    * pay_month
    * pay_day
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
                "cooper_amount INTEGER DEFAULT 0, " +
                "pay_amount INTEGER DEFAULT 0, " +
                "pay_year INTEGER, " +
                "pay_month INTEGER, " +
                "pay_day INTEGER, " +
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
                       int cooper_amount,
                       int pay_amount,
                       int pay_year,
                       int pay_month,
                       int pay_day,
                       long regdate) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO payment(lookup_idx," +
                "lookup_type," +
                "pay_type," +
                "total_amount," +
                "cooper_amount," +
                "pay_amount," +
                "pay_year," +
                "pay_month," +
                "pay_day," +
                "regdate) VALUES(" + lookup_idx + ", " +
                "'" + lookup_type + "', " +
                "'" + pay_type + "', " +
                "" + total_amount + ", " +
                "" + cooper_amount + ", " +
                "" + pay_amount + ", " +
                "" + pay_year + ", " +
                "" + pay_month + ", " +
                "" + pay_day + ", " +
                "" + regdate + ");");
        db.close();
    }

    public void update(long cancel_date,
                       String is_cancel,
                       long regdate) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE payment SET cancel_date = " + cancel_date + ", " +
                "is_cancel = '" + is_cancel + "' " +
                "WHERE regdate = " + regdate + ";");
        db.close();
    }

    public int totalAmountSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int total_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(total_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        total_amount = cursor.getInt(0);
        cursor.close();
        return total_amount;
    }

    public int nomalAmountSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int month_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'garage' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        month_amount = cursor.getInt(0);
        cursor.close();
        return month_amount;
    }

    public int monthAmountSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int month_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'month' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        month_amount = cursor.getInt(0);
        cursor.close();
        return month_amount;
    }

    public int cooperAmountSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int total_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(cooper_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        total_amount = cursor.getInt(0);
        cursor.close();
        return total_amount;
    }

    public int payAmountSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int total_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        total_amount = cursor.getInt(0);
        cursor.close();
        return total_amount;
    }

//    public Map<String, Object> getResultForUpdate(int idx) {
//        // 읽기가 가능하게 DB 열기
//        SQLiteDatabase db = getReadableDatabase();
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
//        Cursor cursor = db.rawQuery("SELECT * FROM payment WHERE idx = " + idx, null);
//        if (cursor.moveToNext()) {
//            map.put("idx", cursor.getInt(0));
//            map.put("lookup_idx", cursor.getInt(1));
//            map.put("lookup_type", cursor.getString(2));
//            map.put("pay_type", cursor.getString(3));
//            map.put("total_amount", cursor.getInt(4));
//            map.put("cooper_amount", cursor.getInt(5));
//            map.put("pay_amount", cursor.getInt(6));
//            map.put("pay_year", cursor.getInt(7));
//            map.put("pay_month", cursor.getInt(8));
//            map.put("pay_day", cursor.getInt(9));
//            map.put("code", cursor.getString(10));
//            map.put("ret_code", cursor.getString(11));
//            map.put("success_code", cursor.getString(12));
//            map.put("success_date", cursor.getString(13));
//            map.put("regdate", cursor.getInt(14));
//            map.put("cancel_date", cursor.getInt(15));
//            map.put("is_cancel", cursor.getString(16));
//
//        }
//
//        return map;
//
//    }

//    public List<Map<String, Object>> getResult() {
//        // 읽기가 가능하게 DB 열기
//        SQLiteDatabase db = getReadableDatabase();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//
//        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
//        Cursor cursor = db.rawQuery("SELECT * FROM payment", null);
//        while (cursor.moveToNext()) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("idx", cursor.getInt(0));
//            map.put("lookup_idx", cursor.getInt(1));
//            map.put("lookup_type", cursor.getString(2));
//            map.put("pay_type", cursor.getString(3));
//            map.put("total_amount", cursor.getInt(4));
//            map.put("cooper_amount", cursor.getInt(5));
//            map.put("pay_amount", cursor.getInt(6));
//            map.put("pay_year", cursor.getInt(7));
//            map.put("pay_month", cursor.getInt(8));
//            map.put("pay_day", cursor.getInt(9));
//            map.put("code", cursor.getString(10));
//            map.put("ret_code", cursor.getString(11));
//            map.put("success_code", cursor.getString(12));
//            map.put("success_date", cursor.getString(13));
//            map.put("regdate", cursor.getInt(14));
//            map.put("cancel_date", cursor.getInt(15));
//            map.put("is_cancel", cursor.getString(16));
//            list.add(map);
//
//        }
//
//        return list;
//
//    }
}