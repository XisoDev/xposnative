package com.example.macarrow.xPos.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Garage_Service extends SQLiteOpenHelper {

    public Garage_Service(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 입차시간 - start_date
    * start_year
    * start_month
    * start_day
    * 출차시간 - end_date
    * 차량번호 - car_num
    * 차종 명 - car_type_title
    * 추가요금 단위 (분) - minute_unit
    * 최초 무료시간 (분) - minute_free
    * 추가요금 - amount_unit
    * 기본요금 - basic_amount
    * 기본시간 (분) - basic_minute
    * 일반 고객 = 0, 월차 고객 > 0 month_idx
    * 지정업체 idx - cooper_idx
    * cooper_title
    * cooper_start
    * cooper_end
    * 할인된 금액 (지정주차) - discount_cooper
    * 할인된 금액 (기타) - discount_self
    * is_daycar
    * 총 요금 - total_amount
    * 결제 요금 - pay_amount
    * total_amount - pay_amount = 0이면 is_paid ="Y" total_amount - pay_amount > 0이면 is_paid ="N"
    * 입/출차 상태 - is_out ('Y' 출차됨 'N' 입차중)
    * 입차 취소 여부 - is_cancel ('Y' 취소 'N' 아니요)
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE garage (" +
                "idx INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "start_date INTEGER NOT NULL, " +
                "start_year INTEGER NOT NULL, " +
                "start_month INTEGER NOT NULL, " +
                "start_day INTEGER NOT NULL, " +
                "end_date INTEGER, " +
                "car_num TEXT, " +
                "car_type_title TEXT, " +
                "minute_unit INTEGER DEFAULT 0, " +
                "minute_free INTEGER DEFAULT 0, " +
                "amount_unit INTEGER DEFAULT 0, " +
                "basic_amount INTEGER DEFAULT 0, " +
                "basic_minute INTEGER DEFAULT 0, " +
                "month_idx INTEGER DEFAULT 0, " +
                "cooper_idx INTEGER DEFAULT 0, " +
                "cooper_title TEXT, " +
                "cooper_start INTEGER, " +
                "cooper_end INTEGER, " +
                "discount_cooper INTEGER DEFAULT 0, " +
                "discount_self INTEGER DEFAULT 0, " +
                "is_daycar TEXT NOT NULL DEFAULT 'N', " +
                "total_amount INTEGER DEFAULT 0, " +
                "pay_amount INTEGER DEFAULT 0, " +
                "is_paid TEXT NOT NULL DEFAULT 'N', " +
                "is_out TEXT NOT NULL DEFAULT 'N', " +
                "is_cancel TEXT NOT NULL DEFAULT 'N');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(long start_date,
                       int start_year,
                       int start_month,
                       int start_day,
                       String car_num,
                       String car_type_title,
                       int minute_unit,
                       int minute_free,
                       int amount_unit,
                       int basic_amount,
                       int basic_minute,
                       int month_idx,
                       int cooper_idx,
                       int discount_cooper,
                       int discount_self) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO garage(start_date," +
                "start_year," +
                "start_month," +
                "start_day," +
                "car_num," +
                "car_type_title," +
                "minute_unit," +
                "minute_free," +
                "amount_unit," +
                "basic_amount," +
                "basic_minute," +
                "month_idx," +
                "cooper_idx," +
                "discount_cooper," +
                "discount_self) VALUES(" + start_date + ", " +
                "" + start_year + ", " +
                "" + start_month + ", " +
                "" + start_day + ", " +
                "'" + car_num + "', " +
                "'" + car_type_title + "', " +
                "" + minute_unit + ", " +
                "" + minute_free + ", " +
                "" + amount_unit + ", " +
                "" + basic_amount + ", " +
                "" + basic_minute + ", " +
                "" + month_idx + ", " +
                "" + cooper_idx + ", " +
                "" + discount_cooper + ", " +
                "" + discount_self + ");");
        db.close();
    }

    public void outCar(long end_date,
                       int total_amount,
                       int pay_amount,
                       int cooper_idx,
                       int discount_cooper,
                       int discount_self,
                       String is_out,
                       String is_paid,
                       int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET end_date = " + end_date + ", " +
                "total_amount = " + total_amount + ", " +
                "pay_amount = " + pay_amount + ", " +
                "cooper_idx = " + cooper_idx + ", " +
                "discount_cooper = " + discount_cooper + ", " +
                "discount_self = " + discount_self + ", " +
                "is_out = '" + is_out + "', " +
                "is_paid = '" + is_paid + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void inDayCar(int total_amount,
                        int pay_amount,
                       String is_out,
                       String is_paid,
                       int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET total_amount = " + total_amount + ", " +
                "pay_amount = " + pay_amount + ", " +
                "is_out = '" + is_out + "', " +
                "is_paid = '" + is_paid + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void outDayCar(long end_date,
                          String is_out,
                         int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET end_date = " + end_date + ", " +
                "is_out = '" + is_out + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void updateForceOut(String is_out,
                               int total_amount,
                               long end_date,
                               String is_paid,
                               int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET is_out = '" + is_out + "', " +
                "total_amount = " + total_amount + ", " +
                "end_date = " + end_date + ", " +
                "is_paid = '" + is_paid + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    //
    public void updateCooper(int cooper_idx,
                             String cooper_title,
                             int cooper_start,
                             int cooper_end,
                             int discount_cooper,
                             int minute_free,
                             int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET cooper_idx = " + cooper_idx + ", " +
                "cooper_title = '" + cooper_title + "', " +
                "cooper_start = " + cooper_start + ", " +
                "cooper_end = " + cooper_end + ", " +
                "discount_cooper = " + discount_cooper + ", " +
                "minute_free = " + minute_free + " " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void updateDayCar(String car_type_title,
                             int minute_unit,
                             int minute_free,
                             int amount_unit,
                             int basic_amount,
                             int basic_minute,
                             int total_amount,
                             String is_daycar,
                             int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET car_type_title = '" + car_type_title + "', " +
                "minute_unit = " + minute_unit + ", " +
                "minute_free = " + minute_free + ", " +
                "amount_unit = " + amount_unit + ", " +
                "basic_amount = " + basic_amount + ", " +
                "basic_minute = " + basic_minute + ", " +
                "total_amount = " + total_amount + ", " +
                "is_daycar = '" + is_daycar + "' " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void cancelCar(long end_date,
                          int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET is_cancel = 'Y', " +
                "is_out = 'Y', " +
                "end_date = " + end_date + ", " +
                "cooper_idx = 0, " +
                "cooper_title = 0, " +
                "cooper_start = 0, " +
                "cooper_end = 0, " +
                "discount_cooper = 0, " +
                "discount_self = 0, " +
                "total_amount = 0, " +
                "pay_amount =  0 " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public void cancelOutCar(int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE garage SET is_out = 'N', " +
                "is_paid = 'N', " +
                "end_date = 0, " +
                "pay_amount = 0, " +
                "cooper_idx = 0, " +
                "cooper_title = '', " +
                "cooper_start = 0, " +
                "cooper_end = 0, " +
                "discount_cooper = 0, " +
                "discount_self = 0 " +
                "WHERE idx = " + idx + ";");
        db.close();

    }

    public int doubleCarNum(String car_num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE car_num = '" + car_num + "' AND month_idx = 0 AND is_out = 'N' AND is_cancel = 'N'", null);
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public int findMonthCarNum(String car_num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE car_num like ? AND month_idx > 0 AND is_out = 'N' AND is_cancel = 'N'", new String[] {'%'+car_num});
        cursor.moveToFirst();
        cursor.getCount();
        return cursor.getCount();
    }

    public int inCarCount(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE start_year = " + year + " AND start_month = " + month + " AND start_day = " + day + " ", null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }

    public int outCarCount(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE is_out = 'Y' AND start_year = " + year + " AND start_month = " + month + " AND start_day = " + day + " ", null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }

    public int cooperPsum(String cooper_title, int cooper_start, int cooper_end) {
        SQLiteDatabase db = getReadableDatabase();
        int discount_cooper = 0;
        Cursor cursor = db.rawQuery("SELECT sum(discount_cooper) FROM garage WHERE cooper_title = ? AND cooper_start >= " + cooper_start + " AND cooper_end <= " + cooper_end + " ", new String[] {cooper_title});
        cursor.moveToFirst();
        discount_cooper = cursor.getInt(0);
        cursor.close();
        return discount_cooper;
    }

    public int cooperDsum(String cooper_title, int cooper_end) {
        SQLiteDatabase db = getReadableDatabase();
        int discount_cooper = 0;
        Cursor cursor = db.rawQuery("SELECT sum(discount_cooper) FROM garage WHERE cooper_title = ? AND cooper_end = " + cooper_end + " ", new String[] {cooper_title});
        cursor.moveToFirst();
        discount_cooper = cursor.getInt(0);
        cursor.close();
        return discount_cooper;
    }

    public Map<String, Object> getResultForUpdate(int idx) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE idx = " + idx, null);
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("start_date", cursor.getLong(1));
            map.put("start_year", cursor.getInt(2));
            map.put("start_month", cursor.getInt(3));
            map.put("start_day", cursor.getInt(4));
            map.put("end_date", cursor.getLong(5));
            map.put("car_num", cursor.getString(6));
            map.put("car_type_title", cursor.getString(7));
            map.put("minute_unit", cursor.getInt(8));
            map.put("minute_free", cursor.getInt(9));
            map.put("amount_unit", cursor.getInt(10));
            map.put("basic_amount", cursor.getInt(11));
            map.put("basic_minute", cursor.getInt(12));
            map.put("month_idx", cursor.getInt(13));
            map.put("cooper_idx", cursor.getInt(14));
            map.put("cooper_title", cursor.getString(15));
            map.put("cooper_start", cursor.getInt(16));
            map.put("cooper_end", cursor.getInt(17));
            map.put("discount_cooper", cursor.getInt(18));
            map.put("discount_self", cursor.getInt(19));
            map.put("is_daycar", cursor.getString(20));
            map.put("total_amount", cursor.getInt(21));
            map.put("pay_amount", cursor.getInt(22));
            map.put("is_paid", cursor.getString(23));
            map.put("is_out", cursor.getString(24));
            map.put("is_cancel", cursor.getString(25));
        }

        return map;

    }

    public List<Map<String, Object>> getResult(String status, String car_num) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = null;
        // 전체
        if(status.equals("all")) {
            cursor = db.rawQuery("SELECT * FROM garage WHERE car_num like ? order by idx desc", new String[] {'%'+car_num+'%'});
        }
        // 입차중
        if(status.equals("in")) {
            cursor = db.rawQuery("SELECT * FROM garage WHERE is_out = 'N' AND is_cancel = 'N' AND car_num like ? order by idx desc", new String[] {'%'+car_num+'%'});
        }
        // 정상출차
        if(status.equals("out")) {
            cursor = db.rawQuery("SELECT * FROM garage WHERE is_out = 'Y' AND is_cancel = 'N' AND is_paid = 'Y'  AND car_num like ? order by idx desc", new String[] {'%'+car_num+'%'});
        }
        // 미결제
        if(status.equals("no_pay")) {
            cursor = db.rawQuery("SELECT * FROM garage WHERE is_out = 'Y' AND is_cancel = 'N' AND is_paid = 'N'  AND car_num like ? order by idx desc", new String[] {'%'+car_num+'%'});
        }
        // 입차취소
        if (status.equals("cancel")) {
            cursor = db.rawQuery("SELECT * FROM garage WHERE is_out = 'Y' AND is_cancel = 'Y' AND is_paid = 'N'  AND car_num like ? order by idx desc", new String[] {'%'+car_num+'%'});
        } while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date", cursor.getLong(1));
            map.put("start_year", cursor.getInt(2));
            map.put("start_month", cursor.getInt(3));
            map.put("start_day", cursor.getInt(4));
            map.put("end_date", cursor.getLong(5));
            map.put("car_num", cursor.getString(6));
            map.put("car_type_title", cursor.getString(7));
            map.put("minute_unit", cursor.getInt(8));
            map.put("minute_free", cursor.getInt(9));
            map.put("amount_unit", cursor.getInt(10));
            map.put("basic_amount", cursor.getInt(11));
            map.put("basic_minute", cursor.getInt(12));
            map.put("month_idx", cursor.getInt(13));
            map.put("cooper_idx", cursor.getInt(14));
            map.put("cooper_title", cursor.getString(15));
            map.put("cooper_start", cursor.getInt(16));
            map.put("cooper_end", cursor.getInt(17));
            map.put("discount_cooper", cursor.getInt(18));
            map.put("discount_self", cursor.getInt(19));
            map.put("is_daycar", cursor.getString(20));
            map.put("total_amount", cursor.getInt(21));
            map.put("pay_amount", cursor.getInt(22));
            map.put("is_paid", cursor.getString(23));
            map.put("is_out", cursor.getString(24));
            map.put("is_cancel", cursor.getString(25));
            list.add(map);
        }

        return list;

    }

    public List<Map<String, Object>> getPeriod(String cooper_title, int cooper_start, int cooper_end) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE cooper_title = ? AND cooper_start >= " + cooper_start + " AND cooper_end <= " + cooper_end + " ", new String[] {cooper_title});
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date", cursor.getLong(1));
            map.put("start_year", cursor.getInt(2));
            map.put("start_month", cursor.getInt(3));
            map.put("start_day", cursor.getInt(4));
            map.put("end_date", cursor.getLong(5));
            map.put("car_num", cursor.getString(6));
            map.put("car_type_title", cursor.getString(7));
            map.put("minute_unit", cursor.getInt(8));
            map.put("minute_free", cursor.getInt(9));
            map.put("amount_unit", cursor.getInt(10));
            map.put("basic_amount", cursor.getInt(11));
            map.put("basic_minute", cursor.getInt(12));
            map.put("month_idx", cursor.getInt(13));
            map.put("cooper_idx", cursor.getInt(14));
            map.put("cooper_title", cursor.getString(15));
            map.put("cooper_start", cursor.getInt(16));
            map.put("cooper_end", cursor.getInt(17));
            map.put("discount_cooper", cursor.getInt(18));
            map.put("discount_self", cursor.getInt(19));
            map.put("is_daycar", cursor.getString(20));
            map.put("total_amount", cursor.getInt(21));
            map.put("pay_amount", cursor.getInt(22));
            map.put("is_paid", cursor.getString(23));
            map.put("is_out", cursor.getString(24));
            map.put("is_cancel", cursor.getString(25));
            list.add(map);
        }

        return list;

    }

    public List<Map<String, Object>> getDay(String cooper_title, int cooper_end) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE cooper_title = ? AND cooper_end = " + cooper_end + " ", new String[] {cooper_title});
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idx", cursor.getInt(0));
            map.put("start_date", cursor.getLong(1));
            map.put("start_year", cursor.getInt(2));
            map.put("start_month", cursor.getInt(3));
            map.put("start_day", cursor.getInt(4));
            map.put("end_date", cursor.getLong(5));
            map.put("car_num", cursor.getString(6));
            map.put("car_type_title", cursor.getString(7));
            map.put("minute_unit", cursor.getInt(8));
            map.put("minute_free", cursor.getInt(9));
            map.put("amount_unit", cursor.getInt(10));
            map.put("basic_amount", cursor.getInt(11));
            map.put("basic_minute", cursor.getInt(12));
            map.put("month_idx", cursor.getInt(13));
            map.put("cooper_idx", cursor.getInt(14));
            map.put("cooper_title", cursor.getString(15));
            map.put("cooper_start", cursor.getInt(16));
            map.put("cooper_end", cursor.getInt(17));
            map.put("discount_cooper", cursor.getInt(18));
            map.put("discount_self", cursor.getInt(19));
            map.put("is_daycar", cursor.getString(20));
            map.put("total_amount", cursor.getInt(21));
            map.put("pay_amount", cursor.getInt(22));
            map.put("is_paid", cursor.getString(23));
            map.put("is_out", cursor.getString(24));
            map.put("is_cancel", cursor.getString(25));
            list.add(map);
        }

        return list;

    }

    public Map<String, Object> getByIdx(String car_num) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Map<String, Object> map = new HashMap<String, Object>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM garage WHERE car_num like ? AND is_out = 'N' AND is_cancel = 'N'", new String[] {'%'+car_num});
        if (cursor.moveToNext()) {
            map.put("idx", cursor.getInt(0));
            map.put("start_date", cursor.getLong(1));
            map.put("start_year", cursor.getInt(2));
            map.put("start_month", cursor.getInt(3));
            map.put("start_day", cursor.getInt(4));
            map.put("end_date", cursor.getLong(5));
            map.put("car_num", cursor.getString(6));
            map.put("car_type_title", cursor.getString(7));
            map.put("minute_unit", cursor.getInt(8));
            map.put("minute_free", cursor.getInt(9));
            map.put("amount_unit", cursor.getInt(10));
            map.put("basic_amount", cursor.getInt(11));
            map.put("basic_minute", cursor.getInt(12));
            map.put("month_idx", cursor.getInt(13));
            map.put("cooper_idx", cursor.getInt(14));
            map.put("cooper_title", cursor.getString(15));
            map.put("cooper_start", cursor.getInt(16));
            map.put("cooper_end", cursor.getInt(17));
            map.put("discount_cooper", cursor.getInt(18));
            map.put("discount_self", cursor.getInt(19));
            map.put("is_daycar", cursor.getString(20));
            map.put("total_amount", cursor.getInt(21));
            map.put("pay_amount", cursor.getInt(22));
            map.put("is_paid", cursor.getString(23));
            map.put("is_out", cursor.getString(24));
            map.put("is_cancel", cursor.getString(25));
        }

        return map;

    }

}