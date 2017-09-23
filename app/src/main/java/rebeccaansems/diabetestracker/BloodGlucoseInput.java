package rebeccaansems.diabetestracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;



public class BloodGlucoseInput extends AppCompatActivity
{
    EditText currentBloodSugar;
    CheckBox didExercise, isSick, isHoromones;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputbloodsugar);

        setTitle("Input");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentBloodSugar = (EditText) findViewById(R.id.e_bloodSugarValue);

        didExercise = (CheckBox) findViewById(R.id.c_exercise);
        isSick = (CheckBox) findViewById(R.id.c_sick);
        isHoromones = (CheckBox) findViewById(R.id.c_horomones);

        Button submit = (Button) findViewById(R.id.b_submitBloodSugarValue);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addData();
            }
        });
    }

    void addData(){
        BloodSugarDataPoint bsdp = new BloodSugarDataPoint(Float.parseFloat(currentBloodSugar.getText().toString()),
                new SimpleDateFormat("dd.HH").format(new Date()), didExercise.isChecked(), isSick.isChecked(), isHoromones.isChecked());
        bsdp.save();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        float highBlood = Float.parseFloat(sharedPref.getString("highBlood", "10.0"));

        if(bsdp.bloodSugarValue > highBlood && bsdp.getId() > 3 &&
                BloodSugarDataPoint.findById(BloodSugarDataPoint.class, bsdp.getId()-1).bloodSugarValue > highBlood&&
                BloodSugarDataPoint.findById(BloodSugarDataPoint.class, bsdp.getId()-2).bloodSugarValue > highBlood){

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("High Blood Sugars")
                    .setMessage("You have had high blood sugars for the last three readings, " +
                            "consider testing for ketones or changing your insulin site.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            gotoMainScreen();
                        }
                    })
                    .setNegativeButton("Contact Doctor", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            gotoMainScreen();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            gotoMainScreen();
        }
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
