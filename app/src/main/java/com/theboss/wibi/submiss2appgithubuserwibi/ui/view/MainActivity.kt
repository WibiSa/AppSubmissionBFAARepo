package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Users
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.UsersAdapter
import com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel.UsersViewModel
import com.theboss.wibi.submiss2appgithubuserwibi.util.helper.MappingHelper
import com.theboss.wibi.submiss2appgithubuserwibi.util.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_items.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private lateinit var userViewModel: UsersViewModel
    private lateinit var userItems: ArrayList<Users>
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        showRecyclerListViewUser()
    }

    private fun showRecyclerListViewUser(){
        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        //Membuat thread baru untuk melihat perubahan (observe) supaya tidak mengganggu kinerja thread utama.
        val handlerThread = HandlerThread("DataObserve")
        handlerThread.start()
        val handler =android.os.Handler(handlerThread.looper)

        //membuat sebuah fungsi yang menjadi turunan ContentObserver supaya bisa melakukan fungsi observe.
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserFavorites(userItems)
            }
        }
        //Setelah observer dibuat, kita daftarkan dengan menggunakan registerContentObserver.
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        //Menanggapi click dr user pada satu item di recyclerView
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{

            //itemView click
            override fun onItemClicked(data: Users) {
                selectedUser(data)
            }

            //btn favorite click
            override fun onBtnFavoriteClicked(view: View, data: Users) {
                var iconFavorite = R.drawable.outline_favorite_black_24dp
                if(data.favorite == 0){
                    //data dimasukkan ke db
                    insertToDb(view, data.id, data.login, data.avatarUrl, data.type)
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.save_to_favorite)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 1

                }else{
                    //data dihapus dr db
                    deleteUserById(view, data.id.toString())
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.delete_form_favorite)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 0
                    iconFavorite = R.drawable.outline_favorite_border_black_24dp
                }
               view.btn_favorite.setImageResource(iconFavorite)
            }
        })
    }

    private fun setupViewModel(){
        //menghubungkan class UsersViewModel dengan MainActivity / View
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UsersViewModel::class.java)

        //mencari user berdasarkan inputan username pada custom_search_view
        btn_search_user.setOnClickListener {
            val username = edit_text_user.text.toString()
            if (username.isEmpty()) return@setOnClickListener //kondisi ya

            showLoading(true)
            userViewModel.setUsers(username)
        }
        //mendapatkan hasil pencarian user
        userViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null){
                //mengisi dengan list terbaru
                this.userItems = userItems
                //implementasi fungsi loadUser
                loadUserFavorites(userItems)
                showLoading(false)
            }
        })
    }

    //load data user favorite dr db
    private fun loadUserFavorites(userItems: ArrayList<Users>){
        //menggunakan coroutine
        GlobalScope.launch (Dispatchers.Main){
            val deferredItemsFavorite = async(Dispatchers.IO) {

                //load data dengan contentResolver (Provider)
                val cursor = this@MainActivity.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val userFavorites = deferredItemsFavorite.await() //data user favorite dr db

            adapter.setData(userItems, userFavorites) //Set data dari APi dan db ke adapter untuk di olah.

        }
    }

    //db insert data user favorite
    private fun insertToDb(
        view: View,
        id: Int,
        login: String?,
        avatarUrl: String?,
        type: String?
    ){
        val values = ContentValues()
        values.put(DatabaseContract.UserFavoriteColumns._ID, id)
        values.put(DatabaseContract.UserFavoriteColumns.LOGIN, login)
        values.put(DatabaseContract.UserFavoriteColumns.AVATAR_URL, avatarUrl)
        values.put(DatabaseContract.UserFavoriteColumns.TYPE, type)
        values.put(DatabaseContract.UserFavoriteColumns.FAVORITE, 1)

        //insert with contentResolver (Provider)
        view.context.contentResolver.insert(CONTENT_URI, values)
    }

    //delete data in db by id
    private fun deleteUserById (view: View, id: String){
        //mwnghapus dengan id
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + id)

        //delete with contentResolver (Provider)
        view.context.contentResolver.delete(uriWithId, null, null)

    }

    //intent ke DetailActivity
    private fun selectedUser(userItems: Users){
        val username = userItems.login

        val intentToDetailActivity = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, username)// data username dikirim
        startActivity(intentToDetailActivity)
    }

    //progress indicator logic
    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }

    //implementasi menu pada action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_favorite ->{
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_change_setting ->{
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.action_reminder_setting ->{
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            else -> return  true
        }
        return super.onOptionsItemSelected(item)
    }
}
