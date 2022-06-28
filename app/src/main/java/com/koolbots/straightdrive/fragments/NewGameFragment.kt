package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
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
import com.koolbots.straightdrive.models.Inning
import com.koolbots.straightdrive.models.Match
import kotlinx.coroutines.selects.select
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class NewGameFragment : Fragment() {
    private val INNING:String="match"
    private var teamOne:EditText?=null
    private var teamTwo:EditText?=null
    private var startGame:TextView?=null
    private var fiveOvers:TextView?=null
    private var tenOvers:TextView?=null
    private var twentyOvers:TextView?=null
    private var fiftyOvers:TextView?=null
    private var firstTeamPlaying:TextView?=null
    private var secondTeamPlaying:TextView?=null
    private val noOfOvers=BooleanArray(4)
    private val whichTeamPlaying=BooleanArray(2)
    private var match:Match?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

                     match=it.getSerializable(INNING) as Match

        }
        noOfOvers[0]=true
        whichTeamPlaying[0]=true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_new_game, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as AppCompatActivity
        ac?:return
        ac.supportActionBar?.show()
        fiveOvers=view.findViewById(R.id.five_overs)
        tenOvers=view.findViewById(R.id.ten_overs)
        twentyOvers=view.findViewById(R.id.twenty_overs)
        fiftyOvers=view.findViewById(R.id.fifty_overs)
        firstTeamPlaying=view.findViewById(R.id.first_team_playing)
        secondTeamPlaying=view.findViewById(R.id.second_team_playing)
        startGame=view.findViewById(R.id.start_game)
        teamOne=view.findViewById(R.id.team1)
        teamTwo=view.findViewById(R.id.team2)
        setOnClickListeners()
        if(match!=null)
        {
            teamOne?.setText(match?.team1)
            teamTwo?.setText(match?.team2)
            if(firstTeamPlaying==teamOne)
            {
                whichTeamPlaying[0]=true
            }
            else
            {
                whichTeamPlaying[1]=true
            }
            updateTeamsUI()
            Arrays.fill(noOfOvers,false)
            if(match?.totalOvers?.toInt()?:5==5)
            {
                noOfOvers[0]=true
            }
            else if(match?.totalOvers?.toInt()==10)
            {
                noOfOvers[1]=true
            }
            else if(match?.totalOvers?.toInt()==20)
            {
                noOfOvers[2]=true
            }
            else if(match?.totalOvers?.toInt()==50)
            {
                noOfOvers[3]=true

            }
            else
            {
                noOfOvers[0]=true

            }
            updateOversUI()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListeners(){
        startGame?.setOnClickListener({
            val teamA=teamOne?.text.toString()
            val teamB=teamTwo?.text.toString()


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
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, GamePlayFragment.newInstance(match,null),"game")?.commit()
        })
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
        fiftyOvers?.setOnClickListener({
            Arrays.fill(noOfOvers, false)
            noOfOvers[3] = true
            updateOversUI()
        })
        firstTeamPlaying?.setOnClickListener({
            Arrays.fill(whichTeamPlaying, false)
            whichTeamPlaying[0] = true
            updateTeamsUI()
        })
        secondTeamPlaying?.setOnClickListener({
            Arrays.fill(whichTeamPlaying, false)
            whichTeamPlaying[1] = true
            updateTeamsUI()
        })
    }

    private fun updateTeamsUI() {
        val context=activity?.applicationContext?:return
        if(!isTablet(context))
        {
            if(whichTeamPlaying[0]==true)
        {
            firstTeamPlaying?.setTextColor(Color.WHITE)
            secondTeamPlaying?.setTextColor(Color.BLACK)

            firstTeamPlaying?.setBackgroundResource(R.drawable.right_circular_background_blue_20)
           secondTeamPlaying?.setBackgroundResource(R.drawable.right_circular_white_20)
        }
        else
        {
            firstTeamPlaying?.setTextColor(Color.BLACK)
            secondTeamPlaying?.setTextColor(Color.WHITE)
            firstTeamPlaying?.setBackgroundResource(R.drawable.left_circular_background_20)
            secondTeamPlaying?.setBackgroundResource(R.drawable.left_circular_blue_20)
        }
        }
        else
        {
            if(whichTeamPlaying[0]==true)
        {
            firstTeamPlaying?.setTextColor(Color.WHITE)
            secondTeamPlaying?.setTextColor(Color.BLACK)

            firstTeamPlaying?.setBackgroundResource(R.drawable.left_circular_blue_30)
           secondTeamPlaying?.setBackgroundResource(R.drawable.right_circular_white_30)
        }
        else
        {
            firstTeamPlaying?.setTextColor(Color.BLACK)
            secondTeamPlaying?.setTextColor(Color.WHITE)
            firstTeamPlaying?.setBackgroundResource(R.drawable.left_circular_white_30)
            secondTeamPlaying?.setBackgroundResource(R.drawable.right_circular_blue_30)
        }
        }
    }

    private fun updateOversUI() {
        val context=activity?.applicationContext?:return
       if(!isTablet(context))
       {
           if(noOfOvers[0])
           {
               fiveOvers?.setBackgroundResource(R.drawable.right_circular_background_blue_20)
               fiveOvers?.setTextColor(Color.WHITE)
               tenOvers?.setBackgroundResource(android.R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(android.R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
               fiftyOvers?.setTextColor(Color.BLACK)


           }
           else if(noOfOvers[1])
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.primary_blue)
               tenOvers?.setTextColor(Color.WHITE)
               twentyOvers?.setBackgroundResource(android.R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
               fiftyOvers?.setTextColor(Color.BLACK)
           }
           else if(noOfOvers[2])
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(R.color.primary_blue)
               twentyOvers?.setTextColor(Color.WHITE)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_20)
               fiftyOvers?.setTextColor(Color.BLACK)
           }
           else
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_background_20)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.left_circular_blue_20)
               fiftyOvers?.setTextColor(Color.WHITE)

           }
       }
        else
       {
           if(noOfOvers[0])
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_blue_30)
               fiveOvers?.setTextColor(Color.WHITE)
               tenOvers?.setBackgroundResource(android.R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(android.R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
               fiftyOvers?.setTextColor(Color.BLACK)


           }
           else if(noOfOvers[1])
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.primary_blue)
               tenOvers?.setTextColor(Color.WHITE)
               twentyOvers?.setBackgroundResource(android.R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
               fiftyOvers?.setTextColor(Color.BLACK)
           }
           else if(noOfOvers[2])
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(R.color.primary_blue)
               twentyOvers?.setTextColor(Color.WHITE)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_white_30)
               fiftyOvers?.setTextColor(Color.BLACK)
           }
           else
           {
               fiveOvers?.setBackgroundResource(R.drawable.left_circular_white_30)
               fiveOvers?.setTextColor(Color.BLACK)
               tenOvers?.setBackgroundResource(R.color.white)
               tenOvers?.setTextColor(Color.BLACK)
               twentyOvers?.setBackgroundResource(R.color.white)
               twentyOvers?.setTextColor(Color.BLACK)
               fiftyOvers?.setBackgroundResource(R.drawable.right_circular_blue_30)
               fiftyOvers?.setTextColor(Color.WHITE)

           }
       }
    }


    companion object {

        @JvmStatic
        fun newInstance(match: Match?):NewGameFragment {
           if(match!=null)
           {
               return NewGameFragment().apply {
                   arguments = Bundle().apply {
                       putSerializable(INNING, match)
                   }
               }
           }
            else
           {
               val m=Match()
               return NewGameFragment().apply {
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