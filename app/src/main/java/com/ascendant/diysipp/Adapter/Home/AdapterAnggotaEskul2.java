package com.ascendant.diysipp.Adapter.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.Eskul.Anggota;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.List;

public class AdapterAnggotaEskul2 extends RecyclerView.Adapter<AdapterAnggotaEskul2.HolderData> {
    private List<Anggota> mList;
    private Context ctx;

    DB_Helper dbHelper;
    Boolean onClick = false;
    RecyclerView recyclerView;
    Destiny destiny;

    public AdapterAnggotaEskul2(Context ctx, List<Anggota> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_anggota_eskul, viewGroup, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderData holderData, int posistion) {
        destiny = new Destiny();
        final Anggota dm = mList.get(posistion);
//        Toast.makeText(ctx, String.valueOf(mList.size()), Toast.LENGTH_SHORT).show();
        if (mList.size() > 0){
            holderData.No.setText(String.valueOf(posistion+1));
            holderData.Nama.setText(dm.getNama_siswa());
            holderData.Kelas.setText(dm.getNama_kelas());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView No, Nama, Kelas;
        LinearLayout card;

        public HolderData(View v) {
            super(v);
            No = v.findViewById(R.id.tvNoEskul);
            Nama = v.findViewById(R.id.tvNamaAnggota);
            Kelas = v.findViewById(R.id.tvNamaKelas);
            card = v.findViewById(R.id.LayoutCardView);
        }
    }
}

