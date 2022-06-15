package com.koolbots.straightdrive.models

import androidx.room.Entity
import androidx.room.Ignore
import java.io.Serializable
@Entity
data  class Batter(
    @Ignore
    var bat:Int=0 ,
    var ballsFaced: Int=0,
    var fallenTo: String="",
    var fours: Int=0,
    var howOut: String="",
    var name: String="",
    var score: Int=0,
    var sixes: Int=0,
    var status: String="Not Out"):Serializable
{

}