package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.events = events;
        this.inflater = LayoutInflater.from(context);
    }

    // возвращает объект ViewHolder, который будет хранить данные по одному объекту Event.
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    // выполняет привязку объекта ViewHolder к объекту Event по определенной позиции.
    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);

        holder.titleView.setText(event.getName());

        Bitmap bmp = BitmapFactory.decodeByteArray(event.getImage(), 0, event.getImage().length);
        holder.eventImageView.setImageBitmap(bmp);
    }

    // возвращает количество объектов в списке.
    @Override
    public int getItemCount() {
        return events.size();
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