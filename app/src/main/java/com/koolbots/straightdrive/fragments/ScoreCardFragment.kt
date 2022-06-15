package com.koolbots.straightdrive.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.GamePlayUtils.GamePlayHelpers
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.Util.SharedData
import com.koolbots.straightdrive.adapters.ScoreCardBattersAdapter
import com.koolbots.straightdrive.adapters.ScoreCardExtrasAdapter
import com.koolbots.straightdrive.models.Inning

const val INNING1="inning1"

const val INNING2="inning2"
class ScoreCardFragment : Fragment() {
    private var battersList:RecyclerView?=null
    private var ballersList:RecyclerView?=null
    private var mainLayout: LinearLayout?=null
    private var extras:TextView?=null
    private var score_wicket:TextView?=null
    private var inning1:Inning?=null
    private var inning2:Inning?=null
    private var team_name:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
                 inning1=it.getSerializable(INNING1) as Inning
                 inning2=it.getSerializable(INNING2) as Inning
            Log.d("Json x", inning2?.bowlers?.get(1)?.overs?.toString()?:" Not available")

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_score_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.applicationContext?:return
        val contex=activity?.application?:return
        mainLayout=view.findViewById(R.id.main_layout)
        if(SharedData.notchHeight?:0>0)
        {
            val layout=mainLayout?:null
            if(layout==null)
                return
            val params: ViewGroup.MarginLayoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = SharedData.notchHeight?:0+5


        }

        team_name=view.findViewById(R.id.score_card_team_name)
        team_name?.text=inning1?.teamName+"  Batting Score Card"
        score_wicket=view.findViewById(R.id.score_card_scoe_wickets)
        extras=view.findViewById(R.id.score_card_wd_lg)

        score_wicket?.text="Total "+inning1?.score+"/"+inning1?.wickets+" in "+inning1?.overs+" "+"Overs"
        extras?.text=GamePlayHelpers.calCulateWide(SerializationToJson.toBowlers(inning2?.bowlersJson)).toString() +" WD "+GamePlayHelpers.calCulateLeg(SerializationToJson.toBowlers(inning2?.bowlersJson))+" LG"
        battersList=view.findViewById(R.id.batters_list)
        battersList?.setHasFixedSize(true)
        battersList?.layoutManager=LinearLayoutManager(context)
        battersList?.adapter=ScoreCardBattersAdapter(context,SerializationToJson.toBatters(inning1?.battersJson))
        battersList=view.findViewById(R.id.bowlers_list)
        battersList?.setHasFixedSize(true)
        battersList?.layoutManager=LinearLayoutManager(context)
        battersList?.adapter=ScoreCardExtrasAdapter(context,SerializationToJson.toBowlers(inning2?.bowlersJson))



    }
    companion object {

        @JvmStatic
        fun newInstance(inning1: Inning?,inning2: Inning?) =
            ScoreCardFragment().apply {
               arguments= Bundle().apply {
                    putSerializable(INNING1,inning1)
                    putSerializable(INNING2,inning2)
                }
            }
    }
}