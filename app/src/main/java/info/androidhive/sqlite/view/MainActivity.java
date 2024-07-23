package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.RecyclerTouchListener;
import info.androidhive.sqlite.view.NotesAdapter.NoteItemListener;

public class MainActivity extends AppCompatActivity implements NotesAdapter.NoteItemListener {

    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        ImageButton deleteAllButton = findViewById(R.id.delete_all);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllDialog();
            }
        });

        mAdapter = new NotesAdapter(this, notesList, this); // Pass 'this' as NoteItemListener
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                // Do nothing on click
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void createNote(String note, String noteregis, String noteemail, String notephone) {
        long id = db.insertNote(note, noteregis, noteemail, notephone);
        Note n = db.getNote(id);

        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }

    private void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showNoteDialog(true, notesList.get(position), position);
                        break;
                    case 1:
                        deleteNote(notesList.get(position));
                        break;
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        final EditText inputNoteregis = view.findViewById(R.id.noteregis);
        final EditText inputNoteemail = view.findViewById(R.id.noteemail);
        final EditText inputNotephone = view.findViewById(R.id.notephone);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.create_student) : getString(R.string.lbl_edit_note_title));


        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
            inputNoteregis.setText(note.getNoteregis());
            inputNoteemail.setText(note.getNoteemail());
            inputNotephone.setText(note.getNotephone());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString()) || TextUtils.isEmpty(inputNoteregis.getText().toString()) || TextUtils.isEmpty(inputNoteemail.getText().toString()) || TextUtils.isEmpty(inputNotephone.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                String noteText = inputNote.getText().toString();
                String regisText = inputNoteregis.getText().toString();
                String emailText = inputNoteemail.getText().toString();
                String phoneText = inputNotephone.getText().toString();

                if (shouldUpdate && note != null) {
                    // Update existing note
                    note.setNote(noteText);
                    note.setNoteregis(regisText);
                    note.setNoteemail(emailText);
                    note.setNotephone(phoneText);

                    int affectedRows = db.updateNote(note);
                    if (affectedRows > 0) {
                        notesList.set(position, note);
                        mAdapter.notifyDataSetChanged();
                        toggleEmptyNotes();
                    }
                } else {
                    // Create new note
                    createNote(noteText, regisText, emailText, phoneText);
                }
            }
        });
    }

    private void deleteNote(Note note) {
        db.deleteNote(note);
        notesList.remove(note);
        mAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    private void deleteAllNotes() {
        db.deleteAllNotes();
        notesList.clear();
        mAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    private void showDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Notes");
        builder.setMessage("Are you sure you want to delete all notes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllNotes();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void toggleEmptyNotes() {
        if (notesList.isEmpty()) {
            noNotesView.setVisibility(View.VISIBLE);
        } else {
            noNotesView.setVisibility(View.GONE);
        }
    }

    private void setBoldOnFocus(EditText editText, final TextView textView) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textView.setTypeface(null, Typeface.BOLD);
                } else {
                    textView.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        deleteNote(notesList.get(position));
    }

    @Override
    public void onEditClick(int position) {
        showNoteDialog(true, notesList.get(position), position);
    }
}