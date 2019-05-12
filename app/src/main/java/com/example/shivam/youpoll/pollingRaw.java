package com.example.shivam.youpoll;

import android.os.Parcel;
import android.os.Parcelable;

public class pollingRaw implements Parcelable {
    public static final Parcelable.Creator<pollingRaw> CREATOR = new Parcelable.Creator<pollingRaw>() {
        @Override
        public pollingRaw createFromParcel(Parcel source) {
            return new pollingRaw(source);
        }

        @Override
        public pollingRaw[] newArray(int size) {
            return new pollingRaw[size];
        }
    };
    String Question;
    String a;
    String b;
    String c;
    String d;
    String av;
    String bv;
    String cv;
    String dv;
    String Count;

    public pollingRaw() {

    }

    protected pollingRaw(Parcel in) {
        this.Question = in.readString();
        this.a = in.readString();
        this.b = in.readString();
        this.c = in.readString();
        this.d = in.readString();
        this.av = in.readString();
        this.bv = in.readString();
        this.cv = in.readString();
        this.dv = in.readString();
        this.Count = in.readString();
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAv() {
        return av;
    }

    public void setAv(String av) {
        this.av = av;
    }

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Question);
        dest.writeString(this.a);
        dest.writeString(this.b);
        dest.writeString(this.c);
        dest.writeString(this.d);
        dest.writeString(this.av);
        dest.writeString(this.bv);
        dest.writeString(this.cv);
        dest.writeString(this.dv);
        dest.writeString(this.Count);
    }
}