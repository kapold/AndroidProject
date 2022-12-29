package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.databases.JsonSerialization;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;
import by.adamovich.eventos.models.Type;
import by.adamovich.eventos.models.User;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    public Context context;

    private final List<Event> events;
    private final List<Type> types;
    private final List<User> users;
    private final List<Request> requests;

    public EventAdapter(Context context, List<Event> events, List<Type> types, List<User> users, List<Request> requests) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;

        this.events = events;
        this.types = types;
        this.users = users;
        this.requests = requests;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);

        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                holder.typeView.setText(getTypeByEventId(event.getIdType()));
                holder.creatorView.setText(getCreatorNicknameById(event.getIdCreator()));

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

                if (!isMineEvent(DataManager.user.getIdUser(), event.getIdEvent())){
                    holder.requestButton.setVisibility(View.VISIBLE);
                    holder.requestButton.setBackgroundColor(Color.parseColor("#e52b50"));
                    holder.requestButton.setText("Запросить");
                    holder.requestButton.setEnabled(true);
                }
                if (isRequestedByUser(DataManager.user.getIdUser(), event.getIdEvent())){
                    holder.requestButton.setBackgroundColor(Color.GRAY);
                    holder.requestButton.setText("Запрошено");
                    holder.requestButton.setEnabled(false);
                }
                if (isFullyOccupied(event)){
                    holder.requestButton.setVisibility(View.INVISIBLE);
                    holder.requestButton.setEnabled(false);
                }
            });
        }).start();

        holder.requestButton.setOnClickListener((v) -> {
            requestItemBtn(holder, position);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView eventImage, bookmarkImage;
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
            bookmarkImage = itemView.findViewById(R.id.bookmarkIV);
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

    // SPEED :)
    private String getTypeByEventId(int idEvent){
        for (Type t: types)
            if (t.getIdType() == idEvent)
                return t.getType();
        return null;
    }

    private String getCreatorNicknameById(int idEvent){
        for (User u: users)
            if (u.getIdUser() == idEvent)
                return u.getNickname();
        return null;
    }

    private boolean isMineEvent(int idUser, int idEvent){
        for (Event e: events)
            if (e.getIdCreator() == idUser && e.getIdEvent() == idEvent)
                return true;
        return false;
    }

    private boolean isRequestedByUser(int idUser, int idEvent){
        for (Request r: requests)
            if (r.getIdSender() == idUser && r.getIdEvent() == idEvent)
                return true;
        return false;
    }

    private boolean isFullyOccupied(Event event){
        if (event.getOccupied() >= event.getCapacity())
            return true;
        return false;
    }
}