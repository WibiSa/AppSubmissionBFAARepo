package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Detail
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.SectionPagerAdapter
import com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var detailViewModel: DetailViewModel
   // private val detail = Detail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //setup ActionBar
        supportActionBar?.title = "User Detail"
        supportActionBar?.elevation = 0f

        //terima data username dari MainActivity
        val usernameFromMainActivity = intent.getStringExtra(EXTRA_USERNAME)

        setupTabs(usernameFromMainActivity)
        setupViewModel(usernameFromMainActivity)
    }

    //Implementasi TabBar + kirim data username ke SectionPagerAdapter
    private fun setupTabs(username: String?){
        val sectionPagerAdapter = SectionPagerAdapter(this, username, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs_layout.setupWithViewPager(view_pager)
    }

    private fun setupViewModel(username: String?){
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        showLoading(true)
        detailViewModel.setDetail(username)

        detailViewModel.getDetail().observe(this, Observer { d ->
            if (d != null){
                bind(d)
                showLoading(false)
            }
        })
    }

    private fun bind(detail: Detail){
        Glide.with(this@DetailActivity).load(detail.avatarUrl).apply(RequestOptions().override(110,110)).into(img_avatar_user)
        tv_username.text = detail.login
        tv_name.text = detail.name
        tv_three_info.text = "${detail.company} . ${detail.location} . ${detail.repository}"
    }

    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }
}
