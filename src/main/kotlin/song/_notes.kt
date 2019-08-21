package song

import com.google.gson.annotations.SerializedName

data class _notes (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_lineIndex") val _lineIndex : Int,
    @SerializedName("_lineLayer") val _lineLayer : Int,
    @SerializedName("_type") val _type : Int,
    @SerializedName("_cutDirection") val _cutDirection : Int
){
    fun adjustBPM(bpm: Double, newBpm: Double, timeOffset: Double){
        this._time =this._time/newBpm *bpm + timeOffset
    }

}