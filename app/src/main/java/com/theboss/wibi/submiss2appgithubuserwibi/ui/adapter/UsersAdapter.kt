package com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Users
import kotlinx.android.synthetic.main.user_items.view.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val data = ArrayList<Users>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<Users>){
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_items, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(data[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(users: Users){
            with(itemView){
                Glide.with(itemView.context).load(users.avatarUrl).apply(RequestOptions().override(55,55)).into(img_user)
                tv_username.text = users.login
                tv_type.text = users.type

                //implementasi ketika itemView di click
                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(users)}
            }
        }
    }

    //interface untuk on item click callback
    interface OnItemClickCallback {
        fun onItemClicked(mdata: Users)
    }
}