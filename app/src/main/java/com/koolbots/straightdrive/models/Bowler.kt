package com.koolbots.straightdrive.models

import androidx.room.Entity
import androidx.room.Ignore
import java.io.Serializable
@Entity
data  class Bowler(
    @Ignore
    var b:Int=0,
    var legByes: Int=0,
    var name: String="",
    var overs: Double=0.0,
    var runs: Int=0,
    var wickets: Int=0,
    var wides: Int=0):Serializable
 {

 }