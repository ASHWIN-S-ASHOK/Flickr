package com.flickr.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.flickr.R;
import com.flickr.model.ItemModel;
import com.flickr.ui.fragment.HomeFragment;

import java.util.ArrayList;


public class ImagePagerAdapter extends FragmentPagerAdapter {
    public static final String ARGS_ITEM = "ITEM";
    private ArrayList<ItemModel> items;
    private Context _context;

    public ImagePagerAdapter(FragmentManager fm, Context context, ArrayList<ItemModel> items) {
        super(fm);
        this._context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, items.get(position));
        return HomeFragment.Companion.newInstance(bundle);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _context.getString(R.string.app_name);
    }


}