package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Request> requests;
    public Context context;

    public RequestAdapter(Context context, List<Request> requests) {
        this.requests = requests;
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
                holder.eventName.setText(DataManager.psHandler.getEventById(request.getIdEvent()).getName());
                holder.senderNick.setText(DataManager.psHandler.getUserById(request.getIdSender()).getNickname());

                if (request.isAccepted()){
                    holder.addButton.setBackgroundColor(Color.GREEN);
                    holder.addButton.setText("Добавлен");
                }
                else if (request.isStandby()){
                    holder.addButton.setBackgroundColor(Color.RED);
                    holder.addButton.setText("Отказано");
                }
            });
        }).start();

        holder.addButton.setOnClickListener((v) -> {
            addSenderBtn(holder);
        });
        holder.rejectButton.setOnClickListener((v) -> {
            rejectSenderBtn(holder);
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // final..
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

    public void addSenderBtn(RequestAdapter.ViewHolder holder){
        

        holder.addButton.setBackgroundColor(Color.GREEN);
        holder.rejectButton.setBackgroundColor(Color.WHITE);
        holder.addButton.setText("Добавлен");
        holder.rejectButton.setText("Отказать");
    }

    public void rejectSenderBtn(RequestAdapter.ViewHolder holder){


        holder.rejectButton.setBackgroundColor(Color.RED);
        holder.addButton.setBackgroundColor(Color.WHITE);
        holder.addButton.setText("Добавить");
        holder.rejectButton.setText("Отказано");
    }
}