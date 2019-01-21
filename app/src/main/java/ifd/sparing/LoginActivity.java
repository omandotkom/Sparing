package ifd.sparing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "TAG_LOGINACTIVITY";
    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView linkDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = findViewById(R.id.input_emailLogin);
        password = findViewById(R.id.input_passwordLogin);
        linkDaftar = findViewById(R.id.link_daftar);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void login(View view) {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(), "Berhasil login, " +
                        authResult.getUser().getEmail(), Toast.LENGTH_LONG).show();

                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference documentReference = db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String namaLengkap = document.getData().get("username").toString();
                                Log.d(TAG, "Welcome, " + namaLengkap);

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                        .Builder()
                                        .setDisplayName(namaLengkap)
                                        .build();
                                FirebaseAuth.getInstance().getCurrentUser()
                                        .updateProfile(profileUpdates).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "User profile changed to " +
                                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                        startActivity(new Intent(LoginActivity.this,
                                                HomeActivity.class));
                                    }
                                });

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal login, " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void linkDaftar(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
    }
}
