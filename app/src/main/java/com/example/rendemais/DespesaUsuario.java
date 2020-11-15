package com.example.rendemais;

import android.os.Parcel;
import android.os.Parcelable;

public class DespesaUsuario implements Parcelable {
    public String ID;
    public String usuario;
    public String nome;
    public String tipo;
    public Double valor;


    public DespesaUsuario() {

    }

    protected DespesaUsuario(Parcel in) {
        ID = in.readString();
        usuario = in.readString();
        nome = in.readString();
        tipo = in.readString();
        valor = in.readDouble();
    }

    public static final Creator<DespesaUsuario> CREATOR = new Creator<DespesaUsuario>() {
        @Override
        public DespesaUsuario createFromParcel(Parcel in) {
            return new DespesaUsuario(in);
        }

        @Override
        public DespesaUsuario[] newArray(int size) {
            return new DespesaUsuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(usuario);
        parcel.writeString(nome);
        parcel.writeString(tipo);
        parcel.writeDouble(valor);
    }
}