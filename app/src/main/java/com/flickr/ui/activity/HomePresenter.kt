package com.flickr.ui.activity

import android.content.Context
import com.facebook.common.internal.Preconditions
import com.flickr.model.FlickrResponseModel
import com.flickr.network.ErrorHandlingAdapter
import com.flickr.network.RequestCall
import com.flickr.network.RequestHelper
import retrofit2.Response
import java.io.IOException


class HomePresenter internal constructor(view: HomeContract.View) : HomeContract.Presenter,
    ErrorHandlingAdapter.RetroCallback<FlickrResponseModel> {

    private val mView: HomeContract.View =
        Preconditions.checkNotNull<HomeContract.View>(view, view.javaClass.simpleName)
    var call: ErrorHandlingAdapter.RetroCall<FlickrResponseModel>? = null

    init {
        mView.setPresenter(this)
    }


    override fun start(context: Context) {

    }

    override fun requestPublic() {
        val requestCall = RequestHelper.createRequest(RequestCall.IPublicFlickr::class.java)
        call = requestCall.imageData()
        call?.enqueue(this)
    }

    override fun onRequestStart() {
        mView.onRequestStart()
    }

    override fun onRequestStop() {
        mView.onRequestStop()
    }

    override fun onSuccess(response: Response<FlickrResponseModel>) {
        val model = response.body()
        if (model != null) {
            mView.onRequestSuccess(model)
        } else {
            mView.onCriticalError()
        }
    }

    override fun onUnauthenticated(response: Response<*>) {
        mView.onRequestError(response.message())
    }

    override fun onClientError(response: Response<*>) {
        mView.onRequestError(response.message())
    }

    override fun onServerError(response: Response<*>) {
        mView.onRequestError(response.message())
    }

    override fun onNetworkError(e: IOException) {
        mView.onRequestError(e.message)
    }

    override fun onUnexpectedError(t: Throwable) {
        mView.onRequestError(t.message)
    }

    override fun cancelRequest() {
        if (call != null) {
            call?.cancel()
        }
    }

}
