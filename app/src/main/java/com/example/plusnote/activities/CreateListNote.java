package com.example.plusnote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.plusnote.R;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.TextNote;

import java.util.ArrayList;

public class CreateListNote extends AppCompatActivity {

    EditText inputListName;
    EditText[] inputLists = new EditText[24];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list_note);
        ImageButton list_icon = findViewById(R.id.list_icon);
        list_icon.setEnabled(false);
        inputListName = findViewById(R.id.inputListName);
        ImageButton cancelButton1 = findViewById(R.id.cancelButton1);
        ImageButton doneButtonList = findViewById(R.id.doneButtonList);
        inputLists[0] = findViewById(R.id.inputList1);
        inputLists[1] = findViewById(R.id.inputList2);
        inputLists[2] = findViewById(R.id.inputList3);
        inputLists[3] = findViewById(R.id.inputList4);
        inputLists[4] = findViewById(R.id.inputList5);
        inputLists[5] = findViewById(R.id.inputList6);
        inputLists[6] = findViewById(R.id.inputList7);
        inputLists[7] = findViewById(R.id.inputList8);
        inputLists[8] = findViewById(R.id.inputList9);
        inputLists[9] = findViewById(R.id.inputList10);
        inputLists[10] = findViewById(R.id.inputList11);
        inputLists[11] = findViewById(R.id.inputList12);
        inputLists[12] = findViewById(R.id.inputList13);
        inputLists[13] = findViewById(R.id.inputList14);
        inputLists[14] = findViewById(R.id.inputList15);
        inputLists[15] = findViewById(R.id.inputList16);
        inputLists[16] = findViewById(R.id.inputList17);
        inputLists[17] = findViewById(R.id.inputList18);
        inputLists[18] = findViewById(R.id.inputList19);
        inputLists[19] = findViewById(R.id.inputList20);
        inputLists[20] = findViewById(R.id.inputList21);
        inputLists[21] = findViewById(R.id.inputList22);
        inputLists[22] = findViewById(R.id.inputList23);
        inputLists[23] = findViewById(R.id.inputList24);
        ////////
        inputListName.requestFocus();
        inputListName.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputListName, InputMethodManager.SHOW_FORCED);
        inputListName.setEnabled(true);
        cancelButton1.setOnClickListener(view -> onBackPressed());
        doneButtonList.setOnClickListener(view -> {
            finish();
//            saveNote();
        });
    }
//    private void saveNote() {
//        if (inputListName.getText().toString().trim().isEmpty()) {
//            return;
//        }
//
//        final ListNote listNote = new ListNote();
//        listNote.setTitle(inputListName.getText().toString());
//        String[] temp = new String[24];
//        for (int i = 0; i < temp.length; i++) {
//            if (!inputLists[i].getText().toString().trim().isEmpty()){
//                temp[i] = String.valueOf(inputLists[i].getText());
//            }
//        }
//        listNote.setItems(temp);
//
//        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                NotesDatabase.getDatabase(getApplicationContext()).listDao().insertNote(listNote);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void unused) {
//                super.onPostExecute(unused);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }
//        new SaveNoteTask().execute();
//    }

}