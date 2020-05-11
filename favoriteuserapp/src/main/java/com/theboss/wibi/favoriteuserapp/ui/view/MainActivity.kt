package com.theboss.wibi.favoriteuserapp.ui.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.theboss.wibi.favoriteuserapp.R
import com.theboss.wibi.favoriteuserapp.data.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.theboss.wibi.favoriteuserapp.data.model.Favorite
import com.theboss.wibi.favoriteuserapp.helper.MappingHelper
import com.theboss.wibi.favoriteuserapp.ui.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.favorite_items.view.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    //inisialisasi
    private lateinit var adapter: FavoriteAdapter
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "User Favorite App"

        showRecyclerListViewFavoriteUser()
        //proses ambil data menggunakan background thread
        loadUserFavoritesAsync()
    }

    //setup recyclerview
    private fun showRecyclerListViewFavoriteUser(){
        adapter = FavoriteAdapter()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object :
            FavoriteAdapter.OnItemClickCallback {

            override fun onItemClicked(data: Favorite) {
                //selectedUser(data)
            }
            override fun btnFavoriteClicked(view: View, data: Favorite) {
                if(data.favorite == 1){
                    //data dihapus dr db
                    deleteUserById(view, data.id.toString())
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.delete_form_favorite)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 0
                    val iconFavorite =
                        R.drawable.outline_favorite_black_24dp
                    view.btn_favorite.setImageResource(iconFavorite)
                }
            }
        })
    }

    //proses ambil data menggunakan background thread
    private fun loadUserFavoritesAsync() {
        GlobalScope.launch ( Dispatchers.Main ){
            progress_bar.visibility = View.VISIBLE
            val deferredUserFavorites = async (Dispatchers.IO){
                //meangambil data dengan contentResolver (provider)
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null,null)
                MappingHelper.mapCursorToArrayList(
                    cursor
                )
            }
            progress_bar.visibility = View.INVISIBLE
            val userFavorites =deferredUserFavorites.await()
            if (userFavorites.size > 0){
                adapter.listFavorites = userFavorites
            }else{
                adapter.listFavorites = ArrayList()
                delay(300)
                Toast.makeText(this@MainActivity, getString(R.string.no_data_now), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //delete data in db by id
    private fun deleteUserById (view: View, id: String){
        //mwnghapus dengan id
        uriWithId = Uri.parse("$CONTENT_URI/$id")

        //delete with contentResolver (Provider)
        view.context.contentResolver.delete(uriWithId, null, null)

    }

    override fun onRestart() {
        showRecyclerListViewFavoriteUser()
        loadUserFavoritesAsync()
        super.onRestart()
    }
}
