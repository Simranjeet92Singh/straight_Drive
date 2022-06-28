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
    var pointsTableAJson:String="",
    var pointsTableBJson:String="",
    var pointsTableCJson:String="",
    var pointsTableDJson:String="",
    var isTournamentCompleted:Boolean?=false,
    var tournamentWinnerName:String?="",
    var isMatch1Completed:Boolean?=false,
    var isMatch2Completed:Boolean?=false,
    var isMatch3Completed:Boolean?=false,
    var isMatch4Completed:Boolean?=false,
    var isMatch5Completed:Boolean?=false,
    var isMatch6Completed:Boolean?=false,
    var isMatch7Completed:Boolean?=false,
    var isMatch1Started:Boolean?=true,
    var isMatch2Started:Boolean?=false,
    var isMatch3Started:Boolean?=false,
    var isMatch4Started:Boolean?=false,
    var isMatch5Started:Boolean?=false,
    var isMatch6Started:Boolean?=false,
    var isMatch7Started:Boolean?=false


):Serializable{

init {
    inning1?.teamName=team1
    inning2?.teamName=team2

}

}