package com.koolbots.straightdrive.models

import androidx.room.Entity
import java.io.Serializable

@Entity
data class PointsTableModel(
    var teamName:String="",
    var matchesPlayed:String="",
    var points:Int=0,
    var nrr:Double=0.0

) :Serializable{

}
