//package com.example.shacharchrisphotoalbum.photoalbumandroid04;
//
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.Cursor;
//import android.content.Context;
//import android.content.ContentValues;
//
//import model.Album;
//
//
//public class MyDBHandler extends SQLiteOpenHelper {
//
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "albums.db";
//    public static final String TABLE_ALBUMS = "albums";
//    public static final String ALBUMS_COLUMN_ID = "_id";
//    public static final String ALBUMS_COLUMN_ALBUM_NAME = "albumName";
//    public static final String ALBUMS_COLUMN_SIZE = "size";
//
//    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String query = "CREATE TABLE " + TABLE_ALBUMS + "(" +
//                ALBUMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
//                ALBUMS_COLUMN_ALBUM_NAME + " TEXT " +
//                ALBUMS_COLUMN_SIZE + " INTEGER" +
//                ");";
//
//
//        sqLiteDatabase.execSQL(query);
//
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);
//        onCreate(sqLiteDatabase);
//    }
//
//    // Add a new row to the database
//    public void addAlbum(Album album) {
//        ContentValues values = new ContentValues();
//        values.put(ALBUMS_COLUMN_ALBUM_NAME, album.getAlbumName());
//        values.put(ALBUMS_COLUMN_SIZE, album.getSize());
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert(TABLE_ALBUMS, null, values);
//        db.close();
//    }
//
//
//    //Delete an album from the database
//    public void deleteAlbum(String albumName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_ALBUMS + " WHERE " + ALBUMS_COLUMN_ALBUM_NAME + "=\"" + albumName + "\";" );
//    }
//
//    //Print out the database as a string
//    public String databaseToString() {
//        String dbString = "";
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_ALBUMS + " WHERE 1;";
//
//        //Cursor point to a location in the results
//        Cursor c = db.rawQuery(query, null);
//        //Move to the first row in the results
//        c.moveToFirst();
//
//        while(!c.isAfterLast()) {
//            if(c.getString(c.getColumnIndex("albumName")) != null) {
//                dbString += c.getString(c.getColumnIndex("albumName"));
//                dbString += "\n";
//            }
//        }
//
//        db.close();
//        return dbString;
//    }
//
//
//
//}
