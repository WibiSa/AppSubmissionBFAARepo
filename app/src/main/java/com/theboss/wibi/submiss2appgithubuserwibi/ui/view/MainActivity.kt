package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Users
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.UsersAdapter
import com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel.UsersViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private lateinit var userViewModel: UsersViewModel

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

        //ketika satu user di click
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(mdata: Users) {
                selectedUser(mdata)
            }
        })
    }

    //Intent ke DetailActivity
    private fun selectedUser(userItems: Users){
        val username = userItems.login

        val intentToDetailActivity = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, username)// data username dikirim
        startActivity(intentToDetailActivity)
    }

    private fun setupViewModel(){
        //menghubungkan kelas UsersViewModel dengan MainActivity / View
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
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    //fungsi tampilan progres indicator
    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}
