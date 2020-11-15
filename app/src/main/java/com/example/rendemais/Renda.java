package com.example.rendemais;

import android.os.Parcel;
import android.os.Parcelable;

public class Renda implements Parcelable {
    public String ID;
    public String usuario;
    public Double renda;
    public Double saldo_final;

    public Renda() {

    }

    protected Renda(Parcel in) {
        ID = in.readString();
        usuario = in.readString();
        renda = in.readDouble();
        saldo_final = in.readDouble();
    }

    public static final Creator<Renda> CREATOR = new Creator<Renda>() {
        @Override
        public Renda createFromParcel(Parcel in) {
            return new Renda(in);
        }

        @Override
        public Renda[] newArray(int size) {
            return new Renda[size];
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
        parcel.writeDouble(renda);
        parcel.writeDouble(saldo_final);
    }
}