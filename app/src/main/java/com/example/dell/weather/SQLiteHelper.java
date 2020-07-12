package com.example.dell.weather;

/**
 * Created by Dell on 26-05-2020.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }

    //insert data
    public void insertData(String name)
    {
        SQLiteDatabase database = getWritableDatabase();
//        query to insert data in DB table
//        RECORD is table name which have crated on  home activity
        String query = "INSERT INTO HomeCityRecord VALUES(NULL,?)";

        SQLiteStatement statement = database.compileStatement(query);

        statement.clearBindings();
        statement.bindString(1,name);
        statement.executeInsert();

    }

    //update Data
//    public void updateData(String name, String Relation, String contactNumber ,byte[] image,int id)
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        //query to update data
//        String sql = "UPDATE RECORD SET name=? , relation=?, contactNumber=? ,image=? where id=?";
//        SQLiteStatement statement = database.compileStatement(sql);
//
//
//        statement.bindString(1,name);
//        statement.bindString(2,Relation);
//        statement.bindString(3,contactNumber);
//        statement.bindBlob(4,image);
//        statement.bindDouble(5,(double)id);
//
//        statement.executeInsert();
//        database.close();
//    }

    //delete Data
    public void deleteData(int id)
    {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM HomeCityRecord WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();;
        statement.bindDouble(1,(double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
