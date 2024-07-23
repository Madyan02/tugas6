package info.androidhive.sqlite.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    private NoteItemListener itemListener; // Listener untuk meng-handle klik edit dan delete

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView noteregis;
        public TextView noteemail;
        public TextView notephone;
        public ImageButton delete;
        public ImageButton edit;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            noteregis = view.findViewById(R.id.noteregis);
            noteemail = view.findViewById(R.id.noteemail);
            notephone = view.findViewById(R.id.notephone);
            delete = view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemListener.onDeleteClick(position);
                        }
                    }
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemListener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }

    public NotesAdapter(Context context, List<Note> notesList, NoteItemListener itemListener) {
        this.context = context;
        this.notesList = notesList;
        this.itemListener = itemListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.note.setText(note.getNote());

        StringBuilder regisBuilder = new StringBuilder();
        regisBuilder.append("Registration No ").append(note.getNoteregis());
        SpannableString spannableRegis = new SpannableString(regisBuilder.toString());
        spannableRegis.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.noteregis.setText(spannableRegis);

        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append("Email ").append(note.getNoteemail());
        SpannableString spannableEmail = new SpannableString(emailBuilder.toString());
        spannableEmail.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.noteemail.setText(spannableEmail);

        StringBuilder phoneBuilder = new StringBuilder();
        phoneBuilder.append("Phone ").append(note.getNotephone());
        SpannableString spannablePhone = new SpannableString(phoneBuilder.toString());
        spannablePhone.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.notephone.setText(spannablePhone);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    // Interface untuk menangani aksi klik pada adapter
    public interface NoteItemListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
}