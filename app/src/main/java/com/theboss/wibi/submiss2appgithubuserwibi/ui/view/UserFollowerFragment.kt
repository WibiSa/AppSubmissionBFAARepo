package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.FollowerAdapter
import com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_user_follower.*

/**
 * Fragment follower.
 */
class UserFollowerFragment : Fragment() {

    private lateinit var adapter: FollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel

    private var receiveDataUsername: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_user_follower, container, false)

        receiveDataUsername = arguments?.getString("passData") //terima data dari bundle argument SectionPagerAdapter
        setupViewModel(receiveDataUsername)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showRecyclerListViewFollower()
    }

    private fun showRecyclerListViewFollower(){
        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()

        rv_follower.layoutManager = LinearLayoutManager(activity)
        rv_follower.adapter = adapter
    }

    private fun setupViewModel(username: String?){
        //menghubungkan viewModelFollower to this
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)

        followerViewModel.setFollower(username)

        followerViewModel.getFollower().observe(this, Observer { followerItems ->
            if (followerItems != null){
                adapter.setData(followerItems)
            }
        })
    }

}