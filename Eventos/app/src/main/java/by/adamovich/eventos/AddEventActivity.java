package by.adamovich.eventos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;


public class AddEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setTitle("New Event");
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
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10);
        final MaterialTimePicker materialTimePicker = materialTimeBuilder.build();
        pickTimeButton.setOnClickListener(v -> {
            materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
        });
        materialTimePicker.addOnPositiveButtonClickListener(selection -> {
           eventTime = String.format("%s, %s", materialTimePicker.getHour(), materialTimePicker.getMinute());
           isTimeSelected = true;
        });

        // Image picker
        imageView.setOnClickListener(v -> imageChooser());
    }
    TextInputLayout titleTIL, eventTypeTIL, peopleCountTIL, placeTIL;
    boolean isDateSelected, isTimeSelected;
    String eventDate, eventTime;
    Button pickDateButton, pickTimeButton;
    ImageView imageView;
    int SELECT_PICTURE = 200;

    private void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri)
                    imageView.setImageURI(selectedImageUri);
            }
        }
    }

    // TODO: start using CLOUDINARY to store images
    private void addEventBtn(){
        if (!isDateSelected){
            Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isTimeSelected){
            Toast.makeText(this, "Выберите время", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
