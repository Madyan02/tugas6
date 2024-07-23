package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.Note;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Note.TABLE_NAME + " ADD COLUMN " + Note.COLUMN_NOTE + " TEXT");
            db.execSQL("ALTER TABLE " + Note.TABLE_NAME + " ADD COLUMN " + Note.COLUMN_NOTEREGIS + " TEXT");
            db.execSQL("ALTER TABLE " + Note.TABLE_NAME + " ADD COLUMN " + Note.COLUMN_NOTEEMAIL + " TEXT");
            db.execSQL("ALTER TABLE " + Note.TABLE_NAME + " ADD COLUMN " + Note.COLUMN_NOTEPHONE + " TEXT");
        }
    }

    // Inserting a note
    public long insertNote(String note, String noteregis, String noteemail, String notephone) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add it
        values.put(Note.COLUMN_NOTE, note);
        values.put(Note.COLUMN_NOTEREGIS, noteregis);
        values.put(Note.COLUMN_NOTEEMAIL, noteemail);
        values.put(Note.COLUMN_NOTEPHONE, notephone);

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    // Getting a single note
    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_NOTEREGIS, Note.COLUMN_NOTEEMAIL, Note.COLUMN_NOTEPHONE},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            // prepare note object
            Note note = new Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Note.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEREGIS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEEMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEPHONE))
            );

            // close the db connection
            cursor.close();
            return note;
        } else {
            return null;
        }
    }

    // Getting all notes
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " + Note.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTE)));
                note.setNoteregis(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEREGIS)));
                note.setNoteemail(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEEMAIL)));
                note.setNotephone(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_NOTEPHONE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();

        // return notes list
        return notes;
    }

    // Updating a note
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getNote());
        values.put(Note.COLUMN_NOTEREGIS, note.getNoteregis());
        values.put(Note.COLUMN_NOTEEMAIL, note.getNoteemail());
        values.put(Note.COLUMN_NOTEPHONE, note.getNotephone());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    // Deleting a note
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    // Getting notes count
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteAllNotes() {
    }
}