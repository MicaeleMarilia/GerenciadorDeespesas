package com.example.rendemais;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    public String ID;
    public String nome;
    public String email;
    public String senha;

    public Usuario() {

    }

    protected Usuario(Parcel in) {
        ID = in.readString();
        nome = in.readString();
        email = in.readString();
        senha = in.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(nome);
        parcel.writeString(email);
        parcel.writeString(senha);
    }
}