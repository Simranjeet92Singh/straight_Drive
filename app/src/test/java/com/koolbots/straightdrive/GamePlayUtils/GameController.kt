package com.koolbots.straightdrive.GamePlayUtils

import android.util.Log
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.Util.UtilityFunctions
import com.koolbots.straightdrive.models.Batter
import com.koolbots.straightdrive.models.Bowler
import com.koolbots.straightdrive.models.Match
import java.text.DecimalFormat
import java.util.*

class GameController(var match : Match?) {

    private var batterOne=Batter()
    private var batterTwo=Batter()


    private var score=0
    private var balls:Double?=(match?.totalOvers)
    private var curentBalls=0
    private var bowlerlerIndex=0
    private var decimalFormat=DecimalFormat("##.#")

    private var stack:LinkedList<String>?= LinkedList()
    private var reduStack:LinkedList<String>?=LinkedList()
    init {
        batterOne.name="Batter 1"
        batterTwo.name="Batter 2"
        balls=balls?.times(6)
        match?.inning1?.batters?.addLast(batterOne)
        match?.inning1?.batters?.addLast(batterTwo)
        Log.d("Console","Size of batter "+match?.inning1?.batters?.size)
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
            match?.inning2?.teamName=tem




        }
    }

    public fun out( status:String?):Match?
    {
        if(match?.second_team_playing?:true==false)
        {
            val bat= match?.inning2?.bowlers
            bat?.get(bowlerlerIndex)?.wickets=bat?.get(bowlerlerIndex)?.wickets as Int+1


        }
        else
        {
            val bat= match?.inning1?.bowlers
            bat?.get(bowlerlerIndex)?.wickets=bat?.get(bowlerlerIndex)?.wickets as Int+1

        }
        match=scored(0,true)
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
            b.name="Batter "+size
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
            if(wickets==10)
            {

            }
            val out=match?.inning2?.batters
            val size=out?.size
            val second=out?.removeLast()
            val first=out?.removeLast()
            val b=Batter()
            b.name="Batter "+size
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

        }
        if(curentBalls%6==0)
        {
            match?.firstPlaying=!(match?.firstPlaying?:true)
        }
        stack?.removeLast()
        stack?.addLast(SerializationToJson.fromMatch(match))

        return finish()
    }
    public fun scored(score:Int,increment_ball:Boolean):Match?
    {
        this.score+=score

        if(match?.second_team_playing==true)
        {
            val bat= match?.inning1?.bowlers
            bat?.get(bowlerlerIndex)?.runs=(bat?.get(bowlerlerIndex)?.runs?:0) + score
            val batters=match?.inning2?.batters
            batters?:return match
            val size=batters?.size
            if(match?.firstPlaying?:true==true)
            {

                batters.get(size-2).score+=score
                batters.get(size-2).ballsFaced+=1

            }
            else
            {


                batters.get(size-1).score+=score
                batters.get(size-1).ballsFaced+=1

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
            Log.d("Batter Size",""+size)
            if(match?.firstPlaying?:true==true)
            {

                batters.get(size-2).score+=score
               batters.get(size-2).ballsFaced+=1


            }
            else
            {

                batters.get(size-1).score+=score
                batters.get(size-1).ballsFaced+=1

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
        if(stack==null||stack?.size?:0<=1)
        {
            return match
        }

        val previous=stack?.pollLast()
        reduStack?.addLast(previous)
        val tempMatch=stack?.pollLast()
        stack?.addLast(tempMatch)
        val p=SerializationToJson.toMatch(previous)


        match= SerializationToJson.toMatch(tempMatch) as Match
        if(p?.second_team_playing==true&&match?.second_team_playing==false)
        {
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
            }
            else if(match?.inning2?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team2

            }
            else{
                match?.winningTeam="Both Team "
            }
        }
        else if((match?.first_team_play?:true&&match?.second_team_playing?:true&&(UtilityFunctions.overToBalls(match?.inning2?.overs?:0.0) ==balls?.toInt())))
        {


            if(match?.inning1?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team1
            }
            else if(match?.inning2?.score?:0>match?.inning2?.score?:0)
            {
                match?.winningTeam=match?.team2

            }
            else{
                match?.winningTeam="Both Team "
            }
        }
        else
        {
            Log.d("Out","x")

            if(match?.first_team_play?:false&&match?.second_team_playing?:false&&(match?.inning2?.score?:0)>(match?.inning1?.score?:0))
            {

                match?.winningTeam=match?.team2
            }

            else if(match?.first_team_play?:true&&match?.second_team_playing?:true&&(w)>=10)

            {
                match?.winningTeam=match?.team1

            }
            else if(match?.first_team_play?:false&&match?.second_team_playing?:false&&match?.inning2?.score?:0==match?.inning1?.score?:0&&match?.inning2?.wickets?:0>=10)
            {

                match?.winningTeam="Both Team "
            }
        }

        return match
    }

    private fun incrementBall()
    {

        if(match?.second_team_playing?:true==false)
        {

          var ov:Double=  match?.inning2?.bowlers?.get(bowlerlerIndex)?.overs as Double


            match?.inning2?.bowlers?.get(bowlerlerIndex)?.overs=ov+1
            var to=match?.inning1?.overs
            match?.inning1?.overs=decimalFormat.format(UtilityFunctions.ballsToOvers(UtilityFunctions.overToBalls(to)+1)).toDouble()

        }
        else
        {
            var to=match?.inning2?.overs
            match?.inning2?.overs=decimalFormat.format(UtilityFunctions.ballsToOvers(UtilityFunctions.overToBalls(to)+1)).toDouble()

            var ov:Double=  match?.inning1?.bowlers?.get(bowlerlerIndex)?.overs as Double

            match?.inning1?.bowlers?.get(bowlerlerIndex)?.overs=ov+1
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
                return

            }
        }

        if(curentBalls%6==0)
        {
            val  prev=bowlerlerIndex
            bowlerlerIndex=(bowlerlerIndex+1)%5
            match?.firstPlaying=!(match?.firstPlaying?:true)
        }
    }
    public fun wide():Match?
    {

        if(match?.second_team_playing?:true==false)
        {
            match?.inning1?.score=match?.inning1?.score as Int+1
            match?.inning2?.wides=match?.inning2?.wides as Int+1
                val bat= match?.inning2?.bowlers
                bat?.get(bowlerlerIndex)?.wides=bat?.get(bowlerlerIndex)?.wides as Int+1
                bat?.get(bowlerlerIndex)?.runs=bat?.get(bowlerlerIndex)?.runs as Int+1


        }
        else
        {
            match?.inning2?.score=match?.inning2?.score as Int+1
            match?.inning1?.wides=match?.inning1?.wides as Int+1

                val bat= match?.inning1?.bowlers
                bat?.get(bowlerlerIndex)?.wides=bat?.get(bowlerlerIndex)?.wides as Int+1
                 bat?.get(bowlerlerIndex)?.runs=bat?.get(bowlerlerIndex)?.runs as Int+1

        }
        stack?.addLast(SerializationToJson.fromMatch(match))
        return finish()
    }
    public fun leg():Match?
    {

        if(match?.second_team_playing?:true==false)
        {
            match?.inning1?.legByes=match?.inning1?.legByes as Int+1

                val bat= match?.inning2?.bowlers
                bat?.get(bowlerlerIndex)?.legByes=bat?.get(bowlerlerIndex)?.legByes as Int+1

        }
        else
        {
            match?.inning2?.legByes=match?.inning2?.legByes as Int+1
                val bat= match?.inning1?.bowlers
                bat?.get(bowlerlerIndex)?.legByes=bat?.get(bowlerlerIndex)?.legByes as Int+1

        }

        return scored(1,true)
    }
    public fun four():Match?
    {
        if(match?.second_team_playing?:false==true)
        {
            match?.inning2?.fours=match?.inning2?.fours as Int+1
            if(match?.firstPlaying?:true==true)
            {
               val bat= match?.inning2?.batters
                val s=bat?.size?:2
                bat?.get(s-2)?.fours=bat?.get(s-2)?.fours as Int+1
            }
            else
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:1
                bat?.get(s-1)?.fours=bat?.get(s-1)?.fours as Int+1

            }
        }
        else
        {
            match?.inning1?.fours= match?.inning1?.fours as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:2
                bat?.get(s-2)?.fours=bat?.get(s-2)?.fours as Int+1
            }
            else
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:1
                bat?.get(s-1)?.fours=bat?.get(s-1)?.fours as Int+1

            }
        }
      return   scored(4,true)
    }
    public fun six():Match?
    {
        if(match?.second_team_playing?:false==true)
        {
            match?.inning2?.sixes=match?.inning2?.sixes as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:2
                bat?.get(s-2)?.sixes=bat?.get(s-2)?.sixes as Int+1
            }
            else
            {
                val bat= match?.inning2?.batters
                val s=bat?.size?:1
                bat?.get(s-1)?.sixes=bat?.get(s-1)?.sixes as Int+1

            }
        }
        else
        {
            match?.inning1?.sixes= match?.inning1?.sixes as Int+1
            if(match?.firstPlaying?:true==true)
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:2
                bat?.get(s-2)?.sixes=bat?.get(s-2)?.sixes as Int+1
            }
            else
            {
                val bat= match?.inning1?.batters
                val s=bat?.size?:1
                bat?.get(s-1)?.sixes=bat?.get(s-1)?.sixes as Int+1

            }
        }
        return   scored(6,true)
    }
    private fun initInning() {
        batterOne= Batter()
        batterOne.name="Batter 1"
        batterTwo= Batter()
        batterTwo.name="Batter 2"
        match?.inning2?.batters?.addLast(batterOne)
        match?.inning2?.batters?.addLast(batterTwo)
        match?.firstPlaying=true
        score=0
        curentBalls=0
        bowlerlerIndex=0
    }


    private fun finish() :Match?{
      return  matchWon()
    }

}
