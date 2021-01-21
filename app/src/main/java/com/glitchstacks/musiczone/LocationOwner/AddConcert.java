package com.glitchstacks.musiczone.LocationOwner;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.glitchstacks.musiczone.AddPlaylist;
import com.glitchstacks.musiczone.Database.SessionManager;
import com.glitchstacks.musiczone.Entries.SignUp;
import com.glitchstacks.musiczone.Entries.SignUp2ndClass;
import com.glitchstacks.musiczone.Matches.MatchesAdapter;
import com.glitchstacks.musiczone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddConcert extends Fragment {

    private EditText inputConcertName, inputDescription, inputDuration;
    private TextInputLayout layoutConcertName, layoutDescription, layoutDuration;
    private DatePicker datePicker;
    private Button btnSetTime, btnNext;
    private TextView txtChosenTime, txtMainGenre;
    private TimePickerDialog timePickerDialog;
    private String concertName, concertDescription, concertDate, concertTime, concertDurationStr, phoneNumber, mainGenre;
    private Integer concertDuration;
    private ImageView concertImage;
    private Uri resultUri;
    private DatabaseReference mDatabase;
    private ArrayList<String> arrayList;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_concert, container, false);

        // Session Manager for Current User
        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);

        // User Information map
        HashMap<String, String> map = sessionManager.getUsersDetailFromSession();

        // UserID
        phoneNumber = map.get(SessionManager.KEY_PHONENUMBER);

        // Hook

        // TextLayout
        layoutConcertName = root.findViewById(R.id.title_layout);
        layoutDescription = root.findViewById(R.id.description_layout);
        layoutDuration = root.findViewById(R.id.duration_layout);

        // EditText
        inputConcertName = root.findViewById(R.id.title_input);
        inputDescription = root.findViewById(R.id.description_input);
        inputDuration = root.findViewById(R.id.duration_input);

        // Date
        datePicker = root.findViewById(R.id.date_picker);

        // Button
        btnSetTime = root.findViewById(R.id.btnSetTime);
        btnNext = root.findViewById(R.id.btnNext);

        // TextView
        txtChosenTime = root.findViewById(R.id.txtChosenTime);
        txtMainGenre = root.findViewById(R.id.txtMainGenre);

        // ArrayList

        arrayList = new ArrayList<>();

        // Memasukkan value ke dalam arrayList
        arrayList.add("EDM");
        arrayList.add("Jazz");
        arrayList.add("Country");
        arrayList.add("Classical");
        arrayList.add("Pop");
        arrayList.add("Indie");
        arrayList.add("Dangdut");

        txtMainGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initiaize dialog
                dialog = new Dialog(getContext());

                // Set customer Dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000, 1500);

                // transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();


                //Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.listView);

                // Initialize array adapter
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.simple_list_item_1, arrayList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the Item from ListView
                        View view = super.getView(position, convertView, parent);

                        // Initialize a TextView for ListView each Item
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text color of TextView (ListView Item)
                        tv.setTextColor(Color.BLACK);

                        // Generate ListView Item using TextView
                        return view;
                    }
                };


                //set Adapter
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Filter array List
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // When item selected from list

                        // Set selected item on text View

                        txtMainGenre.setText(adapter.getItem(position));

                        dialog.dismiss();
                    }
                });
            }
        });






        // Image
        concertImage = root.findViewById(R.id.imgConcert);

        // DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // OnClick Listener for Time
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        // OnClick Listener for Next Button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        // OnClick Listener for Concert Image
        concertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        return root;
    }

    private void validateData() {

        // Hook
        concertName = inputConcertName.getText().toString();
        concertDescription = inputDescription.getText().toString();
        concertDurationStr = inputDuration.getText().toString();
        mainGenre = txtMainGenre.getText().toString();

        Boolean a,b,c,d,e,f;

        b = isGenreValid();
        Log.d("isGenreValid",b.toString());
        a = isConcertNameValid();
        c = isConcertDateValid();
        d = isConcertDescriptionValid();
        e = isDurationValid();
        f = isPictureValid();

        if(!a || !b || !c || !d || !e || !f){
            return;
        }

        // membuat intent untuk aktivitas selanjutnya
        Intent intent = new Intent(getActivity().getApplicationContext(), AddPlaylist.class);

        // memberikan informasi ke aktivitas selanjutnya
        intent.putExtra("concertName", concertName);
        intent.putExtra("concertDescription", concertDescription);

        saveConcertInformation(intent);

        // memulai aktivitas selanjutnya


    }

    private Boolean isGenreValid() {
        // Validate concert Main Genre
        if(mainGenre.isEmpty()){
            Toast.makeText(getContext(), "Choose genre first!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private Boolean isDurationValid() {

        if(concertDurationStr.isEmpty()){
            layoutDuration.setError("field can't be empty");
            return false;
        }

        concertDuration = Integer.parseInt(concertDurationStr);

        if(concertDuration == 0){
            layoutDuration.setError("duration must be more than 0");
            return false;
        }
        else{
            if(concertDuration > 240){
                layoutDuration.setError("concert duration is too long!");
                return false;
            } else{
                layoutDuration.setError(null);
                layoutDuration.setErrorEnabled(false);
            }
        }
        return true;
    }

    private Boolean isPictureValid(){
        if(resultUri == null){
            Toast.makeText(getContext(), "Picture must be chosen!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean isConcertDateValid() {
        Date currentdate = Calendar.getInstance().getTime();

        // Hook
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();

        String month2 = String.format("%02d", month);

        concertDate = String.valueOf(day) + "/" + String.valueOf(month2) + "/" + String.valueOf(year);
        concertTime = txtChosenTime.getText().toString().replaceAll(" ", "");

        Log.d("txtChosentime", txtChosenTime.getText().toString());

        if(txtChosenTime.getText().toString().equals("Chosen Time")){

            Log.d("isChosenTimeEmpty", "Yes");
            Toast.makeText(getContext(), "Date or time cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;

        }

        String temp_date = concertDate + " " + concertTime;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Date temp_date1 = null;

        try {
            temp_date1 = simpleDateFormat.parse(temp_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Boolean isbefore = temp_date1.before(currentdate);

        Log.d("Bingung", isbefore.toString());

        if(isbefore) {

            Toast.makeText(getContext(), "Date can't be in the past!", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }

    private boolean isConcertDescriptionValid() {

        // Validate concert Description
        if(concertDescription.isEmpty()){
            layoutDescription.setError("field cannot be emtpy");
            return false;
        }
        else{
            if(concertDescription.length() > 150){
                layoutDescription.setError("description is too long!");
                return false;
            }else if(concertDescription.length() < 10){
                layoutDescription.setError("description is too short!");
                return false;
            }else{
                layoutDescription.setError(null);
                layoutDescription.setErrorEnabled(false);
            }
        }
        return true;
    }

    private boolean isConcertNameValid() {

        // Validate concertName
        if (concertName.isEmpty()) {
            layoutConcertName.setError("field cannot be empty");
            return false;
        } else {

            if (concertName.length() > 20 || concertName.length()<3){
                layoutConcertName.setError("concert name must between 3-20 character");
                return false;
            }
            else{
                layoutConcertName.setError(null);
                layoutConcertName.setErrorEnabled(false);
            }
        }

        return true;

    }

    private void showTimeDialog() {

        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                txtChosenTime.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute));

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(getContext()));

        timePickerDialog.show();

    }

    private void saveConcertInformation(Intent intent) {

        final String key = mDatabase.child("Concerts").push().getKey();
        DatabaseReference concertDatabase = mDatabase.child("Concerts").child(key);

        intent.putExtra("key", key);

        final Map concertInfo = new HashMap();

        if(resultUri != null){
            Log.d("resultUri", "in");
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("concertImages").child(key);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("uploadFail",e.getMessage().toString());
//                }
//            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {

                            Log.d("uploadSucess","uploadSuccess");
                            Map userInfo = new HashMap();

                             concertInfo.put("id", key);
                             concertInfo.put("concert_name", concertName);
                             concertInfo.put("description", concertDescription);
                             concertInfo.put("duration", concertDuration);
                             concertInfo.put("date", concertDate);
                             concertInfo.put("time", concertTime);
                             concertInfo.put("viewer", 0);
                             concertInfo.put("imageURL", downloadUrl.toString());

                            mDatabase.child("Concerts").child(key).updateChildren(concertInfo);

                            //getActivity().finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("uploadFail",e.getMessage().toString());
                        }
                    });
                    /*
                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                     */
                }
            });
        } else {

            //getActivity().finish();

        }


        startActivity(intent);

        }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            concertImage.setImageURI(resultUri);
        }
        else{
            Toast.makeText(getActivity(), "in", Toast.LENGTH_LONG).show();
        }
    }


}