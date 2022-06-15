package com.koolbots.straightdrive

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.fragments.GamePlayFragment
import com.koolbots.straightdrive.fragments.LandinngScreenFragment

class MainActivity : AppCompatActivity() ,androidx.core.view.OnApplyWindowInsetsListener{
    private var fragmentManager:FragmentManager?=null
    private var fragmentTransaction:FragmentTransaction?=null
    private var customActionBar:View?=null
    private var backButton:ImageView?=null
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statusBarHeight=getStatusBarHeight()
        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setDisplayShowCustomEnabled(true);
        supportActionBar?.setCustomView(R.layout.custom_action_bar);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_pointing_left)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        customActionBar=supportActionBar?.customView
        if(statusBarHeight>0)
        {
            val params: ViewGroup.MarginLayoutParams = customActionBar?.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = statusBarHeight
        }
        else
        {
            val params: ViewGroup.MarginLayoutParams = customActionBar?.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 25
        }
        backButton=customActionBar?.findViewById(R.id.back_arrow)
        window?.decorView?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        var view=layoutInflater?.inflate(R.layout.custom_action_bar, null)
        fragmentManager=supportFragmentManager
        fragmentTransaction=fragmentManager?.beginTransaction()
        var landinngScreenFragment:LandinngScreenFragment=LandinngScreenFragment.newInstance("", "")
        fragmentTransaction?.replace(android.R.id.content, landinngScreenFragment)?.commit()
        backButton?.setOnClickListener({
            fragmentManager?.popBackStack()
        })
    }
    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }
    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }
    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    override fun onApplyWindowInsets(v: View?, insets: WindowInsetsCompat?): WindowInsetsCompat {
        getCutOutSize()
      return  insets!!
    }
    private fun getCutOutSize(){
        val activity = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val insets=activity?.window?.decorView?.rootWindowInsets
            val displayCutout=insets?.displayCutout
            if (displayCutout!=null) {
                val displayCutoutHeightPx = displayCutout.safeInsetLeft
               if(true||displayCutoutHeightPx>0)
               {
                   SharedData.notchHeight=displayCutoutHeightPx
               }
                Log.d(


                        "NotchSupport",
                        "Set the Height-> ${displayCutout.safeInsetLeft} and savedValue-> "
                )
            }else {
                Log.d("NotchSupport", "DisplayCut out is null so set the height to 0")
            }
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onBackPressed() {

        if(fragmentManager?.backStackEntryCount==0)
        super.onBackPressed()

        if(false&&fragmentManager?.backStackEntryCount==0)
        {
            showExitConfirmation()
        }
        else
        {
            super.onBackPressed()
        }
         var frag=fragmentManager?.findFragmentByTag("game")
        if(frag!=null)
        {
            frag=frag as GamePlayFragment
            frag?.resetMusic()
        }

    }

    private fun showExitConfirmation() {
        val view = LayoutInflater.from(this).inflate(R.layout.cutom_confirmation_dialogue, null)
        val alertDialog: Dialog = Dialog(this)
        alertDialog.setContentView(view)
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yes = alertDialog.findViewById<TextView>(R.id.yes)
        val no = alertDialog.findViewById<TextView>(R.id.no)
        yes?.setOnClickListener({
            alertDialog.dismiss()
            finish()
        })
        no?.setOnClickListener({
            alertDialog.dismiss()
        })
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onPause() {
        super.onPause()
        if(fragmentManager?.backStackEntryCount?:2==1)
        {
           // fragmentManager?.popBackStack()
        }
        var frag=fragmentManager?.findFragmentByTag("game")
        if(frag!=null)
        {
            frag=frag as GamePlayFragment
            frag?.resetMusic()
        }
    }


}