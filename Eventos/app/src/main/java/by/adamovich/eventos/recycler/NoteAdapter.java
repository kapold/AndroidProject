package by.adamovich.eventos.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.Note;

public class NoteAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    // возвращает объект ViewHolder, который будет хранить данные по одному объекту Note.
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    // выполняет привязку объекта ViewHolder к объекту Event по определенной позиции.
    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Note note = notes.get(position);
        // holder.[something] для установки контента
        holder.titleView.setText(note.getTitle());
    }

    // возвращает количество объектов в списке.
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // определение переменных
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView eventImageView;
        final TextView titleView;

        ViewHolder(View view){
            super(view);
            eventImageView = view.findViewById(R.id.eventImage);
            titleView = view.findViewById(R.id.eventTitle);
        }
    }
}
