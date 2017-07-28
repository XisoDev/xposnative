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

    public void cancelPay(long cancel_date,
                       String is_cancel,
                       long regdate) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE payment SET cancel_date = " + cancel_date + ", " +
                "is_cancel = '" + is_cancel + "' " +
                "WHERE regdate = " + regdate + ";");
        db.close();
    }

    public void cancelOut(long cancel_date,
                          String is_cancel,
                          int lookup_idx,
                          String lookup_type) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE payment SET cancel_date = " + cancel_date + ", " +
                "is_cancel = '" + is_cancel + "' " +
                "WHERE lookup_idx = " + lookup_idx + " " +
                "AND lookup_type = '" + lookup_type + "';");
        db.close();
    }

    public int totalAmountSum(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        int total_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(total_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " ", null);
        cursor.moveToFirst();
        total_amount = cursor.getInt(0);
        cursor.close();
        return total_amount;
    }

    public int totalAmountSumDay(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int total_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(total_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        total_amount = cursor.getInt(0);
        cursor.close();
        return total_amount;
    }

    public int nomalAmountSum(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'garage' AND pay_year = " + year + " AND pay_month = " + month + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }

    public int nomalAmountSumDay(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'garage' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }

    public int monthAmountSum(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'month' AND pay_year = " + year + " AND pay_month = " + month + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }

    public int monthAmountSumDay(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND lookup_type = 'month' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }

    public int cooperAmountSum(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        int cooper_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(cooper_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " ", null);
        cursor.moveToFirst();
        cooper_amount = cursor.getInt(0);
        cursor.close();
        return cooper_amount;
    }

    public int cooperAmountSumDay(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int cooper_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(cooper_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        cooper_amount = cursor.getInt(0);
        cursor.close();
        return cooper_amount;
    }

    public int payAmountSum(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }

    public int payAmountSumDay(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int pay_amount = 0;
        Cursor cursor = db.rawQuery("SELECT sum(pay_amount) FROM payment WHERE is_cancel = 'N' AND pay_year = " + year + " AND pay_month = " + month + " AND pay_day = " + day + " ", null);
        cursor.moveToFirst();
        pay_amount = cursor.getInt(0);
        cursor.close();
        return pay_amount;
    }
}