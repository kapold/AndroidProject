package by.adamovich.eventos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
//        configCloudinary();
//        imageView.setOnClickListener(view -> {
//            requestPermission();
//        });
        imageView.setOnClickListener(view -> {
            imageChooser();
        });


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

//    private static final int PERMISSION_CODE = 1;
//    private static final int PICK_IMAGE = 1;
//    String filePath;
//    String cloudinaryImagePath = "";
//    Map config = new HashMap();
//
//    private void configCloudinary() {
//        config.put("cloud_name", "dft0czskt");
//        config.put("api_key", "644257398862186");
//        config.put("api_secret", "PVjM2Ls_M2_q0dLS8u_NlG7HZJ0");
//        MediaManager.init(AddEventActivity.this, config);
//    }
//
//    private void requestPermission(){
//        if(ContextCompat.checkSelfPermission
//                (AddEventActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED)
//            accessTheGallery();
//        else
//            ActivityCompat.requestPermissions(
//                    AddEventActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    PERMISSION_CODE
//            );
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                accessTheGallery();
//            else
//                Toast.makeText(AddEventActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void accessTheGallery(){
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        i.setType("image/*");
//        startActivityForResult(i, PICK_IMAGE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//         super.onActivityResult(requestCode, resultCode, data);
//         filePath = getRealPathFromUri(data.getData(), AddEventActivity.this);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 200) {
//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri)
//                    imageView.setImageURI(selectedImageUri);
//            }
//        }
//    }
//    private String getRealPathFromUri(Uri imageUri, AddEventActivity activity){
//         Cursor cursor = activity.getContentResolver().query(imageUri, null,  null, null, null);
//         if(cursor == null) {
//             return imageUri.getPath();
//         }
//         else{
//             cursor.moveToFirst();
//             int idx =  cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//             return cursor.getString(idx);
//         }
//    }
//
//    public void uploadToCloudinary(String filePath) {
//        long time = System.currentTimeMillis();
//        MediaManager.get().upload(filePath).callback(new UploadCallback() {
//            @Override
//            public void onStart(String requestId) {}
//            @Override
//            public void onProgress(String requestId, long bytes, long totalBytes) {}
//            @Override
//            public void onSuccess(String requestId, Map resultData) {
//                cloudinaryImagePath = resultData.get("url").toString();
//                Log.d("URL:", cloudinaryImagePath);
//            }
//            @Override
//            public void onError(String requestId, ErrorInfo error) {}
//            @Override
//            public void onReschedule(String requestId, ErrorInfo error) {}
//        }).dispatch();
//    }

    private void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri)
                    imageView.setImageURI(selectedImageUri);

                try {
                    Uri imageUri = data.getData();
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Bitmap imageBitmap;
    public byte[] getImageByte(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        imageBitmap.recycle();
        return byteArray;
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
        if (imageBitmap == null){
            Toast.makeText(this, "Картинка не выбрана!", Toast.LENGTH_SHORT).show();
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
        DataManager.psHandler.addEvent(event, getImageByte());

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
