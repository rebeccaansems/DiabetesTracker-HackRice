package rebeccaansems.diabetestracker;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Rebecca Ansems on 2017-09-24.
 */

public class Communicate {

    public static void SendMessageDoctor(String message, Context context){
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        String encrypted = encryption.encryptOrNull(message);

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "ansems_rebecca@hotmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "SixTwelveSix - Question");
        email.putExtra(Intent.EXTRA_TEXT, encrypted+" Please call when you get the chance");
        email.setType("message/rfc822");

        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
