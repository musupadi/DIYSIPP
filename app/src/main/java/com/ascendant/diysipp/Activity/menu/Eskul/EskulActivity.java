package com.ascendant.diysipp.Activity.menu.Eskul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ascendant.diysipp.API.ApiRequest;
import com.ascendant.diysipp.API.RetroServer;
import com.ascendant.diysipp.Activity.LoginActivity;
import com.ascendant.diysipp.Activity.menu.ERaport.ERaportActivity;
import com.ascendant.diysipp.Activity.menu.Gallery.GalleryActivity;
import com.ascendant.diysipp.Adapter.Home.AdapterEReport;
import com.ascendant.diysipp.Adapter.Home.AdapterEskul;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Eskul.Eskul;
import com.ascendant.diysipp.Model.Eskul.ResponseData;
import com.ascendant.diysipp.Model.Pena.ResponseModel;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EskulActivity extends AppCompatActivity {
    Destiny destiny;
    RelativeLayout Back;
    DB_Helper dbHelper;
    String Username,Password,Nama,Token,Level,Photo;
    RecyclerView recycler;
    private List<Eskul> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eskul);
        destiny = new Destiny();
        Back = findViewById(R.id.relativeBack);
        recycler = findViewById(R.id.recycler);
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
        dbHelper.DeleteEskul();
        Logic();
    }
    private void Logic(){
        mManager = new GridLayoutManager(EskulActivity.this,2);
        recycler.setLayoutManager(mManager);
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseData> KabarBerita = api.EskulAll(destiny.AUTH(Token));
        KabarBerita.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                try {
                    if (response.body().getStatusCode().equals("000")){
                        mItems=response.body().getData();
                        mAdapter = new AdapterEskul(EskulActivity.this,mItems);
                        recycler.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }else if (response.body().getStatusCode().equals("001") || response.body().getStatusCode().equals("002")){
                        destiny.AutoLogin(Username,Password,EskulActivity.this);
                        Logic();
                    }else{
                        Toast.makeText(EskulActivity.this, "Terjadi Kesalahan ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(EskulActivity.this, "Terjadi Kesalahan User akan Terlogout", Toast.LENGTH_SHORT).show();
                    dbHelper.Logout();
                    Intent intent = new Intent(EskulActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(EskulActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}