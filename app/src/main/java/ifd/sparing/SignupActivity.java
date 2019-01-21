package ifd.sparing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import ifd.sparing.Dialog.ShowDialog;


public class SignupActivity extends AppCompatActivity {
    private final String LOGCAT = "SINGUP";
    private final String APP_ID = "5BC3BF93-F82A-4454-8416-9A4DF0EEB0C5";
    private EditText inputName, inputEmail, inputPassword, inputPhone;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputPhone = findViewById(R.id.input_phone);
        mAuth = FirebaseAuth.getInstance();
        inputName.setText("omandotkom");
        inputEmail.setText("omandotkom@gmail.com");
        inputPhone.setText("08298357776");
        inputPassword.setText("SYSTEM3209");

    }
    public void registerAccount(View view) {

        mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(LOGCAT, user.getEmail());
                            Map<String, Object> userx = new HashMap<>();
                            userx.put("username", inputName.getText().toString());
                            userx.put("password", inputPassword.getText().toString());
                            userx.put("email", inputEmail.getText().toString());
                            userx.put("phone", inputPhone.getText().toString());
                            userx.put("sepakbola", false);
                            userx.put("badminton", false);
                            userx.put("tenismeja", false);
                            userx.put("voli", false);
                            userx.put("futsal", false);
                            userx.put("bolatenis", false);
                            userx.put("lari", false);
                            userx.put("basket", false);
                            db.collection("users").document(user.getEmail())
                                    .set(userx).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                            .Builder()
                                            .setDisplayName(inputName.getText().toString())
                                            .build();
                                    FirebaseAuth.getInstance().getCurrentUser()
                                            .updateProfile(profileUpdates).
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    ShowDialog.showInformation(
                                                            SignupActivity.this, "Berhasil mendaftarkan akun.");
                                                    startActivity(new Intent(SignupActivity.this,
                                                            HomeActivity.class));
                                                }
                                            });

                                      }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Gagal mendaftarkan akun.", Toast.LENGTH_LONG).show();
                                    Log.e(LOGCAT, e.getMessage());
                                }
                            });

                        } else {
                            Log.w(LOGCAT, "gagal!!!!" );
                            Log.e(LOGCAT,task.getException().getMessage());
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
