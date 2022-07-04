package com.koolbots.straightdrive.Util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.koolbots.straightdrive.models.*
import java.util.*

class SerializationToJson
{

    companion object
    {
        val gson=Gson()
        fun fromBatters(batters: LinkedList<Batter>?):String
        {
            return gson.toJson(batters)
        }
        fun fromBowlers(bowlers: LinkedList<Bowler>?):String
        {
            return gson.toJson(bowlers)

        }
        fun fromInning(inning: Inning?):String
        {
            return gson.toJson(inning)

        }
        fun fromMatch(match: Match?):String
        {
            return gson.toJson(match)

        }
        fun fromPointsTable(pointsTable:PointsTableModel?):String
        {
            return gson.toJson(pointsTable)
        }

        fun fromTournamtent(tournamentModel: TournamentModel?):String
        {
            return gson.toJson(tournamentModel)
        }
        fun toTournament(json:String?):TournamentModel{

            return return gson.fromJson(json,
            object : TypeToken<TournamentModel>() {}.type)
        }
        fun toPointsTable(json:String?):PointsTableModel{
            return return gson.fromJson(json,
            object: TypeToken<PointsTableModel>() {}.type)
        }
        fun toBatters(json: String?):LinkedList<Batter>
        {
            return  return gson.fromJson(
                json,
                object : TypeToken<LinkedList<Batter>>() {}.getType()
            );
        }
        fun toBowlers(json: String?):LinkedList<Bowler>
        {
            return  return gson.fromJson(
                json,
                object : TypeToken<LinkedList<Bowler>>() {}.getType()
            );

        }
        fun toInning(json: String?):Inning
        {
            return   gson.fromJson(json, object : TypeToken<Inning>() {}.getType());

        }
        fun toMatch(json: String?):Match
        {
            return  return gson.fromJson(json, object : TypeToken<Match>() {}.getType());

        }


        fun fromMatchToJson(match: Match?):String
        {
           var exlusionStratgy=object :ExclusionStrategy
            {
                override fun shouldSkipField(f: FieldAttributes?): Boolean {
                    if ( f?.getName()
                            .equals("inning1Json")||
                        f?.getName()
                            .equals("inning2Json")||
                        f?.getName()
                            .equals("first_team_play")||
                        f?.getName()
                            .equals("second_team_playing")||
                        f?.getName()
                            .equals("firstPlaying")
                            ) {
                        return true
                    }
                    else{
                       return false
                    }
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {


                    return false
                }

            }

            val gson=GsonBuilder().setExclusionStrategies(exlusionStratgy).create()
            return gson.toJson(match)
        }

    }
}