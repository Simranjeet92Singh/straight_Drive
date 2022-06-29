package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.koolbots.straightdrive.models.TournamentModel

class Schedule : Fragment(){
    private val INNING:String="match"
    private val TOURNAMENT:String="tournament"
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
    private var tournamentModel:TournamentModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            match = it.getSerializable(INNING) as Match
            tournamentModel=it.getSerializable(TOURNAMENT) as TournamentModel

            Log.d("tment button ========",match.toString())

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

        toPlayMatch()
        forViewingMatch()
       if(match?.isFromSeries==false) {


           if (tournamentModel?.teamCount == 3) {
               match1?.text = "Team A Vs Team B"
               match2?.text = "Team C Vs Team A"
               match3?.text = "Team B Vs Team C"
               match4?.text = "Top 1 Team Vs Top 2 Team"
               tv_match4?.text = "Final      "
               linmatch5?.visibility = View.GONE
               linmatch6?.visibility = View.GONE
               linmatch7?.visibility = View.GONE

           }
           if (tournamentModel?.teamCount == 4) {
               match1?.text = "Team A Vs Team B"
               match2?.text = "Team C Vs Team A"
               match3?.text = "Team B Vs Team C"
               match4?.text = "Team D Vs Team B"
               match5?.text = "Team A Vs Team D"
               match6?.text = "Team D Vs Team A"
               match7?.text = "Top 1 Team Vs Top 2 Team"
           }

           pointsTable?.setOnClickListener {

               fragmentManager?.beginTransaction()
                   ?.replace(android.R.id.content, PointsTable.newInstance(null), "game")?.commit()

           }


       }



      else{

//sereis


       }






    }

    fun toPlayMatch(){
        if(match?.isFromSeries==false){
            if (tournamentModel?.isMatch1Started == true)
            {
                match1Button?.text="Play"
                match1Button?.setOnClickListener{
                    val teamA="Team A "
                    val teamB="Team B"



                var newmatch=Match(
                    team1 = teamA,
                    team2 = teamB
                )
                    match?.isFromTournament=true
                    match?.firstBattingTeam=teamA



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

                    newmatch=match!!

                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch!!,tournamentModel!!),"game")?.commit()
                }
            }else if (match?.isMatch2Started!!)
                {
                    match1Button?.text="Play"
                    match2Button?.setOnClickListener{
                        val teamA="Team C "
                        val teamB="Team A"



                        var newmatch=Match(
                            team1 = teamA,
                            team2 = teamB
                        )

                        match?.firstBattingTeam=teamA



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

                        newmatch=match!!
                        fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch!!,tournamentModel!!),"game")?.commit()
                    }

                }
            else if (tournamentModel?.isMatch3Started == true)
            {
                match3Button?.text="Play"
                match3Button?.setOnClickListener{
                    val teamA="Team B "
                    val teamB="Team C"



                    var newmatch=Match(
                        team1 = teamA,
                        team2 = teamB
                    )

                    match?.firstBattingTeam=teamA



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
                    newmatch=match!!
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match!!,tournamentModel!!),"game")?.commit()
                }

            }

            else if (tournamentModel?.isMatch4Started == true)
            {
                match4Button?.text="Play"
                match4Button?.setOnClickListener{
                    val teamA="Team D "
                    val teamB="Team B"



                    var newmatch=Match(
                        team1 = teamA,
                        team2 = teamB
                    )

                    match?.firstBattingTeam=teamA



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
                    newmatch=match!!
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch,tournamentModel!!),"game")?.commit()
                }

            }else if (tournamentModel?.isMatch5Started == true)
            {
                match5Button?.text="Play"
                match5Button?.setOnClickListener{
                    val teamA="Team A "
                    val teamB="Team D"



                    var newmatch=Match(
                        team1 = teamA,
                        team2 = teamB
                    )

                    match?.firstBattingTeam=teamA



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
                    newmatch=match!!
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch,tournamentModel!!),"game")?.commit()
                }
            }

            else if (tournamentModel?.isMatch6Started == true)
            {
                match6Button?.text="Play"
                match6Button?.setOnClickListener{
                    val teamA="Team D "
                    val teamB="Team A"



                    var newmatch=Match(
                        team1 = teamA,
                        team2 = teamB
                    )

                    match?.firstBattingTeam=teamA



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
                    newmatch=match!!
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch,tournamentModel!!),"game")?.commit()
                }
            }
            else if (tournamentModel?.isMatch7Started == true)
            {
                match7Button?.text="Play"
                match7Button?.setOnClickListener{
                    val teamA="Team A"
                    val teamB="Team B"



                    var newmatch=Match(
                        team1 = teamA,
                        team2 = teamB
                    )

                    match?.firstBattingTeam=teamA



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
                    newmatch=match!!
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(newmatch,tournamentModel!!),"game")?.commit()
                }
            }

        }


        else{
            //series
        }
    }

    fun forViewingMatch(){
    if(match?.isFromSeries==false){
        if(match?.isMatch1Completed!!){
            match1Button?.text="View"

        }else if (match?.isMatch2Completed!!)
        {
            match2Button?.text = "View"
        }else if (match?.isMatch3Completed!!)
        {
            match3Button?.text = "View"
        }else if (match?.isMatch7Completed!!)
        {
            match7Button?.text = "View"
        }else if (match?.isMatch4Completed!!)
        {
            match4Button?.text = "View"
        }else if (match?.isMatch5Completed!!)
        {
            match5Button?.text = "View"
        }else if (match?.isMatch6Completed!!)
        {
            match6Button?.text = "View"
        }

    }else{
        //Series

    }



    }


    companion object {

        @JvmStatic
        fun newInstance(match: Match?,tournamentModel: TournamentModel?):Schedule {
            if(match!=null)
            {
                return Schedule().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, match)
                        putSerializable(TOURNAMENT,tournamentModel)
                    }
                }
            }else if (match == null){
                val m=Match()
                return Schedule().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, m)
                        putSerializable(TOURNAMENT,tournamentModel)
                    }
                }
            }
            else
            {
                val m= Match()
                val n=TournamentModel()
                return Schedule().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, m)

                        putSerializable(TOURNAMENT,n)
                    }
                }
            }
        }

    }

    fun isTablet(mContext: Context): Boolean {
        return mContext.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}