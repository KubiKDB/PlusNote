package com.example.plusnote.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.plusnote.adapters.NotesAdapter;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.Note;
import com.example.plusnote.listeners.NotesListener;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements NotesListener {
    private static RecyclerView notesRecyclerView;
    private static List<Note> noteList;
    private static NotesAdapter notesAdapter;
    public static int pnc;

    private static int noteClickedPosition = -1;
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_UPDATE_NOTE = 2;
    private static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static LocalDate stLdate = LocalDate.now();
    public static int pageNumberForDay = 0;
    public int year_count = 2022;
    public int savedYearNum = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};
    //    public static DayOfWeek dayOfWeek;
    public static String notesDay;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);
        /////////
        ImageButton plus = findViewById(R.id.plus);
        ImageButton addText = findViewById(R.id.text_create);
        ImageButton cameraCreate = findViewById(R.id.camera_create);
        ImageButton list_create = findViewById(R.id.list_create);
        ImageButton search = findViewById(R.id.search);
        ImageButton search_do = findViewById(R.id.search_do);
        ImageButton voice_create = findViewById(R.id.voice_create);
        ////////
        ConstraintLayout createNote = findViewById(R.id.createNote);
        ConstraintLayout blur = findViewById(R.id.blur);
        ConstraintLayout blur1 = findViewById(R.id.blur1);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        ConstraintLayout month_choose = findViewById(R.id.month_choose);
        ConstraintLayout year_choose_layout = findViewById(R.id.year_choose_layout);
//        ConstraintLayout noAds = findViewById(R.id.noAdsContainer);
        //////////
        EditText searchTxt = findViewById(R.id.searchEditText);
        //////////
        TextView app_name = findViewById(R.id.app_name);
        TextView month_view = findViewById(R.id.month_view);
        TextView year_view = findViewById(R.id.year_view);
        TextView year1 = findViewById(R.id.year1);
        ////////
        TextView[] months = new TextView[12];
        months[0] = findViewById(R.id.jan);
        months[1] = findViewById(R.id.feb);
        months[2] = findViewById(R.id.mar);
        months[3] = findViewById(R.id.apr);
        months[4] = findViewById(R.id.may);
        months[5] = findViewById(R.id.jun);
        months[6] = findViewById(R.id.jul);
        months[7] = findViewById(R.id.aug);
        months[8] = findViewById(R.id.sep);
        months[9] = findViewById(R.id.oct);
        months[10] = findViewById(R.id.nov);
        months[11] = findViewById(R.id.dec);
        TextView[] years = new TextView[5];
        years[0] = findViewById(R.id.year_choose);
        years[1] = findViewById(R.id.year_choose1);
        years[2] = findViewById(R.id.year_choose2);
        years[3] = findViewById(R.id.year_choose3);
        years[4] = findViewById(R.id.year_choose4);
        TextView mon = findViewById(R.id.weekDay);
        TextView tue = findViewById(R.id.weekDay1);
        TextView wed = findViewById(R.id.weekDay2);
        TextView thu = findViewById(R.id.weekDay3);
        TextView fri = findViewById(R.id.weekDay4);
        TextView sat = findViewById(R.id.weekDay5);
        TextView sun = findViewById(R.id.weekDay6);
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
        final DateTimeFormatter noteDayF = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        notesDay = noteDayF.format(ld1);
        month_view.setText(dtf.format(ld1));
        year_view.setText(dtf1.format(ld1));

        mon.setTextColor(Color.parseColor("#9000FF00"));
        tue.setTextColor(Color.parseColor("#9000FF00"));
        wed.setTextColor(Color.parseColor("#9000FF00"));
        thu.setTextColor(Color.parseColor("#9000FF00"));
        fri.setTextColor(Color.parseColor("#9000FF00"));
        sat.setTextColor(Color.parseColor("#9000FF00"));
        sun.setTextColor(Color.parseColor("#9000FF00"));

        year_count = Integer.parseInt(String.valueOf(year_view.getText()));
        pageNumberForDay = calcPN();
        stLdate = LocalDate.now();
        pager1.setCurrentItem(pageNumberForDay);
        LocalDate ld = LocalDate.of(2022, Month.JANUARY, 1);
        year1.setText(year_view.getText());
        String days = dtf.format(ld);
        for (TextView month : months) {
            month.setText(days);
            ld = ld.plusMonths(1);
            days = dtf.format(ld);
        }
        year_view.setOnClickListener(view -> {
            year_choose_layout.setVisibility(View.VISIBLE);
            blur1.setVisibility(View.VISIBLE);
            savedYearNum = Integer.parseInt(String.valueOf(year_view.getText()));
            LocalDate localDate = LocalDate.now();
            String yearStr = dtf1.format(localDate);
            for (TextView year : years) {
                year.setText(yearStr);
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

//                    mon.setTextColor(Color.parseColor("#9000FF00"));
//                    tue.setTextColor(Color.parseColor("#9000FF00"));
//                    wed.setTextColor(Color.parseColor("#9000FF00"));
//                    thu.setTextColor(Color.parseColor("#9000FF00"));
//                    fri.setTextColor(Color.parseColor("#9000FF00"));
//                    sat.setTextColor(Color.parseColor("#9000FF00"));
//                    sun.setTextColor(Color.parseColor("#9000FF00"));

                month_view.setText(months[finalI].getText());
                pager1.setAdapter(pageAdapter1);
                LocalDate localDate = LocalDate.of(year_count, finalI + 1, 1);
                int pagenum;
                savedYearNum = Integer.parseInt(String.valueOf(year_view.getText()));
                year_count = Integer.parseInt(String.valueOf(year_view.getText()));
                if (year_count > 2024) {
                    pagenum = (localDate.getDayOfYear() + 4 + 366 + 365 * (year_count - 2023)) / 7;
//                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
//                    switch (dayOfWeek) {
//                        case MONDAY:
//                            mon.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case TUESDAY:
//                            tue.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case WEDNESDAY:
//                            wed.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case THURSDAY:
//                            thu.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case FRIDAY:
//                            fri.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case SATURDAY:
//                            sat.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case SUNDAY:
//                            sun.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                    }
                } else {
                    pagenum = (localDate.getDayOfYear() + 4 + 365 * (year_count - 2022)) / 7;
//                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
//                    switch (dayOfWeek) {
//                        case MONDAY:
//                            mon.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case TUESDAY:
//                            tue.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case WEDNESDAY:
//                            wed.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case THURSDAY:
//                            thu.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case FRIDAY:
//                            fri.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case SATURDAY:
//                            sat.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                        case SUNDAY:
//                            sun.setTextColor(Color.parseColor("#FFA500"));
//                            break;
//                    }
                }
                pageNumberForDay = pagenum;
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
            startActivityForResult(
                    new Intent(getApplicationContext(), CreateListNote.class),
                    REQUEST_CODE_ADD_NOTE
            );
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
        });
        voice_create.setOnClickListener(view -> {
            startActivityForResult(
                    new Intent(getApplicationContext(), CreateVoiceNote.class),
                    REQUEST_CODE_ADD_NOTE
            );
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
        });
        cameraCreate.setOnClickListener(view -> {
            startActivityForResult(
                    new Intent(getApplicationContext(), PhotoActivity.class),
                    REQUEST_CODE_ADD_NOTE
            );
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
        });
        addText.setOnClickListener(view -> {
            startActivityForResult(
                    new Intent(getApplicationContext(), CreateTextNote.class),
                    REQUEST_CODE_ADD_NOTE
            );
            app_name.setVisibility(View.VISIBLE);
            createNote.setVisibility(View.GONE);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            plus.setRotation(0);
            blur.setVisibility(View.GONE);
//            noAds.setVisibility(View.GONE);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            year_view.setVisibility(View.VISIBLE);
            month_view.setVisibility(View.VISIBLE);
            search_do.setVisibility(View.GONE);
            searchTxt.setVisibility(View.GONE);
        });
        plus.setOnClickListener(view -> {
            if (searchTxt.getVisibility() == View.VISIBLE) {
                searchTxt.setVisibility(View.GONE);
                app_name.setVisibility(View.VISIBLE);
                plus.setRotation(0);
                blur.setVisibility(View.GONE);
                year_view.setVisibility(View.VISIBLE);
                month_view.setVisibility(View.VISIBLE);
                search_do.setVisibility(View.GONE);
                plus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
        search.setOnClickListener(view -> {
            searchTxt.setVisibility(View.VISIBLE);
            app_name.setVisibility(View.INVISIBLE);
            createNote.setVisibility(View.GONE);
            searchTxt.requestFocus();
            year_view.setVisibility(View.GONE);
            month_view.setVisibility(View.GONE);
            searchTxt.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchTxt, InputMethodManager.SHOW_FORCED);
            plus.setRotation(45);
            plus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            search_do.setVisibility(View.VISIBLE);
        });
        search_do.setOnClickListener(view -> {
            //TODO SEARCH BUTTON
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this, getApplicationContext());
        notesRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES, false, getApplicationContext());
    }

    private int calcPN() {
        LocalDate ld1 = LocalDate.now();
//        TextView mon = findViewById(R.id.weekDay);
//        TextView tue = findViewById(R.id.weekDay1);
//        TextView wed = findViewById(R.id.weekDay2);
//        TextView thu = findViewById(R.id.weekDay3);
//        TextView fri = findViewById(R.id.weekDay4);
//        TextView sat = findViewById(R.id.weekDay5);
//        TextView sun = findViewById(R.id.weekDay6);
        int pagenum;
        if (year_count > 2024) {
            pagenum = (ld1.getDayOfYear() + 4 + 366 + 365 * (year_count - 2023)) / 7;
//            DayOfWeek dayOfWeek = ld1.getDayOfWeek();
//            switch (dayOfWeek) {
//                case MONDAY:
//                    mon.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case TUESDAY:
//                    tue.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case WEDNESDAY:
//                    wed.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case THURSDAY:
//                    thu.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case FRIDAY:
//                    fri.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case SATURDAY:
//                    sat.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case SUNDAY:
//                    sun.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//            }
        } else {
            pagenum = (ld1.getDayOfYear() + 4 + 365 * (year_count - 2022)) / 7;
//            DayOfWeek dayOfWeek = ld1.getDayOfWeek();
//            switch (dayOfWeek) {
//                case MONDAY:
//                    mon.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case TUESDAY:
//                    tue.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case WEDNESDAY:
//                    wed.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case THURSDAY:
//                    thu.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case FRIDAY:
//                    fri.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case SATURDAY:
//                    sat.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//                case SUNDAY:
//                    sun.setTextColor(Color.parseColor("#FFA500"));
//                    break;
//            }
        }
        return pagenum;
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent;
        if (note.isIs_list()) {
            intent = new Intent(getApplicationContext(), CreateListNote.class);
        } else if (note.isIs_image()) {
            intent = new Intent(getApplicationContext(), CreateImageNote.class);
        } else if (note.isIs_voice()) {
            intent = new Intent(getApplicationContext(), CreateVoiceNote.class);
        } else {
            intent = new Intent(getApplicationContext(), CreateTextNote.class);
        }
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    @Override
    public void onNoteClicked(Note note, int position, boolean deleteNote) {
        noteClickedPosition = position;
        Intent intent;
        if (note.isIs_list()) {
            intent = new Intent(getApplicationContext(), CreateListNote.class);
        } else if (note.isIs_image()) {
            intent = new Intent(getApplicationContext(), CreateImageNote.class);
        } else {
            intent = new Intent(getApplicationContext(), CreateTextNote.class);
        }
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        if (deleteNote) {
            intent.putExtra("deleteNote", true);
        }
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    private static void getNotes(final int requestCode, final boolean isNoteDeleted, Context context) {

        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(context)
                        .noteDao()
                        .getAllNotes();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.clear();
                    notesAdapter.notifyDataSetChanged();
                    for (int i = 0; i < notes.size(); i++) {
                        if (notes.get(i).getDate().equals(notesDay)) {
                            noteList.add(notes.get(i));
                        }
                    }
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE, false, getApplicationContext());
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE,
                        data.getBooleanExtra("isNoteDeleted", false),
                        getApplicationContext());
            }
        }
    }

    public static void ClickOnDay(TextView textView, String day, Context context) {
        notesDay = day;
        getNotes(REQUEST_CODE_SHOW_NOTES, false, context);
    }

//    public static void getTodayNotes(Context context){
//        @SuppressLint("StaticFieldLeak")
//        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
//
//            @Override
//            protected List<Note> doInBackground(Void... voids) {
//                return NotesDatabase
//                        .getDatabase(context)
//                        .noteDao()
//                        .getAllNotes();
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            protected void onPostExecute(List<Note> notes) {
//                super.onPostExecute(notes);
//                for (int i = 0; i < notes.size(); i++) {
//                    if (notes.get(i).getDate().equals(notesDay)){
//                        noteList.add(notes.get(i));
//                    }
//                }
//            }
//        }
//        new GetNotesTask().execute();
//    }

}