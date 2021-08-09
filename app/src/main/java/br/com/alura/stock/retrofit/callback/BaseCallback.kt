package br.com.alura.stock.retrofit.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class BaseCallback<T>(private val callbackListener: ResponseListener<T>) : Callback<T> {

    @EverythingIsNonNull
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(response.isSuccessful) {
            val result = response.body()
            if(result != null) {
                callbackListener.onSuccess(result)
            } else {
                callbackListener.onFailure("Bad request")
            }
        }
    }

    @EverythingIsNonNull
    override fun onFailure(call: Call<T>, t: Throwable) {
        callbackListener.onFailure("Bad request ${t.message}")
    }

    interface ResponseListener<T> {
        fun onSuccess(result: T)
        fun onFailure(result: String)
    }
}