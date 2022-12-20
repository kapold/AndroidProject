package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Event> events;
    private final Context context;

    public EventAdapter(Context context, List<Event> events) {
        this.events = events;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    // возвращает объект ViewHolder, который будет хранить данные по одному объекту Event.
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    // выполняет привязку объекта ViewHolder к объекту Event по определенной позиции.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);

        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                holder.typeView.setText(DataManager.psHandler.getTypeById(event.getIdType()));
                holder.creatorView.setText(DataManager.psHandler.getNameById(event.getIdCreator()));
            });
        }).start();
        holder.titleView.setText(event.getName());
        holder.placeView.setText(event.getPlace());
        holder.timeView.setText(event.getTime());
        holder.dateView.setText(event.getDate());
        holder.peopleView.setText(String.format("%s/%s", event.getOccupied(), event.getCapacity()));
        holder.requestButton.setEnabled(true);

        Picasso.get()
                .load(event.getImage())
                .placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_foreground)) // it will show placeholder image when url is not valid.
                .networkPolicy(NetworkPolicy.OFFLINE) // for caching the image url in case phone is offline
                .into(holder.eventImage);
    }

    // возвращает количество объектов в списке.
    @Override
    public int getItemCount() {
        return events.size();
    }

    // определение переменных
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView eventImage;
        final TextView titleView, typeView, placeView, timeView, dateView, creatorView, peopleView;
        final Button requestButton;

        ViewHolder(View itemView){
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            titleView = itemView.findViewById(R.id.eventTitle);
            typeView = itemView.findViewById(R.id.eventType);
            placeView = itemView.findViewById(R.id.eventPlace);
            timeView = itemView.findViewById(R.id.eventTime);
            dateView = itemView.findViewById(R.id.eventDate);
            creatorView = itemView.findViewById(R.id.eventCreator);
            peopleView = itemView.findViewById(R.id.eventPeople);
            requestButton = itemView.findViewById(R.id.reqBtn);
        }
    }
}