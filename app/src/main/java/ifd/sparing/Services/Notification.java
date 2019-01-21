package ifd.sparing.Services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.firestore.CollectionReference;

import ifd.sparing.R;
import ifd.sparing.SplashScreen;
import ifd.sparing.model.Sparing;

public class Notification {
    private Sparing sparing;
    private Context context;

    public Notification(Context x) {
        context = x;
    }

    public Sparing getSparing() {
        return sparing;
    }

    public void setSparing(Sparing sparing) {
        this.sparing = sparing;
    }
    public void showNotification(){
        Intent intent = new Intent(context, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, SplashScreen.CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(sparing.getmOlahraga())
                .setContentText("Ada yang mau olahraga bareng nih :)")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
