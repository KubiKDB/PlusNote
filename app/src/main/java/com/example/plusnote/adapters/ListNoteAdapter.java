//package com.example.plusnote.adapters;
//
//import android.graphics.Color;
//import android.provider.ContactsContract;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.plusnote.R;
//import com.example.plusnote.entities.ListNote;
//import com.example.plusnote.entities.TextNote;
//import com.example.plusnote.listeners.ListNotesListener;
//import com.example.plusnote.listeners.NotesListener;
//
//import java.util.List;
//
//public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ListNoteViewHolder> {
//
//    private List<ListNote> listNotes;
//    private ListNotesListener listNotesListener;
//
//    public ListNoteAdapter(List<ListNote> listNotes) {
//        this.listNotes = listNotes;
//    }
//
//    @NonNull
//    @Override
//    public ListNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ListNoteViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.rec_view_list_item,
//                        parent,
//                        false)
//        );
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ListNoteViewHolder holder, int position) {
//        holder.setListOut(listNotes.get(position));
//        holder.listOut.setOnClickListener(view -> {
//            holder.listOut.setTextColor(Color.parseColor("#50000000"));
//        });
//        holder.list_icon.setOnClickListener(view -> {
//            listNotesListener.onListNoteClicked(listNotes.get(position), position);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return listNotes.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    static class ListNoteViewHolder extends RecyclerView.ViewHolder {
//
//        ImageButton checkbox1;
//        TextView listOut;
//        ImageButton list_icon;
//        LinearLayout linearLayout;
//
//        ListNoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            listOut = itemView.findViewById(R.id.ListOut);
//            checkbox1 = itemView.findViewById(R.id.checkboxList);
//            linearLayout = itemView.findViewById(R.id.linLayout);
//            list_icon = itemView.findViewById(R.id.list_icon_container);
//        }
//
//        void setListOut(ListNote listNote){
//            listOut.setText(listNote.getTitle());
//        }
//    }
//}
