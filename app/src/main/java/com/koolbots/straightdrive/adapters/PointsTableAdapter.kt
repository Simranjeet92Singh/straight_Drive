package com.koolbots.straightdrive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R

import com.koolbots.straightdrive.models.PointsTableModel
import com.koolbots.straightdrive.models.TournamentModel


class PointsTableAdapter (val context: Context, val tournamentModel: TournamentModel?, val list: List<PointsTableModel>?): RecyclerView.Adapter<PointsTableAdapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var teamName:TextView?=null
        var matchesPlayed:TextView?=null
        var points:TextView?=null
        var nrr:TextView?=null
        var llout:LinearLayout?=null

        var parms: LinearLayout.LayoutParams?=null


        init {
            teamName=itemView.findViewById(R.id.teamName)
            matchesPlayed=itemView.findViewById(R.id.matchesPlayed)
            points=itemView.findViewById(R.id.points)
            nrr=itemView.findViewById(R.id.nrr)
            llout=itemView.findViewById(R.id.lloutpointsmain)

            parms = LinearLayout.LayoutParams(0,0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(LayoutInflater.from(context).inflate(R.layout.points_table_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: holder, position: Int) {

        val match=list?.get(position)


        if(tournamentModel?.isFromSeries==true){
            when(match?.teamName){
                "Team A"->{
                    holder.teamName?.text=match?.teamName
                    holder.points?.text=match?.points.toString()
                    holder.nrr?.text=match?.nrr.toString()
                    holder.matchesPlayed?.text=match?.matchesPlayed.toString()
                }

                "Team B"->{
                    holder.teamName?.text=match?.teamName
                    holder.points?.text=match?.points.toString()
                    holder.nrr?.text=match?.nrr.toString()
                    holder.matchesPlayed?.text=match?.matchesPlayed.toString()
                }

                "Team C"->{
                    holder.llout?.layoutParams=holder.parms

                }

                "Team D"->{
                    holder.llout?.layoutParams=holder.parms
                }
            }




        }else{
            holder.teamName?.text=match?.teamName
            holder.points?.text=match?.points.toString()
            holder.nrr?.text=match?.nrr.toString()
            holder.matchesPlayed?.text=match?.matchesPlayed.toString()
        }




    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
}