package com.koolbots.straightdrive.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.koolbots.straightdrive.GamePlayUtils.GameController
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.Util.UtilityFunctions
import com.koolbots.straightdrive.database.quickmatch.GlobalDatabase
import com.koolbots.straightdrive.database.quickmatch.MatchAccessDAO
import com.koolbots.straightdrive.database.tournament.TournamentDAO
import com.koolbots.straightdrive.database.tournament.TournamentDb
import com.koolbots.straightdrive.models.*
import com.koolbots.straightdrive.networks.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class GamePlayFragment : Fragment(),View.OnClickListener {
    private var mediaPlayer:MediaPlayer?=null
    private var one_:MediaPlayer?=null
    private var two_:MediaPlayer?=null
    private var three_:MediaPlayer?=null
    private var four_:MediaPlayer?=null
    private var six_:MediaPlayer?=null
    private var bold_:MediaPlayer?=null
    private var caught_:MediaPlayer?=null
    private var centuary_:MediaPlayer?=null
    private var half_centuary_:MediaPlayer?=null
    private var inning_change:MediaPlayer?=null
    private var hattrick_:MediaPlayer?=null
    private var leg_:MediaPlayer?=null
    private var match_end_:MediaPlayer?=null
    private var over_change_:MediaPlayer?=null
    private var wide_:MediaPlayer?=null
    private var date=System.currentTimeMillis()
    private var mainLayout: LinearLayout?=null
    private var match:Match?=null


    private var scoreA:TextView?=null
    private var scoreB:TextView?=null
    private var overA:TextView?=null
    private var overB:TextView?=null
    private var sixA:TextView?=null
    private var sixB:TextView?=null
    private var fourA:TextView?=null
    private var fourB:TextView?=null
    private var teamA:TextView?=null
    private var teamB:TextView?=null
    private var zero:CardView?=null
    private var one:CardView?=null
    private var two:CardView?=null
    private var three:CardView?=null
    private var four:CardView?=null
    private var six:CardView?=null
    private var wide:CardView?=null
    private var leg:CardView?=null
    private var caught:CardView?=null
    private var bowled:CardView?=null
    private var undo:CardView?=null
    private var redu:CardView?=null
    private var gameController:GameController?=null
    private var matchDAO: MatchAccessDAO?=null
    private var torunamentDAO:TournamentDAO?=null
    private var clickable=true
    private var scoreCardA:ImageView?=null
    private var scoreCardB:ImageView?=null
    private var secondInning=false
    private var batter_a:TextView?=null
    private var batter_b:TextView?=null
     private var music:LinkedList<MediaPlayer> = LinkedList<MediaPlayer>()
    private var handler=Handler()
    private var tournamentModel:TournamentModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer=MediaPlayer.create(activity,R.raw.music_file)
        one_=MediaPlayer.create(activity,R.raw.one)
        two_=MediaPlayer.create(activity,R.raw.two)
        three_=MediaPlayer.create(activity,R.raw.three)
        four_=MediaPlayer.create(activity,R.raw.four)
        six_=MediaPlayer.create(activity,R.raw.six)
        bold_=MediaPlayer.create(activity,R.raw.bowled)
        caught_=MediaPlayer.create(activity,R.raw.catch_out)
        centuary_=MediaPlayer.create(activity,R.raw.century)
        half_centuary_=MediaPlayer.create(activity,R.raw.half_century_and_claps)
        inning_change=MediaPlayer.create(activity,R.raw.inning_change)
        hattrick_=MediaPlayer.create(activity,R.raw.its_a_hattrick)
        leg_=MediaPlayer.create(activity,R.raw.leg_bye)
        match_end_=MediaPlayer.create(activity,R.raw.match_end)
        over_change_=MediaPlayer.create(activity,R.raw.over_change)
        wide_=MediaPlayer.create(activity,R.raw.wide)
//        one_?.prepare()
//        two_?.prepare()
//        three_?.prepare()
//        four_?.prepare()
//        six_?.prepare()
//        bold_?.prepare()
//        caught_?.prepare()
//        centuary_?.prepare()
//        half_centuary_?.prepare()
//        inning_change?.prepare()
//        hattrick_?.prepare()
//        leg_?.prepare()
//        match_end_?.prepare()
//        over_change_?.prepare()
//        wide_?.prepare()
        arguments?.let {
                match=it.getSerializable("match") as Match
                tournamentModel=it.getSerializable("t") as TournamentModel
            Log.d("tt button ========",match.toString())

        }
        Log.d("Console team Playing",""+match?.firstBattingTeam)
        gameController= GameController(match,tournamentModel)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_game_play, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    private fun seekToZero(player:MediaPlayer?)
    {
        if(player?.isPlaying==true)
        {

           player?.pause()
        }
    }
    public fun resetMusic()
    {
        seekToZero(one_)
        seekToZero(two_)
        seekToZero(three_)
        seekToZero(four_)
        seekToZero(six_)
        seekToZero(bold_)
        seekToZero(caught_)
        seekToZero(centuary_)
        seekToZero(half_centuary_)
        seekToZero(inning_change)
        seekToZero(hattrick_)
        seekToZero(leg_)
        seekToZero(match_end_)
        seekToZero(over_change_)
        seekToZero(wide_)

    }
    fun setBatterText(score:Int?,balls:Int?,textView:TextView?,onBat:Boolean)
    {
        var s=score?.toString()
        var balls="("+balls?.toString()+")"
        val ss = SpannableStringBuilder()
                .bold { append(s) }
                .append(balls)
        if(onBat)
        {
            ss.append("*")
        }
        textView?.setText(ss)


    }

  //  @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      batter_a=view.findViewById(R.id.batter_a_text)
      batter_b=view.findViewById(R.id.batter_b_text)
      setBatterText(0,0,batter_a,true)
      setBatterText(0,0,batter_b,false)
      mainLayout=view.findViewById(R.id.main_layout)
        if(SharedData.notchHeight>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = (SharedData.notchHeight?:0)
        }

    //  val formatted: Date = SimpleDateFormat("yyyy-MM-dd").parse(today.g)


      val date = Date()
      val calendar: Calendar = GregorianCalendar()
      calendar.time = date
      val forDate=calendar.get(Calendar.YEAR).toString()+"-"+calendar.get(Calendar.MONTH).plus(1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)

         match?.matchDate= forDate
        initializeMatchStatusView(view)
        initGamePlayUI(view)
        zero?.setOnClickListener(this)
        one?.setOnClickListener(this)
        two?.setOnClickListener(this)
        three?.setOnClickListener(this)
        four?.setOnClickListener(this)
        six?.setOnClickListener(this)
        wide?.setOnClickListener(this)
        leg?.setOnClickListener(this)
        caught?.setOnClickListener(this)
        bowled?.setOnClickListener(this)
        undo?.setOnClickListener(this)
        redu?.setOnClickListener(this)
        setUpScoreCardClickListeners()



    }
    private fun setUpScoreCardClickListeners() {

        scoreCardA?.setOnClickListener({
            val m=SerializationToJson.toMatch(SerializationToJson.fromMatch(match))

            // val inn=SerializationToJson.toInning(match?.inning1Json)
            m?.inning1?.bowlers?.forEach() {
                it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())
            }
            m?.inning2?.bowlers?.forEach() {
                it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())        }
            m?.inning2?.bowlersJson=SerializationToJson.fromBowlers(m?.inning2?.bowlers)
            m?.inning1?.battersJson=SerializationToJson.fromBatters(m?.inning1?.batters)
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, ScoreCardFragment.newInstance(m?.inning1,m?.inning2))?.addToBackStack(null)?.commit()
        })
        scoreCardB?.setOnClickListener({
            val m=SerializationToJson.toMatch(SerializationToJson.fromMatch(match))
            m?.inning1?.bowlers?.forEach() {
                it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())
            }
            m?.inning2?.bowlers?.forEach() {
               it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())
                //
                }

            m?.inning1?.bowlersJson=SerializationToJson.fromBowlers(m?.inning1?.bowlers)
            m?.inning2?.battersJson=SerializationToJson.fromBatters(m?.inning2?.batters)
            //val inn=SerializationToJson.toInning(match?.inning2Json)
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, ScoreCardFragment.newInstance(m?.inning2,m?.inning1))?.addToBackStack(null)?.commit()

        })
    }


    private fun initGamePlayUI(view: View) {
        scoreCardA=view.findViewById(R.id.score_card_ta)
        scoreCardB=view.findViewById(R.id.score_card_tb)
        zero = view.findViewById(R.id.gamePlay_zero)
        one = view.findViewById(R.id.gamePlay_one)
        two = view.findViewById(R.id.gamePlay_two)
        three = view.findViewById(R.id.gamePlay_three)
        four = view.findViewById(R.id.gamePlay_four)
        six=view.findViewById(R.id.gamePlay_six)
        wide = view.findViewById(R.id.gamePlay_wide)
        leg = view.findViewById(R.id.gamePlay_Leg)
        undo = view.findViewById(R.id.gamePlay_undo)
        redu = view.findViewById(R.id.gamePlay_redu)
        caught = view.findViewById(R.id.gamePlay_caught)
        bowled = view.findViewById(R.id.gamePlay_bowled)
    }

    private fun initializeMatchStatusView(view: View) {
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
        scoreA?.text = "0"
        scoreB?.text = "0"
        overA?.text = "-"
        overB?.text = "-"
        fourA?.text = "0"
        fourB?.text = "0"
        sixA?.text = "0"
        sixB?.text = "0"
    }

    companion object {

        @JvmStatic
        fun newInstance(match: Match,tournamentModel: TournamentModel?) :GamePlayFragment{
            if(tournamentModel != null){

                return  GamePlayFragment().apply {
                    arguments=Bundle().apply {
                        putSerializable("match",match)
                        putSerializable("t",tournamentModel)
                    }
                }
            }else {
                val t = TournamentModel()
                return GamePlayFragment().apply {
                    arguments=Bundle().apply {
                        putSerializable("match",match)
                        putSerializable("t",t)
                    }
                }

            }


        }





    }


    fun stopMusic()
 {
    //music.clear()
 }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        if(match?.winningTeam!="")
        {
            return
        }
        if(!clickable)
        {
            return
        }
        handler.removeCallbacksAndMessages(null)
        stopMusic()
        clickable=false
        Handler().postDelayed({
            clickable=true
        }, 50L)
        when(v?.id)
        {

           R.id.gamePlay_zero->
           {
               //playMusic()

              // one_?.start()
               gameController?.emptyRedoStack()
               match=gameController?.scored(0,true,false)
               updateUI(match)

           }
            R.id.gamePlay_one->
            {


                handler.post({
                    one_?.seekTo(0);
                    one_?.start()
                })



                gameController?.emptyRedoStack()
                match=gameController?.scored(1,true, false )
                updateUI(match)

            }
            R.id.gamePlay_two->
            {

                handler.post({
                    two_?.seekTo(0);
                    two_?.start()
                })


                gameController?.emptyRedoStack()
                 match=gameController?.scored(2,true, false)
                updateUI(match)

            }
            R.id.gamePlay_three->
            {
                handler.post({
                    three_?.seekTo(0);
                    three_?.start()
                })

                gameController?.emptyRedoStack()
                 match=gameController?.scored(3,true,false)
                updateUI(match)

            }
            R.id.gamePlay_four->
            {

                handler.post({
                    four_?.seekTo(0);
                    four_?.start()
                })



                gameController?.emptyRedoStack()
                match=gameController?.four()
                updateUI(match)

            }
            R.id.gamePlay_six->
            {

                handler.post({
                    six_?.seekTo(0);
                    six_?.start()
                })


                gameController?.emptyRedoStack()
                match=gameController?.six()
                updateUI(match)

            }
            R.id.gamePlay_wide->
            {
                handler.post({
                    wide_?.seekTo(0);
                    wide_?.start()
                })


                gameController?.emptyRedoStack()
                match=gameController?.wide()
                updateUI(match)

            }
            R.id.gamePlay_Leg->
            {

                handler.post({
                    leg_?.seekTo(0);
                    leg_?.start()
                })


                gameController?.emptyRedoStack()
                match=gameController?.leg()
                updateUI(match)

            }
            R.id.gamePlay_caught->
            {

                handler.post({
                    caught_?.seekTo(0);

                    caught_?.start()
                })

                val tdate=System.currentTimeMillis()
                if(tdate-date>5)
                {
                    date=tdate
                    gameController?.emptyRedoStack()
                    match=gameController?.out("Caught")
                    updateUI(match)
                }

            }
            R.id.gamePlay_bowled->
            {

                handler.post({
                    bold_?.seekTo(0);
                    bold_?.start()
                })
                val tdate=System.currentTimeMillis()
                if(tdate-date>5) {
                    date=tdate
                    gameController?.emptyRedoStack()
                    match = gameController?.out("Bowled")
                    updateUI(match)
                }


            }
            R.id.gamePlay_undo->
            {
               // resetMusic()
               match= gameController?.undo()
                updateUI(match)

            }
            R.id.gamePlay_redu->
            {
               // resetMusic()
                match= gameController?.redo()
                updateUI(match)
            }

        }

    }
    //@RequiresApi(Build.VERSION_CODES.O)
    fun updateDataBase()
    {
        val act=activity?:return
        val deviceInf=DeviceInfo()
        deviceInf.app_version="1.0"
        deviceInf.id= Settings.Secure.getString(getContext()?.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        val apiCall=ApiCallModel()
        val date = Date()
        val calendar: Calendar = GregorianCalendar()
        calendar.time = date
        //  val formatted: Date = SimpleDateFormat("yyyy-MM-dd").parse(today.g)
        val forDate=calendar.get(Calendar.YEAR).toString()+"-"+if(calendar.get(Calendar.MONTH)<9){"0"+calendar.get(Calendar.MONTH).plus(1)}else{calendar.get(Calendar.MONTH).plus(1)}+"-"+if(calendar.get(Calendar.DAY_OF_MONTH)<=9){"0"+calendar.get(Calendar.DAY_OF_MONTH)}else{calendar.get(Calendar.DAY_OF_MONTH)}+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)
        apiCall.current_timestamp=forDate
        match?.matchDate=forDate
       val xx=calendar.get(Calendar.YEAR).toString()+"-"+if(calendar.get(Calendar.MONTH)<9){"0"+calendar.get(Calendar.MONTH).plus(1)}else{calendar.get(Calendar.MONTH).plus(1)}+"-"+if(calendar.get(Calendar.DAY_OF_MONTH)<=9){"0"+calendar.get(Calendar.DAY_OF_MONTH)}else{calendar.get(Calendar.DAY_OF_MONTH)}
        var ind:Int=-1
        for(i in match?.inning1?.batters?.indices?:0..0)
        {
            if( match?.inning1?.batters?.elementAtOrNull(i)?.name.equals("Batter 12"))
            {

            }
        }
        if(ind!=-1) {
            match?.inning1?.batters?.removeAt(ind)
        }
        ind=-1
        for(i in match?.inning2?.batters?.indices?:0..0)
        {
            if( match?.inning2?.batters?.get(i)?.name.equals("Batter 12"))
            {
                ind=i

            }
        }
        if(ind!=-1) {
            match?.inning2?.batters?.removeAt(ind)        }
        ind=-1
        apiCall.device_info=deviceInf
        apiCall.match=match


      //  match?.inning1?.batters?.removeLast()
      // match?.inning2?.batters?.removeLast()

        match?.inning2?.batters?.forEach(
            {
                Log.d("Json", it.name?:"")

            }
        )
        Log.d("Json ",Gson().toJson(apiCall) )
        val apiService=ApiService()
        apiService.callApi(apiCall){
            Toast.makeText(act,"Match uploaded",Toast.LENGTH_LONG).show()
        }
        match?.matchDate=xx


        GlobalScope.launch {
            match?.inning1?.bowlers?.forEach() {
                //it.overs=UtilityFunctions.overToBalls(it.overs).toDouble()
            }
            match?.inning2?.bowlers?.forEach() {
             //   it.overs=UtilityFunctions.overToBalls(it.overs).toDouble()
                   }
            match?.inning1?.battersJson=SerializationToJson.fromBatters(match?.inning1?.batters)
            match?.inning2?.battersJson=SerializationToJson.fromBatters(match?.inning2?.batters)

            match?.inning1?.bowlersJson=SerializationToJson.fromBowlers(match?.inning1?.bowlers)
            match?.inning2?.bowlersJson=SerializationToJson.fromBowlers(match?.inning2?.bowlers)

            match?.inning1Json=SerializationToJson.fromInning(match?.inning1)
            match?.inning2Json=SerializationToJson.fromInning(match?.inning2)
            matchDAO= GlobalDatabase.getInstance(act.applicationContext).matchAccessDAO()
            matchDAO?.addMatch(match)

            tournamentModel?.pointsTableAJson=match?.pointsTableAJson!!
            tournamentModel?.pointsTableBJson=match?.pointsTableBJson!!
            tournamentModel?.pointsTableCJson=match?.pointsTableCJson!!
            tournamentModel?.pointsTableDJson=match?.pointsTableDJson!!
            tournamentModel?.date=xx
           tournamentModel?.isMatch1Completed=    match?.isMatch1Completed
            tournamentModel?.isMatch2Completed=    match?.isMatch2Completed
            tournamentModel?.isMatch3Completed=    match?.isMatch3Completed
            tournamentModel?.isMatch4Completed=    match?.isMatch4Completed

            tournamentModel?.isMatch5Completed=    match?.isMatch5Completed
            tournamentModel?.isMatch6Completed=    match?.isMatch6Completed
            tournamentModel?.isMatch7Completed=    match?.isMatch7Completed
            tournamentModel?.isMatch5Started = match?.isMatch5Started
            tournamentModel?.isMatch6Started = match?.isMatch6Started
            tournamentModel?.isMatch7Started = match?.isMatch7Started

            tournamentModel?.isMatch1Started = match?.isMatch1Started
           tournamentModel?.isMatch2Started =  match?.isMatch2Started
            tournamentModel?.isMatch3Started =  match?.isMatch3Started
            tournamentModel?.isMatch4Started =  match?.isMatch4Started

            tournamentModel?.tournamentWinnerName=match?.tournamentWinnerName

            Log.d("**-*-*-*-*",tournamentModel.toString())
            torunamentDAO=TournamentDb.getInstance(act.applicationContext).tournamentDAO()
            torunamentDAO?.addTournament(tournamentModel)

        }



    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUI(mat: Match?) {
        val m=mat?:return


        if(SharedMusicData.full_century)
        {

            centuary_?.seekTo(0)
            handler.postDelayed({
                centuary_?.seekTo(0);
                centuary_?.start()
            },1500)


        }
         if(SharedMusicData?.half_Century)
        {

            handler.postDelayed({
                half_centuary_?.seekTo(0);
                half_centuary_?.start()
            },1500)
        }
        if(SharedMusicData?.hatrick)
        {

            centuary_?.seekTo(0)
            handler.postDelayed({
                hattrick_?.seekTo(0);
                hattrick_?.start()
            },500)
        }
        if(SharedMusicData.over_finish)
        {
            handler.postDelayed({
                over_change_?.seekTo(0);
                over_change_?.start()
            },500)
        }

        SharedMusicData.clear()

      /*  if(m?.inning1?.batters?.size?:0>11)
        {
            //m?.inning1?.batters?.removeLast()
        }*/
        if(m?.inning2?.batters?.size?:0>11)
        {
            //m?.inning2?.batters?.removeLast()
        }

        if(!secondInning&&match?.second_team_playing?:true)
        {
            secondInning=true
            handler.postDelayed({
                inning_change?.seekTo(0);
                inning_change?.start()
            },1000)
            inningFinishDialogue()

        }
        if(m?.second_team_playing)
        {
           val s=m?.inning2?.batters?.size?:0
            val b2=m?.inning2?.batters?.get(s-1)
            val b1=m?.inning2?.batters?.get(s-2)

            if(m?.firstPlaying==true)
            {
                setBatterText(b1?.score?:0,b1?.ballsFaced?:0,batter_a,true)
                setBatterText(b2?.score?:0,b2?.ballsFaced?:0,batter_b,false)
            }
            else
            {
                setBatterText(b1?.score?:0,b1?.ballsFaced?:0,batter_a,false)
                setBatterText(b2?.score?:0,b2?.ballsFaced?:0,batter_b,true)

            }



        }
        else
        {

            val s=m?.inning1?.batters?.size?:0
            val b2=m?.inning1?.batters?.get(s-1)
            val b1=m?.inning1?.batters?.get(s-2)
           if(m.firstPlaying)
           {
               setBatterText(b1?.score?:0,b1?.ballsFaced?:0,batter_a,true)
               setBatterText(b2?.score?:0,b2?.ballsFaced?:0,batter_b,false)
           }
            else
           {
               setBatterText(b1?.score?:0,b1?.ballsFaced?:0,batter_a,false)
               setBatterText(b2?.score?:0,b2?.ballsFaced?:0,batter_b,true)
           }
        }

        scoreA?.text=m.inning1?.score.toString()+"/"+m.inning1?.wickets.toString()
        scoreB?.text=m.inning2?.score.toString()+"/"+m.inning2?.wickets.toString()
        overA?.text=m.inning1?.overs?.toString()
        overB?.text=m.inning2?.overs?.toString()
        fourA?.text=m.inning1?.fours.toString()
        sixA?.text=m.inning1?.sixes.toString()
        fourB?.text=m.inning2?.fours.toString()
        sixB?.text=m.inning2?.sixes.toString()
        if(m?.winningTeam!="")
        {
            handler.postDelayed({
                match_end_?.seekTo(0);
                match_end_?.start()
            },0)
            wonDialogue()
            match?.inning1?.bowlers?.forEach() {
                it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())
            }
            match?.inning2?.bowlers?.forEach() {
                it.overs=UtilityFunctions.ballsToOvers(it.overs.toInt())        }
            updateDataBase()

        }



    }
    private fun wonDialogue() {
        //resetMusic()
      //  match_end_?.seekTo(0)
       // match_end_?.start()
       // resetMusic()
        //match_end_?.seekTo(0)

        val view = LayoutInflater.from(activity).inflate(R.layout.game_win_dialogue, null)
        val act=activity?:return
        val alertDialog: Dialog = Dialog(activity!!)
        alertDialog.setContentView(view)
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val winning=alertDialog.findViewById<TextView>(R.id.game_won_team)
        val yes = alertDialog.findViewById<TextView>(R.id.yes)
        val no = alertDialog.findViewById<TextView>(R.id.no)
        val image=alertDialog.findViewById<ImageView>(R.id.winning_team)

        setUpWinningImageResource(act,image)
        yes?.setOnClickListener({
            match= Match()
            match_end_?.stop()
            alertDialog.dismiss()
            activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, NewGameFragment.newInstance(null))?.commit()

        })
        if(match?.winningTeam.equals("Both Team"))

        {
            winning?.setText("Match Draw")
        }else
        {
            winning?.setText("Congratulations "+match?.winningTeam)
        }
        no?.setOnClickListener({
            match_end_?.stop()
            alertDialog.dismiss()
            if(match?.isFromTournament == true && match?.isFromSeries ==false){
                activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, TournamentFragment.newInstance("","",match))?.commit()

            }else if(match?.isFromTournament ==false && match?.isFromSeries == true){
                activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, RecentGamesFragment.newInstance("","",match))?.commit()

            }else {
                activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content, RecentGamesFragment.newInstance("","",match))?.commit()

            }

            val maxLogSize = 1000
            val stringLength = match.toString().length
            for (i in 0..stringLength / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > match.toString().length) match.toString().length else end
                Log.v("----**------", match.toString().substring(start, end))
            }
        })


        alertDialog.setCancelable(false)
        alertDialog.show()

    }
    private fun inningFinishDialogue() {
        val view = LayoutInflater.from(activity).inflate(R.layout.inning_finish_dialog, null)
        val act=activity?:return
        val alertDialog: Dialog = Dialog(activity!!)
        alertDialog.setContentView(view)
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val winning=alertDialog.findViewById<TextView>(R.id.game_won_team)
        val yes = alertDialog.findViewById<TextView>(R.id.yes)
        val no = alertDialog.findViewById<TextView>(R.id.no)
        yes.text="OK"
        yes?.setOnClickListener({

            alertDialog.dismiss()

        })
        no?.setOnClickListener({
            alertDialog.dismiss()
            fragmentManager?.popBackStack()
        })

        no?.visibility=View.GONE
        Arrays.fill(SharedMusicData.wickets,0)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        if(match?.winningTeam==null||match?.winningTeam?.isEmpty()==true)
        {
             updateUI(match)
    }}
    private fun setUpWinningImageResource(context: Context,winnigTeamImage:ImageView?) {
        if (UtilityFunctions.isTablet(context)) {
            if (match?.winningTeam?.equals(match?.team1) ?: true) {
                winnigTeamImage?.setImageResource(R.drawable.team_a_win_tablet)
            } else if (match?.winningTeam?.equals(match?.team2) ?: true) {
                winnigTeamImage?.setImageResource(R.drawable.team_b_win_tablet)

            }
            else
            {
                winnigTeamImage?.setImageResource(R.drawable.winning_team_draw_tablet)

            }
        } else {
            if (match?.winningTeam?.equals(match?.team1) ?: true) {
                winnigTeamImage?.setImageResource(R.drawable.team_a_win_phone)
            } else if (match?.winningTeam?.equals(match?.team2) ?: true){
                winnigTeamImage?.setImageResource(R.drawable.team_b_win_phone)

            }else
            {
                winnigTeamImage?.setImageResource(R.drawable.winning_team_draw_phone)

            }
        }
    }



    fun fromMatchToJson(match: ApiCallModel?):String
    {
        var exlusionStratgy=object : ExclusionStrategy
        {
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                if ( f?.getName()
                                .equals("inning1Json") ||
                        f?.getName()
                                .equals("inning2Json") ||
                        f?.getName()
                                .equals("first_team_play") ||
                        f?.getName()
                                .equals("second_team_playing") ||
                        f?.getName()
                                .equals("firstPlaying")
                ) {
                    return true
                }
                else{
                    return false
                }
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {


                return false
            }

        }

        val gson= GsonBuilder().setExclusionStrategies(exlusionStratgy).create()
        return gson.toJson(match)
    }

    override fun onPause() {
        stopMusic()
        super.onPause()

    }


}

