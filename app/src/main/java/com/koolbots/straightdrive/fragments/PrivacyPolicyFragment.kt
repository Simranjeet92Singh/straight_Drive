package com.koolbots.straightdrive.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SharedData


class PrivacyPolicyFragment : Fragment(),View.OnClickListener {
    private var cancelButton: TextView?=null
    private var acceptButton: TextView?=null
    private var mainLayout:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainLayout=view.findViewById(R.id.main_layout)
        val act=activity as AppCompatActivity
        act.supportActionBar?.show()
        val ss = SpannableString(resources.getString(R.string.data_retention_policy))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val uri: Uri = Uri.parse("https://www.koolbots.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                act.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.color=Color.BLUE
            }
        }
        val concm: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                composeEmail()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.color=Color.BLUE
            }
        }
        var str=resources.getString(R.string.data_retention_policy)

        ss.setSpan(clickableSpan, str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(android.graphics.Typeface.BOLD), str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


         val privacy = SpannableString(resources.getString(R.string.privacy_policy_text))
        val pricacyclick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val uri: Uri = Uri.parse("https://www.koolbots.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                act.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.color=Color.BLUE
            }
        }
         str=resources.getString(R.string.privacy_policy_text)

        privacy.setSpan(pricacyclick, str.length-24, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        privacy.setSpan(StyleSpan(android.graphics.Typeface.BOLD), str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val privacy_textview=view.findViewById<TextView>(R.id.privacy_policy_text)
        privacy_textview.setText(privacy, TextView.BufferType.SPANNABLE);
        privacy_textview.setMovementMethod(LinkMovementMethod.getInstance());
        privacy_textview.setHighlightColor(Color.TRANSPARENT);
        val cons = SpannableString(resources.getString(R.string.contact))
        val concl: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val uri: Uri = Uri.parse("https://www.koolbots.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                act.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.color=Color.BLUE
            }
        }
        str=resources.getString(R.string.contact)

        cons.setSpan(clickableSpan, str.length-76, str.length-52,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        cons.setSpan(concm, str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickableSpan, str.length-72, str.length-52,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(android.graphics.Typeface.BOLD), str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val cont=view.findViewById<TextView>(R.id.con)
        cont.setText(cons, TextView.BufferType.SPANNABLE);
        cont.setMovementMethod(LinkMovementMethod.getInstance());
        cont.setHighlightColor(Color.GREEN);




        val data_retendion = SpannableString(resources.getString(R.string.data_retention_policy))

        str=resources.getString(R.string.data_retention_policy)

        data_retendion.setSpan(concm, str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val data=view.findViewById<TextView>(R.id.retension)
        data.setText(data_retendion, TextView.BufferType.SPANNABLE);
        data.setMovementMethod(LinkMovementMethod.getInstance());
        data.setHighlightColor(Color.GREEN);


        if(SharedData.notchHeight>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = (SharedData.notchHeight?:0)+15
        }


        //ss.setSpan(ForegroundColorSpan(Color.BLUE),str.length-20, str.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)




        cancelButton?.setOnClickListener(this)
        acceptButton?.setOnClickListener(this)
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PrivacyPolicyFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    override fun onClick(v: View?) {

    }

    override fun onPause() {
        super.onPause()
        val act=activity as AppCompatActivity
        act.supportActionBar?.hide()
    }
    fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "Support@koolbots.com ")
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        activity?.startActivity(intent)
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {

        }
    }
}