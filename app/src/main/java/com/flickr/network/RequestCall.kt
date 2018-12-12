package com.flickr.network

import com.flickr.model.FlickrResponseModel
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET


class RequestCall {

    interface IPublicFlickr {
        @GET(RequestURL.URL_PUBLIC)
        fun imageData(): ErrorHandlingAdapter.RetroCall<FlickrResponseModel>
    }


}



