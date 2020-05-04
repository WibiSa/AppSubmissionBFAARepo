package com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Following
import kotlinx.android.synthetic.main.following_items.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>()  {
    private val listFollowing = ArrayList<Following>()

    fun setData(items: ArrayList<Following>){
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.following_items, viewGroup, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int = listFollowing.size

    override fun onBindViewHolder(followingViewHolder: FollowingViewHolder, position: Int) {
        followingViewHolder.bind(listFollowing[position])
    }

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(following: Following){
            with(itemView){
                Glide.with(itemView.context).load(following.avatarUrl).apply(RequestOptions().override(55,55)).into(img_avatar_following)
                tv_username.text = following.login
                tv_type.text = following.type
            }
        }
    }
}