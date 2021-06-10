package edu.kit.pse.a1sicht.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The Employee data class represents an entity in the local database.
 *
 * @param googleID the employee's id from google
 * @param id the unique employee's id
 * @param firstName the employee's first name
 * @param lastName the employee's last name
 *
 * @author Tihomir Georgiev
 */
@Entity(
    tableName = "Employees",
    indices = [Index(value = ["id"], unique = true)]
)
data class Employee(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose var id: Int?,
    @ColumnInfo(name = "googleId") @SerializedName("googleId") @Expose var googleID: String?,
    @ColumnInfo(name = "firstName") @SerializedName("firstName") @Expose var firstName: String?,
    @ColumnInfo(name = "lastName") @SerializedName("lastName") @Expose var lastName: String?,
    @ColumnInfo(name = "verified") @SerializedName("verified") @Expose var verified: Boolean
) {
    /**
     * Empty constructor
     */
    constructor() : this(0, "", "", "", false)
}
