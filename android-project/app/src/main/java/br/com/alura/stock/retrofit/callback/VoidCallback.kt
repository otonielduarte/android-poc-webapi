package br.com.alura.stock.retrofit.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class VoidCallback(private val callbackListener: ResponseListener): Callback<Void> {
    @EverythingIsNonNull
    override fun onResponse(call: Call<Void>, response: Response<Void>) {
        if(response.isSuccessful) {
            callbackListener.onSuccess();
        } else {
            callbackListener.onFailure("Bad request")
        }
    }
    @EverythingIsNonNull
    override fun onFailure(call: Call<Void>, t: Throwable) {
        callbackListener.onFailure("Bad request ${t.message}")
    }

    interface ResponseListener {
        fun onSuccess()
        fun onFailure(message: String)
    }
}