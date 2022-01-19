package com.example.plusnote.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.plusnote.R;
import com.example.plusnote.adapters.MyAdapter2;
import com.example.plusnote.adapters.TextNoteAdapter;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.TextNote;
import com.example.plusnote.listeners.NotesListener;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements NotesListener {
    private RecyclerView notesRecyclerView;
    private List<TextNote> textNoteList;
    private TextNoteAdapter textNoteAdapter;

//    private RecyclerView listNotesRecyclerView;
//    private List<ListNote> listNoteList;
//    private ListNoteAdapter listNoteAdapter;

    private int noteClickedPosition = -1;
    public static final int RESULT_CODE_ADD_NOTE = 1;
    public static final int RESULT_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static LocalDate stLdate = LocalDate.now();
    public static int pageNumberForDay = 0;
    public int year_count = 0;
    public int savedYearNum = 0;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) finish();
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        player.setVolume(100, 100);
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;
        OnClickListener clicker = v -> {
            onRecord(mStartRecording);
            if (mStartRecording) {
                setText("Stop recording");
            } else {
                setText("Start recording");
            }
            mStartRecording = !mStartRecording;
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;
        OnClickListener clicker = v -> {
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                setText("Stop playing");
            } else {
                setText("Start playing");
            }
            mStartPlaying = !mStartPlaying;
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);
        //////////
        RecordButton recordButton = new RecordButton(this);
        PlayButton playButton = new PlayButton(this);
        /////////
        ImageButton plus = findViewById(R.id.plus);
        ImageButton addText = findViewById(R.id.text_create);
        ImageButton doneButton = findViewById(R.id.doneButton);
        ImageButton editButton = findViewById(R.id.editButton);
        ImageButton deleteButton = findViewById(R.id.deleteButton);
        ImageButton cancelButton = findViewById(R.id.cancelButton);
        ImageButton voice_create = findViewById(R.id.voice_create);
        ImageButton doneButtonVoice = findViewById(R.id.doneButton1);
//        ImageButton doneButtonList = findViewById(R.id.doneButtonList);
        ImageButton list_create = findViewById(R.id.list_create);
//        ImageButton search = findViewById(R.id.search);
//        ImageButton checkBox = findViewById(R.id.checkbox);
        ////////
        ConstraintLayout createTextNote = findViewById(R.id.createText);
        ConstraintLayout createVoice = findViewById(R.id.createVoice);
        ConstraintLayout createNote = findViewById(R.id.createNote);
        ConstraintLayout VoiceOutContainer = findViewById(R.id.VoiceOutContainer);
        ConstraintLayout main = findViewById(R.id.MainLayout);
        ConstraintLayout createListNote = findViewById(R.id.createList);
        ConstraintLayout blur = findViewById(R.id.blur);
        ConstraintLayout blur1 = findViewById(R.id.blur1);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        ConstraintLayout listOutContainer = findViewById(R.id.ListOutContainer);
        ConstraintLayout month_choose = findViewById(R.id.month_choose);
        ConstraintLayout year_choose_layout = findViewById(R.id.year_choose_layout);
//        ConstraintLayout noAds = findViewById(R.id.noAdsContainer);
        //////////
        EditText inputText = findViewById(R.id.inputText);
        EditText searchTxt = findViewById(R.id.searchEditText);
        EditText inputTextNote = findViewById(R.id.inputTextNote);
        EditText voice_input = findViewById(R.id.inputVoiceText);
//        EditText inputListName = findViewById(R.id.inputListName);
//        EditText list_item1 = findViewById(R.id.inputList1);
        //////////
        TextView listDescription = findViewById(R.id.ListOut);
        TextView app_name = findViewById(R.id.app_name);
        TextView voiceDescription = findViewById(R.id.VoiceDescription);
        TextView month_view = findViewById(R.id.month_view);
        TextView year_view = findViewById(R.id.year_view);
        TextView yearChoose = findViewById(R.id.year_choose);
        TextView yearChoose1 = findViewById(R.id.year_choose1);
        TextView yearChoose2 = findViewById(R.id.year_choose2);
        TextView yearChoose3 = findViewById(R.id.year_choose3);
        TextView yearChoose4 = findViewById(R.id.year_choose4);
        TextView jan = findViewById(R.id.jan);
        TextView feb = findViewById(R.id.feb);
        TextView mar = findViewById(R.id.mar);
        TextView apr = findViewById(R.id.apr);
        TextView may = findViewById(R.id.may);
        TextView jun = findViewById(R.id.jun);
        TextView jul = findViewById(R.id.jul);
        TextView aug = findViewById(R.id.aug);
        TextView sep = findViewById(R.id.sep);
        TextView oct = findViewById(R.id.oct);
        TextView nov = findViewById(R.id.nov);
        TextView dec = findViewById(R.id.dec);
        TextView year1 = findViewById(R.id.year1);
        TextView year2 = findViewById(R.id.year2);
//        TextView textOut = findViewById(R.id.textOut);
        ////////
        TextView[] months = new TextView[12];
        months[0] = jan;
        months[1] = feb;
        months[2] = mar;
        months[3] = apr;
        months[4] = may;
        months[5] = jun;
        months[6] = jul;
        months[7] = aug;
        months[8] = sep;
        months[9] = oct;
        months[10] = nov;
        months[11] = dec;
        TextView[] years = new TextView[5];
        years[0] = yearChoose;
        years[1] = yearChoose1;
        years[2] = yearChoose2;
        years[3] = yearChoose3;
        years[4] = yearChoose4;
        ////////
        blur.setElevation(3);
        constraintLayout.setElevation(4);
        blur1.setElevation(6);
        month_choose.setElevation(7);
        /////////
        ViewPager2 pager1 = findViewById(R.id.vp2);
        FragmentStateAdapter pageAdapter1 = new MyAdapter2(this);
        pager1.setAdapter(pageAdapter1);
        /////////
        LocalDate ld1 = LocalDate.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy EEEE", Locale.ENGLISH);
        final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy MMM dd EEEE", Locale.ENGLISH);
        month_view.setText(dtf.format(ld1));
        year_view.setText(dtf1.format(ld1));
//        int pn = (ld1.getDayOfYear() + 4 + 365 * (year_count - 2022)) / 7;
//        pager1.setCurrentItem(pn);
        LocalDate ld = LocalDate.of(2022, Month.JANUARY, 1);
        year1.setText(year_view.getText());
        String days = dtf.format(ld);
        for (int i = 0; i < months.length; i++) {
            months[i].setText(days);
            ld = ld.plusMonths(1);
            days = dtf.format(ld);
        }
        year_view.setOnClickListener(view -> {
            year_choose_layout.setVisibility(View.VISIBLE);
            blur1.setVisibility(View.VISIBLE);
            savedYearNum = Integer.parseInt(String.valueOf(year_view.getText()));
            LocalDate localDate = LocalDate.now();
            String yearStr = dtf1.format(localDate);
            for (int i = 0; i < years.length; i++) {
                years[i].setText(yearStr);
                localDate = localDate.plusYears(1);
                yearStr = dtf1.format(localDate);
            }
        });
        for (int i = 0; i < years.length; i++) {
            int finalI = i;
            years[i].setOnClickListener(view -> {
                year_choose_layout.setVisibility(View.GONE);
                month_choose.setVisibility(View.VISIBLE);
                year_view.setText(years[finalI].getText());
                year1.setText(years[finalI].getText());
                year_count = Integer.parseInt(String.valueOf(years[finalI].getText()));
            });
        }
        month_view.setOnClickListener(view -> {
            month_choose.setVisibility(View.VISIBLE);
            savedYearNum = Integer.parseInt(String.valueOf(year_view.getText()));
            blur1.setVisibility(View.VISIBLE);
            year1.setText(year_view.getText());
        });
        for (int i = 0; i < months.length; i++) {
            int finalI = i;
            months[i].setOnClickListener(view -> {
                month_view.setText(months[finalI].getText());
                pager1.setAdapter(pageAdapter1);
                LocalDate localDate = LocalDate.of(year_count, finalI + 1, 1);
                int pagenum = 0;
                savedYearNum = Integer.parseInt(String.valueOf(year_view.getText()));
                if (year_count > 2024) {
                    pagenum = (localDate.getDayOfYear() + 4 + 366 + 365 * (year_count - 2023)) / 7;
                } else pagenum = (localDate.getDayOfYear() + 4 + 365 * (year_count - 2022)) / 7;
                pageNumberForDay = pagenum;
                year_count = Integer.parseInt(String.valueOf(year_view.getText()));
                stLdate = LocalDate.of(2022, finalI + 1, 1);
                pager1.setCurrentItem(pagenum);
                month_choose.setVisibility(View.GONE);
                blur1.setVisibility(View.GONE);
            });
        }
        ///////
        year1.setOnClickListener(view -> {

        });
        list_create.setOnClickListener(view -> {
            startActivityForResult(new Intent(getApplicationContext(), CreateListNote.class), RESULT_CODE_ADD_NOTE);
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
            inputText.setText("");
//            search.setVisibility(View.VISIBLE);
        });
//        doneButtonList.setOnClickListener(view -> {
//            if (!inputListName.getText().toString().trim().isEmpty()) {
//                createListNote.setVisibility(View.GONE);
//                createNote.setVisibility(View.GONE);
//                search.setVisibility(View.VISIBLE);
//                plus.setRotation(0);
//                plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//                blur.setVisibility(View.GONE);
//                listDescription.setText(inputListName.getText());
//                listOutContainer.setVisibility(View.VISIBLE);
//
//                list_item1.setCursorVisible(false);
//                list_item1.setInputType(0);
//                list_item1.setClickable(true);
//                list_item1.setFocusable(false);
//                list_item1.setOnClickListener(view1 -> {
//                    list_item1.setTextColor(Color.parseColor("#70FFFFFF"));
//                });
//            }
//        });
//        listDescription.setOnClickListener(view -> {
//            createListNote.setVisibility(View.VISIBLE);
//        });
//        cancelListButton.setOnClickListener(view -> {
//            createListNote.setVisibility(View.GONE);
//            createNote.setVisibility(View.GONE);
//            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//            plus.setRotation(0);
//            blur.setVisibility(View.GONE);
//        });
        voice_create.setOnClickListener(view -> {
            createVoice.setVisibility(View.VISIBLE);
//            noAds.setVisibility(View.GONE);
        });

        VoiceOutContainer.addView(
                playButton,
                new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

        createVoice.addView(
                recordButton,
                new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
        doneButtonVoice.setOnClickListener(view -> {
            VoiceOutContainer.setVisibility(View.VISIBLE);
            voiceDescription.setText(voice_input.getText());
            voice_input.setText("");
            createVoice.setVisibility(View.GONE);
            createNote.setVisibility(View.GONE);
//            search.setVisibility(View.VISIBLE);
            plus.setRotation(0);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            blur.setVisibility(View.GONE);
        });

        int i = 0;
        doneButton.setOnClickListener(view -> {
            String temp = String.valueOf(inputText.getText());
            if (temp.length() > 0) {
                createNote.setVisibility(View.GONE);
                plus.setRotation(0);
//                search.setVisibility(View.VISIBLE);
                plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                temp = "";
                blur.setVisibility(View.GONE);
            }
        });
        blur1.setOnClickListener(view -> {
            month_choose.setVisibility(View.GONE);
            year_choose_layout.setVisibility(View.GONE);
            year_view.setText(savedYearNum + "");
            blur1.setVisibility(View.GONE);
        });
        blur.setOnClickListener(view -> {
            plus.setRotation(0);
            app_name.setVisibility(View.VISIBLE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            createNote.setVisibility(View.GONE);
            blur.setVisibility(View.GONE);
        });
//        editButton.setOnClickListener(view -> {
//            inputText.setEnabled(true);
//            inputTextNote.setEnabled(true);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
//            inputText.requestFocus();
//        });
//        cancelButton.setOnClickListener(view -> {
//            inputText.setText("");
//            createTextNote.setVisibility(View.GONE);
//            createNote.setVisibility(View.GONE);
//            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//            plus.setRotation(0);
//            blur.setVisibility(View.GONE);
//        });
        addText.setOnClickListener(view -> {
            startActivityForResult(new Intent(getApplicationContext(), CreateTextNote.class), RESULT_CODE_ADD_NOTE);
            inputText.setText("");
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
            inputText.setText("");
//            noAds.setVisibility(View.GONE);
        });
        plus.setOnClickListener(view -> {
            if (searchTxt.getVisibility() == View.VISIBLE) {
                searchTxt.setVisibility(View.GONE);
                app_name.setVisibility(View.VISIBLE);
                plus.setRotation(0);
                plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                searchTxt.setText("");
            } else if (createNote.getVisibility() == View.VISIBLE) {
                createNote.setVisibility(View.GONE);
//                noAds.setVisibility(View.GONE);
                blur.setVisibility(View.GONE);
                app_name.setVisibility(View.VISIBLE);
                plus.setRotation(0);
                plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            } else if (createNote.getVisibility() == View.GONE) {
//                noAds.setVisibility(View.VISIBLE);
                blur.setVisibility(View.VISIBLE);
                createNote.setVisibility(View.VISIBLE);
                app_name.setVisibility(View.GONE);
                plus.setRotation(45);
                constraintLayout.setElevation(4);
                plus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            }
        });
//        search.setOnClickListener(view -> {
//            if (searchTxt.getVisibility() == View.GONE) {
//                searchTxt.setVisibility(View.VISIBLE);
//                app_name.setVisibility(View.INVISIBLE);
//                createNote.setVisibility(View.GONE);
//                searchTxt.requestFocus();
//                searchTxt.setFocusableInTouchMode(true);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(searchTxt, InputMethodManager.SHOW_FORCED);
//                plus.setRotation(45);
//                plus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
//            } else if (searchTxt.getVisibility() == View.VISIBLE) {
//                //TODO SEARCH BUTTON
//            }
//        });
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        textNoteList = new ArrayList<>();
        textNoteAdapter = new TextNoteAdapter(textNoteList, this);
        notesRecyclerView.setAdapter(textNoteAdapter);

//        listNoteList = new ArrayList<>();
//        listNoteAdapter = new ListNoteAdapter(listNoteList);
//        listNotesRecyclerView = findViewById(R.id.listNotesRecyclerView);
//        listNotesRecyclerView.setLayoutManager(
//                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//        );
//        listNotesRecyclerView.setAdapter(listNoteAdapter);

//        getListNotes(REQUEST_CODE_SHOW_NOTES, false);
        getNotes(REQUEST_CODE_SHOW_NOTES, false);
    }

    @Override
    public void onTextNoteClicked(TextNote textNote, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateTextNote.class);
        intent.putExtra("IsViewOrUpdate", true);
        intent.putExtra("textnote", textNote);
        startActivityForResult(intent, RESULT_CODE_UPDATE_NOTE);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted) {
        class GetNotesTask extends AsyncTask<Void, Void, List<TextNote>> {
            @Override
            protected List<TextNote> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<TextNote> textNotes) {
                super.onPostExecute(textNotes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES){
                    textNoteList.addAll(textNotes);
                    textNoteAdapter.notifyDataSetChanged();
                } else if (requestCode == RESULT_CODE_ADD_NOTE){
                    textNoteList.add(0, textNotes.get(0));
                    textNoteAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == RESULT_CODE_UPDATE_NOTE){
                    textNoteList.remove(noteClickedPosition);
                    if (isNoteDeleted){
                        textNoteAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        textNoteList.add(noteClickedPosition, textNotes.get(noteClickedPosition));
                        textNoteAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(RESULT_CODE_ADD_NOTE, false);
//            getListNotes(RESULT_CODE_ADD_NOTE, false);
        } else if (requestCode == RESULT_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(RESULT_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }

//    private void getListNotes(final int requestCode, final boolean isNoteDeleted) {
//        class GetNotesTask extends AsyncTask<Void, Void, List<ListNote>> {
//            @Override
//            protected List<ListNote> doInBackground(Void... voids) {
//                return NotesDatabase
//                        .getDatabase(getApplicationContext())
//                        .listDao().getAllNotes();
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            protected void onPostExecute(List<ListNote> listNotes) {
//                super.onPostExecute(listNotes);
//                if (requestCode == REQUEST_CODE_SHOW_NOTES){
//                    listNoteList.addAll(listNotes);
//                    listNoteAdapter.notifyDataSetChanged();
//                } else if (requestCode == RESULT_CODE_ADD_NOTE){
//                    listNoteList.add(0, listNotes.get(0));
//                    listNoteAdapter.notifyItemInserted(0);
//                    listNotesRecyclerView.smoothScrollToPosition(0);
////                } else if (requestCode == RESULT_CODE_UPDATE_NOTE){
////                    listNoteList.remove(noteClickedPosition);
////                    if (isNoteDeleted){
////                        listNoteAdapter.notifyItemRemoved(noteClickedPosition);
////                    } else {
////                        listNoteList.add(noteClickedPosition, listNotes.get(noteClickedPosition));
////                        listNoteAdapter.notifyItemChanged(noteClickedPosition);
////                    }
//                }
//            }
//        }
//        new GetNotesTask().execute();
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }
}