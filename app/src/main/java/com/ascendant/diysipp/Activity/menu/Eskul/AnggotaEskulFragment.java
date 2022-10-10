package com.ascendant.diysipp.Activity.menu.Eskul;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ascendant.diysipp.API.ApiRequest;
import com.ascendant.diysipp.API.RetroServer;
import com.ascendant.diysipp.Adapter.Home.AdapterAnggotaEskul;
import com.ascendant.diysipp.Adapter.Home.AdapterAnggotaEskul2;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Eskul.Anggota;
import com.ascendant.diysipp.Model.Eskul.ResponseData;
import com.ascendant.diysipp.Model.Pena.ResponseModel;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnggotaEskulFragment extends Fragment {

    Destiny destiny;
    DB_Helper dbHelper;
    String Username,Password,Nama,Token,Level,Photo;
    String Eskul,ID;
    RecyclerView recycler;
    private List<DataModel> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    public AnggotaEskulFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anggota_eskul, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        destiny = new Destiny();
        recycler = view.findViewById(R.id.recycler);
        dbHelper = new DB_Helper(getActivity());
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
        Cursor cursor2 = dbHelper.checkEskul();
        if (cursor2.getCount()>0){
            while (cursor2.moveToNext()){
                Eskul = cursor2.getString(0);
                ID = cursor2.getString(1);
            }
        }
        Logic();
    }
    private void Logic(){
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mManager);
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseModel> Data = api.AnggotaEskul(destiny.AUTH(Token),ID);
        Data.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatusCode().equals("000")){
                    mItems=response.body().getData();
                    mAdapter = new AdapterAnggotaEskul(getActivity(),mItems);
                    recycler.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }else if (response.body().getStatusCode().equals("001") || response.body().getStatusCode().equals("002")){
                    destiny.AutoLogin(Username,Password,getActivity());
//                        Toast.makeText(getActivity(), "Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }else{
//                        Toast.makeText(getActivity(), "Terjadi Kesalahan ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
}