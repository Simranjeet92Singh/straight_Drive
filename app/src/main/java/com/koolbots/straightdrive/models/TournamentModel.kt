package com.koolbots.straightdrive.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TournamentModel(
//   @PrimaryKey(autoGenerate = true)
//    var key:Int?=0,
    var tournamentName:String?="",
    var numberOfMatches:String?="",
    var isTournamentCompleted:Boolean?=false,
    var winnerName:String?="",
    var isMatch1Completed:Boolean?=false,
    var isMatch2Completed:Boolean?=false,
    var isMatch3Completed:Boolean?=false,
    var isMatch4Completed:Boolean?=false,
    var isMatch5Completed:Boolean?=false,
    var isMatch6Completed:Boolean?=false,
    var isMatch7Completed:Boolean?=false,

):Serializable {
}