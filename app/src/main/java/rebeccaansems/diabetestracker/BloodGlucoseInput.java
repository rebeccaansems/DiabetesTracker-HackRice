package rebeccaansems.diabetestracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Date;

/**
 * Created by Rebecca Ansems on 2017-09-23.
 */

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
                gotoMainScreen();
            }
        });
    }

    void addData(){
        BloodSugarDataPoint bsdp = new BloodSugarDataPoint(Integer.parseInt(currentBloodSugar.getText().toString()),
                new Date(), didExercise.isChecked(), isSick.isChecked(), isHoromones.isChecked());
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
