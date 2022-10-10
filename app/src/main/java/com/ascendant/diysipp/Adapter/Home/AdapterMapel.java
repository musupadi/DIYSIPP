package com.ascendant.diysipp.Adapter.Home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ascendant.diysipp.Activity.menu.MediaInformasi.EducationTalkshow.DetailEducationTalkshowActivity;
import com.ascendant.diysipp.Activity.menu.MediaPembelajaran.MediaPembelajaranActivity;
import com.ascendant.diysipp.Activity.menu.Tugas.TugasActivity;
import com.ascendant.diysipp.Method.Destiny;
import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.R;
import com.ascendant.diysipp.SharedPreferance.DB_Helper;

import java.util.List;

public class AdapterMapel extends RecyclerView.Adapter<AdapterMapel.HolderData> {
    private List<DataModel> mList;
    private Context ctx;

    DB_Helper dbHelper;
    Boolean onClick=false;
    RecyclerView recyclerView;
    Destiny destiny;
    String ONCLICK;
    public AdapterMapel(Context ctx, List<DataModel> mList,String ONCLICK){
        this.ctx = ctx;
        this.mList = mList;
        this.ONCLICK = ONCLICK;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_mapel,viewGroup,false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderData holderData, int posistion) {
        destiny = new Destiny();
        final DataModel dm = mList.get(posistion);
        holderData.Nama.setText(dm.getNama_mapel());
        holderData.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ONCLICK.equals("TUGAS")){
                    Intent i = new Intent(ctx, TugasActivity.class);
                    i.putExtra("ID", dm.getId_mapel());
                    i.putExtra("NAMA",dm.getNama_mapel());
                    ctx.startActivity(i);
                }else if (ONCLICK.equals("MEDIA")){
                    Intent i = new Intent(ctx, MediaPembelajaranActivity.class);
                    i.putExtra("ID", dm.getId_mapel());
                    i.putExtra("NAMA",dm.getNama_mapel());
                    ctx.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView Nama;
        LinearLayout card;
        public HolderData(View v){
            super(v);
            Nama = v.findViewById(R.id.tvNama);
            card = v.findViewById(R.id.LayoutCardView);
        }
    }
}
