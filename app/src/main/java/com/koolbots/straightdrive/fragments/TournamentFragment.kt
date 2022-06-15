package com.koolbots.straightdrive.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
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
import com.koolbots.straightdrive.database.quickmatch.MatchAccessDAO
import com.koolbots.straightdrive.database.tournament.TournamentDAO
import com.koolbots.straightdrive.database.tournament.TournamentDatabase
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TournamentFragment : Fragment(){

    private var mainLayout: LinearLayout?=null
    private var tournamentList:ArrayList<TournamentModel>?= ArrayList()
    private var tournamentDAO: TournamentDAO?=null
    private var tournament:RecyclerView?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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
        if(SharedData.notchHeight>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = (SharedData.notchHeight?:0)+15


        }
        tournament= view.findViewById(R.id.tournament_games_list)

        val act=activity?:return
        tournament?.layoutManager= LinearLayoutManager(act.applicationContext)
            tournament?.setHasFixedSize(true)
        GlobalScope.launch {
        tournamentDAO=TournamentDatabase.getInstance(act.applicationContext).tournamentDAO()

            tournamentList= tournamentDAO?.getAllTournament() as ArrayList<TournamentModel>

            tournamentList?.forEach(
                {
                    var str=""
                    var y:Int?=0
                    var m:Int?=0
                    var d:Int?=0
                    var dates=it.dateOfMatch?.split("-")
                    y=dates?.get(0)?.toInt()
                    m=dates?.get(1)?.toInt()
                    d=dates?.get(2)?.toInt()
                    if (m != null&&d!=null) {
                        it.dateOfMatch=""+y+"-"+(if(m<=9){ "0"+m.toString() }else{ m.toString()+"" }+"-"+if(d<=9){ "0"+d.toString()}else{d.toString()})

                    }
                    Log.d("Date",it.dateOfMatch?:"")
                }

            )
            tournamentList?.sortByDescending {it.dateOfMatch  }
            MainScope().launch {
                val tournamentAdapter=
                   TournamentAdapter(act.applicationContext, fragmentManager,tournamentList)
                tournament?.adapter=tournamentAdapter
                Log.d("Console","Matches found  "+tournamentList?.size)

        }

        }
        val newTournament=view.findViewById<TextView>(R.id.new_tournament)
        newTournament.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, StartNewTournamentFragment.newInstance(null))?.addToBackStack(null)?.commit()

        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TournamentFragment()
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

}