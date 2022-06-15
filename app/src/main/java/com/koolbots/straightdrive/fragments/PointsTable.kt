package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.models.Match

class PointsTable: Fragment() {

    private val INNING:String="match"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.points_table, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as AppCompatActivity




    }

    companion object {

        @JvmStatic
        fun newInstance(match: Match?):PointsTable {
            if(match!=null)
            {
                return PointsTable().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, match)
                    }
                }
            }
            else
            {
                val m= Match()
                return PointsTable().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, m)
                    }
                }
            }
        }

    }

    fun isTablet(mContext: Context): Boolean {
        return mContext.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}