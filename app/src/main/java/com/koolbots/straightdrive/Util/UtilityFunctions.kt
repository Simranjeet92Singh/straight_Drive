package com.koolbots.straightdrive.Util

import android.content.Context
import android.content.res.Configuration
import android.net.wifi.WifiManager

class UtilityFunctions {
    companion object
    {
        public fun overToBalls(overs:Double?):Int{
            val temp=overs?:return 0
            return temp.toInt()*6+((Math.round((temp%1)*10)).toInt())
        }
        public fun ballsToOvers(balls :Int?):Double
        {
            val temp=balls?:return 0.0
            return ((balls/6)+(balls%6)/10.0) .toDouble()
        }
        fun isTablet(mContext: Context): Boolean {
            return mContext.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        }
        fun getMac(context: Context): String {
            val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = manager.connectionInfo
            return info.macAddress.toUpperCase()
        }

    }
}