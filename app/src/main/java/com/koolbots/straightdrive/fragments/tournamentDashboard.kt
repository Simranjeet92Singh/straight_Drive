package com.koolbots.straightdrive.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.Util.UtilityFunctions
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel

class tournamentDashboard : Fragment() {
    private var mainLayout: LinearLayout?=null
    private var teamAScoreCard: ImageView?=null
    private var teamBScoreCard: ImageView?=null
    private var match: Match?=null
    private var tournamentModel:TournamentModel?=null
    private var winnigTeamImage: ImageView?=null
    private var scoreA: TextView?=null
    private var scoreB: TextView?=null
    private var overA: TextView?=null
    private var overB: TextView?=null
    private var sixA: TextView?=null
    private var sixB: TextView?=null
    private var fourA: TextView?=null
    private var fourB: TextView?=null
    private var teamA: TextView?=null
    private var teamB: TextView?=null
    private var matchWinnerName:TextView?=null
    private var backToMatches:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            match=it.getSerializable("match") as Match
            tournamentModel=it.getSerializable("t") as TournamentModel
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)








    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tournament_match_won, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMatchStatusView(view)
        mainLayout=view.findViewById(R.id.main_layout)


        if (setUpCutout()) return


        val context=activity?.applicationContext?:return
        setUpWinningImageResource(context)
        setUpClickListeners()
        initializeMatchStatusView(view)
    }
    private fun initializeMatchStatusView(view: View) {
        winnigTeamImage=view?.findViewById(R.id.dashboard_winning_image)
        teamAScoreCard=view.findViewById(R.id.score_card_ta)
        teamBScoreCard=view.findViewById(R.id.score_card_tb)
        teamA = view.findViewById(R.id.teamAName)
        scoreA = view.findViewById(R.id.teamAScore)
        overA = view.findViewById(R.id.teamAOvers)
        fourA = view.findViewById(R.id.teamAFours)
        sixA = view.findViewById(R.id.teamASixes)
        teamB = view.findViewById(R.id.teamBName)
        scoreB = view.findViewById(R.id.teamBScore)
        overB = view.findViewById(R.id.teamBOvers)
        fourB = view.findViewById(R.id.teamBFours)
        sixB = view.findViewById(R.id.teamBSixes)
        teamA?.text = match?.team1
        teamB?.text = match?.team2
        matchWinnerName=view.findViewById(R.id.matchWinnerName)
        backToMatches=view.findViewById(R.id.backtoMatches)
        Log.d("Json",match?.inning1Json?:" Not available")
        val inn1= SerializationToJson.toInning(match?.inning1Json)
        val inn2= SerializationToJson.toInning(match?.inning2Json)

        val sc1=((inn1.score?.toString()?:"")+"/"+(inn1.wickets?.toString()?:""))
        scoreA?.text = sc1
        scoreB?.text = ((inn2.score?.toString()?:"")+"/"+(inn2.wickets?.toString()?:""))
        overA?.text = inn1.overs?.toString()?:""
        overB?.text = inn2.overs?.toString()?:""
        fourA?.text = inn1.fours?.toString()?:""
        fourB?.text =  inn2.fours?.toString()?:""
        sixA?.text = inn1.sixes?.toString()?:""
        sixB?.text = inn2.sixes?.toString()?:""

    }

    private fun setUpClickListeners() {
        val inn1= SerializationToJson.toInning(match?.inning1Json)?:return
        val inn2= SerializationToJson.toInning(match?.inning2Json)?:return
        inn1.batters?.sortedWith(compareBy({ it.name }))
        inn2.batters?.sortedWith(compareBy({ it.name }))
        teamAScoreCard?.setOnClickListener({


            fragmentManager?.beginTransaction()?.replace(android.R.id.content, ScoreCardFragment.newInstance(inn1,inn2))?.addToBackStack(null)?.commit()
        })
        teamBScoreCard?.setOnClickListener({
            val inn= SerializationToJson.toInning(match?.inning2Json)
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, ScoreCardFragment.newInstance(inn2,inn1))?.addToBackStack(null)?.commit()

        })
    }

    private fun setUpWinningImageResource(context: Context) {
       if(match?.isFromTournament==true){
           matchWinnerName?.text=match?.winningTeam
           backToMatches?.setOnClickListener({
               if(tournamentModel?.key == 0){

                       val m=Match()
                       m.isFromTournament=true
                       m.isFromSeries=false
                       fragmentManager?.beginTransaction()?.replace(android.R.id.content, TournamentFragment.newInstance("","",m))?.commit()

                   }

               else{
                   fragmentManager?.beginTransaction()?.replace(android.R.id.content, Schedule.newInstance(match, tournamentModel))?.commit()

               }

           })
       }
        if(match?.isFromSeries==true){
            matchWinnerName?.text=match?.winningTeam
            backToMatches?.setOnClickListener({
                if(tournamentModel?.key ==0){
                val m=Match()
                m.isFromTournament=false
                m.isFromSeries=true

                fragmentManager?.beginTransaction()?.replace(android.R.id.content, TournamentFragment.newInstance("","",m))?.commit()

            }else{
                    fragmentManager?.beginTransaction()?.replace(android.R.id.content, Schedule.newInstance(match, tournamentModel))?.commit()

                }

            })
        }
    }

    private fun setUpCutout(): Boolean {
        if (SharedData.notchHeight ?: 0 > 0) {
            val layout = mainLayout ?: null
            if (layout == null)
                return true
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = SharedData.notchHeight ?: 0


        }
        return false
    }

    companion object {


        fun newInstance(match: Match?,tournamentModel: TournamentModel?) = tournamentDashboard().apply {
            arguments= Bundle().apply {
                putSerializable("match",match)
                putSerializable("t",tournamentModel)
            }
        }

    }}