package com.example.macarrow.xPos.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Month_Service extends SQLiteOpenHelper {

    public Month_Service(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 시작 날짜 - start_date_y, start_date_m, start_date_d, start_date
    * 종료 날짜 - end_date_y, end_date_m, end_date_d, end_date
    * 월차금액 - amount
    * 결제 요금 - pay_amount
    * amount - pay_amount = 0 is_paid = "Y" or > 0 "N"
    * 차량번호 - car_num
    * 차종 - car_name
    * 구분 - car_type_title
    * 차주 명 - user_name
    * 연락처 - mobile
    *  - regdate
    * is_stop DEFAULT 'N'
    *  - stop_date
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE month (" +
                "idx INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "start_date_y INTEGER NOT NULL, " +
                "start_date_m INTEGER NOT NULL, " +
                "start_date_d INTEGER NOT NULL, " +
                "start_date INTEGER NOT NULL, " +
                "end_date_y INTEGER NOT NULL, " +
                "end_date_m INTEGER NOT NULL, " +
                "end_date_d INTEGER NOT NULL, " +
                "end_date INTEGER NOT NULL, " +
                "amount INTEGER DEFAULT 0, " +
                "pay_amount INTEGER DEFAULT 0, " +
                "is_paid TEXT NOT NULL DEFAULT 'N', " +
                "car_num TEXT, " +
                "car_name TEXT, " +
                "car_type_title TEXT, " +
                "user_name TEXT, " +
                "mobile TEXT, " +
                "regdate INTEGER, " +
                "is_stop TEXT NOT NULL DEFAULT 'N', " +
                "stop_date INTEGER);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(int start_date_y,
                       int start_date_m,
                       int start_date_d,
                       int start_date,
                       int end_date_y,
                       int end_date_m,
                       int end_date_d,
                       int end_date,
                       int amount,
                       String car_num,
                       String car_name,
                       String car_type_title,
                       String user_name,
                       String mobile) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO month(start_date_y," +
                "start_date_m," +
                "start_date_d," +
                "start_date," +
                "end_date_y," +
                "end_date_m," +
                "end_date_d," +
                "end_date," +
                "amount," +
                "car_num," +
                "car_name," +
                "car_type_title," +
                "user_name," +
                "mobile) VALUES(" + start_date_y + ", " +
                "" + start_date_m + ", " +
                "" + start_date_d + ", " +
                "" + start_date + ", " +
                "" + end_date_y + ", " +
                "" + end_date_m + ", " +
                "" + end_date_d + ", " +
                "" + end_date + ", " +
                "" + amount + ", " +
                "'" + car_num + "', " +
                "'" + car_name + "', " +
                "'" + car_type_title + "', " +
                "'" + user_name + "', " +
                "'" + mobile + "');");
        db.close();
    }

    public void update(int start_date_y,
                       int start_date_m,
                       int start_date_d,
                       int start_date,
                       int end_date_y,
                       int end_date_m,
                       int end_date_d,
                       int end_date,
                       int amount,
                       String car_name,
                       String car_type_title,
                       String user_name,
                       String mobile,
                       String is_stop,
                       int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE month SET start_date_y = " + start_date_y + ", " +
                "start_date_m = " + start_date_m + ", " +
                "start_date_d = " + start_date_d + ", " +
                "start_date = " + start_date + ", " +
                "end_date_y = " + end_date_y + ", " +
                "end_date_m = " + end_date_m + ", " +
                "end_date_d = " + end_date_d + ", " +
                "end_date = " + end_date + ", " +
                "amount = " + amount + ", " +
                "car_name = '" + car_name + "', " +
                "car_type_title = '" + car_type_title + "', " +
                "user_name = '" + user_name + "', " +
                "mobile = '" + mobile + "', " +
                "is_stop = '" + is_stop + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void updatePay(int pay_amount,
                          int end_date_y,
                          int end_date_m,
                          int end_date_d,
                          int end_date,
                          String is_paid,
                          long regdate,
                          int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE month SET pay_amount = " + pay_amount + ", " +
                "end_date_y = " + end_date_y + ", " +
                "end_date_m = " + end_date_m + ", " +
                "end_date_d = " + end_date_d + ", " +
                "end_date = " + end_date + ", " +
                "is_paid = '" + is_paid + "' " +
                "regdate = '" + regdate + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public int findMonthCar(int today, String car_num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE car_num like ? AND is_stop='N' AND end_date >= " + today + " AND start_date <= " + today + "", new String[] {'%'+car_num});
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public int findCarNum(int today, String car_num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE car_num like ? AND is_stop='N' AND end_date >= " + today + " AND start_date <= " + today + "", new String[] {'%'+car_num+'%'});
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public int doubleCarNum(String car_num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE car_num = '" + car_num + "';", null);
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public Map<String, Object> getResultForUpdate(String car_num) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데
        // 이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE car_num = '" + car_num + "';", null);
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("start_date_y", cursor.getInt(1));
            map.put("start_date_m", cursor.getInt(2));
            map.put("start_date_d", cursor.getInt(3));
            map.put("start_date", cursor.getInt(4));
            map.put("end_date_y", cursor.getInt(5));
            map.put("end_date_m", cursor.getInt(6));
            map.put("end_date_d", cursor.getInt(7));
            map.put("end_date", cursor.getInt(8));
            map.put("amount", cursor.getInt(9));
            map.put("pay_amount", cursor.getInt(10));
            map.put("is_paid", cursor.getString(11));
            map.put("car_num", cursor.getString(12));
            map.put("car_name", cursor.getString(13));
            map.put("car_type_title", cursor.getString(14));
            map.put("user_name", cursor.getString(15));
            map.put("mobile", cursor.getString(16));
            map.put("regdate", cursor.getInt(17));
            map.put("is_stop", cursor.getString(18));
            map.put("stop_date", cursor.getInt(19));
        }

        return map;

    }

    public List<Map<String, Object>> getResult(int today, String status) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Cursor cursor = null;
        // 목록
        if(status.equals("possibility")) {
            cursor = db.rawQuery("SELECT * FROM month WHERE start_date <= " + today + " AND end_date >= " + today + " AND is_stop = 'N' order by idx desc", null);
        }
        // 만료목록
        if(status.equals("expired")) {
            cursor = db.rawQuery("SELECT * FROM month WHERE end_date < " + today + " OR is_stop = 'Y' order by idx desc", null);
        }
        // 대기목록
        if(status.equals("wait")) {
            cursor = db.rawQuery("SELECT * FROM month WHERE start_date > " + today + " AND is_stop = 'N' order by idx desc", null);
        } while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date_y", cursor.getInt(1));
            map.put("start_date_m", cursor.getInt(2));
            map.put("start_date_d", cursor.getInt(3));
            map.put("start_date", cursor.getInt(4));
            map.put("end_date_y", cursor.getInt(5));
            map.put("end_date_m", cursor.getInt(6));
            map.put("end_date_d", cursor.getInt(7));
            map.put("end_date", cursor.getInt(8));
            map.put("amount", cursor.getInt(9));
            map.put("pay_amount", cursor.getInt(10));
            map.put("is_paid", cursor.getString(11));
            map.put("car_num", cursor.getString(12));
            map.put("car_name", cursor.getString(13));
            map.put("car_type_title", cursor.getString(14));
            map.put("user_name", cursor.getString(15));
            map.put("mobile", cursor.getString(16));
            map.put("regdate", cursor.getInt(17));
            map.put("is_stop", cursor.getString(18));
            map.put("stop_date", cursor.getInt(19));
            list.add(map);
        }

        return list;

    }

    public List<Map<String, Object>> getByCarNum(int today, String car_num) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = null;
        if (car_num.equals("")) {
            cursor = db.rawQuery("SELECT * FROM month WHERE is_stop='N' AND end_date >= " + today + " AND start_date <= " + today + " order by idx desc", null);
        }
        else {
            cursor = db.rawQuery("SELECT * FROM month WHERE car_num like ? AND is_stop='N' AND end_date >= " + today + " AND start_date <= " + today + " order by idx desc", new String[] {'%'+car_num+'%'});
        }
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date_y", cursor.getInt(1));
            map.put("start_date_m", cursor.getInt(2));
            map.put("start_date_d", cursor.getInt(3));
            map.put("start_date", cursor.getInt(4));
            map.put("end_date_y", cursor.getInt(5));
            map.put("end_date_m", cursor.getInt(6));
            map.put("end_date_d", cursor.getInt(7));
            map.put("end_date", cursor.getInt(8));
            map.put("amount", cursor.getInt(9));
            map.put("pay_amount", cursor.getInt(10));
            map.put("is_paid", cursor.getString(11));
            map.put("car_num", cursor.getString(12));
            map.put("car_name", cursor.getString(13));
            map.put("car_type_title", cursor.getString(14));
            map.put("user_name", cursor.getString(15));
            map.put("mobile", cursor.getString(16));
            map.put("regdate", cursor.getInt(17));
            map.put("is_stop", cursor.getString(18));
            map.put("stop_date", cursor.getInt(19));
            list.add(map);
        }

        return list;

    }

    public int calMonthInCnt(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE is_stop = 'N' AND start_date_y = " + year + " AND start_date_m = " + month + " AND start_date_d = " + day + " ", null);
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public int calMonthOutCnt(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE is_stop = 'N' AND end_date_y = " + year + " AND end_date_m = " + month + " AND end_date_d = " + day + " ", null);
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public List<Map<String, Object>> calMonth(int year, int month, int day) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE is_stop = 'N' AND start_date_y = " + year + " AND start_date_m = " + month + " AND start_date_d = " + day + " OR end_date_y = " + year + " AND end_date_m = " + month + " AND end_date_d = " + day + " ", null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date_y", cursor.getInt(1));
            map.put("start_date_m", cursor.getInt(2));
            map.put("start_date_d", cursor.getInt(3));
            map.put("start_date", cursor.getInt(4));
            map.put("end_date_y", cursor.getInt(5));
            map.put("end_date_m", cursor.getInt(6));
            map.put("end_date_d", cursor.getInt(7));
            map.put("end_date", cursor.getInt(8));
            map.put("amount", cursor.getInt(9));
            map.put("pay_amount", cursor.getInt(10));
            map.put("is_paid", cursor.getString(11));
            map.put("car_num", cursor.getString(12));
            map.put("car_name", cursor.getString(13));
            map.put("car_type_title", cursor.getString(14));
            map.put("user_name", cursor.getString(15));
            map.put("mobile", cursor.getString(16));
            map.put("regdate", cursor.getInt(17));
            map.put("is_stop", cursor.getString(18));
            map.put("stop_date", cursor.getInt(19));
            list.add(map);
        }

        return list;

    }

    public Map<String, Object> getMonthCarNum(int today, String car_num) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM month WHERE car_num like ? AND is_stop='N' AND end_date >= " + today + " AND start_date <= " + today + "", new String[] {'%'+car_num});
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("start_date_y", cursor.getInt(1));
            map.put("start_date_m", cursor.getInt(2));
            map.put("start_date_d", cursor.getInt(3));
            map.put("start_date", cursor.getInt(4));
            map.put("end_date_y", cursor.getInt(5));
            map.put("end_date_m", cursor.getInt(6));
            map.put("end_date_d", cursor.getInt(7));
            map.put("end_date", cursor.getInt(8));
            map.put("amount", cursor.getInt(9));
            map.put("pay_amount", cursor.getInt(10));
            map.put("is_paid", cursor.getString(11));
            map.put("car_num", cursor.getString(12));
            map.put("car_name", cursor.getString(13));
            map.put("car_type_title", cursor.getString(14));
            map.put("user_name", cursor.getString(15));
            map.put("mobile", cursor.getString(16));
            map.put("regdate", cursor.getInt(17));
            map.put("is_stop", cursor.getString(18));
            map.put("stop_date", cursor.getInt(19));
        }

        return map;

    }
}