package com.example.bigassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DBManager {
    private SQLiteDatabase sqlDB;
//    private SQLiteDatabase sqlDB_writer;

    // Database
    static final String DBname = "BigAssignment";

    // Table Account
    static final String Account_TableName = "Account";
    static final String Account_ColFullName = "FullName";
    static final String Account_ColEmail = "Email";
    static final String Account_ColPassword = "Password";
    static final String Account_ColID = "ID";
    static final int DBVersion = 1;
    static final String Sql_CreateTableAccount = "Create table IF NOT EXISTS " + Account_TableName +
            " (ID integer primary key autoincrement, " +
            Account_ColFullName + " text," +
            Account_ColEmail + " text," +
            Account_ColPassword + " text);";

    // Table Note
    static final String Note_TableName="Note";
    static final String Note_Account_ID="Account_ID";
    static final String Note_ColDateTime="DateTime";
    static final String Note_ColTitle="Title";
    static final String Note_ColDescription="Description";
    static final String Note_ColRemTime="Time";
    static final String Note_ColRemDate="Date";
    static final String Note_ColID="ID";
    static final String Sql_CreateTableNote = "Create table IF NOT EXISTS "+Note_TableName +
            "(ID integer primary key autoincrement,"+
            Note_ColDateTime+ " text,"+
            Note_ColTitle+" text,"+
            Note_ColDescription+" text,"+
            Note_ColRemTime+" text,"+
            Note_ColRemDate+" text,"+
            Note_Account_ID + " integer,"+
            " FOREIGN KEY ("+Note_Account_ID+") REFERENCES "+Account_TableName+"("+Account_ColID+"));";


    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;

        DatabaseHelperUser(Context context){
            super(context,DBname,null,DBVersion);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context, "Note database created", Toast.LENGTH_LONG).show();
            db.execSQL(Sql_CreateTableAccount);
            db.execSQL(Sql_CreateTableNote);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF EXISTS "+Account_TableName);
            db.execSQL("Drop table IF EXISTS "+Note_TableName);
            onCreate(db);
        }
    }

    DBManager(Context context){
        DatabaseHelperUser db = new DatabaseHelperUser(context);
        sqlDB = db.getWritableDatabase();
    }

    public long Insert(ContentValues values, String table_name){
        long ID = sqlDB.insert(table_name,"",values);
        return ID;
    }

    public Cursor query(String table_name, String[] Projection, String Selection, String[] SelectionArgs, String SortOrder){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(table_name);

        Cursor cursor = qb.query(sqlDB,Projection,Selection,SelectionArgs,null,null,SortOrder);
        return cursor;
    }

    public int Delete(String table_name, String Selection, String[] SelectionArgs){
        int count=sqlDB.delete(table_name,Selection,SelectionArgs);
        return count;
    }
    public int Update(String table_name,ContentValues values, String Selection, String[] SelectionArgs){
        int count=sqlDB.update(table_name,values,Selection,SelectionArgs);
        return count;
    }

    public long RowCount(String table_name){
        long count= DatabaseUtils.queryNumEntries(sqlDB,table_name);
        return count;
    }
}
