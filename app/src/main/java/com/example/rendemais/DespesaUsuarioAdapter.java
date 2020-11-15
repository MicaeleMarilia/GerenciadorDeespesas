package com.example.rendemais;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class DespesaUsuarioAdapter extends ArrayAdapter<DespesaUsuario> {

    public DespesaUsuarioAdapter(@NonNull Context context, @NonNull List<DespesaUsuario> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DespesaUsuario desUse = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvNome= (TextView) convertView.findViewById(R.id.textViewNome);

        // Populate the data into the template view using the data object
        tvNome.setText(desUse.nome+"      "+desUse.valor+"     "+desUse.tipo);


        // Return the completed view to render on screen
        return convertView;
    }
}
