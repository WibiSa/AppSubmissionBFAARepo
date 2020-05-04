package com.theboss.wibi.submiss2appgithubuserwibi.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.loopj.android.http.AsyncHttpClient.log
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.ui.view.UserFollowerFragment
import com.theboss.wibi.submiss2appgithubuserwibi.ui.view.UserFollowingFragment

class SectionPagerAdapter(private val context: Context, passDataFromDetailActivity: String? , fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {
   @StringRes
   private val tabTitles = intArrayOf(R.string.tab_follower, R.string.tab_following)

    private val passDataUsername = passDataFromDetailActivity //data dari DetailActivity

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> {
                fragment = UserFollowerFragment()
                //kirim data username ke UserFollowerFragment
                val bundle1 = Bundle()
                bundle1.putString("passData", passDataUsername)
                fragment.arguments = bundle1
                log.d("IniPassDataFollower", fragment.arguments.toString())
            }
            1 -> {
                fragment = UserFollowingFragment()
                //kirim data username ke UserFollowingFragment
                val bundle2 = Bundle()
                bundle2.putString("passData", passDataUsername)
                fragment.arguments = bundle2
                log.d("IniPassDataFollowing", fragment.arguments.toString())
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}