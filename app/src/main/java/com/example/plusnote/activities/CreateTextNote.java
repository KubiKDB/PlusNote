package com.example.plusnote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.plusnote.R;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.Note;

public class CreateTextNote extends AppCompatActivity {
    private ImageButton
            cancelButton,
            deleteButton,
            editButton,
            reminderButton,
            copyButton,
            shareButton,
            doneButton;
    private EditText inputTextTitle, inputTextNote;
    private Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_note);
        ///////
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        reminderButton = findViewById(R.id.reminderButton);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);
        doneButton = findViewById(R.id.doneButton);
        ///////
        inputTextTitle = findViewById(R.id.inputTextTitle);
        inputTextNote = findViewById(R.id.inputTextNote);
        //////

        editButton.setEnabled(false);
        copyButton.setEnabled(false);
        shareButton.setEnabled(false);
        doneButton.setEnabled(false);
        reminderButton.setEnabled(false);

        inputTextTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputTextTitle.getText().toString().trim().isEmpty()){
                    doneButton.setEnabled(true);
                    doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButton.setEnabled(true);
                    reminderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButton.setEnabled(false);
                    doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButton.setEnabled(false);
                    reminderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        inputTextTitle.requestFocus();
        inputTextTitle.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputTextTitle, InputMethodManager.SHOW_FORCED);
        inputTextTitle.post(() -> inputTextTitle.setSelection(inputTextTitle.getText().length()));

        cancelButton.setOnClickListener(view -> {
            onBackPressed();
            imm.hideSoftInputFromWindow(inputTextTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButton.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputTextTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });

        editButton.setOnClickListener(view -> {
            inputTextTitle.setEnabled(true);
            inputTextNote.setEnabled(true);
            inputTextTitle.requestFocus();
            inputTextTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputTextTitle, InputMethodManager.SHOW_FORCED);
            inputTextTitle.post(() -> inputTextTitle.setSelection(inputTextTitle.getText().length()));
            doneButton.setEnabled(true);
            doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

        });

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            if (getIntent().getBooleanExtra("deleteNote", false)){
                deleteNote();
            }
            setViewOrUpdateNote();
        }

        if (alreadyAvailableNote != null){
            deleteButton.setOnClickListener(view -> {
                deleteNote();
            });
        }
    }

    private void deleteNote(){
        class DeleteNoteTask extends AsyncTask<Void,Void,Void>{
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
        new DeleteNoteTask().execute();
    }

    private void setViewOrUpdateNote() {
        inputTextTitle.setEnabled(false);
        inputTextNote.setEnabled(false);
        editButton.setEnabled(true);
        copyButton.setEnabled(true);
        shareButton.setEnabled(true);
        doneButton.setEnabled(false);

        Log.e("DateSavedText", alreadyAvailableNote.getDate());

        deleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        copyButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));


        inputTextTitle.setText(alreadyAvailableNote.getTextNoteTitle());
        inputTextNote.setText(alreadyAvailableNote.getTextNoteText());
    }

    private void saveNote() {
        if (inputTextTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputTextTitle.getText().toString());
        note.setTextNoteText(inputTextNote.getText().toString());
        note.setIs_list(false);
        note.setIs_image(false);

        if (alreadyAvailableNote != null) {
            note.setDate(alreadyAvailableNote.getDate());
            note.setId(alreadyAvailableNote.getId());
        } else {
            note.setDate(MainActivity.notesDay);
        }

        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
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