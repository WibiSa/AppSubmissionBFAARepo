package com.theboss.wibi.submiss2appgithubuserwibi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter.FollowingAdapter
import com.theboss.wibi.submiss2appgithubuserwibi.ui.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_user_following.*

/**
 * Fragment Following.
 */
class UserFollowingFragment : Fragment() {
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    private var receiveDataUsername: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_user_following, container, false)

        receiveDataUsername = arguments?.getString("passData")
        setupViewModel(receiveDataUsername)

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showRecyclerListViewFollowing()
    }

    private fun showRecyclerListViewFollowing(){
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = adapter
    }

    private fun setupViewModel(username: String?){
        //menghubungkan viewModel
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        followingViewModel.setFollowing(username)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null){
                adapter.setData(followingItems)
            }
        })
    }

}
