package com.example.plusnote.adapters;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plusnote.R;
import com.example.plusnote.entities.TextNote;
import com.example.plusnote.listeners.NotesListener;

import java.util.List;

public class TextNoteAdapter extends RecyclerView.Adapter<TextNoteAdapter.TextNoteViewHolder> {

    private List<TextNote> textNotes;
    private NotesListener notesListener;

    public TextNoteAdapter(List<TextNote> textNotes, NotesListener notesListener) {
        this.textNotes = textNotes;
        this.notesListener = notesListener;
    }

    @NonNull
    @Override
    public TextNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextNoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recycler_view_item,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TextNoteViewHolder holder, int position) {
        holder.setTextNote(textNotes.get(position));
        holder.textOut.setOnClickListener(view -> {
            holder.textOut.setTextColor(Color.parseColor("#50000000"));
        });
        holder.text_icon.setOnClickListener(view -> {
            notesListener.onTextNoteClicked(textNotes.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return textNotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TextNoteViewHolder extends RecyclerView.ViewHolder {

        ImageButton checkbox;
        TextView textOut;
        ImageButton text_icon;
        LinearLayout linearLayout;

        TextNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textOut = itemView.findViewById(R.id.TitleOut);
            checkbox = itemView.findViewById(R.id.checkbox);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            text_icon = itemView.findViewById(R.id.text_container_icon);
        }

        void setTextNote(TextNote textNote){
            textOut.setText(textNote.getTitle());
        }
    }
}
