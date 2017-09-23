package rebeccaansems.diabetestracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    EditText lowBlood, highBlood, veryHighBlood;
    EditText breakfastTime, lunchTime, supperTime, nightTime, lateNightTime;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        lowBlood = (EditText)findViewById(R.id.e_lowBlood);
        highBlood = (EditText)findViewById(R.id.e_highBlood);
        veryHighBlood = (EditText)findViewById(R.id.e_veryHighBlood);

        breakfastTime = (EditText)findViewById(R.id.e_breakfastTime);
        lunchTime = (EditText)findViewById(R.id.e_lunchTime);
        supperTime = (EditText)findViewById(R.id.e_supperTime);
        nightTime = (EditText)findViewById(R.id.e_nightTime);
        lateNightTime = (EditText)findViewById(R.id.e_lateNightTime);

        LoadSavedData();

        setTitle("Settings");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button submitSettings = (Button) findViewById(R.id.b_submitSettings);
        submitSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveData();
                gotoMainScreen();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    void LoadSavedData(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        lowBlood.setText(sharedPref.getString("lowBlood", "4.0"), TextView.BufferType.EDITABLE);
        highBlood.setText(sharedPref.getString("highBlood", "10.0"), TextView.BufferType.EDITABLE);
        veryHighBlood.setText(sharedPref.getString("veryHighBlood", "15.0"), TextView.BufferType.EDITABLE);

        breakfastTime.setText(sharedPref.getString("breakfastTime", "09:00"), TextView.BufferType.EDITABLE);
        lunchTime.setText(sharedPref.getString("lunchTime", "12:00"), TextView.BufferType.EDITABLE);
        supperTime.setText(sharedPref.getString("supperTime", "17:00"), TextView.BufferType.EDITABLE);
        nightTime.setText(sharedPref.getString("nightTime", "23:00"), TextView.BufferType.EDITABLE);
        lateNightTime.setText(sharedPref.getString("lateNightTime", "03:00"), TextView.BufferType.EDITABLE);
    }

    void saveData(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("lowBlood", lowBlood.getText().toString());
        editor.putString("highBlood", highBlood.getText().toString());
        editor.putString("veryHighBlood", veryHighBlood.getText().toString());

        editor.putString("breakfastTime", breakfastTime.getText().toString());
        editor.putString("lunchTime", lunchTime.getText().toString());
        editor.putString("supperTime", supperTime.getText().toString());
        editor.putString("nightTime", nightTime.getText().toString());
        editor.putString("lateNightTime", lateNightTime.getText().toString());

        editor.commit();
    }

    void gotoMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
