package com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Detail
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel: ViewModel() {

    val d = MutableLiveData<Detail>()

    fun setDetail(username: String?){
        val apiUrl = "https://api.github.com/users/$username"
        val tokenValue = "db97a1f9a372c1e6faedd9e912e88ec47fcae572"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent", "request")
        client.get(apiUrl, object : AsyncHttpResponseHandler(){
            //ketika berhasil
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val detailItems = Detail()
                    detailItems.login = responseObject.getString("login")
                    detailItems.name = responseObject.getString("name")
                    detailItems.avatarUrl = responseObject.getString("avatar_url")
                    detailItems.company = responseObject.getString("company")
                    detailItems.location = responseObject.getString("location")
                    detailItems.repository = responseObject.getInt("public_repos")

                    d.postValue(detailItems)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            //ketika gagal
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun getDetail(): LiveData<Detail>{
        return d
    }
}