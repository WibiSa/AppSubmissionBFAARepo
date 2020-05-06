package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.UserFavoriteHelper
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Favorite
import com.theboss.wibi.submiss2appgithubuserwibi.util.helper.MappingHelper
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity() {
    //inisialisasi
    private lateinit var adapter: FavoriteAdapter
    private lateinit var userFavoriteHelper: UserFavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "User Favorite"
        showRecyclerListViewFavoriteUser()

        userFavoriteHelper = UserFavoriteHelper.getInstance(applicationContext)
        userFavoriteHelper.open()

        //proses ambil data menggunakan background thread
        loadUserFavoritesAsync()
        /**berhasil load data tapi belum terjaga kalau di rotasi*/

    }

    //proses ambil data menggunakan background thread
    private fun loadUserFavoritesAsync() {
        GlobalScope.launch ( Dispatchers.Main ){
            progress_bar.visibility = View.VISIBLE
            val deferredUserFavorites = async (Dispatchers.IO){
                val cursor = userFavoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progress_bar.visibility = View.INVISIBLE
            val userFavorites =deferredUserFavorites.await()
            if (userFavorites.size > 0){
                adapter.listFavorites = userFavorites
            }else{
                adapter.listFavorites = ArrayList()
                delay(300)
                Toast.makeText(this@FavoriteActivity, getString(R.string.no_data_now), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //setup recyclerview
    private fun showRecyclerListViewFavoriteUser(){
        adapter = FavoriteAdapter()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{

            override fun onItemClicked(data: Favorite) {
                selectedUser(data)
            }
            //untuk menghapus belum dibuat ...

        })
    }

    private fun selectedUser(userFav: Favorite){
        val username = userFav.login

        val intentToDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, username)
        startActivity(intentToDetail)
    }

    override fun onDestroy() {
        super.onDestroy()
        userFavoriteHelper.close()
    }
}
