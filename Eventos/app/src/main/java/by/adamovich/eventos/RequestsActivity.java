package by.adamovich.eventos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;
import by.adamovich.eventos.recycler.EventAdapter;
import by.adamovich.eventos.recycler.RequestAdapter;

public class RequestsActivity extends AppCompatActivity {
    RecyclerView requestsRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        setTitle("Запросы");

        requestsRecycler = findViewById(R.id.requestsRecycler);
        reloadRequestsRecycler();
    }

    private void reloadRequestsRecycler(){
        try{
            new Thread(() -> {
                List<Request> resultList = DataManager.psHandler.getRequests();
                RequestAdapter requestAdapter = new RequestAdapter(this, resultList);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    requestsRecycler.setAdapter(requestAdapter);
                });
            }).start();
        }
        catch (Exception ex){
            Toast.makeText(this, "Произошла ошибка загрузки списка событий", Toast.LENGTH_SHORT).show();
            Log.d("setInitialData(): ", ex.getMessage());
        }
    }
}