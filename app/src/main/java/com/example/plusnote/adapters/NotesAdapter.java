package com.example.plusnote.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plusnote.R;
import com.example.plusnote.entities.Note;
import com.example.plusnote.listeners.NotesListener;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>
        /*implements ItemTouchHelperAdapter*/ {

    private List<Note> notes;
    private NotesListener notesListener;
    private boolean isEnabled = true;
    private Context context;
//    private boolean isPlaying = false;
//    private MediaPlayer mediaPlayer = null;
//    private Handler seekbarHandler;
//    private Runnable updateSeekbar;
//    private boolean resumeAudio = false;
//    private long elapsedMillis = 0;


    public NotesAdapter(List<Note> notes, NotesListener notesListener, Context context) {
        this.notes = notes;
        this.notesListener = notesListener;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recycler_view_item,
                        parent,
                        false
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.btnContainer.setVisibility(View.GONE);
//        if (notes.get(position).isIs_voice()) {
//            holder.seekbar.setVisibility(View.VISIBLE);
//            holder.audio_length.setVisibility(View.VISIBLE);
//            holder.play_btn.setVisibility(View.VISIBLE);
//            holder.playback_timer.setVisibility(View.VISIBLE);
//        } else {
//            holder.titleOut.setTextSize(21);
//        }
//        holder.audio_length.setText(notes.get(position).getAudio_length());
//        holder.seekbar.setProgress(0);
//        holder.play_btn.setOnClickListener(view -> {
//            if (isPlaying) {
//                holder.playback_timer.stop();
//                pauseAudio(holder);
//                holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            } else {
//                if (!resumeAudio){
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//                    holder.playback_timer.start();
//                    if (mediaPlayer != null){
//                        mediaPlayer.stop();
//                    }
//                    playAudio(notes.get(position).getImage_path(), holder);
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                    resumeAudio = !resumeAudio;
//                } else {
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
//                    holder.playback_timer.start();
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                    resumeAudio(holder);
//                }
//            }
//            isPlaying = !isPlaying;
//        });
//        holder.titleOut.setOnClickListener(view -> {
//            if (isEnabled) {
//                if (notes.get(position).isIs_list()) {
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote_dark);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_listnote_dark);
//                } else if (notes.get(position).isIs_image()) {
//                    if (notes.get(position).isIs_video()) {
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_video_dark);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_video_dark);
//                    } else if (notes.get(position).isFrom_gallery()){
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_image_dark);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_image_dark);
//                    } else {
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera2_dark);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_camera2_dark);
//                    }
//                } else if (notes.get(position).isIs_voice()) {
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone_dark);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_microphone_dark);
//                } else if (notes.get(position).isIs_photo()){
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote_dark);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_textnote_dark);
//                }
//                holder.titleOut.setTextColor(Color.parseColor("#707FCEFF"));
//                holder.note_icon_text.setEnabled(false);
//                isEnabled = !isEnabled;
//            } else {
//                if (notes.get(position).isIs_list()) {
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_listnote);
//                } else if (notes.get(position).isIs_image()) {
//                    if (notes.get(position).isIs_video()) {
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_video);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_video);
//                    } else if (notes.get(position).isFrom_gallery()){
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_image);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_image);
//                    }else {
//                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera2);
//                        holder.viewButton.setBackgroundResource(R.drawable.ic_camera2);
//                    }
//                } else if (notes.get(position).isIs_voice()) {
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_microphone);
//                } else {
//                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote);
//                    holder.viewButton.setBackgroundResource(R.drawable.ic_textnote);
//                }
//                holder.titleOut.setTextColor(Color.parseColor("#7FCEFF"));
//                holder.note_icon_text.setEnabled(true);
//                isEnabled = !isEnabled;
//            }
//        });

        holder.note_icon_text.setOnClickListener(view -> holder.btnContainer.setVisibility(View.VISIBLE));
        if (notes.get(position).isIs_list()) {
            holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote);
            holder.viewButton.setBackgroundResource(R.drawable.ic_listnote);
        } else if (notes.get(position).isIs_image()) {
            if (notes.get(position).isIs_video()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_video);
                holder.viewButton.setBackgroundResource(R.drawable.ic_video);
            } else if (notes.get(position).isFrom_gallery()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_image);
                holder.viewButton.setBackgroundResource(R.drawable.ic_image);
            } else {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera);
                holder.viewButton.setBackgroundResource(R.drawable.ic_camera);
            }
        } else if (notes.get(position).isIs_voice()) {
            holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone);
            holder.viewButton.setBackgroundResource(R.drawable.ic_microphone);
        } else {
            holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote);
            holder.viewButton.setBackgroundResource(R.drawable.ic_textnote);
        }
        holder.viewButton.setOnClickListener(view -> {
            notesListener.onNoteClicked(notes.get(position), position);
            holder.btnContainer.setVisibility(View.GONE);
        });
        holder.deleteButton.setOnClickListener(view -> notesListener.onNoteClicked(notes.get(position), position, true));
        holder.cancelButton.setOnClickListener(view -> holder.btnContainer.setVisibility(View.GONE));
        holder.btnContainer.setOnClickListener(view -> {});
//        holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                pauseAudio(holder);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                int progress = seekBar.getProgress();
//                mediaPlayer.seekTo(progress);
//                holder.playback_timer.setBase(SystemClock.elapsedRealtime() - mediaPlayer.getCurrentPosition());
//                if (resumeAudio){
//                    resumeAudio(holder);
//                } else {
//                    mediaPlayer = new MediaPlayer();
//                    String path = notes.get(holder.getAdapterPosition()).getImage_path();
//                    resumeAudio(path, holder);
//                }
//                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
//                    holder.playback_timer.stop();
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                    isPlaying = false;
//                    resumeAudio = false;
//                    seekBar.setProgress(0);
//                    stopAudio();
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleOut, audio_length;
        SeekBar seekbar;
        Chronometer playback_timer;
        ImageButton
                note_icon_text,
                cancelButton,
                deleteButton,
                play_btn,
                viewButton;
        ConstraintLayout outContainer;
        ConstraintLayout btnContainer;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOut = itemView.findViewById(R.id.TitleOut);
            note_icon_text = itemView.findViewById(R.id.text_container_icon);
            outContainer = itemView.findViewById(R.id.textOutContainer);
            btnContainer = itemView.findViewById(R.id.btncontainer);
            cancelButton = itemView.findViewById(R.id.cancelButtonSwipe);
            deleteButton = itemView.findViewById(R.id.deleteButtonSwipe);
            viewButton = itemView.findViewById(R.id.open_view);
            play_btn = itemView.findViewById(R.id.play_button);
            audio_length = itemView.findViewById(R.id.audio_length);
            playback_timer = itemView.findViewById(R.id.playback_timer);
            seekbar = itemView.findViewById(R.id.seekbar_audio);
        }

        void setNote(Note note) {
            titleOut.setText(note.getTextNoteTitle());
        }
    }

//    private void stopAudio() {
//        mediaPlayer.stop();
//        seekbarHandler.removeCallbacks(updateSeekbar);
//    }
//
//    private void pauseAudio(NoteViewHolder holder){
//        mediaPlayer.pause();
//        holder.playback_timer.stop();
//        elapsedMillis = SystemClock.elapsedRealtime() - holder.playback_timer.getBase();
//        seekbarHandler.removeCallbacks(updateSeekbar);
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//    }
//
//    private void resumeAudio(NoteViewHolder holder){
//        mediaPlayer.start();
//        holder.playback_timer.start();
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void resumeAudio(String path, NoteViewHolder holder){
//        try {
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        holder.playback_timer.start();
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void playAudio(String path, NoteViewHolder holder) {
//        mediaPlayer = new MediaPlayer();
//
//        try {
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//        }
//
//        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
//            holder.playback_timer.stop();
//            holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//            holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            isPlaying = false;
//            resumeAudio = false;
//            holder.seekbar.setProgress(0);
//            stopAudio();
//        });
//
//        holder.seekbar.setMax(mediaPlayer.getDuration());
//        seekbarHandler = new Handler();
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void updateRunnable(NoteViewHolder holder){
//        updateSeekbar = new Runnable() {
//            @Override
//            public void run() {
//                holder.seekbar.setProgress(mediaPlayer.getCurrentPosition());
//                seekbarHandler.postDelayed(this, 100);
//            }
//        };
//    }
}
