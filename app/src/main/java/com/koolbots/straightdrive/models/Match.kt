package com.koolbots.straightdrive.models

import androidx.room.*
import java.io.Serializable
@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    var key:Int = 0,
    var inning1Json: String?="",
    var inning2Json: String?="",
    var team1: String?="",
    var team2: String?="",
    var firstBattingTeam: String?="",
    @Ignore
    var inning1: Inning?= Inning(),
    @Ignore
    var inning2: Inning?= Inning(),
    @Ignore
    var hatrick:Int=0,
    var matchDate: String?="",
     var status: String?="",
    var totalOvers: Double?=0.0,
    var winningTeam: String?="",
    var loosingTeam: String?="",
    var first_team_play:Boolean=false,
    var second_team_playing:Boolean=false,
    var firstPlaying:Boolean=true,
   //Tournament

    var isFromTournament:Boolean=false,
    var isFromSeries:Boolean=false,




):Serializable{

init {
    inning1?.teamName=team1
    inning2?.teamName=team2

}

}