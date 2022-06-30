package com.koolbots.straightdrive.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koolbots.straightdrive.R
import com.koolbots.straightdrive.Util.SerializationToJson
import com.koolbots.straightdrive.adapters.PointsTableAdapter
import com.koolbots.straightdrive.models.PointsTableModel
import com.koolbots.straightdrive.models.TournamentModel
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator

class PointsTable: Fragment() {

    private val INNING:String="tournament"
    private  var rec:RecyclerView?=null
    private var tournamentModel:TournamentModel?=null
    private var pointTable:PointsTableModel?=null
    private var pointsTableList: ArrayList<PointsTableModel>?=ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            tournamentModel=it.getSerializable(INNING) as TournamentModel

        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.points_table, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as AppCompatActivity
        rec=view.findViewById(R.id.pointsTableList)

        rec?.layoutManager= LinearLayoutManager(ac.applicationContext)
        rec?.setHasFixedSize(true)
        val a = SerializationToJson.toPointsTable(tournamentModel?.pointsTableAJson)
        val b = SerializationToJson.toPointsTable(tournamentModel?.pointsTableBJson)
        val c = SerializationToJson.toPointsTable(tournamentModel?.pointsTableCJson)
        val d = SerializationToJson.toPointsTable(tournamentModel?.pointsTableDJson)
        pointsTableList?.add(a)
        pointsTableList?.add(b)
        pointsTableList?.add(c)
        pointsTableList?.add(d)

       var comparator: Comparator<PointsTableModel> = Comparator.comparing(PointsTableModel::points)
           .thenComparing(PointsTableModel::nrr)

        val sortedList :List<PointsTableModel> = pointsTableList?.stream()!!
            .sorted(comparator)
            .collect(Collectors.toList())
        Log.d("pointsTableList",pointsTableList.toString())
        val tournamentAdapter=
            PointsTableAdapter(ac.applicationContext,tournamentModel, sortedList!!)
        rec?.adapter=tournamentAdapter
        val sharedPref:SharedPreferences = ac.getSharedPreferences("FinalTeams",MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putString("TeamA",sortedList?.get(0).teamName)
        editor.putString("TeamB",sortedList.get(1).teamName)
        editor.commit()
    }


    companion object {

        @JvmStatic
        fun newInstance(tournamentModel: TournamentModel?):PointsTable {

                return PointsTable().apply {
                    arguments = Bundle().apply {
                        putSerializable(INNING, tournamentModel)
                    }
                }


        }

    }

    fun isTablet(mContext: Context): Boolean {
        return mContext.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}