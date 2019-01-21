package ifd.sparing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecurityPermission;
import java.util.ArrayList;

import ifd.sparing.adapters.GroupListAdapter;
import ifd.sparing.model.GroupModel;
import ifd.sparing.model.Sparing;

public class RecentChat extends AppCompatActivity {
    private final String TAG = "RECENT_CHAT_LOG";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private ArrayList<GroupModel> groupsList = new ArrayList<GroupModel>();
    private RecyclerView rvRecentChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat);
         rvRecentChat= (RecyclerView) findViewById(R.id.rvRecentChat);
        collectionReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("joinedthread");



       /* groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));


        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));

        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));


        groupsList.add(new GroupModel("14:04", "Badminton - Khalid", "pada mau ketemu dimana ?"));
*/

loadData();
    }

    private void loadData() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments() != null && queryDocumentSnapshots.getDocuments().size() != 0) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                //todo perbaiki kode dibawah ini yaaa
                                Sparing sparing = new Sparing();
                                sparing.setChatID(documentSnapshot.getString("chatID"));
                                GroupModel groupModel = new GroupModel();
                                groupModel.setTitle(documentSnapshot.getString("olahraga"));
                                groupModel.setLastChat("Inisiator : " + documentSnapshot.getString("initiator"));
                                groupModel.setTime(documentSnapshot.getString("waktu"));
                                groupModel.setSparing(sparing);

                            groupsList.add(groupModel);
                            }

                            GroupListAdapter adapter = new GroupListAdapter(groupsList, getBaseContext());
                            // Attach the adapter to the recyclerview to populate items
                            rvRecentChat.setAdapter(adapter);
                            // Set layout manager to position the items
                            rvRecentChat.setLayoutManager(new LinearLayoutManager(RecentChat.this));
                            // That's all!

                        }
                    };
                    runnable.run();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data, " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, e.getMessage());
            }
        });
    }
}
