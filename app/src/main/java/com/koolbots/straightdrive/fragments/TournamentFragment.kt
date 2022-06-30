package com.koolbots.straightdrive.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.koolbots.straightdrive.adapters.TournamentAdapter
import com.koolbots.straightdrive.database.quickmatch.GlobalDatabase
import com.koolbots.straightdrive.database.quickmatch.MatchAccessDAO
import com.koolbots.straightdrive.database.tournament.TournamentDAO
import com.koolbots.straightdrive.database.tournament.TournamentDb

import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TournamentFragment : Fragment(){

    private var mainLayout: LinearLayout?=null
    private var matches:ArrayList<Match>?= ArrayList()
    private var tournamentList:ArrayList<TournamentModel>? = ArrayList()
    private var match:Match?=null
    private var tournamentModel:TournamentModel?=null
    private var tournamentDAO: TournamentDAO?=null
    private var tournament:RecyclerView?=null

    private var font:TextView?=null
    private var newTournament:TextView?=null
    private val INNING:String="match"
    private val recentgamesList: ArrayList<TournamentModel>?=ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            match= it.getSerializable(INNING) as Match?
//          isFromSeries=it.getSerializable(fromSeies) as Boolean
//            isFromTournamnt=it.getSerializable(fromTournament) as Boolean
            Log.d("tout button ========",match.toString())

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tournament, container, false)

    }

    override fun onPause() {
        super.onPause()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as AppCompatActivity
        ac ?: return
        ac.supportActionBar?.show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ac.getWindow().getAttributes().layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        mainLayout=view.findViewById(R.id.main_layout)
        newTournament=view.findViewById(R.id.new_tournament)
        font=view.findViewById(R.id.font)
        if(SharedData.notchHeight>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = (SharedData.notchHeight?:0)+15


        }
        recentgamesList?.clear()
        tournament= view.findViewById(R.id.tournament_games_list)

        val act=activity?:return
        tournament?.layoutManager= LinearLayoutManager(act.applicationContext)
            tournament?.setHasFixedSize(true)

        GlobalScope.launch {
            tournamentDAO= TournamentDb.getInstance(act.applicationContext).tournamentDAO()
            tournamentList= tournamentDAO?.getAllMatch() as ArrayList<TournamentModel>
            //Log.d("Console","Matches found"+matches?.size)
//            val cmp = compareBy<Match> { LocalDateTime.parse(it., DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            tournamentList?.forEach(
                {
                    var str=""
                    var y:Int?=0
                    var m:Int?=0
                    var d:Int?=0
                    var dates=it.date?.split("-")
                    y=dates?.get(0)?.toInt()
                    m=dates?.get(1)?.toInt()
                    d=dates?.get(2)?.toInt()
                    if (m != null&&d!=null) {
                        it.date=""+y+"-"+(if(m<=9){ "0"+m.toString() }else{ m.toString()+"" }+"-"+if(d<=9){ "0"+d.toString()}else{d.toString()})

                    }
                    Log.d("Date",it.date?:"")
                }

            )
            tournamentList?.sortByDescending {it.date  }


            MainScope().launch {


                    val tournamentAdapter=
                        TournamentAdapter(act.applicationContext, fragmentManager,tournamentList)
                    tournament?.adapter=tournamentAdapter



                Log.d("Console","Matches found  "+matches?.size)

            }




        }

        val newTournament=view.findViewById<TextView>(R.id.new_tournament)

        if(match?.isFromSeries == true) {
        font?.setText("Series")
        newTournament?.setText("New Series")

        }

        newTournament.setOnClickListener{
                activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, StartNewTournamentFragment.newInstance(match))?.addToBackStack(null)?.commit()

            }



    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String,match:Match?) :TournamentFragment{


               return TournamentFragment().apply {
                   arguments = Bundle().apply {
//                       putSerializable(fromTournament, isFromTournament)
                       putSerializable(INNING, match)

                   }
               }


        }

    }






    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

}