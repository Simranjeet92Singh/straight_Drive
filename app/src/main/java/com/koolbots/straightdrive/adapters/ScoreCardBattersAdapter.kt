package com.koolbots.straightdrive.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.models.Batter
import com.koolbots.straightdrive.models.Bowler
import java.util.*

class ScoreCardBattersAdapter(val context: Context?,val batters: LinkedList<Batter>?): RecyclerView.Adapter<ScoreCardBattersAdapter.holder>() {

    init {
        this?.batters?.sortWith(compareBy({
            it.name.split(" ")?.get(1)?.toInt()
        }))
    }
    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var batter:TextView?=null
        var status:TextView?=null
        var bowler:TextView?=null
        var score:TextView?=null
        init {

            batter=itemView.findViewById(R.id.batter_batsman)
            status=itemView.findViewById(R.id.batter_status)
            bowler=itemView.findViewById(R.id.batter_bowler)
            score=itemView.findViewById(R.id.batter_score)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreCardBattersAdapter.holder {
        return holder(LayoutInflater.from(context).inflate(R.layout.batting_card_item,parent,false))
    }



    override fun getItemCount(): Int {
        return 11
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: holder, position: Int) {
        var batter:Batter?=null
        if(position<batters?.size?:0)
        {

             batter=batters?.get(position)
            holder.batter?.text=batter?.name
        }
        else
        {
            holder.batter?.text=batter?.name
            holder.status?.text=""
            holder.bowler?.text=""
            holder.score?.text=""
            return
        }
        holder.status?.text=batter?.status
        holder.score?.text=batter?.score.toString()+"("+batter?.ballsFaced+")"
        if(position==10)
        {
           // holder.status?.text="Not Out"

        }
        if(batter?.status.equals("Caught"))
        {
            holder.bowler?.text=batter?.fallenTo

            holder?.status?.setTextColor(context?.getColor(R.color.primary_orange)?:Color.GREEN)
        }
        else     if(batter?.status.equals("Bowled"))
        {
            holder.bowler?.text= batter?.fallenTo

            holder?.status?.setTextColor(context?.getColor(R.color.primary_red)?:Color.GREEN)
        }
        else
        {
            holder.bowler?.text=batter?.fallenTo

            holder?.status?.setTextColor(context?.getColor(R.color.green)?:Color.GREEN)

        }

    }


}