package com.ascendant.diysipp.Activity.menu.LiveStreaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.ascendant.diysipp.API.ApiRequest;
import com.ascendant.diysipp.API.RetroServer;
import com.ascendant.diysipp.Activity.LoginActivity;
import com.ascendant.diysipp.Activity.menu.MediaPembelajaran.ListMediaPembelajaranActivity;
import com.ascendant.diysipp.Activity.menu.MediaPembelajaran.MediaPembelajaranActivity;
import com.ascendant.diysipp.Adapter.Home.AdapterLiveStreaming;
import com.ascendant.diysipp.Adapter.Home.AdapterMediaPembelajarans;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Media;
import com.ascendant.diysipp.Model.Pena.ResponseModel;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveStreamingActivity extends AppCompatActivity {
    private List<DataModel> mItems = new ArrayList<>();
    Destiny destiny;
    DB_Helper dbHelper;
    String Username,Password,Nama,Token,Level,Photo;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);
        destiny = new Destiny();
        rv = findViewById(R.id.recycler);
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
        GetMediaPembelajaran();
    }

    private void GetMediaPembelajaran(){
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseModel> Data=api.LiveStreaming(destiny.AUTH(Token));
        Data.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try {
                    if (response.body().getStatusCode().equals("000")){
                        mManager = new GridLayoutManager(LiveStreamingActivity.this,1);
                        rv.setLayoutManager(mManager);
                        mItems=response.body().getData();
                        mAdapter = new AdapterLiveStreaming(LiveStreamingActivity.this,mItems);
                        rv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(LiveStreamingActivity.this, "Terjadi Kesalahan ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(LiveStreamingActivity.this, "Terjadi Kesalahan User akan Terlogout", Toast.LENGTH_SHORT).show();
                    dbHelper.Logout();
                    Intent intent = new Intent(LiveStreamingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
}