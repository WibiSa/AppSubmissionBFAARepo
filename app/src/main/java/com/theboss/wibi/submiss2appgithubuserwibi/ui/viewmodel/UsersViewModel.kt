package com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Users
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UsersViewModel : ViewModel() {
    //Variabel mutablelivedata -> livedata yang bisa diubah nilainya
    val listUsers = MutableLiveData<ArrayList<Users>>()

    //Fungsi Set viewModel user
    fun setUsers(username: String) {
        val listItems = ArrayList<Users>()
        val apiUrl = "https://api.github.com/search/users?q=$username"
        val tokenValue = "db97a1f9a372c1e6faedd9e912e88ec47fcae572"

        //Request api
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent", "request")
        client.get(apiUrl, object : AsyncHttpResponseHandler(){
            //ketika request berhasil
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON-nya
                    val result = String(responseBody)
                    val responseObject  = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val user = items.getJSONObject(i)
                        val userItems = Users()
                        userItems.id = user.getInt("id")
                        userItems.login = user.getString("login")
                        userItems.avatarUrl = user.getString("avatar_url")
                        userItems.type = user.getString("type")
                        userItems.score = user.getDouble("score")
                        listItems.add(userItems)
                    }
                    //post nilai ke mutableLiveData variabel
                    listUsers.postValue(listItems)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            //ketika request gagal
            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    //fungsi get viewModel user
    fun getUsers(): LiveData<ArrayList<Users>>{
        return listUsers
    }
}