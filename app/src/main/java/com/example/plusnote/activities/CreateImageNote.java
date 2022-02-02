package com.example.plusnote.activities;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.example.plusnote.activities.VideoActivity.isVideo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.plusnote.R;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateImageNote extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;
    private ImageButton
            cancelButtonImage,
            deleteButtonImage,
            editButtonImage,
            reminderButtonImage,
            copyButtonImage,
            shareButtonImage,
            doneButtonImage,
            play_button_video,
            image_icon;
    private EditText inputImageTitle;
    private Note alreadyAvailableNote;
    public static String selectedImagePath;
    ImageView imageView;
    public static File outputFile;
    boolean isPlaying = false;
    ConstraintLayout video_controller;
    VideoView videoView;
    Chronometer playback_timer_video;
    TextView video_length;
    private long timePaused = 0;
    boolean needToRecreate = true;

    private Runnable updateSeekbar;
    private Handler seekbarHandler;
    SeekBar seekbar_video;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image_note);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        videoView = findViewById(R.id.video_view);
        ///////
        video_controller = findViewById(R.id.video_controller);
        cancelButtonImage = findViewById(R.id.cancelButtonImage);
        deleteButtonImage = findViewById(R.id.deleteButtonImage);
        editButtonImage = findViewById(R.id.editButtonImage);
        reminderButtonImage = findViewById(R.id.reminderButtonImage);
        copyButtonImage = findViewById(R.id.copyButtonImage);
        shareButtonImage = findViewById(R.id.shareButtonImage);
        doneButtonImage = findViewById(R.id.doneButtonImage);
        image_icon = findViewById(R.id.image_icon);
        imageView = findViewById(R.id.image_view);
        playback_timer_video = findViewById(R.id.playback_timer_video);
        video_length = findViewById(R.id.video_length);
        seekbar_video = findViewById(R.id.seekbar_video);
        play_button_video = findViewById(R.id.play_button_video);
        ///////
        inputImageTitle = findViewById(R.id.inputImageTitle);
        //////
        editButtonImage.setEnabled(false);
        copyButtonImage.setEnabled(false);
        shareButtonImage.setEnabled(false);
        doneButtonImage.setEnabled(false);
        reminderButtonImage.setEnabled(false);

        inputImageTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputImageTitle.getText().toString().trim().isEmpty()) {
                    doneButtonImage.setEnabled(true);
                    doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButtonImage.setEnabled(true);
                    reminderButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButtonImage.setEnabled(false);
                    doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButtonImage.setEnabled(false);
                    reminderButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        play_button_video.setOnClickListener(view -> {
            if (isPlaying) {
                videoView.pause();
                playback_timer_video.stop();
                timePaused = SystemClock.elapsedRealtime() - playback_timer_video.getBase();
                seekbarHandler.removeCallbacks(updateSeekbar);
                play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            } else {
                playback_timer_video.setBase(SystemClock.elapsedRealtime() - timePaused);
                playback_timer_video.start();
                if (needToRecreate) {
                    if (alreadyAvailableNote != null) {
                        outputFile = new File(alreadyAvailableNote.getImage_path());
                    }
                    videoView.setVideoPath(outputFile.getAbsolutePath());
                    needToRecreate = false;
                }
                videoView.start();
                play_button_video.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
                if (alreadyAvailableNote != null) {
                    seekbar_video.setMax(Integer.parseInt(alreadyAvailableNote.getAudio_length().substring(3)) * 1000);
                } else
                    seekbar_video.setMax(Integer.parseInt(VideoActivity.timer_string.substring(3)) * 1000);
                seekbarHandler = new Handler();
                updateSeekbar = new Runnable() {
                    @Override
                    public void run() {
                        seekbar_video.setProgress(videoView.getCurrentPosition());
                        seekbarHandler.postDelayed(this, 100);
                    }
                };
                seekbarHandler.postDelayed(updateSeekbar, 0);
                videoView.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.seekTo(1);
                    mediaPlayer.stop();
                    playback_timer_video.stop();
                    playback_timer_video.setBase(SystemClock.elapsedRealtime());
                    play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                    isPlaying = false;
                    needToRecreate = true;
                    seekbar_video.setProgress(0);
                });
            }

            isPlaying = !isPlaying;

        });

        seekbar_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                videoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                videoView.seekTo(progress);
                playback_timer_video.setBase(SystemClock.elapsedRealtime() - videoView.getCurrentPosition());
                videoView.start();

                videoView.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.seekTo(1);
                    mediaPlayer.stop();
                    playback_timer_video.stop();
                    playback_timer_video.setBase(SystemClock.elapsedRealtime());
                    play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                    isPlaying = false;
                    needToRecreate = true;
                    seekbar_video.setProgress(0);
                });
            }
        });

        if (isVideo) {
            image_icon.setBackgroundResource(R.drawable.ic_video);
            videoView.seekTo(1);
        }


        cancelButtonImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            imm.hideSoftInputFromWindow(inputImageTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButtonImage.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputImageTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        editButtonImage.setOnClickListener(view -> {
            inputImageTitle.setEnabled(true);
            inputImageTitle.requestFocus();
            inputImageTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputImageTitle, InputMethodManager.SHOW_FORCED);
            inputImageTitle.post(() -> inputImageTitle.setSelection(inputImageTitle.getText().length()));
            doneButtonImage.setEnabled(true);
            doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        });
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            if (getIntent().getBooleanExtra("deleteNote", false)) {
                deleteNote();
            }
            setViewOrUpdateNote();
        } else {
            if (isVideo) {
                videoView.setVisibility(View.VISIBLE);
                video_controller.setVisibility(View.VISIBLE);
                videoView.setVideoPath(outputFile.getAbsolutePath());
                videoView.seekTo(1);
                video_length.setText(VideoActivity.timer_string);
            } else {
                Bitmap temp = BitmapFactory.decodeFile(selectedImagePath);
                if (temp.getWidth() > temp.getHeight()) {
                    if (temp.getWidth() == 1920) {
                        temp = RotateBitmap(temp, 270);
                    } else temp = RotateBitmap(temp, 90);
                }
                if (VideoActivity.fromGallery) {
                    temp = RotateBitmap(temp, 0);
                } else {
                    image_icon.setBackgroundResource(R.drawable.ic_camera2);
                }
                imageView.setImageBitmap(temp);
            }
        }
        if (alreadyAvailableNote != null) {
            deleteButtonImage.setOnClickListener(view -> deleteNote());
        }

    }

    private void deleteNote() {
        class DeleteNoteTask extends AsyncTask<Void, Void, Void> {
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

    @SuppressLint("SetTextI18n")
    private void setViewOrUpdateNote() {
        inputImageTitle.setEnabled(false);
        editButtonImage.setEnabled(true);
        copyButtonImage.setEnabled(true);
        shareButtonImage.setEnabled(true);
        doneButtonImage.setEnabled(false);

        Log.e("DateSavedImage", alreadyAvailableNote.getDate());

        deleteButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        copyButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

        inputImageTitle.setText(alreadyAvailableNote.getTextNoteTitle());
        try {
            if (alreadyAvailableNote.isIs_video()) {
                image_icon.setBackgroundResource(R.drawable.ic_video);
                videoView.setVisibility(View.VISIBLE);
                video_controller.setVisibility(View.VISIBLE);
                video_length.setText(alreadyAvailableNote.getAudio_length());
                videoView.setVideoPath(alreadyAvailableNote.getImage_path());
                videoView.seekTo(1);
            } else {
                Bitmap temp = BitmapFactory.decodeFile(alreadyAvailableNote.getImage_path());
                if (temp.getWidth() > temp.getHeight()) {
                    if (temp.getWidth() == 1920) {
                        temp = RotateBitmap(temp, 270);
                    } else temp = RotateBitmap(temp, 90);
                }
                if (alreadyAvailableNote.isFrom_gallery()) {
                    temp = RotateBitmap(temp, 0);
                } else {
                    image_icon.setBackgroundResource(R.drawable.ic_camera2);
                }
                imageView.setImageBitmap(temp);
            }
        } catch (Exception e) {
            if (!getIntent().getBooleanExtra("deleteNote", false)) {
                Toast.makeText(this, "Image has been deleted from gallery", Toast.LENGTH_LONG)
                        /*.setGravity(Gravity.CENTER, 0, 0)*/.show();
            }
        }
    }

    private void saveNote() {
        if (inputImageTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputImageTitle.getText().toString());
        if (!getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            note.setImage_path(selectedImagePath);
            note.setAudio_length(VideoActivity.timer_string);
            note.setFrom_gallery(VideoActivity.fromGallery);
            note.setIs_video(isVideo);
            if (isVideo){
                note.setImage_path(outputFile.getAbsolutePath());
            }
            note.setDate(MainActivity.notesDay);
        } else {
            note.setIs_video(alreadyAvailableNote.isIs_video());
            note.setIs_photo(alreadyAvailableNote.isIs_photo());
            note.setImage_path(alreadyAvailableNote.getImage_path());
            note.setAudio_length(alreadyAvailableNote.getAudio_length());
            note.setFrom_gallery(alreadyAvailableNote.isFrom_gallery());
            note.setDate(alreadyAvailableNote.getDate());
        }
        note.setIs_image(true);
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
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

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying) {
            videoView.stopPlayback();
        }
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekbar_video.setProgress(videoView.getCurrentPosition());
                seekbarHandler.postDelayed(this, 100);
            }
        };
    }
}