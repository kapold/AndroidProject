package by.adamovich.eventos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Request;
import by.adamovich.eventos.recycler.MyRequestAdapter;
import by.adamovich.eventos.recycler.RequestAdapter;

public class MyRequestsActivity extends AppCompatActivity {
    RecyclerView requestsRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        setTitle("Запросы");

        requestsRecycler = findViewById(R.id.myRequestsRecycler);
        reloadRequestsRecycler();
    }

    private void reloadRequestsRecycler(){
        try{
            new Thread(() -> {
                List<Request> resultList = DataManager.psHandler.getRequestsFromUser(DataManager.user);
                MyRequestAdapter myRequestAdapter = new MyRequestAdapter(this, resultList);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    requestsRecycler.setAdapter(myRequestAdapter);
                });
            }).start();
        }
        catch (Exception ex){
            Toast.makeText(this, "Произошла ошибка загрузки списка событий", Toast.LENGTH_SHORT).show();
            Log.d("setInitialData(): ", ex.getMessage());
        }
    }
}