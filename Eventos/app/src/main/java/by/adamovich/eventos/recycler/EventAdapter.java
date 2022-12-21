package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Event> events;
    public Context context;

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

                holder.titleView.setText(event.getName());
                holder.placeView.setText(event.getPlace());
                holder.timeView.setText(event.getTime());
                holder.dateView.setText(event.getDate());
                holder.peopleView.setText(String.format("%s/%s", event.getOccupied(), event.getCapacity()));

                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                Glide.with(context)
                        .load(event.getImage())
                        .apply(requestOptions)
                        .placeholder(R.drawable.loading_spinner)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.eventImage);

                if (!DataManager.psHandler.isMineEvent(DataManager.user.getIdUser(), event.getIdEvent())){
                    holder.requestButton.setVisibility(View.VISIBLE);
                    holder.requestButton.setBackgroundColor(Color.parseColor("#e52b50"));
                    holder.requestButton.setText("Запросить");
                    holder.requestButton.setEnabled(true);
                }
                if(DataManager.psHandler.isRequestedByUser(DataManager.user.getIdUser(), event.getIdEvent())){
                    holder.requestButton.setBackgroundColor(Color.GRAY);
                    holder.requestButton.setText("Запрошено");
                    holder.requestButton.setEnabled(false);
                }
            });
        }).start();

        holder.requestButton.setOnClickListener((v) -> {
            requestItemBtn(holder, position);
        });
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
            eventImage = itemView.findViewById(R.id.eventImageMain);
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

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void requestItemBtn(ViewHolder holder, int position){
        Event event = events.get(position);
        Request request = new Request(DataManager.user.getIdUser(), event.getIdEvent(), false, false);
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                DataManager.psHandler.addRequest(request);
            });
        }).start();

        holder.requestButton.setBackgroundColor(Color.GRAY);
        holder.requestButton.setText("Запрошено");
        holder.requestButton.setEnabled(false);
    }
}