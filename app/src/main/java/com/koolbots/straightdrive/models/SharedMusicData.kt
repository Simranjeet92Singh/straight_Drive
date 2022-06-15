package com.koolbots.straightdrive.models

import java.util.*

public object  SharedMusicData {
    var inningFinished=false
    var matchFinished=false
    var half_Century=false
    var full_century=false
    var over_finish=false
    var hatrick=false
    var wickets=arrayOf(0,0,0,0,0,0)
    public fun clear()
    {
        half_Century=false
        full_century=false
        over_finish=false
        hatrick= false
       // Arrays.fill(wickets,0)
    }
}