package com.koolbots.straightdrive.GamePlayUtils

import android.util.Log
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.Util.UtilityFunctions
import com.koolbots.straightdrive.models.*
import kotlinx.coroutines.processNextEventInCurrentThread
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class GameController(var match : Match?,var tournamentModel: TournamentModel?) {
    private var score=0
    private var balls:Double?=(match?.totalOvers)
    private var curentBalls=0
    private var bowlerlerIndex=0
    private var decimalFormat=DecimalFormat("##.#")
    private var ballindex=0;
    private var stack:LinkedList<String>?= LinkedList()
    private var reduStack:LinkedList<String>?=LinkedList()
    private var batterOne=Batter()
    private var batterTwo=Batter()



    init {
        batterOne.name="Batter 1"
        batterTwo.name="Batter 2"
        balls=balls?.times(6)
        match?.inning1?.batters?.addLast(batterOne)
        match?.inning1?.batters?.addLast(batterTwo)
        Log.d("Console","Size  "+match?.inning1?.batters?.size)
        setUpFirstBatting()
        for(i in 1..5)
        {
            val b1=Bowler()
            b1.name="Bowler "+i
            match?.inning1?.bowlers?.add(b1)
            val b2=Bowler()
            b2.name="Bowler "+i
            match?.inning2?.bowlers?.add(b2)
        }

        stack?.addLast(SerializationToJson.fromMatch(match))
    //    match=match?.copy()

    }


    public fun setUpFirstBatting() {
        if (match?.team1.equals(match?.firstBattingTeam) ){
            match?.first_team_play = true
            match?.second_team_playing = false
        } else {
            val temp = match?.team1.toString()
            match?.team2 = temp
            match?.team1 = match?.firstBattingTeam
            match?.first_team_play = true
            match?.second_team_playing = false
            val tem=match?.inning1?.teamName?:""
            match?.inning1?.teamName=match?.inning2?.teamName
            match?.inning2?.teamName=temp




        }
    }

    public fun out( status:String?):Match?
    {
        match?.hatrick = match?.hatrick?.plus(1)?:0
        Log.d("Hatr",""+match?.hatrick)
        if(match?.hatrick==3)
        {
            SharedMusicData.hatrick=true
            match?.hatrick =0
        }

        if(match?.second_team_playing?:true==false)
        {
            val bat= match?.inning2?.bowlers
            bat?.getOrNull(bowlerlerIndex)?.wickets=bat?.getOrNull(bowlerlerIndex)?.wickets as Int+1


        }
        else
        {
            val bat= match?.inning1?.bowlers
            bat?.getOrNull(bowlerlerIndex)?.wickets=bat?.getOrNull(bowlerlerIndex)?.wickets as Int+1

        }
        match=scored(0,true,false)
        Log.d("Out","done")
        if(curentBalls%6==0)
        {
            match?.firstPlaying=!(match?.firstPlaying?:true)
        }
        if(match?.second_team_playing?:true==false)
        {

            val bat= match?.inning2?.bowlers

            match?.inning1?.wickets=match?.inning1?.wickets as Int+1
            val wickets= match?.inning1?.wickets as Int


            val out=match?.inning1?.batters
            val size=out?.size
            val second=out?.removeLast()
            val first=out?.removeLast()
            val b=Batter()
            b.name="Batter "+(size?.plus(1))
            if(match?.firstPlaying?:true==true)
            {
                if(curentBalls%6==0) {
                    first?.fallenTo = "Bowler " + (bowlerlerIndex )

                }else
                {
                    first?.fallenTo = "Bowler " + (bowlerlerIndex +1)

                }
                first?.howOut = status?:"Bowled"
                first?.status=status?:"Not Out"
                out?.addLast(first)
                out?.addLast(b)
                out?.addLast(second)

            }
            else
            {
                if(curentBalls%6==0) {
                    second?.fallenTo = "Bowler " + (bowlerlerIndex )
                }else
                {
                    second?.fallenTo = "Bowler " + (bowlerlerIndex +1)

                }
                second?.howOut = status?:"Bowled"
                second?.status=status?:"Not Out"
                out?.addLast(second)
                out?.addLast(first)
                out?.addLast(b)
            }
            if(wickets==10)
            {
                match?.second_team_playing=true
                initInning()
                return match
            }


        }
        else
        {
            val bat= match?.inning1?.bowlers
            match?.inning2?.wickets=match?.inning2?.wickets as Int+1
            val wickets= match?.inning2?.wickets as Int

            val out=match?.inning2?.batters
            val size=out?.size
            val second=out?.removeLast()
            val first=out?.removeLast()
            val b=Batter()
            b.name="Batter "+(size?.plus(1))
            if(match?.firstPlaying?:true==true)
            {
                if(curentBalls%6==0) {
                    first?.fallenTo = "Bowler " + (bowlerlerIndex )
                }else
                {
                    first?.fallenTo = "Bowler " + (bowlerlerIndex +1)

                }
                first?.howOut = status?:"Bowled"
                first?.status=status?:"Not Out"
                out?.addLast(first)
                out?.addLast(b)
                out?.addLast(second)


            }
            else
            {
                if(curentBalls%6==0) {
                    second?.fallenTo = "Bowler " + (bowlerlerIndex )
                }else
                {
                    second?.fallenTo = "Bowler " + (bowlerlerIndex +1)

                }
                first?.howOut = status?:"Bowled"
                second?.status=status?:"Not Out"
                out?.addLast(second)
                out?.addLast(first)
                out?.addLast(b)
            }
            if(wickets>=10)
            {
                return finish()
            }

        }
        if(curentBalls%6==0)
        {
            match?.firstPlaying=!(match?.firstPlaying?:true)
        }
        stack?.removeLast()
        stack?.addLast(SerializationToJson.fromMatch(match))

        return finish()
    }
    public fun scored(score:Int,increment_ball:Boolean,ifout:Boolean):Match?
    {
        this.score+=score
      if(score!=0)
      {
          match?.hatrick=0
      }
        if(match?.second_team_playing==true)
        {
            val bat= match?.inning1?.bowlers
            bat?.getOrNull(bowlerlerIndex)?.runs=(bat?.getOrNull(bowlerlerIndex)?.runs?:0) + score
            val batters=match?.inning2?.batters
            batters?:return match
            val size=batters?.size
            if(match?.firstPlaying?:true==true)
            {
                val previosscore=batters.get(size-2).score
                batters.get(size-2).score+=score
                batters.get(size-2).ballsFaced+=1
                if(previosscore<50&&batters.get(size-2).score>=50)
                {
                    SharedMusicData.half_Century=true
                }
                if(previosscore<100&&batters.get(size-2).score>=100)
                {
                    SharedMusicData.full_century=true
                }


            }
            else
            {

                val previosscore=batters.get(size-1).score
                batters.get(size-1).score+=score
                batters.get(size-1).ballsFaced+=1

                if(previosscore<50&&batters.get(size-1).score>=50)
                {
                    SharedMusicData.half_Century=true
                }
                if(previosscore<100&&batters.get(size-1).score>=100)
                {
                    SharedMusicData.full_century=true
                }

            }
            match?.inning2?.score= score+match?.inning2?.score as Int

        }
        else
        {
            val bat= match?.inning2?.bowlers
            bat?.get(bowlerlerIndex)?.runs=(bat?.get(bowlerlerIndex)?.runs?:0) + score
            val batters=match?.inning1?.batters
            batters?:return match
            val size=batters?.size
            if(match?.firstPlaying?:true==true)
            {

                val previosscore=batters.get(size-2).score
                batters.get(size-2).score+=score
                batters.get(size-2).ballsFaced+=1
                if(previosscore<50&&batters.get(size-2).score>=50)
                {
                    SharedMusicData.half_Century=true
                }
                if(previosscore<100&&batters.get(size-2).score>=100)
                {
                    SharedMusicData.full_century=true
                }
              //  batters.get(size-2).ballsFaced+=1


            }
            else
            {
                val previosscore=batters.get(size-1).score


                batters.get(size-1).score+=score
                batters.get(size-1).ballsFaced+=1
                if(previosscore<50&&batters.get(size-1).score>=50)
                {
                    SharedMusicData.half_Century=true
                }
                if(previosscore<100&&batters.get(size-1).score>=100)
                {
                    SharedMusicData.full_century=true
                }

            }
            match?.inning1?.score=score +match?.inning1?.score as Int

        }

        if(increment_ball)
        {
            incrementBall()
        }
        if(score==1 ||score==3)
        {
            match?.firstPlaying=match?.firstPlaying?.not()?:true
        }

        stack?.addLast(SerializationToJson.fromMatch(match))

        return finish()
    }
    public fun redo():Match?
    {
        if(reduStack==null||reduStack?.size?:0==0)
        {
            return match
        }
        curentBalls++
        if(curentBalls%6==0)
        {
            bowlerlerIndex++
        }
        val recent=reduStack?.pollLast()
        stack?.addLast(recent)
        match=SerializationToJson.toMatch(recent)
        return match
    }
    public fun emptyRedoStack()
    {
        reduStack?.clear()
    }
    public fun undo():Match?{
        Log.d("Undo","Stack Size "+stack?.size)
        if(stack==null||stack?.size?:0<=1)
        {
            Log.d("Undo","Not done")
            return match

        }

        if(curentBalls%6==0)
        {
            if(bowlerlerIndex>0)
            {
                bowlerlerIndex--;
            }

        }
//        if(ballindex>0)
//        {
//            SharedMusicData.wickets[bowlerlerIndex]=0
//            ballindex--
//        }
//        else
//        {
//            SharedMusicData.wickets[5]=0
//            ballindex=5
//        }
        val previous=stack?.pollLast()
        reduStack?.addLast(previous)
        val tempMatch=stack?.pollLast()
        stack?.addLast(tempMatch)
        val p=SerializationToJson.toMatch(previous)


        match= SerializationToJson.toMatch(tempMatch) as Match

        if(p?.second_team_playing==true&&match?.second_team_playing==false)
        {
            match?.second_team_playing=true
          curentBalls=balls?.minus(1)?.toInt() as  Int
        }
        else
        {

            curentBalls-=1
        }
        return match

    }

    private fun matchWon():Match? {
          val w=match?.inning2?.wickets?:0
        if((match?.first_team_play?:true&&match?.second_team_playing?:true&&w >=10 ))
        {
            if(match?.inning1?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team1
                match?.loosingTeam=match?.team2

                tournament(match?.winningTeam,match?.loosingTeam)

                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed


                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed

            }
            else if(match?.inning2?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team2
                match?.loosingTeam=match?.team1



                tournament(match?.winningTeam,match?.loosingTeam)

                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
            else{
                match?.winningTeam="Both Team "
                match?.loosingTeam="Both Team "
                bothTeamWon(match?.winningTeam,match?.loosingTeam)
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
        }
        else if((match?.first_team_play?:true&&match?.second_team_playing?:true&&(UtilityFunctions.overToBalls(match?.inning2?.overs?:0.0) ==balls?.toInt())))
        {


            if(match?.inning1?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team1
                match?.loosingTeam=match?.team2


                tournament(match?.winningTeam,match?.loosingTeam)
                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
            else if(match?.inning2?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team2
                match?.loosingTeam=match?.team1



                tournament(match?.winningTeam,match?.loosingTeam)

                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed

            }
            else{
                match?.winningTeam="Both Team "
                match?.loosingTeam="Both Team "
                bothTeamWon(match?.winningTeam,match?.loosingTeam)
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started
                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
        }
        else
        {
            Log.d("Out","x")

            if(match?.first_team_play?:false&&match?.second_team_playing?:false&&(match?.inning2?.score?:0)>(match?.inning1?.score?:0))
            {

                match?.winningTeam=match?.team2
                match?.loosingTeam=match?.team1



                tournament(match?.winningTeam,match?.loosingTeam)

                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started

                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }

            else if(match?.first_team_play?:true&&match?.second_team_playing?:true&&(w)>=10)

            {
                match?.winningTeam=match?.team1
                match?.loosingTeam=match?.team2


                tournament(match?.winningTeam,match?.loosingTeam)

                match?.pointsTableAJson=tournamentModel?.pointsTableAJson!!
                match?.pointsTableBJson=tournamentModel?.pointsTableBJson!!
                match?.pointsTableCJson=tournamentModel?.pointsTableCJson!!
                match?.pointsTableDJson=tournamentModel?.pointsTableDJson!!
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started
                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
            else if(match?.first_team_play?:false&&match?.second_team_playing?:false&&match?.inning2?.score?:0==match?.inning1?.score?:0&&match?.inning2?.wickets?:0>=10)
            {

                match?.winningTeam="Both Team "
                match?.loosingTeam="Both Team "
                bothTeamWon(match?.winningTeam,match?.loosingTeam)
                checkMatch()
                tournamentWon(match?.winningTeam)
                match?.isMatch1Started=tournamentModel?.isMatch1Started
                match?.isMatch2Started=tournamentModel?.isMatch2Started
                match?.isMatch3Started=tournamentModel?.isMatch3Started
                match?.isMatch4Started=tournamentModel?.isMatch4Started
                match?.isMatch5Started=tournamentModel?.isMatch5Started
                match?.isMatch6Started=tournamentModel?.isMatch6Started
                match?.isMatch7Started=tournamentModel?.isMatch7Started

                match?.isMatch5Completed=tournamentModel?.isMatch5Completed
                match?.isMatch6Completed=tournamentModel?.isMatch6Completed
                match?.isMatch7Completed=tournamentModel?.isMatch7Completed

                match?.isMatch1Completed=tournamentModel?.isMatch1Completed
                match?.isMatch2Completed=tournamentModel?.isMatch2Completed
                match?.isMatch3Completed=tournamentModel?.isMatch3Completed
                match?.isMatch4Completed=tournamentModel?.isMatch4Completed
            }
        }

        return match
    }

    fun checkMatch():TournamentModel{
        if(tournamentModel?.isMatch1Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =true
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =true
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =false



        }else if(tournamentModel?.isMatch2Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =true
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =true
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =false
        }else if(tournamentModel?.isMatch3Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =true

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =true
            tournamentModel?.isMatch4Completed =false
        }else if(tournamentModel?.isMatch4Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=true
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =true
        }else if(tournamentModel?.isMatch5Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=true
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =true
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =false
        }else if(tournamentModel?.isMatch6Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=true

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=true
            tournamentModel?.isMatch7Completed=false

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =false
        }else if(tournamentModel?.isMatch7Started!!){
            tournamentModel?.isMatch1Started = false
            tournamentModel?.isMatch2Started =false
            tournamentModel?.isMatch3Started =false
            tournamentModel?.isMatch4Started =false

            tournamentModel?.isMatch5Started=false
            tournamentModel?.isMatch6Started=false
            tournamentModel?.isMatch7Started=false

            tournamentModel?.isMatch5Completed =false
            tournamentModel?.isMatch6Completed=false
            tournamentModel?.isMatch7Completed=true

            tournamentModel?.isMatch1Completed =false
            tournamentModel?.isMatch2Completed =false
            tournamentModel?.isMatch3Completed =false
            tournamentModel?.isMatch4Completed =false
        }

        return tournamentModel!!
    }

    fun tournamentWon(wTeam:String?):TournamentModel{
        if(tournamentModel?.isMatch7Completed!!){

            tournamentModel?.tournamentWinnerName=wTeam
        }

        return tournamentModel!!
    }

        fun bothTeamWon(wTeam:String?,lTeam:String?):TournamentModel{
            if(wTeam == lTeam){
                val m =match?.team1
                val n =match?.team2
                matchBothWon(m,n)
            }
            return tournamentModel!!
        }

    fun matchBothWon(wTeam:String?,lTeam:String?):TournamentModel{
        var v :String?=null
        var w : String?=null

        if(wTeam == "Team A" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableAJson
            w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)
            return tournamentModel!!

        }else if(wTeam == "Team A" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableAJson
            w = tournamentModel?.pointsTableCJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team A" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableAJson
            w = tournamentModel?.pointsTableDJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)
            return tournamentModel!!

        }else if(wTeam == "Team B" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableAJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team B" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableCJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team B" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableDJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team C" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableAJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team C" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)
            return tournamentModel!!

        }else if(wTeam == "Team C" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableDJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team D" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableAJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)
            return tournamentModel!!

        }else if(wTeam == "Team D" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)

            return tournamentModel!!

        }else if(wTeam == "Team D" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableCJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+1
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+1
            q.nrr = j.nrr+t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)
            return tournamentModel!!
        }else{
            return tournamentModel!!
        }








    }
    fun tournament(wTeam:String?,lTeam:String?):TournamentModel{
                var v :String?=null
                var w : String?=null

        if(wTeam == "Team A" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableAJson
           w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team A" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableAJson
            w = tournamentModel?.pointsTableCJson
            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team A" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableAJson
            w = tournamentModel?.pointsTableDJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team B" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableAJson
            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team B" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableCJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team B" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableBJson
            w = tournamentModel?.pointsTableDJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team C" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableAJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team C" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team C" && lTeam =="Team D"){
            v = tournamentModel?.pointsTableCJson
            w = tournamentModel?.pointsTableDJson
            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team D" && lTeam =="Team A"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableAJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team D" && lTeam =="Team B"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableBJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else if(wTeam == "Team D" && lTeam =="Team C"){
            v = tournamentModel?.pointsTableDJson
            w = tournamentModel?.pointsTableCJson

            val p = PointsTableModel()
            val  q=PointsTableModel()
            val  j = SerializationToJson.toPointsTable(v)
            val k = SerializationToJson.toPointsTable(w)
            val t = ((match?.inning1?.score)!! /(((match?.inning1?.overs!!.toInt())*6)+(match?.inning1?.overs!!)%0.10))
            -((match?.inning2?.score)!! /(((match?.inning2?.overs!!.toInt())*6)+(match?.inning2?.overs!!)%0.10))


            p.teamName=wTeam!!
            p.points=k.points+2
            p.nrr=k.nrr+t

            q.teamName=lTeam!!
            q.points=j.points+0
            q.nrr = j.nrr-t

            v = SerializationToJson.fromPointsTable(p)
            w = SerializationToJson.fromPointsTable(q)



            return tournamentModel!!
        }else{
            return tournamentModel!!
        }







    }

    private fun incrementBall()
    {
        ballindex=(ballindex+1)%6
        if(match?.second_team_playing?:true==false)
        {

          var ov:Double=  match?.inning2?.bowlers?.getOrNull(bowlerlerIndex)?.overs as Double


            match?.inning2?.bowlers?.getOrNull(bowlerlerIndex)?.overs=ov+1
            var to=match?.inning1?.overs
            match?.inning1?.overs=decimalFormat.format(UtilityFunctions.ballsToOvers(UtilityFunctions.overToBalls(to)+1)).toDouble()

        }
        else
        {
            var to=match?.inning2?.overs
            match?.inning2?.overs=decimalFormat.format(UtilityFunctions.ballsToOvers(UtilityFunctions.overToBalls(to)+1)).toDouble()

            var ov:Double=  match?.inning1?.bowlers?.getOrNull(bowlerlerIndex)?.overs as Double

            match?.inning1?.bowlers?.getOrNull(bowlerlerIndex)?.overs=ov+1
        }
        curentBalls++
        if(curentBalls==balls?.toInt())
        {
            if(match?.second_team_playing?:true==true&& match?.first_team_play?:true==true)
            {
                finish()
                return
            }
            else
            {
               match?.first_team_play=true
                match?.second_team_playing=true
                match?.inning1?.score=score
                match?.inning1?.overs=curentBalls/6+curentBalls%6 .toDouble()


                initInning()
                finish()
                return

            }
        }
        if(checkHatric())
        {
            SharedMusicData.hatrick=true
            Arrays.fill(SharedMusicData.wickets,0)
        }
        if(curentBalls%6==0)
        {
            ballindex=0
            Arrays.fill(SharedMusicData.wickets,0)
            val  prev=bowlerlerIndex
            bowlerlerIndex=(bowlerlerIndex+1)%5
            match?.firstPlaying=!(match?.firstPlaying?:true)
            SharedMusicData?.over_finish=true
            match?.hatrick=0
        }
    }
    public fun checkHatric():Boolean
    {

        if(SharedMusicData.wickets[0]==1 && SharedMusicData.wickets[1]==1 && SharedMusicData.wickets[2]==1)
        {
            Arrays.fill(SharedMusicData.wickets,0)
            return true
        }
        if(SharedMusicData.wickets[1]==1 && SharedMusicData.wickets[2]==1 && SharedMusicData.wickets[3]==1)
        {
            Arrays.fill(SharedMusicData.wickets,0)
            return true
        }
        if(SharedMusicData.wickets[2]==1 && SharedMusicData.wickets[3]==1 && SharedMusicData.wickets[4]==1)
        {
            Arrays.fill(SharedMusicData.wickets,0)
            return true
        }
        if(SharedMusicData.wickets[3]==1 && SharedMusicData.wickets[4]==1 && SharedMusicData.wickets[5]==1)
        {
            Arrays.fill(SharedMusicData.wickets,0)
            return true
        }

        return false
    }
    public fun wide():Match?
    {
        this.score+=1
        match?.hatrick=0
        if(match?.second_team_playing?:true==false)
        {
            match?.inning1?.score=match?.inning1?.score as Int+1
            match?.inning2?.wides=match?.inning2?.wides as Int+1
                val bat= match?.inning2?.bowlers
                bat?.getOrNull(bowlerlerIndex)?.wides=bat?.getOrNull(bowlerlerIndex)?.wides as Int+1
               bat?.getOrNull(bowlerlerIndex)?.runs=bat?.getOrNull(bowlerlerIndex)?.runs as Int+1


        }
        else
        {
            match?.inning2?.score=match?.inning2?.score as Int+1
            match?.inning1?.wides=match?.inning1?.wides as Int+1

                val bat= match?.inning1?.bowlers
                bat?.getOrNull(bowlerlerIndex)?.wides=bat?.getOrNull(bowlerlerIndex)?.wides as Int+1
                 bat?.getOrNull(bowlerlerIndex)?.runs=bat?.getOrNull(bowlerlerIndex)?.runs as Int+1

        }
        stack?.addLast(SerializationToJson.fromMatch(match))
        return finish()
    }
    public fun leg():Match?
    {
        match?.hatrick=0
        this.score+=1

        if(match?.second_team_playing?:true==false)
        {
            match?.inning1?.score=(match?.inning1?.score as Int)+1
            match?.inning2?.legByes=(match?.inning2?.legByes as Int)+1
            val bat= match?.inning2?.bowlers
            bat?.getOrNull(bowlerlerIndex)?.legByes=bat?.getOrNull(bowlerlerIndex)?.legByes as Int+1
             bat?.getOrNull(bowlerlerIndex)?.runs=bat?.getOrNull(bowlerlerIndex)?.runs as Int+1


        }
        else
        {
            match?.inning2?.score=(match?.inning2?.score as Int)+1
            match?.inning1?.legByes=(match?.inning1?.legByes as Int)+1

            val bat= match?.inning1?.bowlers
            bat?.getOrNull(bowlerlerIndex)?.legByes=bat?.getOrNull(bowlerlerIndex)?.legByes as Int+1
            bat?.getOrNull(bowlerlerIndex)?.runs=bat?.getOrNull(bowlerlerIndex)?.runs as Int+1

        }
        incrementBall()
        stack?.addLast(SerializationToJson.fromMatch(match))
        return finish()
     //   return scored(0,true)
    }
    public fun four():Match?
    {
        match?.hatrick=0
        if(match?.second_team_playing?:false==true)
        {
            match?.inning2?.fours=match?.inning2?.fours as Int+1
            if(match?.firstPlaying?:true==true)
            {
               val bat= match?.inning2?.batters
                val s=bat?.size?:2
                bat?.getOrNull(s-2)?.fours=bat?.getOrNull(s-2)?.fours as Int+1
            }
            else
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:1
                bat?.getOrNull(s-1)?.fours=bat?.getOrNull(s-1)?.fours as Int+1

            }
        }
        else
        {
            match?.inning1?.fours= match?.inning1?.fours as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:2
                bat?.getOrNull(s-2)?.fours=bat?.getOrNull(s-2)?.fours as Int+1
            }
            else
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:1
                bat?.getOrNull(s-1)?.fours=bat?.getOrNull(s-1)?.fours as Int+1

            }
        }
      return   scored(4,true,false)
    }
    public fun six():Match?
    {
        match?.hatrick=0
        if(match?.second_team_playing?:false==true)
        {
            match?.inning2?.sixes=match?.inning2?.sixes as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:2
                bat?.getOrNull(s-2)?.sixes=bat?.getOrNull(s-2)?.sixes as Int+1
            }
            else
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:1
                bat?.getOrNull(s-1)?.sixes=bat?.getOrNull(s-1)?.sixes as Int+1

            }
        }
        else
        {
            match?.inning1?.sixes= match?.inning1?.sixes as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:2
                bat?.getOrNull(s-2)?.sixes=bat?.getOrNull(s-2)?.sixes as Int+1
            }
            else
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:1
                bat?.getOrNull(s-1)?.sixes=bat?.getOrNull(s-1)?.sixes as Int+1

            }
        }
        return   scored(6,true,false)
    }
    private fun initInning() {
        batterOne= Batter()
        batterOne.name="Batter 1"
        batterTwo= Batter()
        batterTwo.name="Batter 2"
        match?.inning2?.batters?.addLast(batterOne)
        match?.inning2?.batters?.addLast(batterTwo)
        match?.firstPlaying=true
        //match?.inning1?.score=score
        score=0
        curentBalls=0
        bowlerlerIndex=0
        ballindex=0
        match?.firstPlaying=true

        Arrays.fill(SharedMusicData.wickets,0)
        stack?.clear()
       stack?.addLast(SerializationToJson.fromMatch(match))
        reduStack?.clear()
        match?.hatrick=0


    }


    private fun finish() :Match?{
      return  matchWon()
    }

}
