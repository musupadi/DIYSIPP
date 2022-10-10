package com.ascendant.diysipp.Model;

import androidx.annotation.Nullable;

import com.ascendant.diysipp.Model.Pena.SoalModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTugasNew {
    @SerializedName("statusCode")
    @Expose
    public String statusCode;

    @SerializedName("statusMessage")
    @Expose
    public String statusMessage;

    @SerializedName("data")
    @Nullable
    SoalModel data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Nullable
    public SoalModel getData() {
        return data;
    }

    public void setData(@Nullable SoalModel data) {
        this.data = data;
    }
}
