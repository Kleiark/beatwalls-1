package structure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import old_structures.OldParameters
import song.Difficulty
import song._obstacles
import kotlin.math.abs
import kotlin.random.Random


data class Wall(
    @Expose
    @SerializedName("startRow") var startRow: Double,
    @Expose
    @SerializedName("duration") var duration: Double,
    @Expose
    @SerializedName("width") var width: Double,
    @Expose
    @SerializedName("height") var height: Double,
    @Expose
    @SerializedName("startHeight") var startHeight: Double,
    @Expose
    @SerializedName("startTime") var startTime: Double
){

    /**Changes the MyObstacle Type to an _obstacle Type */
    fun to_obstacle(): _obstacles {
        //first, so it adjust the startRow
        val tempWidth = calculateWidth()
        val tempLineIndex = calculateLineIndex()

        //other parameters
        val tempStartTime = startTime.coerceAtLeast(0.001)
        val tempType = type()

        val tempDuration = calculateDuration()

        return _obstacles(
            tempStartTime,
            tempLineIndex,
            tempType,
            tempDuration,
            tempWidth
        )
    }

    private fun calculateDuration(): Double{
        val tempDuration = if (duration <0.0001 && duration >-0.0001) 0.0001 else duration
        return tempDuration.coerceAtLeast(-3.0)
    }

    /**returns th _obstacle value of the width*/
    //TODO until negative width is allowed, this is needed. Once negative width is allowed, do this like lineIndex
    private fun calculateWidth():Int{
        //makes sure its not 0 width
        width = if(width > -0.001 && width < 0.001) 0.001 else width

        //calculate the width
        return if( width >= 0.0)
            (width * 1000 +1000).toInt()
        else{
            startRow += width
            (abs(width)*1000+1000).toInt()
        }
    }


    /**Return the _obstacle value of the startRow*/
    private fun calculateLineIndex():Int {
        val i = startRow +2
        return if( i >= 0.0)
            (i* 1000 +1000).toInt()
        else
            (i*1000-1000).toInt()
    }


    /**returns the type given heigt and startheight */
    private fun type():Int {

        val wallH= if(height >-0.01 && height <0.01) 0.01 else abs(height)

        val startH = if(height >0) startHeight else startHeight + height

        var tWallH:Int = (((1.0/3.0)*(wallH/(4.0/3.0)))*1000).toInt()
        tWallH = when {
            tWallH>4000 -> 4000
            tWallH<0 -> 0
            else -> tWallH
        }

        var tStartH:Int =  (250*(startH/(4.0/3.0))).toInt()
        tStartH = when {
            tStartH>999 -> 999
            tStartH<0 -> 0
            else -> tStartH
        }
        return  (tWallH * 1000 + tStartH+4001)
    }


    /**overwrites the values, depending on the given parameters*/
    fun adjustParameters(oldParameters: OldParameters){
        //Adding all the values
        var tempDuration = duration + oldParameters.duration
        val tempHeight = height + oldParameters.wallHeight
        val tempStartHeight = startHeight + oldParameters.wallStartHeight
        val tempStartRow = startRow + oldParameters.startRow
        val tempWidth = width + oldParameters.width
        var tempStartTime = startTime + oldParameters.startTime

        //adjusting the scale
        if(tempDuration>0)  tempDuration *= oldParameters.scale
        tempStartTime *= oldParameters.scale

        //adjust the values
        this.duration =tempDuration
        this.height =tempHeight
        this.startHeight =tempStartHeight
        this.startRow =tempStartRow
        this.width =tempWidth
        this.startTime=tempStartTime

    }

    fun adjustToBPM(baseBPM:Double,difficulty: Difficulty): Wall {
        TODO()
    }
    fun fuckUp() =
        Wall(ra(startRow), ra(duration), ra(width), ra(height), ra(startHeight), ra(startTime))

    fun ground(h:Double) =
        Wall(startRow, duration, width, height + (startHeight - h), h, startTime)

    fun sky(h:Double) =
        Wall(startRow, duration, width, (h - startHeight), startHeight, startTime)

    fun extend(a:Double) =
        Wall(startRow, a - startTime, width, height, startHeight, startTime)

    private fun ra(i:Double) = i+Random.nextDouble(-0.2 ,0.2)
    fun fast() = this.copy(duration= -2.0)
    fun hyper() = this.copy(duration = -3.0)

    /**returns the mirrored obstacle */
    fun mirror(d:Boolean): List<Wall> {
        val a =  mutableListOf(this.copy(startRow = -startRow, width = -width))
        if (d) a.add(this.copy())
        return a
    }
    fun verticalMirror(sh:Double = 2.0,d: Boolean): MutableList<Wall> {
        val a = mutableListOf<Wall>()
        a.add(this.copy(startHeight = 2 * sh - startHeight, height = -height))
        if (d) a.add(this.copy())
        return a
    }
    fun pointMirror(sh: Double = 2.0,d:Boolean): MutableList<Wall> {
        val a = mutableListOf<Wall>()
        a.addAll(this.mirror(false).flatMap { it.verticalMirror(d =false) })
        if (d) a.add(this.copy())
        return a
    }


    fun scale(s:Double) = this.copy(
        duration= if(duration>0) duration*s else duration,
        startTime = startTime*s)
    fun verticalScale(s:Double) = this.copy(
        startRow= startRow*s,
        height = height*s,
        startHeight= startHeight*s
    )
    fun extendX(a:Double) = this.copy(
        width = a-startRow
    )
    fun extendY(a:Double) = this.copy(
        height = a-startHeight
    )
    fun extendZ(a: Double) = this.copy(
        duration = a - startTime
    )
    fun time(a:Double) = this.copy(
        startTime = startTime+a
    )
    fun repeat(a: Int, o: Double = 1.0): MutableList<Wall> {
        val list = mutableListOf<Wall>()
        for (i in 0 until a){
            list.add(this.copy(startTime= this.startTime + i*o))
        }
        return list
    }
    fun split(a: Int): MutableList<Wall> {
        val list = mutableListOf<Wall>()
        for (i in 0 until a){
            if (this.height>this.width)
                list.add(this.copy(startHeight = startHeight+height* i / a,height = 1.0/a))
            else
                list.add(this.copy(startRow = startRow + width * i / a,width = 1.0/a))
        }
        return list
    }
}

