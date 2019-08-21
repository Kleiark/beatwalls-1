package song

import com.google.gson.annotations.SerializedName

data class _events (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_type") var _type : Int,
    @SerializedName("_value") val _value : Int
){
    fun adjustBPM(bpm: Double, newBpm: Double, timeOffset: Double){
        this._time =this._time/newBpm *bpm + timeOffset
    }

}