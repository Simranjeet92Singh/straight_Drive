package com.koolbots.straightdrive.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "TournamentModel")
data class TournamentModel(

    @PrimaryKey(autoGenerate = true)
    var key:Int=0,
    var tournamentName:String?="",
    var date:String?="",
    var numberOfMatches:Int?=3,
    var isTournamentCompleted:Boolean?=false,
    var tournamentWinnerName:String?="",
    var match1Winner:String?="",
    var match2Winner:String?="",
    var match3Winner:String?="",
    var match4Winner:String?="",
    var match5Winner:String?="",
    var match6Winner:String?="",
    var match7Winner:String?="",
    var totalOvers:Double?=0.0,



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
    var isMatch7Started:Boolean?=false,
    @Ignore
   var pointsTableModel: PointsTableModel=PointsTableModel(),
    var teamCount:Int=3,
    var pointsTableAJson:String="",
    var pointsTableBJson:String="",
    var pointsTableCJson:String="",
    var pointsTableDJson:String="",
    var isFromSeries:Boolean?=false,
    var isFromTournament:Boolean?=false,

    var match1:String="",
    var match2:String="",
    var match3:String="",
    var match4:String="",
    var match5:String="",
    var match6:String="",
    var match7:String="",

):Serializable 