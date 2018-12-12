package com.flickr.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaModel implements Parcelable {
    public String m;

    protected MediaModel(Parcel in) {
        m = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MediaModel> CREATOR = new Parcelable.Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };
}