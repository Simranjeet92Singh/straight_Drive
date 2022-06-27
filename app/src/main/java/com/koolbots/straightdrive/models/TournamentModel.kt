package com.koolbots.straightdrive.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TournamentModel(
   @PrimaryKey(autoGenerate = true)
    var key:Int?=0,
    var tournamentName:String?="",
   var numberOfMatches:String?="",
   var dateOfMatch:String?="",
   var isTournamentCompleted:Boolean?=false,

   var isMatch1Completed:Boolean?=false,
   var isMatch2Completed:Boolean?=false,
   var isMatch3Completed:Boolean?=false,
   var isMatch4Completed:Boolean?=false,
   var isMatch5Completed:Boolean?=false,
   var isMatch6Completed:Boolean?=false,
   var isMatch7Completed:Boolean?=false,

   var ismatch1Started:Boolean?=true,
   var ismatch2Started:Boolean?=false,
   var ismatch3Started:Boolean?=false,
   var ismatch4Started:Boolean?=false,
   var ismatch5Started:Boolean?=false,
   var ismatch6Started:Boolean?=false,
   var ismatch7Started:Boolean?=false,


   ):Serializable {
}