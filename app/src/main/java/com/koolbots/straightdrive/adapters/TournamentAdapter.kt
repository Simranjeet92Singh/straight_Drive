package com.koolbots.straightdrive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.fragments.NewGameFragment
import com.koolbots.straightdrive.fragments.RecentGamesDashBoard
import com.koolbots.straightdrive.models.Inning
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel

class TournamentAdapter(val context:Context, val fragmentManager: FragmentManager?, val list: ArrayList<TournamentModel>?): RecyclerView.Adapter<TournamentAdapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var viewGame: Button?=null
        var rematchGame: ImageView?=null
        var tournamentName: TextView?=null
        var noOfMatches: TextView?=null
        var date: TextView?=null


        init {
            viewGame=itemView.findViewById(R.id.view_game)
            rematchGame=itemView.findViewById(R.id.rematch)
            tournamentName=itemView.findViewById(R.id.tournamentName)
            noOfMatches=itemView.findViewById(R.id.noOfMatches)
            date=itemView.findViewById(R.id.recent_date)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(LayoutInflater.from(context).inflate(R.layout.tournament_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        val match=list?.get(position)



        holder.viewGame?.setOnClickListener({
//            fragmentManager?.beginTransaction()?.replace(android.R.id.content, RecentGamesDashBoard.newInstance(match))?.addToBackStack(null)?.commit()

        })
        holder.rematchGame?.setOnClickListener({
//            fragmentManager?.beginTransaction()?.replace(android.R.id.content, NewGameFragment.newInstance(match))?.addToBackStack(null)?.commit()

        })
//        val inningOne: Inning = SerializationToJson.toInning(match?.inning1Json)
//        val inningTwo: Inning = SerializationToJson.toInning(match?.inning2Json)

        holder.tournamentName?.text=match?.tournamentName
        holder.noOfMatches?.text=match?.numberOfMatches.toString()
        holder.date?.text=match?.date

    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
}