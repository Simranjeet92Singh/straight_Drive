package com.koolbots.straightdrive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.fragments.NewGameFragment
import com.koolbots.straightdrive.fragments.RecentGamesDashBoard
import com.koolbots.straightdrive.fragments.Schedule
import com.koolbots.straightdrive.models.Inning
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel

class TournamentAdapter(val context:Context, val fragmentManager: FragmentManager?, val list: ArrayList<TournamentModel>?,val isFromTournament:Boolean?): RecyclerView.Adapter<TournamentAdapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var viewGame: Button?=null
        var rematchGame: ImageView?=null
        var tournamentName: TextView?=null
        var noOfMatches: TextView?=null
        var date: TextView?=null
        var rootView: LinearLayout?=null
        var parms: LinearLayout.LayoutParams?=null


        init {
            viewGame=itemView.findViewById(R.id.view_game)
            rematchGame=itemView.findViewById(R.id.rematch)
            tournamentName=itemView.findViewById(R.id.tournamentName)
            noOfMatches=itemView.findViewById(R.id.noOfMatches)
            date=itemView.findViewById(R.id.recent_date)
            rootView=itemView.findViewById(R.id.lloutT)
            parms = LinearLayout.LayoutParams(0,0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(LayoutInflater.from(context).inflate(R.layout.tournament_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: holder, position: Int) {

        val match=list?.get(position)

               if(isFromTournament==true) {


                   if (match?.isFromTournament == true) {

                       holder.viewGame?.setOnClickListener({
                            var matchT=Match()
                           matchT.isFromSeries=false
                           matchT.isFromTournament=true
                           fragmentManager?.beginTransaction()
                               ?.replace(android.R.id.content, Schedule.newInstance(matchT, match))
                               ?.addToBackStack(null)?.commit()

                       })
                       holder.rematchGame?.setOnClickListener({
//            fragmentManager?.beginTransaction()?.replace(android.R.id.content, NewGameFragment.newInstance(match))?.addToBackStack(null)?.commit()

                       })
//        val inningOne: Inning = SerializationToJson.toInning(match?.inning1Json)
//        val inningTwo: Inning = SerializationToJson.toInning(match?.inning2Json)

                       holder.tournamentName?.text = match?.tournamentName
                       holder.noOfMatches?.text = match?.numberOfMatches.toString()
                       holder.date?.text = match?.date


                   } else {
                       holder.rootView?.layoutParams = holder.parms
                   }
               }
                else {
                   if (match?.isFromSeries == true) {
                       holder.viewGame?.setOnClickListener({
                          var matchT=Match()
                           matchT.isFromSeries=true
                           matchT.isFromTournament=false
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, Schedule.newInstance(matchT,match))?.addToBackStack(null)?.commit()

                       })
                       holder.rematchGame?.setOnClickListener({
//            fragmentManager?.beginTransaction()?.replace(android.R.id.content, NewGameFragment.newInstance(match))?.addToBackStack(null)?.commit()

                       })
//        val inningOne: Inning = SerializationToJson.toInning(match?.inning1Json)
//        val inningTwo: Inning = SerializationToJson.toInning(match?.inning2Json)

                       holder.tournamentName?.text = match?.tournamentName
                       holder.noOfMatches?.text = match?.numberOfMatches.toString()
                       holder.date?.text = match?.date
                   } else {
                       holder.rootView?.layoutParams = holder.parms
                   }


               }
    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
}