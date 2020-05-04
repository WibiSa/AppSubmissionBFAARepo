package com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Following
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {
    //mutableLiveData
    val listFollowing = MutableLiveData<ArrayList<Following>>()

    fun setFollowing(username: String?){
        val listItems = ArrayList<Following>()
        val apiUrl = "https://api.github.com/users/$username/following"
        val tokenValue = "db97a1f9a372c1e6faedd9e912e88ec47fcae572"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent", "request")
        client.get(apiUrl, object : AsyncHttpResponseHandler(){
            //ketika berhasil
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()){
                        val following = responseArray.getJSONObject(i)
                        val followingItems = Following()
                        followingItems.login = following.getString("login")
                        followingItems.avatarUrl = following.getString("avatar_url")
                        followingItems.type = following.getString("type")

                        listItems.add(followingItems)
                    }
                    //postValue ke mutable LiveData
                    listFollowing.postValue(listItems)
                }catch (e:Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            //ketika gagal
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun getFollowing() : LiveData<ArrayList<Following>>{
        return listFollowing
    }
}