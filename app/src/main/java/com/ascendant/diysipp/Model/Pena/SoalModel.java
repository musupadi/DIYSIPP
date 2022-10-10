package com.ascendant.diysipp.Model.Pena;

import androidx.annotation.Nullable;

import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.Model.Produk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SoalModel {
    @SerializedName("soal")
    @Nullable
    List<DataModel> soal;

    @SerializedName("jenis")
    @Expose
    public String jenis;

    @Nullable
    public List<DataModel> getSoal() {
        return soal;
    }

    public void setSoal(@Nullable List<DataModel> soal) {
        this.soal = soal;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
