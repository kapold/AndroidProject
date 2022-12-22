package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.ChipGroup;
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
import by.adamovich.eventos.models.Type;
import by.adamovich.eventos.models.User;
import by.adamovich.eventos.recycler.EventAdapter;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView eventRecycler;
    ShimmerFrameLayout shimmerFrameLayout;
    TextInputLayout searchTIL;
    EditText searchET;
    SwipeRefreshLayout refreshLayout;
    AutoCompleteTextView filterACTV;
    CharSequence searchRequest = "";

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
                searchRequest = s;
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
                    Intent reqIntent = new Intent(this, RequestsActivity.class);
                    startActivity(reqIntent);
                    drawerLayout.closeDrawers();
                    break;
                case R.id.checkMyRequestsItem:
                    Intent myReqIntent = new Intent(this, MyRequestsActivity.class);
                    startActivity(myReqIntent);
                    drawerLayout.closeDrawers();
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
                    List<User> importList;
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

        // Refresh
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            reloadEventRecycler();
            refreshLayout.setRefreshing(false);
        });

        // Filter
        filterACTV = findViewById(R.id.filterACTV);
        List<String> filterTypes = DataManager.psHandler.getStringTypes();
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, filterTypes);
        filterACTV.setAdapter(filterAdapter);
        filterACTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft ) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DataManager.filter = s.toString();
                Toast.makeText(MainActivity.this, DataManager.filter, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadEventRecycler();
    }

    public void reloadEventRecycler(){
        try{
            new Thread(() -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    searchET.setEnabled(false);
                });

                List<Event> resultList = DataManager.psHandler.getEvents();
                if (resultList == null)
                    return;
                EventAdapter eventAdapter = new EventAdapter(this, resultList,
                        DataManager.psHandler.getTypes(), DataManager.psHandler.getUsers(), DataManager.psHandler.getRequests());

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

    public void searchEventBtn(View view){
        searchEvent();
    }

    private void searchEvent(){
        try{
            new Thread(() -> {
                if (searchRequest == "" && DataManager.filter.equals("Все"))
                    return;

                List<Event> eventsList = DataManager.psHandler.getEvents();
                List<Type> typesList = DataManager.psHandler.getTypes();
                List<Event> resultList = new ArrayList<>();
                for (Event e: eventsList)
                    if (e.getName().toUpperCase().contains(searchRequest.toString().toUpperCase()) &&
                            (getTypeByEventId(e.getIdType(), typesList).contains(DataManager.filter) ||
                                    DataManager.filter.equals("Все")))
                        resultList.add(e);

                EventAdapter eventAdapter = new EventAdapter(this, resultList,
                        typesList, DataManager.psHandler.getUsers(), DataManager.psHandler.getRequests());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    eventRecycler.setAdapter(eventAdapter);
                });
            }).start();
        }
        catch (Exception ex){
            Toast.makeText(this, "Произошла ошибка поиска", Toast.LENGTH_SHORT).show();
            Log.d("searchEvent(): ", ex.getMessage());
        }
    }

    public void clearFilter(View view){
        DataManager.filter = "Все";
        filterACTV.setText("Все");
        List<String> filterTypes = DataManager.psHandler.getStringTypes();
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, filterTypes);
        filterACTV.setAdapter(filterAdapter);
        searchEvent();
    }

    private String getTypeByEventId(int idEvent, List<Type> types){
        for (Type t: types)
            if (t.getIdType() == idEvent)
                return t.getType();
        return null;
    }
}