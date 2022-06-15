package com.koolbots.straightdrive.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
@Entity
 data class Inning(
    @PrimaryKey(autoGenerate = true)
    var inning: Int=0,
    var  battersJson: String?="",
    var  bowlersJson: String?="",
    @Ignore var  batters: LinkedList<Batter>?= LinkedList<Batter>(),
    @Ignore  var  bowlers: LinkedList<Bowler>?= LinkedList<Bowler>(),
    var legByes: Int?=0,
    var overs: Double?=0.0,
    var score: Int?=0,
    var teamName: String?="",
    var wickets: Int?=0,
    var wides: Int?=0,
    var status:String?="",
    var fours:Int?=0,
    var sixes:Int?=0,

    ):Serializable
 {



 }