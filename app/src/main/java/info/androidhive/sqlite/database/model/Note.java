package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_NOTEREGIS = "noteregis";
    public static final String COLUMN_NOTEEMAIL = "noteemail";
    public static final String COLUMN_NOTEPHONE = "notephone";

    private int id;
    private String note;
    private String noteregis;
    private String noteemail;
    private String notephone;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_NOTEREGIS + " TEXT,"
                    + COLUMN_NOTEEMAIL + " TEXT,"
                    + COLUMN_NOTEPHONE + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String note, String noteregis, String noteemail, String notephone) {
        this.id = id;
        this.note = note;
        this.noteregis = noteregis;
        this.noteemail = noteemail;
        this.notephone = notephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteregis() {
        return noteregis;
    }

    public void setNoteregis(String noteregis) {
        this.noteregis = noteregis;
    }

    public String getNoteemail() {
        return noteemail;
    }

    public void setNoteemail(String noteemail) {
        this.noteemail = noteemail;
    }

    public String getNotephone() {
        return notephone;
    }

    public void setNotephone(String notephone) {
        this.notephone = notephone;
    }
}