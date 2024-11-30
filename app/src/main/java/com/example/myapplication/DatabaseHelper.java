package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlayersDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PLAYERS = "players";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_FIRSTNAME = "firstName";

    private Cursor cursor;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

@Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYERS + "("
                + COLUMN_NUMBER + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_FIRSTNAME + " TEXT)";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    public boolean addPlayer(int number, String name, String firstName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_FIRSTNAME, firstName);

        long result = db.insert(TABLE_PLAYERS, null, values);
        return result != -1;
    }

    public boolean deletePlayer(int number) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PLAYERS, COLUMN_NUMBER + "=?", new String[]{String.valueOf(number)});
        return rows > 0;
    }

    public String[] getPlayerByNumber(int number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_NUMBER + "=?", new String[]{String.valueOf(number)});
        if (cursor.moveToFirst()) {
            String[] player = {cursor.getString(1), cursor.getString(2)};
            cursor.close();
            return player;
        }
        cursor.close();
        return null;
    }

    public String getAllPlayers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYERS, null);
        StringBuilder players = new StringBuilder();
        while (cursor.moveToNext()) {
            players.append("Number: ").append(cursor.getInt(0))
                    .append("\nName: ").append(cursor.getString(1))
                    .append("\nFirst Name: ").append(cursor.getString(2))
                    .append("\n\n");
        }
        cursor.close();
        return players.toString();
    }

    public String[] getNextPlayer() {
        if (cursor == null || cursor.isClosed()) {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYERS, null);
            if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
        }
        if (cursor.moveToNext()) {
            return new String[]{String.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2)};
        }
        return null;
    }

    public String[] getPreviousPlayer() {
        if (cursor != null && !cursor.isClosed() && cursor.moveToPrevious()) {
            return new String[]{String.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2)};
        }
        return null;
    }

    public String[] getFirstPlayer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYERS, null);
        if (cursor.moveToFirst()) {
            String[] player = {String.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2)};
            cursor.close();
            return player;
        }
        return null;
    }
}
