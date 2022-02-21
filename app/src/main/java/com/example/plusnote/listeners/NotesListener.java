package com.example.plusnote.listeners;

import com.example.plusnote.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
    void onNoteClicked(Note note, int position, boolean deleteNote);
    void onNoteClicked(Note note, int position, boolean deleteNote, boolean shareNote);
    void onNoteClicked(Note note, int position, boolean deleteNote, boolean shareNote, boolean setReminder);
}
