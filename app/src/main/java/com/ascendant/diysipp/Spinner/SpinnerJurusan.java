package com.ascendant.diysipp.Spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ascendant.diysipp.Model.DataModel;
import com.ascendant.diysipp.R;

import java.util.List;

public class SpinnerJurusan extends ArrayAdapter<DataModel> {
    public SpinnerJurusan(Context context, List<DataModel> list) {
        super(context, 0, list);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_spinner, parent, false
            );
        }
        TextView textSpinner = convertView.findViewById(R.id.tvSpinner);
        TextView textId = convertView.findViewById(R.id.tvId);
        DataModel currentItem = getItem(position);
        if (currentItem != null) {
            textSpinner.setText(currentItem.getNama_penjurusan());
            textId.setText(currentItem.getId_penjurusan());
        }
        return convertView;
    }
}

