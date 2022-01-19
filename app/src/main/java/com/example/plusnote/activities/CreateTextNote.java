package com.example.plusnote.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plusnote.R;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.TextNote;

import java.util.ArrayList;

public class CreateTextNote extends AppCompatActivity {
    EditText inputTextTitle, inputTextNote;

    private TextNote alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_note);
        ///////
        ImageButton cancelButton = findViewById(R.id.cancelButton);
        ImageButton plus = findViewById(R.id.plus);
        ImageButton doneButton = findViewById(R.id.doneButton);
        ImageButton deleteButton = findViewById(R.id.deleteButton);
        ///////
        inputTextTitle = findViewById(R.id.inputTextTitle);
        inputTextNote = findViewById(R.id.inputTextNote);
        ///////
        ConstraintLayout createNote = findViewById(R.id.createNote);
        ConstraintLayout blur = findViewById(R.id.blur);
        ConstraintLayout textOutContainer = findViewById(R.id.textOutContainer);
        ////////
        inputTextTitle.requestFocus();
        inputTextTitle.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputTextTitle, InputMethodManager.SHOW_FORCED);
        inputTextTitle.post(new Runnable() {
            @Override
            public void run() {
                inputTextTitle.setSelection(inputTextTitle.getText().length());
            }
        });
        inputTextTitle.setEnabled(true);
        cancelButton.setOnClickListener(view -> {
            onBackPressed();
        });
        doneButton.setOnClickListener(view -> {
            saveNote();
//            String temp = String.valueOf(inputTextTitle.getText());
//            if (!temp.trim().isEmpty()) {
//                textOutContainer.setVisibility(View.VISIBLE);
//                textOut.setText(inputTextTitle.getText());
//                createNote.setVisibility(View.GONE);
//                plus.setRotation(0);
//                inputTextTitle.setEnabled(false);
//                inputTextNote.setEnabled(false);
//                arrayAdapter.notifyDataSetChanged();
//                onBackPressed();
//        }
        });

        if (getIntent().getBooleanExtra("IsViewOrUpdate", false)){
            alreadyAvailableNote = (TextNote) getIntent().getSerializableExtra("textnote");
            setViewOrUpdateTextNote();
        }

        if (alreadyAvailableNote != null){
            deleteButton.setOnClickListener(view -> {
                deleteTextNote();
            });
        }

    }

    private void deleteTextNote() {
        class DeleteTaskNote extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                        .deleteNote(alreadyAvailableNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                intent.putExtra("isNoteDeleted", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new DeleteTaskNote().execute();
    }

    private void setViewOrUpdateTextNote(){
        inputTextTitle.setText(alreadyAvailableNote.getTitle());
        inputTextNote.setText(alreadyAvailableNote.getNote_text());
    }

    private void saveNote() {
        if (inputTextTitle.getText().toString().trim().isEmpty()) {
            return;
        }

        final TextNote textNote = new TextNote();
        textNote.setTitle(inputTextTitle.getText().toString());
        textNote.setNote_text(inputTextNote.getText().toString());

        if (alreadyAvailableNote != null){
            textNote.setId(alreadyAvailableNote.getId());
        }

        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(textNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }
}