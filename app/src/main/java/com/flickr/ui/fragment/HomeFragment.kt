package com.flickr.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flickr.R
import com.flickr.adapter.ImagePagerAdapter.ARGS_ITEM
import com.flickr.model.ItemModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    var item: ItemModel? = null

    companion object {

        fun newInstance(bundle: Bundle): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = arguments?.getParcelable(ARGS_ITEM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textTitle.text = item?.title
        imageItem.setImageURI(item?.media?.m)
    }

}
