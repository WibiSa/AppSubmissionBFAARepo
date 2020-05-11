package com.theboss.wibi.favoriteuserapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.theboss.wibi.favoriteuserapp.data.model.Favorite
import com.theboss.wibi.favoriteuserapp.R
import kotlinx.android.synthetic.main.favorite_items.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    internal var listFavorites = ArrayList<Favorite>()
    //set data
    set(listFavorites) {
        if (listFavorites.size > 0){
            this.listFavorites.clear()
        }
        this.listFavorites.addAll(listFavorites)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.favorite_items, viewGroup, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = listFavorites.size

    override fun onBindViewHolder(favoriteViewholder: FavoriteViewHolder, position: Int) {
        favoriteViewholder.bind(listFavorites[position], position)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite, position: Int){
            with(itemView){
                tv_username.text = favorite.login
                tv_type.text = favorite.type
                Glide.with(this.context).load(favorite.avatarUrl).apply(RequestOptions().override(55,55)).into(img_user)

                if(favorite.favorite == 1) {
                    val iconFavorite =
                        R.drawable.outline_favorite_black_24dp
                    btn_favorite.setImageResource(iconFavorite)
                }

                //implementasi itemView click
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favorite) }

                //implementasi tbn fav click
                btn_favorite.setOnClickListener{
                    onItemClickCallback?.btnFavoriteClicked(itemView, favorite)
                    removeItem(position)
                }
            }
        }
    }

    //fungsi menghapus item
    fun removeItem(position: Int){
        this.listFavorites.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorites.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
        fun btnFavoriteClicked(view: View, data: Favorite)
    }
}