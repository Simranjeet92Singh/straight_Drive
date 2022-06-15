package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val noOfOvers=BooleanArray(4)
    private val whichTeamPlaying=BooleanArray(2)
    private var pointsTable:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        noOfOvers[0]=true
        match1Button=view.findViewById(R.id.match1Button)
        pointsTable=view.findViewById(R.id.points_table)
        pointsTable?.setOnClickListener{

            fragmentManager?.beginTransaction()?.replace(android.R.id.content, PointsTable.newInstance(null),"game")?.commit()

        }
        match1Button?.setOnClickListener{
            val teamA="teama"
            val teamB="teamb"


            if(teamA==null||teamA=="")
            {
                Snackbar.make(it,"Please enter Team 1 name",1500).show()
                return@setOnClickListener
            }
            if(teamB==null||teamB=="")
            {
                Snackbar.make(it,"Please enter Team 2 name",1500).show()
                return@setOnClickListener
            }
            if(teamA.trim().toLowerCase().equals(teamB.trim().toLowerCase()))
            {
                Snackbar.make(it,"Please enter distinct  names of both the  teams",1500).show()
                return@setOnClickListener
            }
            val match=Match(
                team1 = teamA,
                team2 = teamB
            )
            if(whichTeamPlaying[0])
            {
                match.firstBattingTeam=teamA
            }
            else
            {
                match.firstBattingTeam=teamB
            }


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
            else
            {
                match?.totalOvers=50.toDouble()

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