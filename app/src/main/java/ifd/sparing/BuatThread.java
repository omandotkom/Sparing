package ifd.sparing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ifd.sparing.POJOs.SampleSearchModel;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BuatThread extends AppCompatActivity {
    private final String TAG = "ActivityBuatThread";
    int PLACE_PICKER_REQUEST = 1988;
    private EditText rt_Jam, et_Tanggal, pickPlace, currentPlayer, neededPlayer;
    private TextView lblNamaOlahraga, lblHariIni, lblNamaInitiator;
    private String olahraga;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Penanggalan penanggalan = new Penanggalan();
    private Map<String, Object> thread = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buat_thread);
        Button button = (Button) findViewById(R.id.buttonSelesai);
        rt_Jam = (EditText) findViewById(R.id.rt_Jam);
        et_Tanggal = (EditText) findViewById(R.id.et_Tanggal);
        lblNamaOlahraga = (TextView) findViewById(R.id.lblNamaOlahraga);
        lblHariIni = findViewById(R.id.lblHariIni);
        lblNamaInitiator = findViewById(R.id.namaInitiator);
        pickPlace = findViewById(R.id.pickPlace);
        //buttonSelesai = findViewById(R.id.buttonSelesai);
        currentPlayer = findViewById(R.id.currentPlayer);
        neededPlayer = findViewById(R.id.neededPlayer);
        lblNamaInitiator.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        lblHariIni.setText(formattedDate);

        if (getIntent().getExtras().getString("namaOlahraga") != null) {
            lblNamaOlahraga.setText(getIntent().getExtras().getString("namaOlahraga"));
            olahraga = getIntent().getExtras().getString("namaOlahraga");
        } else {
            Log.e(TAG, "Tidak diatur");
        }
        pickPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogPickPlace();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Uncomment this line
                //startActivity(new Intent(BuatThread.this,AjakTemanLagi.class));
                saveThread();
            }
        });
        rt_Jam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    jamPicker();
                }
            }
        });
        et_Tanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, place.getId());
                pickPlace.setText(place.getAddress().toString());

            }
        }


    }

    public void dialogPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesNotAvailableException googlePlayNotAvailable) {
            Log.e(TAG, googlePlayNotAvailable.getMessage());
        } catch (GooglePlayServicesRepairableException googlePlayReparaible) {
            Log.e(TAG, googlePlayReparaible.getMessage());
        }

    }

    private void jamPicker() {
        Log.d(TAG, "Jam picker is called");
        TimePickerBuilder tpb = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        tpb.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
            @Override
            public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                penanggalan.setJam(hourOfDay);
                penanggalan.setMenit(minute);
                rt_Jam.setText(penanggalan.timeString());
            }
        });
        tpb.show();

    }

    private void datePicker() {
        int Year = Calendar.getInstance().get(Calendar.YEAR);
        Log.d(TAG, String.valueOf(Year));
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setYear(Year);
        dpb.addDatePickerDialogHandler(new DatePickerDialogFragment.DatePickerDialogHandler() {
            @Override
            public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
                penanggalan.setTahun(year);
                penanggalan.setBulan(monthOfYear);
                penanggalan.setHari(dayOfMonth);
                et_Tanggal.setText(penanggalan.dateString());
                Log.d(TAG, "Selected datetime : " + penanggalan.toString());
            }
        });
        dpb.show();
    }

    private void saveThread() {
        new SimpleSearchDialogCompat<SampleSearchModel>(BuatThread.this, "Pilih Kota", "Pilih kota tempat olahraga akan dilakukan", null, createSampleData(), new SearchResultListener<SampleSearchModel>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, SampleSearchModel sampleSearchModel, int i) {
                String documentName = FirebaseAuth.getInstance().getCurrentUser().getEmail() + FirebaseAuth.getInstance().getCurrentUser().getUid() + getRnd.get();

                thread.put("initiator", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                thread.put("alamat", pickPlace.getText().toString());
                thread.put("olahraga", olahraga);
                thread.put("datetime", penanggalan.toString());
                thread.put("tanggal", penanggalan.getTanggalString());
                thread.put("jam", penanggalan.getJamString());
                thread.put("pemainsekarang", Integer.valueOf(currentPlayer.getText().toString()));
                thread.put("pemainyangdibutuhkan", Integer.valueOf(neededPlayer.getText().toString()));
                thread.put("isFull", false);
                thread.put("chatID", documentName);
                thread.put("kota", sampleSearchModel.getTitle());

                db.collection("threads").document(documentName).set(thread).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, Object> pesanq = new HashMap<>();
                        pesanq.put("sender", "Place");
                        pesanq.put("message", pickPlace.getText().toString());
                        pesanq.put("timestamp", getCurrentTimeStamp());
                        db.collection("chats").document(documentName).set(pesanq).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("chats").document(documentName).collection("chatlist").add(pesanq).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent i = new Intent(BuatThread.this, HomeActivity.class);
                                        //i.putExtra("chatID", documentName);
                                        Toast.makeText(getApplicationContext(), "Berhasil membuat thread", Toast.LENGTH_LONG).show();
                                        startActivity(i);
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal membuat thread, " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Error : " + e.getMessage());
                    }
                });


                baseSearchDialogCompat.dismiss();
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

    private long getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();

        Timestamp timestamp = new Timestamp(cal.getTime());
        return System.currentTimeMillis();
    }

    static class getRnd {
        static long current = System.currentTimeMillis();

        static public synchronized long get() {
            return current++;
        }
    }

    private class Penanggalan {
        private int tahun, bulan, hari, jam, menit;

        public String getJamString() {
            return this.jam + " : " + this.menit;
        }

        public String getTanggalString() {
            return this.tahun + "/" + this.bulan + "/" + this.hari;
        }

        public int getTahun() {
            return tahun;
        }

        public void setTahun(int tahun) {
            this.tahun = tahun;
        }

        public int getBulan() {
            return bulan;
        }

        public void setBulan(int bulan) {
            this.bulan = bulan;
        }

        public int getHari() {
            return hari;
        }

        public void setHari(int hari) {
            this.hari = hari;
        }

        public int getJam() {
            return jam;
        }

        public void setJam(int jam) {
            this.jam = jam;
        }

        public int getMenit() {
            return menit;
        }

        public void setMenit(int menit) {
            this.menit = menit;
        }

        @Override
        public String toString() {
            return tahun + "-" + bulan + "-" + hari + " " + jam + ":" + menit;
        }

        public String dateString() {
            return tahun + "-" + bulan + "-" + hari;
        }

        public String timeString() {
            return jam + ":" + menit;
        }
    }
}
