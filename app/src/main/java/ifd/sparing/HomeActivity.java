package ifd.sparing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import ifd.sparing.Services.Notification;
import ifd.sparing.adapters.ThreadListAdapter;
import ifd.sparing.model.Sparing;

public class HomeActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    private final String APP_ID = "5BC3BF93-F82A-4454-8416-9A4DF0EEB0C5";
    private final String TAG = "HomeActivityLOG";
    private TextView tvNavbarEmail;
    private TextView tvNamaUser;
    private CircleImageView profilePic;
    private ArrayList<Sparing> sparingList;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView rvSparing;

    @Override
    protected void onResume() {
        loadSparingList();
        loadNavbarProfileData();

        super.onResume();
    }
    private void loadSparingList(){
        db.collection("users").document(firebaseUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("kota") != null) {
                    String kota = documentSnapshot.getString("kota");
                    db.collection("threads").whereEqualTo("kota", kota).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            sparingList.clear();
                            if (queryDocumentSnapshots.getDocuments().size() != 0) {
                                for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()) {
                                    if (passedFilter(documentSnapshot,documentSnapshot1)){
                                        Sparing sparing = new Sparing();
                                        sparing.setThreadID(documentSnapshot1.getId());
                                        sparing.setChatID(documentSnapshot1.getString("chatID"));
                                        sparing.setChannelID(documentSnapshot1.getString("channelurl"));
                                        sparing.setmOlahraga(documentSnapshot1.getString("olahraga"));
                                        //sparing.setPemainSekarang(Integer.valueOf(documentSnapshot1.getString("pemainsekarang")));
                                        //sparing.setmTotalAnggota(Integer.valueOf(documentSnapshot1.getString("pemainyangdibutuhkan")));
                                        //TODO perbaiki pemainsekarang is not a java.lang.String
                                        sparing.setPemainSekarang(Integer.parseInt(documentSnapshot1.get("pemainsekarang").toString()));
                                        sparing.setPemainYangdibutuhkan(Integer.parseInt(documentSnapshot1.get("pemainyangdibutuhkan").toString()));
                                        //Log.d(TAG,"Pemain Sekarang :" + documentSnapshot1.get("pemainsekarang"));
                                        //sparing.setPemainSekarang(2);
                                        //sparing.setPemainYangdibutuhkan(3);
                                        sparing.setmTanggal(documentSnapshot1.getString("tanggal"));
                                        sparing.setJam(documentSnapshot1.getString("jam"));
                                        sparing.setFull(documentSnapshot1.getBoolean("isFull"));
                                        if (!sparing.isFull()){
                                            sparingList.add(sparing);}
                                    }

                                }
                                loadRecycler();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "failed to fetch data, " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView badminton = (ImageView) findViewById(R.id.btn_bulutangkis);
        badminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,BuatThread.class));
            }
        });
        toolbar.setNavigationIcon(R.drawable.if_talk_chat_message);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(HomeActivity.this, RecentChat.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            }
        });
        rvSparing = (RecyclerView) findViewById(R.id.rvSparing);

        sparingList = new ArrayList<Sparing>();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        LinearLayout linearLayout = (LinearLayout)  headerView.findViewById(R.id.lnr);
        tvNavbarEmail = linearLayout.findViewById(R.id.tvNavbarEmail);
        tvNamaUser = linearLayout.findViewById(R.id.tvNavbarNamaUser);
        profilePic = (CircleImageView) linearLayout.findViewById(R.id.imageProfile);
        loadNavbarProfileData();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        loadSparingList();
    }

    private void loadNavbarProfileData() {
        tvNamaUser.setText(firebaseUser.getDisplayName());
        tvNavbarEmail.setText(firebaseUser.getEmail());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Uri photoUrl = firebaseUser.getPhotoUrl();
                if (photoUrl != null) {
                    profilePic.setImageURI(photoUrl);
                }
            }
        };
        runnable.run();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Intent i = new Intent(HomeActivity.this, BuatThread.class);
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_futsal: {
                i.putExtra("namaOlahraga", "Futsal");
                startActivity(i);

                break;
            }
            case R.id.nav_sepakbola: {
                i.putExtra("namaOlahraga", "Sepakbola");
                startActivity(i);

                break;
            }
            case R.id.nav_bulutangkis: {
                i.putExtra("namaOlahraga", "Bulutangkis");
                startActivity(i);

                break;
            }
            case R.id.nav_bolavoli: {
                i.putExtra("namaOlahraga", "Bola Voli");

                startActivity(i);
                break;
            }
            case R.id.nav_bolabasket: {
                i.putExtra("namaOlahraga", "Bola Basket");
                startActivity(i);

                break;
            }
            case R.id.nav_bolatenis: {
                i.putExtra("namaOlahraga", "Bola Tenis");
                startActivity(i);

                break;
            }
            case R.id.nav_tenismeja: {
                i.putExtra("namaOlahraga", "Tenis Meja");
                startActivity(i);

                break;
            }
            case R.id.nav_lari: {
                i.putExtra("namaOlahraga", "Lari");
                startActivity(i);

                break;
            }
            case R.id.nav_logout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean passedFilter(DocumentSnapshot userDocumentSnapShot,DocumentSnapshot threadDocumentSnapShot){
       switch (threadDocumentSnapShot.getString("olahraga")){
           case "Futsal" : {
               return userDocumentSnapShot.getBoolean("futsal");

           }
           case "Sepakbola" : {
               return userDocumentSnapShot.getBoolean("sepakbola");

           }
           case "Bulutangkis" : {
               return userDocumentSnapShot.getBoolean("badminton");

           }
           case "Bola Voli" : {
               return userDocumentSnapShot.getBoolean("voli");

           }
           case "Bola Basket" : {
               return userDocumentSnapShot.getBoolean("basket");

           }
           case "Bola Tenis" : {
               return userDocumentSnapShot.getBoolean("bolatenis");

           }
           case "Tenis Meja" : {
               return userDocumentSnapShot.getBoolean("tenismeja");

           }
           case "Lari" : {
               return userDocumentSnapShot.getBoolean("tenismeja");

           }
           default : {
               return true;
           }

       }
    }
    private void loadRecycler() {
        ThreadListAdapter adapter = new ThreadListAdapter(sparingList, this,FirebaseAuth.getInstance().getCurrentUser().getEmail());
        rvSparing.setAdapter(adapter);
        rvSparing.setLayoutManager(new LinearLayoutManager(this));

    }

}