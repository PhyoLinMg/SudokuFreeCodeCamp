package dev.linmg.sudokufreecodecamp.common

import android.app.Activity
import android.widget.Toast
import dev.linmg.sudokufreecodecamp.R
import dev.linmg.sudokufreecodecamp.domain.Difficulty

internal fun Activity.makeToast(message:String,toastLength:Int){
    Toast.makeText(this,message,toastLength).show()
}



internal fun Long.toTime():String{
    if (this>=3600)  return "+59:59"
    var minutes=((this%3600) /60).toString()
    if(minutes.length==1) minutes="0$minutes"
    var seconds=(this%60).toString()
    if(seconds.length==1) seconds="0$seconds"
    return String.format("$minutes$seconds")
}


internal val Difficulty.toLocalizedResource:Int get()
{
    return when(this){
        Difficulty.EASY -> R.string.easy
        Difficulty.MEDIUM -> R.string.medium
        Difficulty.HARD -> R.string.hard
    }
}