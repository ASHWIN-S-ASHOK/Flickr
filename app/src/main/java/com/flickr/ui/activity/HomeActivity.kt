package com.flickr.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.flickr.R
import com.flickr.adapter.ImagePagerAdapter
import com.flickr.model.FlickrResponseModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContract.View, SwipeRefreshLayout.OnRefreshListener {
    private var mPresenter: HomeContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        HomePresenter(this)
        swipeRefresh.setOnRefreshListener(this)
        mPresenter?.requestPublic()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.cancelRequest()
    }

    override fun onRequestStart() {
        progressBar.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = true
    }

    override fun onRequestStop() {
        progressBar.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    override fun onRequestError(message: String?) {
        setError(message)
    }

    override fun onCriticalError() {
        setError(getString(R.string.text_something_went_wrong))
    }

    private fun setError(errorMessage: String?) {
        swipeRefresh.visibility = View.GONE
        pagerIndicator.visibility = View.GONE
        textMessage.visibility = View.VISIBLE
        textMessage.text = errorMessage
    }

    override fun onRequestSuccess(response: FlickrResponseModel) {
        if (response.items.size > 0) {
            viewPager.adapter = ImagePagerAdapter(supportFragmentManager, this, response.items)
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    swipeRefresh.setPaused(state != ViewPager.SCROLL_STATE_IDLE);
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p0: Int) {
                }

            })
            pagerIndicator.setViewPager(viewPager)
        } else {
            setError(getString(R.string.text_no_images_available))
        }
    }

    override fun setPresenter(presenter: HomeContract.Presenter?) {
        mPresenter = checkNotNull(presenter)
    }

    override fun onRefresh() {
        mPresenter?.requestPublic()
    }
}
