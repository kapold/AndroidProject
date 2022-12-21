package by.adamovich.eventos.recycler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.R;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Request> requests;
    public Context context;

    public MyRequestAdapter(Context context, List<Request> requests) {
        this.requests = requests;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MyRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_request_item, parent, false);
        return new MyRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRequestAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);

        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                Event event = DataManager.psHandler.getEventById(request.getIdEvent());
                holder.infoView.setText(event.getName());
                if (request.isAccepted){
                    holder.resultView.setTextColor(Color.GREEN);
                    holder.resultView.setText("ОДОБРЕНО");
                }
                else if (request.isStandby){
                    holder.resultView.setTextColor(Color.RED);
                    holder.resultView.setText("ОТКАЗАНО");
                }
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView infoView, resultView;

        ViewHolder(View itemView){
            super(itemView);
            infoView = itemView.findViewById(R.id.myRequestInfo);
            resultView = itemView.findViewById(R.id.myRequestResult);
        }
    }
}