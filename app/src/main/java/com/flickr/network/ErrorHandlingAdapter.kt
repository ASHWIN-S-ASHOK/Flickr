/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flickr.network

import android.os.Handler
import android.os.Looper
import com.facebook.common.internal.Preconditions
import com.flickr.R
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 * A sample showing a custom [CallAdapter] which adapts the built-in [Call] to a custom
 * version whose callback has more granular methods.
 */
class ErrorHandlingAdapter {
    /**
     * A callback which offers granular callbacks for various conditions.
     */
    interface RetroCallback<T> {
        /**
         * On Request Start.
         */
        fun onRequestStart()

        /**
         * On Request Start.
         */
        fun onRequestStop()

        /**
         * Called for [200, 300) responses.
         */
        fun onSuccess(response: Response<T>)

        /**
         * Called for 401 responses.
         */
        fun onUnauthenticated(response: Response<*>)

        /**
         * Called for [400, 500) responses, except 401.
         */
        fun onClientError(response: Response<*>)

        /**
         * Called for [500, 600) response.
         */
        fun onServerError(response: Response<*>)

        /**
         * Called for network errors while making the callPastEvents.
         */
        fun onNetworkError(e: IOException)

        /**
         * Called for unexpected errors while making the callPastEvents.
         */
        fun onUnexpectedError(t: Throwable)
    }

    interface RetroCall<T> {
        fun cancel()

        fun enqueue(callback: RetroCallback<T>)

        fun clone(): RetroCall<T>

    }

    class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
        override fun get(returnType: Type, annotations: Array<Annotation>,
                         retrofit: Retrofit
        ): CallAdapter<*, *>? {
            if (CallAdapter.Factory.getRawType(returnType) != RetroCall::class.java) {
                return null
            }
            if (returnType !is ParameterizedType) {
                throw IllegalStateException(
                        "RetroCall must have generic type (e.g., RetroCall<ResponseBody>)")
            }
            val responseType = CallAdapter.Factory.getParameterUpperBound(0, returnType)
            val callbackExecutor = retrofit.callbackExecutor()
            return ErrorHandlingCallAdapter<R>(responseType, callbackExecutor!!)
        }

        private class ErrorHandlingCallAdapter<R> internal constructor(private val responseType: Type, private val callbackExecutor: Executor) : CallAdapter<R, RetroCall<R>> {

            override fun responseType(): Type {
                return responseType
            }

            override fun adapt(call: Call<R>): RetroCall<R> {
                return RetroCallAdapter(call, callbackExecutor)
            }
        }
    }

    /**
     * Adapts a [Call] to [RetroCall].
     */
    internal class RetroCallAdapter<T>(private val call: Call<T>, private val callbackExecutor: Executor) : RetroCall<T> {
        private val mHandler = Handler(Looper.getMainLooper())

        override fun cancel() {
            call.cancel()
        }

        override fun enqueue(callback: RetroCallback<T>) {
            Preconditions.checkNotNull(callback, "RetroCallback cannot be null!")
            callback.onRequestStart()
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    mHandler.post {
                        val code = response.code()
                        if (code in 200..299) {
                            callback.onSuccess(response)
                        } else if (code == 401) {
                            callback.onUnauthenticated(response)
                        } else if (code in 400..499) {
                            callback.onClientError(response)
                        } else if (code in 500..599) {
                            callback.onServerError(response)
                        } else {
                            callback.onUnexpectedError(RuntimeException("Unexpected response $response"))
                        }
                        callback.onRequestStop()
                    }

                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    mHandler.post {
                        if (throwable is IOException) {
                            callback.onNetworkError(OfflineException("No Internet Connection"))
                        } else {
                            callback.onUnexpectedError(throwable)
                        }
                        callback.onRequestStop()
                    }
                }
            })
        }

        override fun clone(): RetroCall<T> {
            return RetroCallAdapter(call.clone(), callbackExecutor)
        }
    }


}