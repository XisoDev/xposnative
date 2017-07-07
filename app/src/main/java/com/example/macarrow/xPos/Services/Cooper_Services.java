package com.example.macarrow.xPos.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cooper_Services extends SQLiteOpenHelper {

    public Cooper_Services(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 업체 명 - coop_title
    * 전화번호 - coop_tel
    * 주소 - coop_address
    * 대표자 명 - coop_user_name
    * 최대 지원 시간 (분) - minute_max
    * 추가요금 (원) - amount_unit
    * 추가요금 단위 (분) - minute_unit
    * regdate
    * 중단 - is_end Y=중단 N=활성
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE cooper (" +
                "idx INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "coop_title TEXT NOT NULL, " +
                "coop_tel TEXT, " +
                "coop_address TEXT, " +
                "coop_user_name TEXT, " +
                "minute_max INTEGER DEFAULT 0, " +
                "regdate INTEGER, " +
                "is_end TEXT NOT NULL DEFAULT 'N');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(String coop_title,
                       String coop_tel,
                       String coop_address,
                       String coop_user_name,
                       int minute_max) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO cooper(coop_title," +
                "coop_tel," +
                "coop_address," +
                "coop_user_name," +
                "minute_max) VALUES('" + coop_title + "', " +
                "'" + coop_tel + "', " +
                "'" + coop_address + "', " +
                "'" + coop_user_name + "', " +
                "" + minute_max + ");");
        db.close();
    }

    public void update(String coop_title,
                       String coop_tel,
                       String coop_address,
                       String coop_user_name,
                       int minute_max,
                       String is_end,
                       int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE cooper SET coop_title = '" + coop_title + "', " +
                "coop_tel = '" + coop_tel + "', " +
                "coop_address = '" + coop_address + "', " +
                "coop_user_name = '" + coop_user_name + "', " +
                "minute_max = " + minute_max + ", " +
                "is_end = '" + is_end + "' " +
                "WHERE idx = " + idx + ";");
        db.close();
    }

    public void delete(int idx) {

        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM cooper WHERE idx = " + idx + ";");
        db.close();
    }

    public int cooper() {

        SQLiteDatabase db = getReadableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        Cursor cursor = db.rawQuery("SELECT * FROM cooper", null);
        cursor.moveToFirst();
        cursor.getCount();
        cursor.close();
        return cursor.getCount();
    }

    public int coopTitle(String coop_title) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cooper WHERE coop_title = '" + coop_title + "' ", null);
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public Map<String, Object> getResultForUpdate(int idx) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM cooper WHERE idx = " + idx, null);
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("coop_title", cursor.getString(1));
            map.put("coop_tel", cursor.getString(2));
            map.put("coop_address", cursor.getString(3));
            map.put("coop_user_name", cursor.getString(4));
            map.put("minute_max", cursor.getInt(5));
            map.put("regdate", cursor.getInt(6));
            map.put("is_end", cursor.getString(7));
        }

        return map;

    }

    public List<Map<String, Object>> getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM cooper", null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("coop_title", cursor.getString(1));
            map.put("coop_tel", cursor.getString(2));
            map.put("coop_address", cursor.getString(3));
            map.put("coop_user_name", cursor.getString(4));
            map.put("minute_max", cursor.getInt(5));
            map.put("regdate", cursor.getInt(6));
            map.put("is_end", cursor.getString(7));
            list.add(map);
        }

        return list;

    }
}