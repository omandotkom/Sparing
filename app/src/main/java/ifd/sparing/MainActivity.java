package ifd.sparing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ifd.sparing.Dialog.ShowDialog;
import ifd.sparing.POJOs.SampleSearchModel;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "TAG_MAINACTIVITY";
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 212;
    private FusedLocationProviderClient mFusedLocationClient;
    private CircleImageView profilePic;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView textViewNama;
    private EditText editTextKota;
    private RadioGroup radioGroup;
    private Map<String, String> alamat;
    private CheckBox chkSepakbola, chkBadminton,
            chkTenisMeja, chkVoli, chkFutsal, chkBolaTenis, chkLari, chkBasket;

    private int timeInterval = 3000;
    private int fastestTimeInterval = 3000;
    private boolean runAsBackgroundService = false;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chkSepakbola = findViewById(R.id.chkSepakbola);
        chkBadminton = findViewById(R.id.chkBadminton);
        chkTenisMeja = findViewById(R.id.chkTenisMeja);
        chkVoli = findViewById(R.id.chkVoli);
        chkFutsal = findViewById(R.id.chkFutsal);
        chkBolaTenis = findViewById(R.id.chkBolaTenis);
        chkLari = findViewById(R.id.chkLari);
        chkBasket = findViewById(R.id.chkBasket);


        profilePic = findViewById(R.id.profile_image);
        textViewNama = findViewById(R.id.lblNama);
        editTextKota = findViewById(R.id.editTextKota);
        radioGroup = findViewById(R.id.radioGroupGender);
        alamat = new HashMap<>();
        loadData();
      }

    @Override
    protected void onStart() {
        super.onStart();
      }

    @Override
    protected void onStop() {
        super.onStop();
      }

    private void loadData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                profilePic.setImageURI(user.getPhotoUrl());
                textViewNama.setText(user.getDisplayName());
                mFusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(MainActivity.this);

                //getLocation();
getUserLocation();
                db.collection("users").
                        document(user.getEmail()).get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();

                                    if (document.exists()) {

                                        if (document.getData().get("gender") != null) {
                                            String gender = document.getData().get("gender").toString();
                                            switch (gender) {
                                                case "Pria": {
                                                    radioGroup.check(R.id.radioButtonGenderPria);
                                                    break;
                                                }
                                                case "Wanita": {
                                                    radioGroup.check(R.id.radioButtonGenderWanita);
                                                    break;
                                                }
                                            }
                                        }

                                        if (document.getBoolean("sepakbola") != null) {
                                            chkSepakbola.setChecked(document.getBoolean("sepakbola"));
                                        }
                                        if (document.getBoolean("badminton") != null) {
                                            chkBadminton.setChecked(document.getBoolean("badminton"));
                                        }
                                        if (document.getBoolean("tenismeja") != null) {
                                            chkTenisMeja.setChecked(document.getBoolean("tenismeja"));
                                        }
                                        if (document.getBoolean("voli") != null) {
                                            chkVoli.setChecked(document.getBoolean("voli"));
                                        }
                                        if (document.getBoolean("futsal") != null) {
                                            chkFutsal.setChecked(document.getBoolean("futsal"));
                                        }
                                        if (document.getBoolean("bolatenis") != null) {
                                            chkBolaTenis.setChecked(document.getBoolean("bolatenis"));
                                        }
                                        if (document.getBoolean("lari") != null) {
                                            chkLari.setChecked(document.getBoolean("lari"));
                                        }
                                        if (document.getBoolean("basket") != null) {
                                            chkBasket.setChecked(document.getBoolean("basket"));
                                        }


                                    }

                                }
                            }
                        });
            }
        };
        runnable.run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getUserLocation();
                } else {
                    Toast.makeText(getBaseContext(),
                            "Layanan lokasi dibutuhkan untuk mencari teman.",
                            Toast.LENGTH_LONG).show();
                }
                return;

            }
        }
    }

    private void getLocationdeprecated() {
        if (ContextCompat.checkSelfPermission(this.getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Not granted");

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            Log.d(TAG, "Permission granted");

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location locations) {
                            if (locations != null) {
                                Log.d(TAG, locations.toString());
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(
                                            locations.getLatitude(), locations.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                                    Log.d(TAG, "Address Line " + address);
                                    alamat.put("address", address);
                                    alamat.put("city", city);
                                    alamat.put("state", state);
                                    alamat.put("country", country);
                                    alamat.put("knownName", knownName);
                                    editTextKota.setText(address);
                                    Log.d(TAG, city);
                                } catch (IOException Ex) {
                                    Toast.makeText(getApplicationContext(),
                                            Ex.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            });
        }

    }

    public void pickImage(View view) {
        ImagePicker.create(MainActivity.this)
                .returnMode(ReturnMode.ALL)
                .toolbarImageTitle("Pilih gambar")
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .theme(R.style.AppTheme_NoActionBar) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log

                .start(); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                profilePic.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(image.getPath()))

                        .build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Berhasil mengubah foto.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void getUserLocation(){
        SmartLocation.with(getApplicationContext()).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                SmartLocation.with(getApplicationContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {
                        if (list.size()!=0){
                        String kota = list.get(0).getLocality();
                        editTextKota.setText(kota);}
                    }
                });
            }
        });
    }
    public void saveProfile(View view) {
        new SimpleSearchDialogCompat<SampleSearchModel>(MainActivity.this, "Pilih Kota", "Pilih kota tempat olahraga akan dilakukan", null, createSampleData(), new SearchResultListener<SampleSearchModel>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, SampleSearchModel sampleSearchModel, int i) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, Object> userx = new HashMap<>();
                userx.put("sepakbola", chkSepakbola.isChecked());
                userx.put("badminton", chkBadminton.isChecked());
                userx.put("tenismeja", chkTenisMeja.isChecked());
                userx.put("voli", chkVoli.isChecked());
                userx.put("futsal", chkFutsal.isChecked());
                userx.put("bolatenis", chkBolaTenis.isChecked());
                userx.put("lari", chkLari.isChecked());
                userx.put("basket", chkBasket.isChecked());
                userx.put("lokasi", editTextKota.getText().toString());
                userx.put("kota", sampleSearchModel.getTitle());
                baseSearchDialogCompat.dismiss();
                subsribeToTopic(sampleSearchModel.getTitle());
                String gender = "-";
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButtonGenderPria: {
                        gender = "Pria";
                        break;
                    }
                    case R.id.radioButtonGenderWanita: {
                        gender = "Wanita";
                        break;
                    }
                }
                userx.put("gender", gender);


                db.collection("users").document(user.getEmail()).
                        update(userx).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ShowDialog.showInformation(MainActivity.this, "Data berhasil diperbaharui");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Kesalahan dalam menyimpan data, " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).show();


    }

    private ArrayList<SampleSearchModel> createSampleData() {
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        items.add(new SampleSearchModel("Purwokerto"));
        items.add(new SampleSearchModel("Purbalingga"));
        items.add(new SampleSearchModel("Tegal"));
        items.add(new SampleSearchModel("Brebes"));
        items.add(new SampleSearchModel("Kebumen"));
        items.add(new SampleSearchModel("Banjarnegara"));
        items.add(new SampleSearchModel("Cirebon"));
        return items;
    }
    private void subsribeToTopic(String topic){
        //todo uncomment the line
        /*
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Berhasil subscribe topic "+ topic;
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

}
