package by.adamovich.eventos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Type;
import by.adamovich.eventos.recycler.EventAdapter;


public class AddEventActivity extends AppCompatActivity {
    TextInputLayout titleTIL, eventTypeTIL, peopleCountTIL, placeTIL;
    boolean isDateSelected, isTimeSelected;
    String eventDate, eventTime;
    Button pickDateButton, pickTimeButton, addEventButton;
    ImageView imageView;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Новое событие");
        eventTime = "";
        eventDate = "";
        imageView = findViewById(R.id.eventImage);
        titleTIL = findViewById(R.id.titleTIL);
        eventTypeTIL = findViewById(R.id.eventTypeTIL);
        peopleCountTIL = findViewById(R.id.peopleCountTIL);
        placeTIL = findViewById(R.id.placeTIL);
        addEventButton = findViewById(R.id.addEventBtn);
        isDateSelected = false;
        isTimeSelected = false;

        // Date picker
        pickDateButton = findViewById(R.id.chooseDateBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Выберите дату").setCalendarConstraints(new CalendarConstraints.Builder()
                .setStart(MaterialDatePicker.todayInUtcMilliseconds())
                .build());
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        pickDateButton.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            eventDate = materialDatePicker.getHeaderText();
            isDateSelected = true;
        });

        // Time picker
        pickTimeButton = findViewById(R.id.chooseTimeBtn);
        MaterialTimePicker.Builder materialTimeBuilder = new MaterialTimePicker.Builder();
        materialTimeBuilder
                .setTitleText("Выбериет время")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10);
        final MaterialTimePicker materialTimePicker = materialTimeBuilder.build();
        pickTimeButton.setOnClickListener(v -> {
            materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
        });
        materialTimePicker.addOnPositiveButtonClickListener(selection -> {
           eventTime = String.format("%s:%s", materialTimePicker.getHour(), materialTimePicker.getMinute());
           isTimeSelected = true;
        });

        // Image picker
        if (!DataManager.isMediaManagerInitialized)
            configCloudinary();
        imageView.setOnClickListener(view -> {
            requestPermission();
        });

        autoCompleteTextView = findViewById(R.id.eventTypeText);
        List<String> typesForAdapter = DataManager.psHandler.getStringTypes();
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, typesForAdapter));
    }

    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;
    String filePath;
    String cloudinaryImagePath = "";
    Map config = new HashMap();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configCloudinary() {
        config.put("cloud_name", "dft0czskt");
        config.put("api_key", "644257398862186");
        config.put("api_secret", "PVjM2Ls_M2_q0dLS8u_NlG7HZJ0");
        MediaManager.init(AddEventActivity.this, config);
        DataManager.isMediaManagerInitialized = true;
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission
                (AddEventActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            accessTheGallery();
        else
            ActivityCompat.requestPermissions(
                    AddEventActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                accessTheGallery();
            else
                Toast.makeText(AddEventActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void accessTheGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = getRealPathFromUri(data.getData(), AddEventActivity.this);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this).load(data.getData()).apply(requestOptions).into(imageView);

        // get cloudinary
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                blockChoosingElements(true);
                uploadToCloudinary(filePath);
                blockChoosingElements(false);
            });
        }).start();
    }

    private String getRealPathFromUri(Uri imageUri, AddEventActivity activity){
         Cursor cursor = activity.getContentResolver().query(imageUri, null,  null, null, null);
         if(cursor == null) {
             return imageUri.getPath();
         }
         else{
             cursor.moveToFirst();
             int idx =  cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
             return cursor.getString(idx);
         }
    }

    public void uploadToCloudinary(String filePath) {
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {}
            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {}
            @Override
            public void onSuccess(String requestId, Map resultData) {
                cloudinaryImagePath = resultData.get("url").toString();
                Log.d("CLOUDINARY: ", "Картинка загружена! " + cloudinaryImagePath);
            }
            @Override
            public void onError(String requestId, ErrorInfo error) {}
            @Override
            public void onReschedule(String requestId, ErrorInfo error) {}
        }).dispatch();
    }

    public void blockChoosingElements(boolean blocked){
        if (blocked){
            addEventButton.setEnabled(false);
            imageView.setEnabled(false);
        }
        else{
            addEventButton.setEnabled(true);
            imageView.setEnabled(true);
        }
    }

    public void addEventBtn(View view){
        if (!isDateSelected){
            Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isTimeSelected){
            Toast.makeText(this, "Выберите время", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = titleTIL.getEditText().getText().toString(),
               eventType = eventTypeTIL.getEditText().getText().toString(),
               peopleCount = peopleCountTIL.getEditText().getText().toString(),
               place = placeTIL.getEditText().getText().toString();
        if (title.equals("") || eventType.equals("") || peopleCount.equals("") ||
                place.equals("")){
            if (title.equals(""))
                titleTIL.setError("Заполните поле!");
            else
                titleTIL.setError(null);

            if (eventType.equals(""))
                eventTypeTIL.setError("Заполните поле!");
            else
                eventTypeTIL.setError(null);

            if (peopleCount.equals(""))
                peopleCountTIL.setError("Заполните поле!");
            else
                peopleCountTIL.setError(null);

            if (place.equals(""))
                placeTIL.setError("Заполните поле!");
            else
                placeTIL.setError(null);

            return;
        }
        else{
            titleTIL.setError(null);
            eventTypeTIL.setError(null);
            peopleCountTIL.setError(null);
            placeTIL.setError(null);
        }

        Event event = new Event(DataManager.user.getIdUser(), title, getIdType(eventType), place,
                eventTime, eventDate, Integer.parseInt(peopleCount));
        DataManager.psHandler.addEvent(event, cloudinaryImagePath);

        Toast.makeText(this, "Событие добавлено!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int getIdType(String typeName){
        List<Type> types = DataManager.psHandler.getTypes();
        int id = 0;
        for (Type t: types)
            if (t.getType().equals(typeName))
                id = t.getIdType();
        return id;
    }
}