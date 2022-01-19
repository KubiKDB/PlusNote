//package com.example.plusnote.dao;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.example.plusnote.entities.ListNote;
//
//import java.util.List;
//
//@Dao
//public interface ListDao {
//
//    @Query("SELECT * FROM listnotes ORDER BY ID DESC")
//    List<ListNote> getAllNotes();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertNote(ListNote listNote);
//
//    @Delete
//    void deleteNote(ListNote listNote);
//}
