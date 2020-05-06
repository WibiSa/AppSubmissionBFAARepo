package com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Favorite
import com.theboss.wibi.submiss2appgithubuserwibi.data.model.Users
import kotlinx.android.synthetic.main.user_items.view.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val data = ArrayList<Users>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    //argument fungsi setData terdiri dari Arraylist model Users dan Favorite
    fun setData(items: ArrayList<Users>, itemsFavorite: ArrayList<Favorite>){
        data.clear()

        for (i in 0 until items.size){
            val j = 0
            if(j >= itemsFavorite.size)break
            for (j in 0 until itemsFavorite.size){
                //kondisi cek data items(data Users dr API) apakah sudah difavoritkan(dengam memberi nilai Users.favorite = 1 jika ketemu)
                if (itemsFavorite[j].login.toString() == items[i].login.toString()){
                    items[i].favorite = 1
                }
            }
        }

        data.addAll(items)
        notifyDataSetChanged()
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


                if(users.favorite == 1) {
                    val iconFavorite = R.drawable.outline_favorite_black_24dp
//                    Glide.with(itemView.context).load(iconFavorite)
//                        .apply(RequestOptions().override(55, 55)).into(btn_favorite)
                    btn_favorite.setImageResource(iconFavorite)
                }else{
                    val iconFavorite = R.drawable.outline_favorite_border_black_24dp
//                    Glide.with(itemView.context).load(iconFavorite)
//                        .apply(RequestOptions().override(55, 55)).into(btn_favorite)
                    btn_favorite.setImageResource(iconFavorite)
                }
                //implementasi ketika itemView di click
                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(users)}

                //implementasi ketika btn fav di click
                btn_favorite.setOnClickListener { onItemClickCallback?.onBtnFavoriteClicked(itemView, users) }
            }
        }
    }

    //interface untuk on item click callback
    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
        //add btn_fav itemClicked
        fun onBtnFavoriteClicked(view: View, data: Users)
    }
}