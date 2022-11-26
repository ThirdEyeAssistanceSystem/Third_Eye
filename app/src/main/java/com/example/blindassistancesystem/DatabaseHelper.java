package com.example.blindassistancesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static  final String DatabaseName="ThirdEye";
    public static final String TableName="EmergencyContact";
    public static final String Name="Name";
    public static final String Number="Number";
    private SQLiteDatabase db;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName,null, 3);
        this.db = db;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table " + TableName + "(Name text, Number text)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String query = "drop table if exists " + TableName + "";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
    public  void AddRecord(Contact record)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(Name, record.getName());
        contentValues.put(Number, record.getNumber());

        SQLiteDatabase database=this.getWritableDatabase();
        database.insert(TableName, null, contentValues);
        database.close();
    }
    public ArrayList<Contact> fetchAlldata()
    {
        ArrayList<Contact>arrayList=new ArrayList<Contact>();
        SQLiteDatabase database=this.getReadableDatabase();

        Cursor cursor=database.rawQuery("Select * from EmergencyContact",null);
        if(cursor.moveToFirst())
        {
            do {
                Contact contact=new Contact();
                contact.setName(cursor.getString(0));
                contact.setNumber(cursor.getString(1));
                arrayList.add(contact);
            }
            while (cursor.moveToNext());
        }
        return arrayList;
    }

    public void DeleteData(String Name, String Number)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TableName,"Name=? and Number=?", new String[]{Name,Number});
        db.close();
    }

    public void updateContact(String Cname, String Cnumber, String Name, String Number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", Cname);
        values.put("Number", Cnumber);
        db.update(TableName, values, "Name=? and Number=?", new String[] {Name,Number});
        db.close();
    }
}
