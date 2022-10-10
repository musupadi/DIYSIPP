package com.ascendant.diysipp.Activity.Marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ascendant.diysipp.API.ApiRequest;
import com.ascendant.diysipp.API.RetroServer;
import com.ascendant.diysipp.Activity.LoginActivity;
import com.ascendant.diysipp.Activity.MainActivity;
import com.ascendant.diysipp.Adapter.Home.AdapterGuru;
import com.ascendant.diysipp.Adapter.Universal.AdapterProduk;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Pena.Marketplace.ProdukModel;
import com.ascendant.diysipp.Model.Pena.Marketplace.ResponseMarketplace;
import com.ascendant.diysipp.Model.Produk;
import com.ascendant.diysipp.Model.ResponseProduk;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketplaceActivity extends AppCompatActivity {
    Destiny destiny;
    RelativeLayout Back;
    DB_Helper dbHelper;
    String Username,Password,Nama,Token,Level,Photo;
    RecyclerView recycler;
    private List<ProdukModel> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    LottieAnimationView Anim;
    LinearLayout LAnim;
    TextView TAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);
        destiny = new Destiny();
        Anim = findViewById(R.id.lottieAnim);
        LAnim = findViewById(R.id.linearAnim);
        TAnim = findViewById(R.id.tvAnim);
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
        Logic();
    }
    private void Logic(){
        mManager = new GridLayoutManager(MarketplaceActivity.this,1);
        recycler.setLayoutManager(mManager);
        LAnim.setVisibility(View.VISIBLE);
        TAnim.setVisibility(View.GONE);
        Anim.setAnimation("loading.json");
        Anim.playAnimation();
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseMarketplace> MP = api.ProdukSiswa(destiny.AUTH(Token));
        MP.enqueue(new Callback<ResponseMarketplace>() {
            @Override
            public void onResponse(Call<ResponseMarketplace> call, Response<ResponseMarketplace> response) {
                try {
                    if (response.body().getStatusCode().equals("000")){
                        mItems = response.body().getData();
                        if (mItems.size()<1){
                            TAnim.setVisibility(View.VISIBLE);
                            TAnim.setText("Guru Belum Ada");
                            Anim.setAnimation("notfound.json");
                            Anim.playAnimation();
                        }else{
                            LAnim.setVisibility(View.GONE);
                            mAdapter = new AdapterProduk(MarketplaceActivity.this,mItems);
                            recycler.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }else if (response.body().getStatusCode().equals("001") || response.body().getStatusCode().equals("002")){
                        destiny.AutoLogin(Username,Password,MarketplaceActivity.this);
                        Intent intent = new Intent(MarketplaceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MarketplaceActivity.this, "Terjadi Kesalahan ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(MarketplaceActivity.this, "Terjadi Kesalahan User akan Terlogout", Toast.LENGTH_SHORT).show();
                    dbHelper.Logout();
                    Intent intent = new Intent(MarketplaceActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMarketplace> call, Throwable t) {
                Toast.makeText(MarketplaceActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}