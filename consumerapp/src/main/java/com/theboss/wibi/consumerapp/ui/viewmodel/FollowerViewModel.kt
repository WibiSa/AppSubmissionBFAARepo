package com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.theboss.wibi.consumerapp.data.model.Follower
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerViewModel : ViewModel() {
    //mutable livedata
    val listFollower = MutableLiveData<ArrayList<Follower>>()

    fun setFollower(username: String?){
        val listItems = ArrayList<Follower>()
        val apiUrl = "https://api.github.com/users/$username/followers"
        val tokenValue = "db97a1f9a372c1e6faedd9e912e88ec47fcae572"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent", "request")
        client.get(apiUrl, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result =  String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until  responseArray.length()){
                        val follower = responseArray.getJSONObject(i)
                        val followerItems = Follower()
                        followerItems.login = follower.getString("login")
                        followerItems.avatarUrl = follower.getString("avatar_url")
                        followerItems.type = follower.getString("type")

                        listItems.add(followerItems)
                    }
                    //post value ke mutablelivedata
                    listFollower.postValue(listItems)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun getFollower(): LiveData<ArrayList<Follower>>{
        return listFollower
    }
}