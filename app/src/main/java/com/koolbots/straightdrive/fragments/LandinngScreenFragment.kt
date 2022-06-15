package com.koolbots.straightdrive.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.koolbots.straightdrive.R
import kotlin.math.acos


class LandinngScreenFragment : Fragment() {
    private var privacyPolicyButton:TextView?=null
    private var newGame:TextView?=null
    private var tournamentButton:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        val act=activity as AppCompatActivity
        act.supportActionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_landinng_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newGame=view.findViewById(R.id.games)
        newGame?.setOnClickListener(
                {

                    activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content,RecentGamesFragment.newInstance("",""),"Recent Games  Fragment")?.addToBackStack(null)?.commit()

                }

        )
        privacyPolicyButton=view.findViewById(R.id.privacy_policy)
        privacyPolicyButton?.setOnClickListener({
            activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content,PrivacyPolicyFragment.newInstance("",""),"Privacy Policy  Fragment")?.addToBackStack(null)?.commit()

        })

        tournamentButton=view.findViewById(R.id.tournament)

        tournamentButton?.setOnClickListener({
            activity?.supportFragmentManager?.beginTransaction()?.replace(android.R.id.content,TournamentFragment.newInstance("",""),"Recent Games  Fragment")?.addToBackStack(null)?.commit()
        Toast.makeText(requireContext(),"hello",Toast.LENGTH_SHORT).show()
        })
        //Tournament and series button click

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onResume() {
        super.onResume()
        val act=activity as AppCompatActivity
        act.supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        val act=activity as AppCompatActivity
        act.supportActionBar?.hide()
    }
    companion object {
              @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LandinngScreenFragment()
    }

}