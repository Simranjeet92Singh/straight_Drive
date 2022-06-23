package com.koolbots.straightdrive.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.fragments.NewGameFragment
import com.koolbots.straightdrive.fragments.RecentGamesDashBoard
import com.koolbots.straightdrive.models.Inning
import com.koolbots.straightdrive.models.Match

class RecentGamesAdapter(val context:Context,val fragmentManager: FragmentManager?,val list: ArrayList<Match>?): RecyclerView.Adapter<RecentGamesAdapter.holder>() {

    class holder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var viewGame:Button?=null
        var rematchGame:ImageView?=null
        var team1:TextView?=null
        var team2:TextView?=null
        var date:TextView?=null
        var score:TextView?=null

        init {
            viewGame=itemView.findViewById(R.id.view_game)
            rematchGame=itemView.findViewById(R.id.rematch)
            team1=itemView.findViewById(R.id.recent_team1)
            team2=itemView.findViewById(R.id.recent_team2)
            date=itemView.findViewById(R.id.recent_date)
            score=itemView.findViewById(R.id.recent_score)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
       return holder(LayoutInflater.from(context).inflate(R.layout.recent_game_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        val match=list?.get(position)

        if(match?.team1!=""){
            holder.viewGame?.setOnClickListener({
                fragmentManager?.beginTransaction()?.replace(android.R.id.content,RecentGamesDashBoard.newInstance(match))?.addToBackStack(null)?.commit()

            })
            holder.rematchGame?.setOnClickListener({
                fragmentManager?.beginTransaction()?.replace(android.R.id.content,NewGameFragment.newInstance(match))?.addToBackStack(null)?.commit()

            })
            val inningOne:Inning=SerializationToJson.toInning(match?.inning1Json)
            val inningTwo:Inning=SerializationToJson.toInning(match?.inning2Json)

            holder.team1?.text=match?.team1
            holder.team2?.text=match?.team2
            holder.date?.text=match?.matchDate
            holder.score?.text=""+inningOne.score+"/"+inningOne.wickets+"  |  "+inningTwo.score+"/"+inningTwo.wickets


        }else{
            // do nothing
        }

    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
}