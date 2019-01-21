package ifd.sparing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import com.firebase.ui.auth.AuthUI;

public class SplashScreen extends AppCompatActivity {
private final int RC_SIGN_IN = 312;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //user signed in
            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
            finish();
        } else {
            //user not signed in
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }
    }
public static final String CHANNEL_ID = "SPARING_APP_OMAN";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (requestCode==RC_SIGN_IN){
           startActivity(new Intent(SplashScreen.this,HomeActivity.class));
           finish();

       }
        }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SparingChannel";
            String description = "Sparing Notification to notify user of all incoming sport.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
