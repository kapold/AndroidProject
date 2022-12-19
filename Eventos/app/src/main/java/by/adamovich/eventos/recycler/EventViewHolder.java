package by.adamovich.eventos.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import by.adamovich.eventos.R;

public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView eventTitle;
    ImageView eventImage;
    View view;

    EventViewHolder(View itemView)
    {
        super(itemView);
        eventTitle = itemView.findViewById(R.id.eventTitle);
        eventImage = itemView.findViewById(R.id.eventImage);
        view = itemView;
    }
}
