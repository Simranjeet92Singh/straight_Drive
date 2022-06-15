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
   var dateOfMatch:String?=""
):Serializable {
}