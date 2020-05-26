package com.example.mobilproje;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "events.db";
    public static final int DATABASE_VERSION = 2;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  \"events\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"category\"\tTEXT,\n" +
                "\t\"detail\"\tTEXT,\n" +
                "\t\"date\"\tTEXT,\n" +
                "\t\"time\"\tTEXT,\n" +
                "\t\"hatirlatmaSure\"\tTEXT,\n" +
                "\t\"hatirlatmaTip\"\tTEXT,\n" +
                "\t\"tekrarSure\"\tTEXT,\n" +
                "\t\"tekrarTip\"\tTEXT,\n" +
                "\t\"location\"\tTEXT\n" +
                ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "events");
        onCreate(db);
    }
}