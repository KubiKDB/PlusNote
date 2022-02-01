package com.example.plusnote.activities;

import static com.example.plusnote.activities.VideoActivity.isVideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plusnote.R;
import com.example.plusnote.database.NotesDatabase;
import com.example.plusnote.entities.Note;

import java.io.File;
import java.io.IOException;

public class CreateVoiceNote extends AppCompatActivity {
    private ImageButton
            cancelButtonVoice,
            deleteButtonVoice,
            editButtonVoice,
            reminderButtonVoice,
            copyButtonVoice,
            shareButtonVoice,
            doneButtonVoice,
            play_btn,
            record_voice;
    private EditText inputVoiceTitle;
    private Note alreadyAvailableNote;
    private boolean isRecording = false;
    private Chronometer timer, playback_timer;
    private MediaRecorder mediaRecorder;
    private File outputFile = null;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    private TextView textView;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private boolean resumeAudio = false;
    private long elapsedMillis = 0;
    private SoundPool mSound1, mSound2;
    private int mMelody = 1, mPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voice_note);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSound1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound1.load(this, R.raw.start_recording, 1);
        mSound2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound2.load(this, R.raw.end_recording, 1);
        ///////
        cancelButtonVoice = findViewById(R.id.cancelButtonVoice);
        deleteButtonVoice = findViewById(R.id.deleteButtonVoice);
        editButtonVoice = findViewById(R.id.editButtonVoice);
        reminderButtonVoice = findViewById(R.id.reminderButtonVoice);
        copyButtonVoice = findViewById(R.id.copyButtonVoice);
        shareButtonVoice = findViewById(R.id.shareButtonVoice);
        doneButtonVoice = findViewById(R.id.doneButtonVoice);
        record_voice = findViewById(R.id.voice_icon);
        play_btn = findViewById(R.id.play_button);
        inputVoiceTitle = findViewById(R.id.inputVoiceTitle);
        timer = findViewById(R.id.timer);
        seekBar = findViewById(R.id.seekbar_audio);
        playback_timer = findViewById(R.id.playback_timer);
        textView = findViewById(R.id.audio_length);

        record_voice.setOnClickListener(view -> {
            if (isRecording) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                mPlay = mSound1.play(mMelody, (float) 0.3, (float) 0.3, 1, 0, 1);
                stopRecording();
                record_voice.setBackgroundResource(R.drawable.ic_microphone_dark);
                record_voice.setEnabled(false);
                timer.setVisibility(View.GONE);

                seekBar.setVisibility(View.VISIBLE);
                playback_timer.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                play_btn.setVisibility(View.VISIBLE);
                textView.setText(timer.getText());

                doneButtonVoice.setEnabled(true);
                doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

                inputVoiceTitle.setVisibility(View.VISIBLE);
                inputVoiceTitle.requestFocus();
                inputVoiceTitle.setFocusableInTouchMode(true);
                imm.showSoftInput(inputVoiceTitle, InputMethodManager.SHOW_FORCED);
                inputVoiceTitle.post(() -> inputVoiceTitle.setSelection(inputVoiceTitle.getText().length()));
            } else {
                if (checkPermissions()) {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    mPlay = mSound2.play(mMelody, (float) 0.5, (float) 0.5, 1, 0, 1);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                    }
                    startRecording();
                    doneButtonVoice.setEnabled(false);
                    doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    record_voice.setBackgroundResource(R.drawable.ic_microphone_recording);
                    timer.setVisibility(View.VISIBLE);
                }
            }
            isRecording = !isRecording;
        });

        cancelButtonVoice.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            imm.hideSoftInputFromWindow(inputVoiceTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButtonVoice.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputVoiceTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        editButtonVoice.setOnClickListener(view -> {
            inputVoiceTitle.setEnabled(true);
            inputVoiceTitle.requestFocus();
            inputVoiceTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputVoiceTitle, InputMethodManager.SHOW_FORCED);
            inputVoiceTitle.post(() -> inputVoiceTitle.setSelection(inputVoiceTitle.getText().length()));
            doneButtonVoice.setEnabled(true);
            doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        });
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            if (getIntent().getBooleanExtra("deleteNote", false)) {
                deleteNote();
            }
            play_btn.setOnClickListener(view -> {
                try {
                    if (isPlaying) {
                        playback_timer.stop();
                        pauseAudio();
                        play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    } else {
                        if (!resumeAudio) {
                            playback_timer.setBase(SystemClock.elapsedRealtime());
                            playback_timer.start();
                            playAudio(alreadyAvailableNote.getImage_path());
                            play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                            resumeAudio = !resumeAudio;
                        } else {
                            playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                            playback_timer.start();
                            play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                            resumeAudio();
                        }
                    }
                    isPlaying = !isPlaying;
                } catch (Exception e) {
                    if (!getIntent().getBooleanExtra("deleteNote", false)) {
                        Toast.makeText(this, "Audio has been deleted", Toast.LENGTH_LONG)
                                /*.setGravity(Gravity.CENTER, 0, 0)*/.show();
                    }
                }
            });
            setViewOrUpdateNote();
        } else {
            play_btn.setOnClickListener(view -> {
                if (isPlaying) {
                    playback_timer.stop();
                    pauseAudio();
                    play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    if (!resumeAudio) {
                        playback_timer.setBase(SystemClock.elapsedRealtime());
                        playback_timer.start();
                        playAudio(outputFile.getAbsolutePath());
                        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                    } else {
                        playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                        playback_timer.start();
                        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
                        resumeAudio();
                    }
                    resumeAudio = !resumeAudio;
                }
                isPlaying = !isPlaying;
            });
        }
        if (alreadyAvailableNote != null) {
            deleteButtonVoice.setOnClickListener(view -> {
                deleteNote();
            });
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                playback_timer.setBase(SystemClock.elapsedRealtime() - mediaPlayer.getCurrentPosition());
                if (resumeAudio) {
                    resumeAudio();
                } else {
                    mediaPlayer = new MediaPlayer();
                    String path;
                    if (alreadyAvailableNote != null) {
                        path = alreadyAvailableNote.getImage_path();
                    } else path = outputFile.getAbsolutePath();
                    resumeAudio(path);
                }
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    playback_timer.stop();
                    playback_timer.setBase(SystemClock.elapsedRealtime());
                    play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                    isPlaying = false;
                    resumeAudio = false;
                    seekBar.setProgress(0);
                    stopAudio();
                });
            }
        });
    }

    private void stopAudio() {
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playback_timer.stop();
        elapsedMillis = SystemClock.elapsedRealtime() - playback_timer.getBase();
        seekbarHandler.removeCallbacks(updateSeekbar);
        play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
    }

    private void resumeAudio() {
        mediaPlayer.start();
        playback_timer.start();
        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void resumeAudio(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {}
        playback_timer.start();
        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void playAudio(String path) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {}

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            playback_timer.stop();
            playback_timer.setBase(SystemClock.elapsedRealtime());
            play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            isPlaying = false;
            resumeAudio = false;
            seekBar.setProgress(0);
            stopAudio();
        });

        seekBar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        outputFile = PhotoActivity.getOutputMediaFile(PhotoActivity.MEDIA_TYPE_AUDIO);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 100);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isPlaying) {
            stopAudio();
        }

        if (isRecording) {
            stopRecording();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
            return false;
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

    private void setViewOrUpdateNote() {
        inputVoiceTitle.setEnabled(false);
        inputVoiceTitle.setVisibility(View.VISIBLE);
        editButtonVoice.setEnabled(true);
        copyButtonVoice.setEnabled(true);
        shareButtonVoice.setEnabled(true);
        doneButtonVoice.setEnabled(false);
        record_voice.setEnabled(false);
        seekBar.setVisibility(View.VISIBLE);
        playback_timer.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        play_btn.setVisibility(View.VISIBLE);
        textView.setText(alreadyAvailableNote.getAudio_length());

        deleteButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        copyButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

        inputVoiceTitle.setText(alreadyAvailableNote.getTextNoteTitle());
    }

    private void saveNote() {
        if (inputVoiceTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputVoiceTitle.getText().toString());
        note.setIs_voice(true);
        if (alreadyAvailableNote == null) {
            note.setImage_path(outputFile.getAbsolutePath());
            note.setAudio_length((String) timer.getText());
        } else {
            note.setImage_path(alreadyAvailableNote.getImage_path());
            note.setAudio_length(alreadyAvailableNote.getAudio_length());
        }

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
}