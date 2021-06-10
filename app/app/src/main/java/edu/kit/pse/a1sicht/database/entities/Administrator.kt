package edu.kit.pse.a1sicht.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The Administrator data class represents an entity in the local database.
 *
 * @param googleID the administrator's id from google
 * @param id the unique administrator's id
 * @param firstName the administrator's first name
 * @param lastName the administrator's last name
 *
 * @author Tihomir Georgiev
 */
@Entity(tableName = "Admins")
data class Administrator(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose var id: Int?,
    @ColumnInfo(name = "googleId") @SerializedName("googleId") @Expose var googleID: String?,
    @ColumnInfo(name = "firstName") @SerializedName("firstName") @Expose var firstName: String?,
    @ColumnInfo(name = "lastName")@SerializedName("lastName") @Expose var lastName: String?
) {
    /**
     * Empty constructor
     */
    constructor() : this(null, "", "", "")
}