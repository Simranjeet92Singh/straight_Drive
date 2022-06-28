package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.PointsTableModel
import com.koolbots.straightdrive.models.TournamentModel
import java.util.*

class StartNewTournamentFragment : Fragment() {
    private val INNING: String = "match"
    private var startNewTournament: TextView? = null
    private var tournamentName: EditText? = null
    private var t1name: TextView? = null
    private var t2name: TextView? = null
    private var t3name: TextView? = null
    private var t4name: TextView? = null
    private var fiveOvers: TextView? = null
    private var tenOvers: TextView? = null
    private var twentyOvers: TextView? = null
    private var threeTeam: TextView? = null
    private var fourTeam: TextView? = null
    private var match: Match? = null
    private var tv4: TextView? = null
    private val noOfOvers = BooleanArray(3)
    private val noOfTeams = BooleanArray(2)
    private var tournamentModel:TournamentModel?=null




    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {

            match = it.getSerializable(INNING) as Match
            Log.d("tourt button ========",match.toString())

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_new_tournament, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as AppCompatActivity
        ac ?: return
        ac.supportActionBar?.show()

        fiveOvers = view.findViewById(R.id.five_overs)
        tenOvers = view.findViewById(R.id.ten_overs)
        twentyOvers = view.findViewById(R.id.twenty_overs)
        tournamentName = view.findViewById(R.id.tName)
        t1name = view.findViewById(R.id.t1name)
        t2name = view.findViewById(R.id.t2name)
        t3name = view.findViewById(R.id.t3name)
        t4name = view.findViewById(R.id.t4name)
        threeTeam = view.findViewById(R.id.three)
        fourTeam = view.findViewById(R.id.four)
        tv4 = view.findViewById(R.id.tv4)
        tv4?.setTextColor(Color.GRAY)
        t4name?.setTextColor(Color.GRAY)
        t4name?.setBackgroundResource(R.drawable.rounded_sides_grey)
        t1name?.text = "Team A"
        t2name?.text = "Team B"
        t3name?.text = "Team C"
        t4name?.text = "Team D"

        startNewTournament = view.findViewById<TextView>(R.id.start_tournament)


        setOnClickListeners()
        if (match != null) {
            //No of Overs
            Arrays.fill(noOfOvers, false)
            if (match?.totalOvers?.toInt() ?: 5 == 5) {
                noOfOvers[0] = true
            } else if (match?.totalOvers?.toInt() == 10) {
                noOfOvers[1] = true
            } else if (match?.totalOvers?.toInt() == 20) {
                noOfOvers[2] = true
            } else {
                noOfOvers[0] = true

            }
            updateOversUI()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListeners() {

        startNewTournament?.setOnClickListener {

            val tournamentName=tournamentName?.text.toString()


            if(tournamentName==null||tournamentName=="")
            {
                Snackbar.make(it,"Please enter Tournament name",1500).show()
                return@setOnClickListener
            }
             match=Match(
                team1 = "teamA",
                team2 = "teamB"
            )
            tournamentModel= TournamentModel()

            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
             if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
             if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }
            match?.matchDate="2021-25-2021"


            if(noOfTeams[0]){
                tournamentModel?.teamCount=3
            }
            if(noOfTeams[1]){
                tournamentModel?.teamCount=4
            }
            val pointsTable=PointsTableModel()
            match?.isFromTournament=true
            tournamentModel?.tournamentName=tournamentName
            tournamentModel?.pointsTableAJson=SerializationToJson.fromPointsTable(pointsTable)
            tournamentModel?.pointsTableBJson=SerializationToJson.fromPointsTable(pointsTable)
            tournamentModel?.pointsTableCJson=SerializationToJson.fromPointsTable(pointsTable)
            tournamentModel?.pointsTableDJson=SerializationToJson.fromPointsTable(pointsTable)



            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, Schedule.newInstance(match,tournamentModel))
                ?.commit()

        }



        fiveOvers?.setOnClickListener(
            {
                Arrays.fill(noOfOvers, false)
                noOfOvers[0] = true
                updateOversUI()
            }
        )
        tenOvers?.setOnClickListener({
            Arrays.fill(noOfOvers, false)
            noOfOvers[1] = true
            updateOversUI()
        })
        twentyOvers?.setOnClickListener({
            Arrays.fill(noOfOvers, false)
            noOfOvers[2] = true
            updateOversUI()
        })

        threeTeam?.setOnClickListener(
            {
                Arrays.fill(noOfTeams, false)
                noOfTeams[0] = true
                updateteamUI()
                tv4?.setTextColor(Color.GRAY)
                t4name?.setTextColor(Color.GRAY)
                t4name?.setBackgroundResource(R.drawable.rounded_sides_grey)

            }
        )
        fourTeam?.setOnClickListener({
            Arrays.fill(noOfTeams, false)
            noOfTeams[1] = true
            updateteamUI()
            tv4?.setTextColor(Color.WHITE)
            t4name?.setTextColor(Color.BLACK)
            t4name?.setBackgroundResource(R.drawable.rounded_sides_white)

        })


    }

    private fun updateteamUI() {
        val context = activity?.applicationContext ?: return
        if (!isTablet(context)) {
            if(noOfTeams[0]){
                threeTeam?.setBackgroundResource(R.drawable.right_circular_background_blue_20)
                threeTeam?.setTextColor(Color.WHITE)
                fourTeam?.setBackgroundResource(R.drawable.right_circular_white_20)
                fourTeam?.setTextColor(Color.BLACK)
            }else if(noOfTeams[1]){
                threeTeam?.setBackgroundResource(R.drawable.left_circular_background_20)
                threeTeam?.setTextColor(Color.BLACK)
                fourTeam?.setBackgroundResource(R.drawable.left_circular_blue_20)
                fourTeam?.setTextColor(Color.WHITE)
            }else
            {
                threeTeam?.setBackgroundResource(R.drawable.left_circular_background_20)
                threeTeam?.setTextColor(Color.BLACK)
                fourTeam?.setBackgroundResource(R.drawable.right_circular_white_20)
                fourTeam?.setTextColor(Color.WHITE)
            }



        }else{
            if(noOfTeams[0]){
                threeTeam?.setBackgroundResource(R.drawable.left_circular_blue_30)
                threeTeam?.setTextColor(Color.WHITE)
                fourTeam?.setBackgroundResource(R.drawable.right_circular_white_30)
                fourTeam?.setTextColor(Color.BLACK)
            }else if(noOfTeams[1]){
                threeTeam?.setBackgroundResource(R.drawable.left_circular_white_30)
                threeTeam?.setTextColor(Color.BLACK)
                fourTeam?.setBackgroundResource(R.drawable.right_circular_blue_30)
                fourTeam?.setTextColor(Color.WHITE)
            }else
            {
                threeTeam?.setBackgroundResource(R.drawable.left_circular_white_30)
                threeTeam?.setTextColor(Color.BLACK)
                fourTeam?.setBackgroundResource(R.drawable.right_circular_white_30)
                fourTeam?.setTextColor(Color.WHITE)
            }

        }
    }

    private fun updateOversUI() {
        val context = activity?.applicationContext ?: return
        if (!isTablet(context)) {
            if (noOfOvers[0]) {
                fiveOvers?.setBackgroundResource(R.drawable.right_circular_background_blue_20)
                fiveOvers?.setTextColor(Color.WHITE)
                tenOvers?.setBackgroundResource(android.R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
                twentyOvers?.setTextColor(Color.BLACK)


            } else if (noOfOvers[1]) {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.primary_blue)
                tenOvers?.setTextColor(Color.WHITE)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
                twentyOvers?.setTextColor(Color.BLACK)

            } else if (noOfOvers[2]) {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.left_circular_blue_20)
                twentyOvers?.setTextColor(Color.WHITE)

            } else {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
                twentyOvers?.setTextColor(Color.BLACK)

            }
        } else {
            if (noOfOvers[0]) {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_blue_30)
                fiveOvers?.setTextColor(Color.WHITE)
                tenOvers?.setBackgroundResource(android.R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
                twentyOvers?.setTextColor(Color.BLACK)


            } else if (noOfOvers[1]) {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.primary_blue)
                tenOvers?.setTextColor(Color.WHITE)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
                twentyOvers?.setTextColor(Color.BLACK)
            } else if (noOfOvers[2]) {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_blue_30)
                twentyOvers?.setTextColor(Color.WHITE)

            } else {
                fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
                fiveOvers?.setTextColor(Color.BLACK)
                tenOvers?.setBackgroundResource(R.color.white)
                tenOvers?.setTextColor(Color.BLACK)
                twentyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
                twentyOvers?.setTextColor(Color.BLACK)

            }
        }
    }




    companion object {

        @JvmStatic
        fun newInstance(match: Match?):StartNewTournamentFragment {
            if(match!=null)
            {
                return StartNewTournamentFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, match)
                    }
                }
            }
            else
            {
                val m= Match()
                return StartNewTournamentFragment().apply {
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