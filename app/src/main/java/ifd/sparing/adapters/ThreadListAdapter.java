package ifd.sparing.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ifd.sparing.MessageListActivityNew;
import ifd.sparing.R;
import ifd.sparing.model.Sparing;


public class ThreadListAdapter extends RecyclerView.Adapter<ThreadListAdapter.ViewHolder> {
    private List<Sparing> sparingList;
    private Context ctx;
    private final String TAG = "ThreadListAdapter";
    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userDocumentReference;
    private Map<String, Object> thread = new HashMap<>();
    @NonNull
    @Override
    public ThreadListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View inviteView = inflater.inflate(R.layout.item_sparinglist, viewGroup, false);

        // Return a new holder instance
        ThreadListAdapter.ViewHolder viewHolder = new ThreadListAdapter.ViewHolder(inviteView);
        return viewHolder;
    }

    public ThreadListAdapter(List<Sparing> grp, Context c, String userEmail) {
        this.sparingList = grp;
        this.ctx = c;
        email = userEmail;
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadListAdapter.ViewHolder holder, int i) {
        Sparing sparing = sparingList.get(i);

        // Set item views based on your views and data model
        TextView title = holder.namaOlahraga;
        title.setText(sparing.getmOlahraga());

        TextView tanggal = holder.datetimeOlahraga;
        tanggal.setText(sparing.getStringDatetime());


        TextView kapasitas = holder.kapasitas;
        kapasitas.setText("Jumlah Pemain Sekarang : " + sparing.getPemainSekarang() + "\n" +
                "Membutuhkan " + sparing.getPemainYangdibutuhkan() + " pemain lagi.");

        ConstraintLayout constraintLayout = holder.c;
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   CollectionReference userCollectionReference = db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .collection("joinedthread");
                userCollectionReference.whereEqualTo("chatID",sparing.getChatID())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult()!=null && task.getResult().getDocuments()!=null && task.getResult().getDocuments().size()>0){
                                    //jikaa result tidak null, documents tidak null dan sizenya lebih dari 0, berarti dah ada.
                                    String chatID = task.getResult().getDocuments().get(0).getString("chatID");
                                    Log.d(TAG,"Already joined, chat id : " + chatID);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,e.getMessage());
                            Toast.makeText(ctx,"Unable to fetch data from server",Toast.LENGTH_LONG).show();
                            }
                        });*/
                db.collection("threads").document(sparing.getThreadID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String alamat = documentSnapshot.getString("alamat");
                        String total = String.valueOf(documentSnapshot.get("pemainyangdibutuhkan"));
                        String sekarang = String.valueOf(documentSnapshot.get("pemainsekarang"));
                        String description = "Lokasi : " + alamat + "\n\nAnda yakin ingin bergabung ? " + "\nBergabung : " + sekarang + " dari " + total;
                        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(ctx)
                                .setTitle(sparing.getmOlahraga())
                                .setDescription(description)
                                .setCancelable(false)
                                .setIcon(getDrawable(documentSnapshot.getString("olahraga")))
                                .setPositiveText("Saya Ikut")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        sparing.setChatID(documentSnapshot.getString("chatID"));
                                        thread.put("chatID", documentSnapshot.getString("chatID"));
                                        thread.put("olahraga", documentSnapshot.getString("olahraga"));
                                        thread.put("initiator", documentSnapshot.getString("initiator"));
                                        thread.put("waktu", documentSnapshot.getString("tanggal") + " " + documentSnapshot.getString("jam"));
                                        userDocumentReference = db.collection("users").document(email);
                                        userDocumentReference.collection("joinedthread")
                                                .add(thread).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                updateData(sparing);
                                            }
                                        });

                                    }
                                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeText("Tidak Ikut").build();
                        dialog.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, "Gagal mengunduh informasi.", Toast.LENGTH_LONG).show();
                        return;
                    }
                });

            }
        });

    }

    private int getDrawable(String olahraga) {
        Log.d(TAG, "Getting image " + olahraga);
        switch (olahraga.toLowerCase()) {
            //TODO : Dilengkapi seleksnya sama ridwan atau hesa
            case "bulutangkis": {
                return R.drawable.ic_menu_bulutangkis;
            }
            case "sepakbola": {
                return R.drawable.ic_menu_sepakbola;
            }
            case "bola basket": {
                return R.drawable.ic_menu_bolabasket;
            }
            case "bola tenis": {
                return R.drawable.ic_menu_bolatenis;
            }
            default: {
                return R.drawable.ic_menu_bolatenis;
            }

        }
    }
    @Override
    public int getItemCount() {
        return sparingList.size();
    }

    private void updateData(Sparing sparing) {
        DocumentReference documentReference = db.collection("threads").document(sparing.getThreadID());
        thread = new HashMap<>();
        int sekarang = Integer.valueOf(sparing.getPemainSekarang());
        int dibutuhkan = Integer.valueOf(sparing.getPemainYangdibutuhkan());
        sekarang = sekarang + 1;

        dibutuhkan = dibutuhkan - 1;
        sparing.setPemainYangdibutuhkan(dibutuhkan);
        sparing.setPemainSekarang(sekarang);
        thread.put("isFull", sparing.isFull());
        thread.put("pemainsekarang", sekarang);
        thread.put("pemainyangdibutuhkan", dibutuhkan);
        documentReference.update(thread).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(ctx, MessageListActivityNew.class);
                i.putExtra("chatID", sparing.getChatID());
                ctx.startActivity(i);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Gagal dalam memperbarui data " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, e.getMessage());
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView namaOlahraga, datetimeOlahraga, kapasitas;
        private ConstraintLayout c;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaOlahraga= (TextView) itemView.findViewById(R.id.nmOlahraga);
            kapasitas = (TextView) itemView.findViewById(R.id.kapasitas);
            datetimeOlahraga = (TextView) itemView.findViewById(R.id.datetimeOlahraga);
            c = (ConstraintLayout) itemView.findViewById(R.id.dalem);

        }
    }
}
