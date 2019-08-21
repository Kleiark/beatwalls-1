package song

import reader.readDifficulty
import com.google.gson.annotations.SerializedName
import mu.KotlinLogging
import java.io.File
import java.nio.file.Paths
import reader.readInfo
import reader.writeInfo

private val logger = KotlinLogging.logger {}
class Song(file:File) {
    val info: Info = readInfo(Paths.get(file.toString(), "info.dat").toFile())
    var difficultyList =  mapOf<Difficulty,File>()
    init {
        for(i in info._difficultyBeatmapSets){
            for(j in i._difficultyBeatmaps){
                val diffPath = Paths.get(file.toString(),(j._beatmapFilename))
                val diffFile = File(diffPath.toUri())
                val diffPair = Pair<Difficulty,File>(readDifficulty(diffFile),diffFile)
                difficultyList = difficultyList + diffPair

                if(diffPair.component1().containsCommand("bw" ) && !j._customData._requirements.contains("Mapping Extension")){
                    logger.info { "Added Mapping Extensions Requirements to $diffFile" }
                    j._customData._requirements.add("Mapping Extensions")
                    writeInfo(info, Paths.get(file.toString(), "info.dat").toFile())
                }
            }
        }
    }
}



data class Info (

    @SerializedName("_version") val _version : String,
    @SerializedName("_songName") var _songName : String,
    @SerializedName("_songSubName") val _songSubName : String,
    @SerializedName("_songAuthorName") val _songAuthorName : String,
    @SerializedName("_levelAuthorName") val _levelAuthorName : String,

    @SerializedName("_beatsPerMinute") var _beatsPerMinute : Double,
    @SerializedName("_songTimeOffset") val _songTimeOffset : Double,
    @SerializedName("_shuffle") val _shuffle : Double,
    @SerializedName("_shufflePeriod") val _shufflePeriod : Double,

    @SerializedName("_previewStartTime") val _previewStartTime : Double,
    @SerializedName("_previewDuration") val _previewDuration : Double,

    @SerializedName("_songFilename") var _songFilename : String,
    @SerializedName("_coverImageFilename") val _coverImageFilename : String,

    @SerializedName("_environmentName") val _environmentName : String,

    @SerializedName("_customData") val InfoCustomData : InfoCustomData,

    @SerializedName("_difficultyBeatmapSets") val _difficultyBeatmapSets : List<_difficultyBeatmapSets>
)

data class InfoCustomData(

    @SerializedName("_contributors") val _contributors: List<_contributors>,
    @SerializedName("_customEnvironment") val _customEnvironment: String,
    @SerializedName("_customEnvironmentHash") val _customEnvironmentHash: String
)
data class _contributors(
    @SerializedName("_role") val _role: String,
    @SerializedName("_name") val _name:String,
    @SerializedName("_iconPath") val _iconPath: String
)

data class _customData (

    @SerializedName("_difficultyLabel") val _difficultyLabel : String,

    @SerializedName("_editorOffset") val _editorOffset : Int,
    @SerializedName("_editorOldOffset") val _editorOldOffset : Int,

    @SerializedName("_colorLeft") val _colorLeft: _beatmapColor,
    @SerializedName("_colorRight") val _colorRight : _beatmapColor,

    @SerializedName("_warnings") val _warnings : List<String>,
    @SerializedName("_information") val _information : List<String>,
    @SerializedName("_suggestions") val _suggestions : List<String>,
    @SerializedName("_requirements") val _requirements : ArrayList<String>
)

data class _beatmapColor(
    @SerializedName("r") val r:Double,
    @SerializedName("g") val g:Double,
    @SerializedName("b") val b:Double
)


data class _difficultyBeatmaps (

    @SerializedName("_difficulty") val _difficulty : String,
    @SerializedName("_difficultyRank") val _difficultyRank : Int,
    @SerializedName("_beatmapFilename") var _beatmapFilename : String,

    @SerializedName("_noteJumpMovementSpeed") val _noteJumpMovementSpeed : Double,
    @SerializedName("_noteJumpStartBeatOffset") val _noteJumpStartBeatOffset : Double,

    @SerializedName("_customData") val _customData : _customData

)

data class _difficultyBeatmapSets (

    @SerializedName("_beatmapCharacteristicName") val _beatmapCharacteristicName : String,
    @SerializedName("_difficultyBeatmaps") val _difficultyBeatmaps : List<_difficultyBeatmaps>
)

