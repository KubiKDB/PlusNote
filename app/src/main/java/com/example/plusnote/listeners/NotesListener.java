package com.example.plusnote.listeners;

import com.example.plusnote.entities.TextNote;

public interface NotesListener {
    void onTextNoteClicked(TextNote textNote, int position);
}
