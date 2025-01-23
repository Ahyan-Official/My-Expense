package com.zybooks.myexpense.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE expense (id INTEGER PRIMARY KEY, idcategory INTEGER, description TEXT, amount INTEGER, date DEFAULT CURRENT_DATE);";
    private static final String CREATE_TABLE_INCOME = "CREATE TABLE income (id INTEGER PRIMARY KEY, idcategory INTEGER, description TEXT, amount INTEGER, date DEFAULT CURRENT_DATE);";
    private static final String CREATE_TABLE_CATEGORY_INCOME = "CREATE TABLE category_income (id INTEGER PRIMARY KEY, type TEXT);";
    private static final String CREATE_TABLE_CATEGORY_EXPENSE = "CREATE TABLE category_expense (id INTEGER PRIMARY KEY, type TEXT);";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        // Create Table
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_INCOME);
        db.execSQL(CREATE_TABLE_CATEGORY_INCOME);
        db.execSQL(CREATE_TABLE_CATEGORY_EXPENSE);

        // Insert To Table category_income
        db.execSQL("INSERT INTO 'category_income' ('type') VALUES ('Salary') ");
        db.execSQL("INSERT INTO 'category_income' ('type') VALUES ('Profit') ");
        db.execSQL("INSERT INTO 'category_income' ('type') VALUES ('Gift') ");
        db.execSQL("INSERT INTO 'category_income' ('type') VALUES ('Others') ");

        // Insert To Table category_expense
        db.execSQL("INSERT into 'category_expense' ('type') VALUES ('Bills') ");
        db.execSQL("INSERT into 'category_expense' ('type') VALUES ('Transportation') ");
        db.execSQL("INSERT into 'category_expense' ('type') VALUES ('Shopping') ");
        db.execSQL("INSERT into 'category_expense' ('type') VALUES ('Health') ");
        db.execSQL("INSERT into 'category_expense' ('type') VALUES ('Others') ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public void onReset(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // on reset drop older tables
        db.execSQL("DROP TABLE IF EXISTS expense");
        db.execSQL("DROP TABLE IF EXISTS income");
        db.execSQL("DROP TABLE IF EXISTS category_income");
        db.execSQL("DROP TABLE IF EXISTS category_expense");

        onCreate(db);
    }
}
