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
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel

class TournamentWonDashBoard : Fragment() {

    private var match: Match?=null
    private var tournamentModel: TournamentModel?=null


    private var matchWinnerName: TextView?=null
    private var backToMatches: TextView?=null
    private var topName: TextView?=null

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
        return inflater.inflate(R.layout.tournament_final_won, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        matchWinnerName=view.findViewById(R.id.matchWinnerName)
        backToMatches=view.findViewById(R.id.backtoMatches)
        topName=view.findViewById(R.id.topname)



        if(match?.isFromTournament==true){
            topName?.text="Tournament"
            matchWinnerName?.text=tournamentModel?.tournamentWinnerName
            backToMatches?.setOnClickListener({
                fragmentManager?.beginTransaction()?.replace(android.R.id.content, Schedule.newInstance(match, tournamentModel))?.commit()

            })
        }
        if(match?.isFromSeries==true){
            topName?.text="Series"
            matchWinnerName?.text=tournamentModel?.tournamentWinnerName
            backToMatches?.setOnClickListener({
                fragmentManager?.beginTransaction()?.replace(android.R.id.content, Schedule.newInstance(match, tournamentModel))?.commit()

            })
        }


    }






    companion object {


        fun newInstance(match: Match?, tournamentModel: TournamentModel?) = TournamentWonDashBoard().apply {
            arguments= Bundle().apply {
                putSerializable("match",match)
                putSerializable("t",tournamentModel)
            }
        }

    }}