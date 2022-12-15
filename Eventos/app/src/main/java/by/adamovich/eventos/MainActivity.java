package by.adamovich.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import by.adamovich.eventos.databases.JsonSerialization;
import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.databases.SharedPreferencesHelper;
import by.adamovich.eventos.databases.XmlSerialization;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.User;
import by.adamovich.eventos.recycler.EventAdapter;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity   {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ArrayList<Event> events = new ArrayList<>();
    RecyclerView eventRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        JsonSerialization jsonSerialization = new JsonSerialization("userInfo.json");
        XmlSerialization xmlSerialization = new XmlSerialization("userInfo.xml");
        SharedPreferencesHelper spHelper = new SharedPreferencesHelper(this);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.addEventItem:
                    Intent newEventIntent = new Intent(this, AddEventActivity.class);
                    startActivity(newEventIntent);
                    drawerLayout.closeDrawers();
                    break;
                case R.id.checkRequestsItem:
                    Toast.makeText(this, "Пака шо нет, ждем апдейт!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.saveInfoItem:
                    ArrayList<User> exportList = new ArrayList<>();
                    exportList.add(DataManager.user);
                    jsonSerialization.exportToJSON(this, exportList);
                    xmlSerialization.exportXml(this, exportList);
                    spHelper.savePreferences(DataManager.user);
                    Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.loadInfoItem:
                    List<User> importList = new ArrayList<>();
                    importList = jsonSerialization.importFromJSON(this);
                    Toast.makeText(this, "User from Json:\n" + importList.get(0).toString(), Toast.LENGTH_SHORT).show();
                    importList = xmlSerialization.importXml(this);
                    Toast.makeText(this, "User from Xml:\n" + importList.get(0).toString(), Toast.LENGTH_SHORT).show();
                    spHelper.loadPreferences();
                    break;
                case R.id.exitItem:
                    DataManager.user = null;
                    Intent startActivity = new Intent(this, StartActivity.class);
                    startActivity(startActivity);
                    finish();
                    break;
            }
            return false;
        });

        // Recycler
        setInitialData();
        eventRecycler = findViewById(R.id.eventsRecycler);
        EventAdapter eventAdapter = new EventAdapter(this, events);
        eventRecycler.setAdapter(eventAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setInitialData(){
        try{
            // TODO: Multithreading
            //events = (ArrayList<Event>) DataManager.psHandler.getEvents();
        }
        catch (Exception ex){
            Toast.makeText(this, "Произошла ошибка загрузки списка событий", Toast.LENGTH_SHORT).show();
            Log.d("setInitialData(): ", ex.getMessage());
        }
    }
}