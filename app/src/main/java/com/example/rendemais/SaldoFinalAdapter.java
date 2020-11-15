package com.example.rendemais;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SaldoFinalAdapter extends ArrayAdapter<Renda> {

    public SaldoFinalAdapter(@NonNull Context context, @NonNull List<Renda> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Renda rnd = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.saldo_final, parent, false);
        }
        // Lookup view for data population
        TextView tvSalFinal= (TextView) convertView.findViewById(R.id.textViewSaldoFinal);

        // Populate the data into the template view using the data object
        String saldo_final = String.valueOf(rnd.saldo_final);
        String renda = String.valueOf(rnd.renda);

        tvSalFinal.setText("Renda: "+renda+"    |    Saldo Final: "+saldo_final);

        // Return the completed view to render on screen
        return convertView;
    }
}
