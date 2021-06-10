package edu.kit.pse.a1sicht.database.entities

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import edu.kit.pse.a1sicht.networking.RetrofitClient
import java.sql.Timestamp

/**
 * The Review data class represents an entity in the local database.
 *
 * @param id the unique review's id
 * @param name the name of the review
 * @param room the place where review takes place
 * @param date the time when review starts
 * @param timeSlotLength the length of the time slots of the review
 * @param numberOfTimeSlots the number of time slots for the review
 * @param employee_id the id of the employee who has created the review
 * @param info additional information about the review
 *
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@Entity(
    tableName = "Reviews",
    indices = [Index(value = ["review_id"], unique = true),
        Index(value = ["employee_id"])]
)
data class Review(
    @PrimaryKey @ColumnInfo(name = "review_id") @SerializedName("id")@Expose  val id: Int?,
    @ColumnInfo(name = "name")  @SerializedName("name")@Expose var name: String?,
    @ColumnInfo(name = "room")  @SerializedName("location")@Expose var room: String?,
    @JsonAdapter(RetrofitClient.DateAdapter::class)
    @ColumnInfo(name = "date")  @SerializedName("date")@Expose var date: Timestamp?,
    @ColumnInfo(name = "timeSlotLength") @SerializedName("lengthOfTimeslot")@Expose  var timeSlotLength: Int,
    @ColumnInfo(name = "numberOfTimeSlots") @SerializedName("numberOfTimeslots")@Expose  var numberOfTimeSlots: Int,
    @ColumnInfo(name = "employee_id") @SerializedName("createdBy")@Expose var employee_id: Int,
    @ColumnInfo(name = "info") @SerializedName("description")@Expose var info: String?
) {
    /**
     * Empty constructor
     */
    constructor() : this(0, "", "", Timestamp(0, 0, 0, 0, 0, 0,0), 0, 0, 0, "")
}