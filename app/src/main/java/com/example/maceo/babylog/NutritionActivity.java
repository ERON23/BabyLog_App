package com.example.maceo.babylog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    Button mStartButtonMeal;
    TextView mDateAndTimeMeal;
    Button mSaveToDataBaseBtn;
    private EditText mMealNote;
    private Spinner mMealSpinner, mSupplementSpinner;
    private FirebaseAuth mAuth;
    private String mDate;
    private String mTime;


    private int dayFinal, monthFinal, yearFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStartButtonMeal =(Button) findViewById(R.id.btn_start_meal);
        mDateAndTimeMeal =(TextView) findViewById(R.id.txt_time_and_date_meal);
        mSaveToDataBaseBtn =(Button) findViewById(R.id.btn_save_to_database);
        mMealSpinner = (Spinner) findViewById(R.id.meal_spinner);
        mSupplementSpinner = (Spinner) findViewById(R.id.supplement_spinner);
        mMealNote = (EditText) findViewById(R.id.meal_notes_txt);


        mSaveToDataBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mealChosen = mMealSpinner.getSelectedItem().toString();
                String supplementChosen = mSupplementSpinner.getSelectedItem().toString();
                String dateAndTime = mDateAndTimeMeal.getText().toString();
                String mealNote = mMealNote.getText().toString();

                //spinner for meal feeding
                mMealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String meal_arrays = adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //spinner for supplement feeding
                mSupplementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String supplement_arrays = adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                //database stuff here
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user_id).child("Feeding").child("Meal Feeding").child("Time Stamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("Meal_Consumed", mMealSpinner);
                newPost.put("Supplement_Consumed", mSupplementSpinner);
                newPost.put("Date_and_Time", mDateAndTimeMeal);
                newPost.put("Meal_Note", mMealNote);

                current_user_db.child(mDate).child(mTime).setValue(newPost);

                /*Intent i =new Intent(getApplicationContext(),FeedingActivity.class);
                startActivity(i);*/
            }
        });


        mStartButtonMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NutritionActivity.this, NutritionActivity.this,
                        year,month,day);
                datePickerDialog.show();

            }
        });
    }

    @Override

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog =new TimePickerDialog(NutritionActivity.this, NutritionActivity.this,
                hour,minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

        mDate = monthFinal + "-"+dayFinal+ "-"+yearFinal;


    }



    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String amOrPm = " AM";
        if(hour > 12){
            hour = hour - 12;
            amOrPm = " PM";
        }
        mDateAndTimeMeal.setText(monthFinal + "/"+ dayFinal + "/"+ yearFinal + " ("+ hour + ":"+ minute + amOrPm +")");
        mTime = " ("+ hour + ":"+ minute + amOrPm+")";
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
