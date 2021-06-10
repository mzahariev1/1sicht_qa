package edu.kit.pse.a1sicht.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The Student data class represents an entity in the local database.
 *
 * @param googleID the student's id from google
 * @param id the unique student's id
 * @param firstName the student's first name
 * @param lastName the student's last name
 * @param matriculationNumber the student's matriculation number
 *
 * @author Tihomir Georgiev
 */
@Entity(tableName = "Students")
data class Student(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id")@Expose var id: Int,
    @ColumnInfo(name = "googleId") @SerializedName("googleId") @Expose var googleID: String?,
    @ColumnInfo(name = "firstName") @SerializedName("firstName") @Expose var firstName: String?,
    @ColumnInfo(name = "lastName") @SerializedName("lastName") @Expose var lastName: String?,
    @ColumnInfo(name = "matriculationNumber") @SerializedName("matriculationNumber")@Expose var matriculationNumber: Int
){
    /**
     * Empty constructor
     */
    constructor() : this(0,"", "", "", 0)
}