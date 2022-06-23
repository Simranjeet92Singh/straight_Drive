package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.models.Match

class Schedule : Fragment(){
    private val INNING:String="match"
    private var match1Button:TextView?=null
    private var match2Button:TextView?=null
    private var match3Button:TextView?=null
    private var match4Button:TextView?=null
    private var match5Button:TextView?=null
    private var match6Button:TextView?=null
    private var match7Button:TextView?=null

    private val noOfOvers=BooleanArray(4)
    private val whichTeamPlaying=BooleanArray(2)
    private var pointsTable:TextView?=null
    private var match: Match? = null
    private var match1:TextView?=null
    private var match2:TextView?=null
    private var match3:TextView?=null
    private var match4:TextView?=null
    private var match5:TextView?=null
    private var match6:TextView?=null
    private var match7:TextView?=null
    private var linmatch5:LinearLayout?=null
    private var linmatch6:LinearLayout?=null
    private var linmatch7:LinearLayout?=null
    private var tv_match4:TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            match = it.getSerializable(INNING) as Match

        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.schedule, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as AppCompatActivity
        whichTeamPlaying[1]=true

        match1Button=view.findViewById(R.id.match1Button)
        match2Button=view.findViewById(R.id.match2Button)
        match3Button=view.findViewById(R.id.match3Button)
        match4Button=view.findViewById(R.id.match4Button)
        match5Button=view.findViewById(R.id.match5Button)
        match6Button=view.findViewById(R.id.match6Button)
        match7Button=view.findViewById(R.id.match7Button)

        pointsTable=view.findViewById(R.id.points_table)
        match1=view.findViewById(R.id.macth1)
        match2=view.findViewById(R.id.macth2)
        match3=view.findViewById(R.id.macth3)
        match4=view.findViewById(R.id.macth4)
        match5=view.findViewById(R.id.macth5)
        match6=view.findViewById(R.id.macth6)
        match7=view.findViewById(R.id.macth7)
        linmatch5=view.findViewById(R.id.linMatch5)
        linmatch6=view.findViewById(R.id.linMatch6)
        linmatch7=view.findViewById(R.id.linMatch7)
        tv_match4=view.findViewById(R.id.tv_match4)
        if(match?.teamCount==3){
            match1?.text="Team A Vs Team B"
            match2?.text="Team C Vs Team A"
            match3?.text="Team B Vs Team C"
            match4?.text="Top 1 Team Vs Top 2 Team"
            tv_match4?.text="Final      "
            linmatch5?.visibility=View.GONE
            linmatch6?.visibility=View.GONE
            linmatch7?.visibility=View.GONE

        }
        if(match?.teamCount==4){
            match1?.text="Team A Vs Team B"
            match2?.text="Team C Vs Team A"
            match3?.text="Team B Vs Team C"
            match4?.text="Team D Vs Team B"
            match5?.text="Team A Vs Team D"
            match6?.text="Team D Vs Team A"
            match7?.text="Top 1 Team Vs Top 2 Team"
        }

        pointsTable?.setOnClickListener{

            fragmentManager?.beginTransaction()?.replace(android.R.id.content, PointsTable.newInstance(null),"game")?.commit()

        }
        match1Button?.setOnClickListener{
            val teamA="Team A "
            val teamB="Team B"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

                match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }

        match2Button?.setOnClickListener{
            val teamA="Team C "
            val teamB="Team A"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }
        match3Button?.setOnClickListener{
            val teamA="Team B "
            val teamB="Team C"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }
        match4Button?.setOnClickListener{
            val teamA="Team D "
            val teamB="Team B"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }
        match5Button?.setOnClickListener{
            val teamA="Team A "
            val teamB="Team D"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }
        match6Button?.setOnClickListener{
            val teamA="Team D "
            val teamB="Team A"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }
        match7Button?.setOnClickListener{
            val teamA="teama"
            val teamB="teamb"



            val match=Match(
                team1 = teamA,
                team2 = teamB
            )

            match.firstBattingTeam=teamA



            if(noOfOvers[0])
            {
                match?.totalOvers=5.toDouble()
            }
            else if(noOfOvers[1])
            {
                match?.totalOvers=10.toDouble()

            }
            else if(noOfOvers[2])
            {
                match?.totalOvers=20.toDouble()

            }

            match?.matchDate="2021-25-2021"
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match),"game")?.commit()
        }



    }








    companion object {

        @JvmStatic
        fun newInstance(match: Match?):Schedule {
            if(match!=null)
            {
                return Schedule().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, match)
                    }
                }
            }
            else
            {
                val m= Match()
                return Schedule().apply {
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