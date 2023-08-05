package aandrosov.bookstore;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import androidx.core.os.HandlerCompat;

public class Utils {

    public static void showSimpleDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .create().show();
    }
}
