package com.flickr.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
        public String title;
        public MediaModel media;
        public String author;
    
    private ItemModel(Parcel in) {
        title = in.readString();
        media = (MediaModel) in.readValue(MediaModel.class.getClassLoader());
        author = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeValue(media);
        dest.writeString(author);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
}