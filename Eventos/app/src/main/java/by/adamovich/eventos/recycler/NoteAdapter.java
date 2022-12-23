package by.adamovich.eventos.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    public Context context;
    private final List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.notes = notes;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note = notes.get(position);

        if (note.getTitle().length() >= 23)
            holder.titleView.setText(note.getTitle().substring(0, 23) + "...");
        else
            holder.titleView.setText(note.getTitle());

        if (note.getText().length() >= 23)
            holder.textView.setText(note.getText().substring(0, 23) + "...");
        else
            holder.textView.setText(note.getText());

        String time = note.getDate().split(" ")[1];
        String month = note.getDate().split(" ")[0].split("/")[1];
        String day = note.getDate().split(" ")[0].split("/")[2];
        switch (Integer.parseInt(month)){
            case 1:
                month = "Января";
                break;
            case 2:
                month = "Февраля";
                break;
            case 3:
                month = "Марта";
                break;
            case 4:
                month = "Апреля";
                break;
            case 5:
                month = "Мая";
                break;
            case 6:
                month = "Июня";
                break;
            case 7:
                month = "Июля";
                break;
            case 8:
                month = "Августа";
                break;
            case 9:
                month = "Сентября";
                break;
            case 10:
                month = "Октября";
                break;
            case 11:
                month = "Ноября";
                break;
            case 12:
                month = "Декабря";
                break;
        }
        holder.dateView.setText(time + ", " + day + " " + month);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, textView, dateView;

        ViewHolder(View itemView){
            super(itemView);
            titleView = itemView.findViewById(R.id.noteTitleTV);
            textView = itemView.findViewById(R.id.noteTextTV);
            dateView = itemView.findViewById(R.id.noteDateTV);
        }
    }
}