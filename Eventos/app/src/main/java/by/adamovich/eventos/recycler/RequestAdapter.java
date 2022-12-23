package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;
import by.adamovich.eventos.models.User;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Request> requests;
    private final List<Event> events;
    private final List<User> users;
    public Context context;

    public RequestAdapter(Context context, List<Request> requests, List<User> users, List<Event> events) {
        this.requests = requests;
        this.events = events;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.request_item, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);

        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                holder.eventName.setText(getEventById(request.getIdEvent()).getName());
                holder.senderNick.setText(getUserById(request.getIdSender()).getNickname());

                if (request.isAccepted()){
                    holder.addButton.setBackgroundColor(Color.GREEN);
                    holder.addButton.setText("Добавлен");
                }
                else if (request.isStandby()){
                    holder.rejectButton.setBackgroundColor(Color.RED);
                    holder.rejectButton.setText("Отказано");
                }
            });
        }).start();

        holder.addButton.setOnClickListener((v) -> {
            if (holder.addButton.getText().toString().equals("Добавлен")){
                Toast.makeText(context, "Уже добавлен", Toast.LENGTH_SHORT).show();
                return;
            }
            addSenderBtn(holder, position);
        });
        holder.rejectButton.setOnClickListener((v) -> {
            if (holder.rejectButton.getText().toString().equals("Отказано")){
                Toast.makeText(context, "Уже отказано", Toast.LENGTH_SHORT).show();
                return;
            }
            rejectSenderBtn(holder, position);
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView eventName, senderNick;
        final Button addButton, rejectButton;

        ViewHolder(View itemView){
            super(itemView);

            addButton = itemView.findViewById(R.id.addSenderBtn);
            rejectButton = itemView.findViewById(R.id.rejSenderBtn);
            eventName = itemView.findViewById(R.id.eventNameR);
            senderNick = itemView.findViewById(R.id.eventSenderR);
        }
    }

    public void addSenderBtn(RequestAdapter.ViewHolder holder, int position){
        Request request = requests.get(position);
        request.setAccepted(true);
        request.setStandby(false);
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                DataManager.psHandler.updateRequest(request);
                DataManager.psHandler.incrementOccupied(DataManager.psHandler.getEventById(request.getIdEvent()));
            });
        }).start();

        holder.addButton.setBackgroundColor(Color.GREEN);
        holder.rejectButton.setBackgroundColor(Color.WHITE);
        holder.addButton.setText("Добавлен");
        holder.rejectButton.setText("Отказать");
    }

    public void rejectSenderBtn(RequestAdapter.ViewHolder holder, int position){
        Request request = requests.get(position);
        request.setAccepted(false);
        request.setStandby(true);
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                DataManager.psHandler.updateRequest(request);
                // TODO: check for +
                if (request.isAccepted())
                    DataManager.psHandler.decrementOccupied(DataManager.psHandler.getEventById(request.getIdEvent()));
            });
        }).start();

        holder.rejectButton.setBackgroundColor(Color.RED);
        holder.addButton.setBackgroundColor(Color.WHITE);
        holder.addButton.setText("Добавить");
        holder.rejectButton.setText("Отказано");
    }

    private Event getEventById(int idEvent){
        for (Event e: events)
            if (e.getIdEvent() == idEvent)
                return e;
        return null;
    }

    private User getUserById(int idSender){
        for (User u: users)
            if (u.getIdUser() == idSender)
                return u;
        return null;
    }
}