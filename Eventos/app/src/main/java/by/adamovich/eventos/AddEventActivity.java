package by.adamovich.eventos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.List;

import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Type;


public class AddEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setTitle("New Event");
        eventTime = "";
        eventDate = "";
        imageView = findViewById(R.id.eventImage);
        titleTIL = findViewById(R.id.titleTIL);
        eventTypeTIL = findViewById(R.id.eventTypeTIL);
        peopleCountTIL = findViewById(R.id.peopleCountTIL);
        placeTIL = findViewById(R.id.placeTIL);
        isDateSelected = false;
        isTimeSelected = false;

        // Date picker
        pickDateButton = findViewById(R.id.chooseDateBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Выберите дату");
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
        imageView.setOnClickListener(v -> imageChooser());

        autoCompleteTextView = findViewById(R.id.eventTypeText);
        List<String> typesForAdapter = DataManager.psHandler.getStringTypes();
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, typesForAdapter));
    }
    TextInputLayout titleTIL, eventTypeTIL, peopleCountTIL, placeTIL;
    boolean isDateSelected, isTimeSelected;
    String eventDate, eventTime;
    Button pickDateButton, pickTimeButton;
    ImageView imageView;
    AutoCompleteTextView autoCompleteTextView;

    private void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri)
                    imageView.setImageURI(selectedImageUri);
            }
        }
    }

    private int getIdType(String typeName){
        List<Type> types = DataManager.psHandler.getTypes();
        int id = 0;
        for (Type t: types)
            if (t.getType().equals(typeName))
                id = t.getIdType();
        return id;
    }

    // TODO: start using CLOUDINARY to store images
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
                eventTime, eventDate, Integer.parseInt(peopleCount), "IMG NET");
        DataManager.psHandler.addEvent(event);

        Toast.makeText(this, "Событие добавлено!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
