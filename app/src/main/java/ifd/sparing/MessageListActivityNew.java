package ifd.sparing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageListActivityNew extends AppCompatActivity {
    private ChatView chatView;
    private final String TAG = "CHAT_PERCOBAAN";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String chatID;
    private String channelurl;
    private Message generalMessage = null;
    private CollectionReference chatCollReference = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_new);
        chatView = (ChatView) findViewById(R.id.chatView);
        chatID = getIntent().getExtras().getString("chatID");
        channelurl = getIntent().getExtras().getString("channelurl");
        Log.d(TAG, "CHAT ID : " + chatID);
        initChatView();

        if (chatID != null) {
            db.collection("chats").document(chatID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        if (!documentSnapshot.getString("sender").equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                            addMessage(false, documentSnapshot.getString("sender"), documentSnapshot.getString("message"), documentSnapshot.getString("waktu"));

                            CollectionReference collectionReference = db.collection("chats").document(chatID).collection("chatlist");
                            collectionReference.orderBy("timestamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        Runnable r = new Runnable() {
                                            @Override
                                            public void run() {

                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                                                    String nama = documentSnapshot.getString("nama");
                                                    String pesan = documentSnapshot.getString("pesan");
                                                    String waktu = documentSnapshot.getString("waktu");
                                                    if (documentSnapshot.getString("uid") != null && documentSnapshot.getString("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                        addMessage(true, nama, pesan, waktu);

                                                    } else {
                                                        addMessage(false, nama, pesan, waktu);

                                                    }


                                                }
                                            }
                                        };
                                        r.run();

                                    }
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failure, " + e.getMessage());
                }
            });
        }

        chatCollReference = db.collection("chats")
                .document(chatID).collection("chatlist");
        chatCollReference.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "This shit is called ");
                if (queryDocumentSnapshots.getDocuments().size() != 0 && queryDocumentSnapshots.getDocuments().get(0) != null) {
                    Log.d(TAG, "shit long is " + queryDocumentSnapshots.getDocuments().size());
                    int idx = queryDocumentSnapshots.getDocuments().size() - 1;
                    if (queryDocumentSnapshots.getDocuments().get(idx).getString("pesan") != null) {
                        Log.d(TAG, "Pesan terakhir " + queryDocumentSnapshots.getDocuments().get(idx).getString("pesan"));
                        if (!queryDocumentSnapshots.getDocuments().get(idx).getString("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            String nama = queryDocumentSnapshots.getDocuments().get(idx).getString("pesan");
                            Log.d(TAG, nama);
                            addMessage(false, queryDocumentSnapshots.getDocuments().get(idx).getString("nama"), queryDocumentSnapshots.getDocuments().get(idx).getString("pesan"), queryDocumentSnapshots.getDocuments().get(idx).getString("waktu"));
                        } else {
                            addMessage(true, queryDocumentSnapshots.getDocuments().get(idx).getString("nama"), queryDocumentSnapshots.getDocuments().get(idx).getString("pesan"), queryDocumentSnapshots.getDocuments().get(idx).getString("waktu"));

                        }
                    } else {
                        Log.e(TAG, "ITS NULL");
                    }


                } else {
                    Log.d(TAG, " shit is null");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void loadMessage() {
        db.collection("chats").document(chatID).collection("chatlist").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        String nama = documentSnapshot.getString("nama");
                        String pesan = documentSnapshot.getString("pesan");
                        Log.d(TAG, nama + " says " + pesan);
                    }
                }
            }
        });
    }

    public String getCurrentTimeUsingCalendar() {

        Calendar cal = Calendar.getInstance();

        Date date = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    private long getCurrentTimeStamp() {
    /*Calendar cal = Calendar.getInstance();

       Timestamp timestamp = new Timestamp(cal.getTime());
       return timestamp;*/
        return System.currentTimeMillis();
    }

    private void addMessage(boolean isSender, String usrName, String message, String time) {
        Message message2 = new Message();
        message2.setBody(message);
        if (isSender) {
            message2.setType(Message.RightSimpleMessage);
        } else {
            message2.setType(Message.LeftSimpleMessage);
        }
        message2.setTime(time);
        message2.setUserName(usrName);
        message2.setUserIcon(null);
        if (message2 != null)
            chatView.addMessage(message2);


    }

    private void initChatView() {
        if (chatView != null) {
            chatView.setOnClickCameraButtonListener(new ChatView.OnClickCameraButtonListener() {
                @Override
                public void onCameraButtonClicked() {
                    Toast.makeText(getApplicationContext(), "Fitur ini dalam pengembangan", Toast.LENGTH_LONG).show();
                }
            });
            chatView.setOnClickGalleryButtonListener(new ChatView.OnClickGalleryButtonListener() {
                @Override
                public void onGalleryButtonClick() {
                    Toast.makeText(getApplicationContext(), "Fitur ini dalam pengembangan", Toast.LENGTH_LONG).show();
                }
            });

            chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
                                                      @Override
                                                      public void onSendButtonClick(String s) {
                                                          Map<String, Object> pesanq = new HashMap<>();
                                                          pesanq.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                          pesanq.put("nama", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                          pesanq.put("pesan", s);
                                                          pesanq.put("waktu", getCurrentTimeUsingCalendar());
                                                          pesanq.put("timestamp", getCurrentTimeStamp());
                                                          FirebaseFirestore.getInstance().collection("chats")
                                                                  .document(chatID).collection("chatlist")
                                                                  .add(pesanq).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                              @Override
                                                              public void onSuccess(DocumentReference documentReference) {
                                                                  Log.d(TAG, "Berhasil kirim pesan");
                                                              }
                                                          }).addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Log.e(TAG, e.getMessage());
                                                              }
                                                          });

                                                      }
                                                  }
            );

        }
    }
}
