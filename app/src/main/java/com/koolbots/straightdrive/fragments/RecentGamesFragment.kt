package com.koolbots.straightdrive.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.adapters.RecentGamesAdapter
import com.koolbots.straightdrive.database.quickmatch.GlobalDatabase
import com.koolbots.straightdrive.database.quickmatch.MatchAccessDAO
import com.koolbots.straightdrive.models.Match
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private var newGame:TextView?=null
private var recentGames:RecyclerView?=null




class RecentGamesFragment : Fragment() {
    private var display:Int=0
    private var mainLayout: LinearLayout?=null
    private var matches:ArrayList<Match>?= ArrayList()

    private var matchDAO: MatchAccessDAO?=null
    private val INNING:String="match"
    private val recentgamesList: ArrayList<Match>?=ArrayList()
    private var match:Match?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            matches= it.getSerializable(INNING) as ArrayList<Match>?
            match= it.getSerializable(INNING) as Match?
            Log.d("toment button ========",match.toString())

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

           return inflater.inflate(R.layout.fragment_recent_games, container, false)


    }

    override fun onPause() {
        super.onPause()

    }
    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as AppCompatActivity
        ac?:return
        ac.supportActionBar?.show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ac.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        Log.d("Console in Rcent", " " + SharedData.notchHeight)
        mainLayout=view.findViewById(R.id.main_layout)
        if(SharedData.notchHeight>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = (SharedData.notchHeight?:0)+15


        }

           recentgamesList?.clear()
        recentGames=view.findViewById(R.id.recent_games_list)
        val act=activity?:return
        newGame=view.findViewById(R.id.new_game)

        recentGames?.layoutManager=LinearLayoutManager(act.applicationContext)
        recentGames?.setHasFixedSize(false)

        GlobalScope.launch {
            matchDAO= GlobalDatabase.getInstance(act.applicationContext).matchAccessDAO()
           matches= matchDAO?.getAllMatch() as ArrayList<Match>
            Log.d("tournament button **********",matches.toString())

            //Log.d("Console","Matches found"+matches?.size)
            val cmp = compareBy<Match> { LocalDateTime.parse(it.matchDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            matches?.forEach(
                    {
                        var str=""
                        var y:Int?=0
                        var m:Int?=0
                        var d:Int?=0
                        var dates=it.matchDate?.split("-")
                        y=dates?.get(0)?.toInt()
                        m=dates?.get(1)?.toInt()
                        d=dates?.get(2)?.toInt()
                        if (m != null&&d!=null) {
                            it.matchDate=""+y+"-"+(if(m<=9){ "0"+m.toString() }else{ m.toString()+"" }+"-"+if(d<=9){ "0"+d.toString()}else{d.toString()})

                        }
                        Log.d("Date",it.matchDate?:"")
                    }

            )
            matches?.sortByDescending {it.matchDate  }



            MainScope().launch {

//                for (i in 0..matches?.size!!-1){


//                    if(matches?.get(i)?.isFromTournament==false && matches?.get(i)?.isFromSeries==false){
//                        val  t = matches?.get(i)
//                        recentgamesList?.add(i,t!!)


//                        recentgamesList?.get(i)?.team1=matches?.get(i)?.team1.toString()!!
//                        recentgamesList?.get(i)?.team2=matches?.get(i)?.team2.toString()!!
//                        recentgamesList?.get(i)?.matchDate=matches?.get(i)?.matchDate.toString()!!
//                        recentgamesList?.get(i)?.inning1Json=matches?.get(i)?.inning1Json.toString()!!
//                        recentgamesList?.get(i)?.inning2Json=matches?.get(i)?.inning2Json.toString()!!




//                    }else{
//                        val n=Match()
//
//
//                        val maxLogSize = 1000
//                        val stringLength = n.toString().length
//                        for (i in 0..stringLength / maxLogSize) {
//                            val start = i * maxLogSize
//                            var end = (i + 1) * maxLogSize
//                            end = if (end > n.toString().length) n.toString().length else end
//                            Log.v("----**------", n.toString().substring(start, end))
//                        }
//
//                        recentgamesList?.add(i,n!!)
//                    }
//                }

                    val recentGamesAdapter=RecentGamesAdapter(act.applicationContext, fragmentManager,matches)

                val maxLogSize = 1000
                val stringLength = recentgamesList.toString().length
                for (i in 0..stringLength / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > recentgamesList.toString().length) recentgamesList.toString().length else end
                    Log.v("----------------------------------", recentgamesList.toString().substring(start, end))
                }



                    recentGames?.adapter=recentGamesAdapter
                    Log.d("Console","Matches found  "+matches?.size)


                }




        }

        newGame?.setOnClickListener(

                {
                    activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, NewGameFragment.newInstance(match))?.addToBackStack(null)?.commit()

                }
        )



    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String,match: Match?) :
                RecentGamesFragment{
            return RecentGamesFragment().apply {
                arguments = Bundle().apply {

                    putSerializable(INNING, match)

                }
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }


}