package com.theboss.wibi.consumergithubuserapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//inisialisasi
        private lateinit var adapter: FavoriteAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            supportActionBar?.title = "User Favorite App"

            showRecyclerListViewFavoriteUser()
            //proses ambil data menggunakan background thread
            //loadUserFavoritesAsync()
        }

        //setup recyclerview
        private fun showRecyclerListViewFavoriteUser(){
            adapter = FavoriteAdapter()

            rv_favorite.layoutManager = LinearLayoutManager(this)
            rv_favorite.setHasFixedSize(true)
            rv_favorite.adapter = adapter

            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{

                override fun onItemClicked(data: Favorite) {
                    //selectedUser(data)
                }
                //untuk menghapus belum dibuat ...
            })
        }

        //proses ambil data menggunakan background thread
//        private fun loadUserFavoritesAsync() {
//            GlobalScope.launch ( Dispatchers.Main ){
//                progress_bar.visibility = View.VISIBLE
//                val deferredUserFavorites = async (Dispatchers.IO){
//                    //meangambil data dengan contentResolver (provider)
//                    val cursor = contentResolver.query(DatabaseContract.UserFavoriteColumns.CONTENT_URI, null, null, null,null)
//                    MappingHelper.mapCursorToArrayList(cursor)
//                }
//                progress_bar.visibility = View.INVISIBLE
//                val userFavorites =deferredUserFavorites.await()
//                if (userFavorites.size > 0){
//                    adapter.listFavorites = userFavorites
//                }else{
//                    adapter.listFavorites = ArrayList()
//                    delay(300)
//                    Toast.makeText(this@MainActivity, getString(R.string.no_data_now), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

//    //intent ke halaman detail sesuai user yg dipilih
//    private fun selectedUser(userFav: Favorite){
//        val username = userFav.login
//
//        val intentToDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
//        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, username)
//        startActivity(intentToDetail)
//    }
    }
