package com.example.plusnote.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.plusnote.entities.TextNote;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM textnotes ORDER BY ID DESC")
    List<TextNote> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(TextNote textNote);

    @Delete
    void deleteNote(TextNote textNote);
}
