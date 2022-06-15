package com.koolbots.straightdrive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.models.Bowler
import java.util.*

class ScoreCardExtrasAdapter(val context: Context?,val bowlers:LinkedList<Bowler>?): RecyclerView.Adapter<ScoreCardExtrasAdapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var bowler: TextView?=null
        var runs: TextView?=null
        var overs: TextView?=null
        var extras: TextView?=null
        var wickets: TextView?=null
        init {
            bowler=itemView.findViewById(R.id.bowlers_bowler)
            runs=itemView.findViewById(R.id.bowlers_runs)
            overs=itemView.findViewById(R.id.bowlers_overs)
            extras=itemView.findViewById(R.id.bowlers_extras)
            wickets=itemView.findViewById(R.id.bowlers_wickets)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreCardExtrasAdapter.holder {
        return holder(LayoutInflater.from(context).inflate(R.layout.extras_card_item,parent,false))
    }

    override fun onBindViewHolder(holder: ScoreCardExtrasAdapter.holder, position: Int) {
        val bowler=bowlers?.get(position)
        holder.bowler?.text="Bowler "+(position+1).toString()
        holder.extras?.text=(bowler?.legByes?.plus(bowler?.wides?:0)).toString()
        holder.runs?.text=bowler?.runs?.toString()
        holder.overs?.text=bowler?.overs?.toString()
                //((bowler?.overs?.toInt()?.div(6)).toString()+"."+bowler?.overs?.toInt()?.rem(6))
        holder.wickets?.text=bowler?.wickets.toString()

    }

    override fun getItemCount(): Int {
        return bowlers?.size?:0
    }
}