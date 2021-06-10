package edu.kit.pse.a1sicht.database.entities

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import edu.kit.pse.a1sicht.networking.RetrofitClient
import java.sql.Timestamp
import java.util.*

/**
 * The Timeslot data class represents an entity in the local database.
 *
 * @param id the unique time slot's id
 * @param startTime the start time of the time slot
 * @param duration the duration of the time slot
 * @param maxStudents the maximum number of students for time slot
 * @param currentStudents the number of registered students for time slot
 * @param belongsTo the id of the review of the time slot
 *
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@Entity(
    tableName = "TimeSlots",
    indices = [Index(value = ["id"], unique = true),
        Index(value = ["belongs_To"])]
)
data class Timeslot(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose var id: Int?,
    @JsonAdapter(RetrofitClient.DateAdapter::class)
    @ColumnInfo(name = "start_Time") @SerializedName("startTime") @Expose var startTime: Timestamp?,
    @ColumnInfo(name = "duration") @SerializedName("duration") @Expose var duration: Int,
    @ColumnInfo(name = "max_Students") @SerializedName("maxNumberOfStudents") @Expose var maxStudents: Int,
    @ColumnInfo(name = "current_Students")@SerializedName("currentNumberOfStudents") @Expose var currentStudents: Int,
    @ColumnInfo(name = "belongs_To") @SerializedName("belongsTo") @Expose var belongsTo: Int
){
    /**
     * Empty constructor
     */
    constructor() : this(0, Timestamp(0,0,0,0,0,0,0), 0, 0, 0,0)
}