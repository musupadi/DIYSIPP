package com.ascendant.diysipp.Activity.menu.Tugas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ascendant.diysipp.API.ApiRequest;
import com.ascendant.diysipp.API.RetroServer;
import com.ascendant.diysipp.Activity.LoginActivity;
import com.ascendant.diysipp.Activity.menu.MediaPembelajaran.MediaPembelajaranActivity;
import com.ascendant.diysipp.Adapter.Home.AdapterSoalPG;
import com.ascendant.diysipp.Adapter.Home.AdapterSoalPGNew;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Pena.ResponseModel;
import com.ascendant.diysipp.Model.ResponseTugasNew;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoalPGNewActivity extends AppCompatActivity {
    String ID,NAMA;
    Button Jawab;
    Destiny destiny;
    RelativeLayout Back;
    DB_Helper dbHelper;
    String Username,Password,Nama,Token,Level,Photo;
    Dialog dialog;
    RecyclerView recyclerView;
    Button JawabSoal;

    private List<DataModel> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    ArrayList<String> JAWABAN = new ArrayList<String>();
    ArrayList<String> ID_TUGAS = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_pgnew);
        Declaration();
        GETDATA();
    }
    private void Declaration(){
        Jawab = findViewById(R.id.btnJawab);
        destiny = new Destiny();
        dbHelper = new DB_Helper(this);
        Cursor cursor = dbHelper.checkUser();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Username = cursor.getString(0);
                Password = cursor.getString(1);
                Nama = cursor.getString(2);
                Token = cursor.getString(3);
                Level = cursor.getString(4);
                Photo = cursor.getString(5);
            }
        }
        dbHelper.DeleteTugas();
    }
    private void GETDATA(){
        Intent intent = getIntent();
        ID = intent.getExtras().getString("ID");
        NAMA = intent.getExtras().getString("NAMA");
        getSupportActionBar().setTitle(NAMA);
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseTugasNew> Data=api.TugasNew(destiny.AUTH(Token),ID);
        Data.enqueue(new Callback<ResponseTugasNew>() {
            @Override
            public void onResponse(Call<ResponseTugasNew> call, Response<ResponseTugasNew> response) {
                try {
                    if (response.body().getStatusCode().equals("000")){
                        recyclerView = findViewById(R.id.recycler);
                        JawabSoal = findViewById(R.id.btnJawabSoal);
                        mManager = new GridLayoutManager(SoalPGNewActivity.this,1);
                        recyclerView.setItemViewCacheSize(100);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        recyclerView.setLayoutManager(mManager);
                        mAdapter = new AdapterSoalPGNew(SoalPGNewActivity.this,response.body().getData().getSoal().size(),response.body().getData().getSoal(), JAWABAN,ID_TUGAS);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
//                    if (!Level.equals("siswa")){
//                        Jawab.setVisibility(View.GONE);
//                    }
//                    Jawab.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                        Tutup.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.hide();
//                            }
//                        });
                        JawabSoal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (JAWABAN.contains("")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SoalPGNewActivity.this);

                                    // Set a title for alert dialog
                                    builder.setTitle("Pemberitahuan");

                                    // Ask the final question
                                    builder.setMessage("Ada Beberapa Soal Yang Belum diisi dan semua soal harus dikerjakan\nSalah Satu Nomor Yang Belum Diisi Adalah "+String.valueOf(JAWABAN.indexOf("")+1));

                                    // Set the alert dialog yes button click listener
//                                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Do something when user clicked the Yes button
//                                        // Set the TextView visibility GONE
//                                        Send();
//                                    }
//                                });

                                    // Set the alert dialog no button click listener
                                    builder.setNegativeButton("Baik", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do something when No button clicked
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    // Display the alert dialog on interface
                                    dialog.show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SoalPGNewActivity.this);

                                    // Set a title for alert dialog
                                    builder.setTitle("Pemberitahuan");

                                    // Ask the final question
                                    builder.setMessage("Semua soal sudah terisi tapi sebaiknya di check dulu apakah yakin dengan jawaban yang akan dikirim?");

                                    // Set the alert dialog yes button click listener
                                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do something when user clicked the Yes button
                                            // Set the TextView visibility GONE
                                            Send();
                                        }
                                    });

                                    // Set the alert dialog no button click listener
                                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do something when No button clicked
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    // Display the alert dialog on interface
                                    dialog.show();
                                }

                            }
                        });
                    }else if (response.body().getStatusCode().equals("001") || response.body().getStatusCode().equals("002")){
                        destiny.AutoLogin(Username,Password,SoalPGNewActivity.this);
                        Intent intent = new Intent(SoalPGNewActivity.this, MediaPembelajaranActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SoalPGNewActivity.this, "Terjadi Kesalahan ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d( "Zyarga : ",e.toString());
                    Toast.makeText(SoalPGNewActivity.this, "Terjadi Kesalahan User akan Terlogout", Toast.LENGTH_SHORT).show();
                    dbHelper.Logout();
                    Intent intent = new Intent(SoalPGNewActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseTugasNew> call, Throwable t) {

            }
        });

    }
    public void Send(){
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseModel> Data=api.TugasPG(destiny.AUTH(Token),ID,JAWABAN,ID_TUGAS);
        Data.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(SoalPGNewActivity.this, response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SoalPGNewActivity.this,NewTugasActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(SoalPGNewActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}