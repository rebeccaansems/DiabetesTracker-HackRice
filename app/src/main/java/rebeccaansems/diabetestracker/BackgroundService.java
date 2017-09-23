package rebeccaansems.diabetestracker;

/**
 * Created by SriramHariharan on 9/23/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                //Insert action code here
                //      Toast.makeText(getApplicationContext(), "ran!", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor infoedit = preferences.edit();


                try {
        //            ClssPkg p = ClssPkg.getFromServer(muser, mPassword);

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.alert)
                                            .setContentTitle("six-twelve-six")
                                            .setContentText("Dangerous trend of glucose readings");
                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());


                } catch (Exception e) {
                    Log.e("THE ERROR", e.toString());
                }
                handler.postDelayed(runnable, 10000);
                //1800000 is 30 mins

            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //     Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}
