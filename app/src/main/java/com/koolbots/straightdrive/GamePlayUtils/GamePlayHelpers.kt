package com.koolbots.straightdrive.GamePlayUtils

import com.koolbots.straightdrive.models.Bowler
import java.util.*

class GamePlayHelpers {
    companion object
    {

        fun calCulateWide(bowlers: LinkedList<Bowler>):Int {
            var wides = 0
            for (b in bowlers) {
                wides += b.wides
            }
            return wides
        }
         fun calCulateLeg(bowlers: LinkedList<Bowler>):Int {
            var leg = 0
            for (b in bowlers) {
                leg += b.legByes
            }
            return leg
        }
    }
}