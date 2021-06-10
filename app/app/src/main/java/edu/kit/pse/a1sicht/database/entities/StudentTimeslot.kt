package edu.kit.pse.a1sicht.database.entities

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The StudentTimeslot data class represents an entity in the local database.
 *
 * @param studentID the student's id
 * @param timeSlotID the timeslot's id
 *
 * @author Tihomir Georgiev
 */
@Entity(
    tableName = "StudentTimeslot",
    primaryKeys = ["student_id","timeSlot_id"],
    indices = [Index(value = ["timeSlot_id"]),
        Index(value = ["student_id"])]
)
data class StudentTimeslot(
    @ColumnInfo(name = "student_id") @SerializedName("studentId") @Expose var studentID: Int,
    @ColumnInfo(name = "timeSlot_id") @SerializedName("timeslotId") @Expose var timeSlotID: Int
) {
    /**
     * Empty constructor
     */
    constructor() : this(0, 0)
}