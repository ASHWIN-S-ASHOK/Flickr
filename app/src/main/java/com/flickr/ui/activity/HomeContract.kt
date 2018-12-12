package com.flickr.ui.activity

import com.flickr.base.BasePresenter
import com.flickr.base.BaseView
import com.flickr.model.FlickrResponseModel


interface HomeContract {

    interface View : BaseView<Presenter> {
        fun onRequestStart()

        fun onRequestStop()

        fun onRequestError(message: String?)

        fun onCriticalError()

        fun onRequestSuccess(response: FlickrResponseModel)
    }

    interface Presenter : BasePresenter {

        fun requestPublic()
        fun cancelRequest()

    }
}
