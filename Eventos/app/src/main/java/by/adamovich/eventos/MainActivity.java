package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import by.adamovich.eventos.databases.JsonSerialization;
import by.adamovich.eventos.databases.SharedPreferencesHelper;
import by.adamovich.eventos.databases.XmlSerialization;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.User;
import by.adamovich.eventos.recycler.EventAdapter;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ArrayList<Event> events = new ArrayList<>();
    RecyclerView eventRecycler;
    ShimmerFrameLayout shimmerFrameLayout;
    TextInputLayout searchTIL;
    EditText searchET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Shimmer
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

        // Search
        searchTIL = findViewById(R.id.searchTIL);
        searchET = searchTIL.getEditText();
        searchET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        // Drawer creation
        drawerLayout = findViewById(R.id.main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Serialization
        JsonSerialization jsonSerialization = new JsonSerialization("userInfo.json");
        XmlSerialization xmlSerialization = new XmlSerialization("userInfo.xml");
        SharedPreferencesHelper spHelper = new SharedPreferencesHelper(this);

        // Drawer menu
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
                case R.id.notesItem:
                    Intent newNotesIntent = new Intent(this, NotesActivity.class);
                    startActivity(newNotesIntent);
                    drawerLayout.closeDrawers();
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
        eventRecycler = findViewById(R.id.eventsRecycler);
        reloadEventRecycler();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void reloadEventRecycler(){
        try{
            new Thread(() -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    searchET.setEnabled(false);
                });

                List<Event> resultList = DataManager.psHandler.getEvents();
                EventAdapter eventAdapter = new EventAdapter(this, resultList);

                handler.post(() -> {
                    shimmerFrameLayout.hideShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    eventRecycler.setVisibility(View.VISIBLE);
                    searchET.setEnabled(true);

                    eventRecycler.setAdapter(eventAdapter);
                });
            }).start();
        }
        catch (Exception ex){
            Toast.makeText(this, "Произошла ошибка загрузки списка событий", Toast.LENGTH_SHORT).show();
            Log.d("setInitialData(): ", ex.getMessage());
        }
    }
}