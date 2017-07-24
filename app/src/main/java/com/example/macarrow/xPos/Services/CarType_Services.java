package com.example.macarrow.xPos.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarType_Services extends SQLiteOpenHelper {

    public CarType_Services(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 차종 명 - car_type_title
    * 최초 무료시간 (분) - minute_free
    * 기본요금 - basic_amount
    * 기본시간 (분) - basic_minute
    * 추가요금 - amount_unit
    * 추가요금 단위 (분) - minute_unit
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE car_type (" +
                "idx INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "car_type_title TEXT NOT NULL, " +
                "minute_free INTEGER DEFAULT 0, " +
                "basic_amount INTEGER DEFAULT 0, " +
                "basic_minute INTEGER DEFAULT 0, " +
                "amount_unit INTEGER DEFAULT 0, " +
                "minute_unit INTEGER DEFAULT 0, " +
                "is_daycar TEXT NOT NULL DEFAULT 'N');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(String car_type_title,
                       int minute_free,
                       int basic_amount,
                       int basic_minute,
                       int amount_unit,
                       int minute_unit,
                       String is_daycar) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO car_type(car_type_title," +
                "minute_free," +
                "basic_amount," +
                "basic_minute," +
                "amount_unit," +
                "minute_unit," +
                "is_daycar) VALUES('" + car_type_title + "', " +
                "" + minute_free + ", " +
                "" + basic_amount + ", " +
                "" + basic_minute + ", " +
                "" + amount_unit + ", " +
                "" + minute_unit + ", " +
                "'" + is_daycar + "');");
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
        db.execSQL("UPDATE car_type SET car_type_title = '" + car_type_title + "', " +
                "minute_free = " + minute_free + ", " +
                "basic_amount = " + basic_amount + ", " +
                "basic_minute = " + basic_minute + ", " +
                "amount_unit = " + amount_unit + ", " +
                "minute_unit = " + minute_unit + " " +
                "WHERE idx = " + idx + ";");
        db.close();
    }

    public void delete(int idx) {

        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM car_type WHERE idx = " + idx + ";");
        db.close();

    }

    public int car_type(String is_daycar) {

        SQLiteDatabase db = getReadableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        Cursor cursor = db.rawQuery("SELECT * FROM car_type WHERE is_daycar = ?", new String[] {is_daycar});
        cursor.moveToFirst();
        cursor.getCount();
        cursor.close();
        return cursor.getCount();

    }

    public Map<String, Object> getResultForUpdate(int idx) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM car_type WHERE idx = " + idx, null);
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("car_type_title", cursor.getString(1));
            map.put("minute_free", cursor.getInt(2));
            map.put("basic_amount", cursor.getInt(3));
            map.put("basic_minute", cursor.getInt(4));
            map.put("amount_unit", cursor.getInt(5));
            map.put("minute_unit", cursor.getInt(6));
            map.put("is_daycar", cursor.getString(7));
        }

        return map;

    }

    public List<Map<String, Object>> getResult(String is_daycar) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = null;
        if(is_daycar == null) {
            cursor = db.rawQuery("SELECT * FROM car_type", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM car_type WHERE is_daycar = ?", new String[] {is_daycar});
        }
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("car_type_title", cursor.getString(1));
            map.put("minute_free", cursor.getInt(2));
            map.put("basic_amount", cursor.getInt(3));
            map.put("basic_minute", cursor.getInt(4));
            map.put("amount_unit", cursor.getInt(5));
            map.put("minute_unit", cursor.getInt(6));
            map.put("is_daycar", cursor.getString(7));
            list.add(map);
        }

        return list;

    }
}