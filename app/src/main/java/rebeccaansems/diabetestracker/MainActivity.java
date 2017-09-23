package rebeccaansems.diabetestracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openedMainScreen();
    }

    void openedMainScreen(){
        Button gotoSettings = (Button) findViewById(R.id.b_gotoSettings);
        gotoSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoSettings();
            }
        });
    }

    void gotoSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}
