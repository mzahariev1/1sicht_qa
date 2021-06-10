package edu.kit.pse.a1sicht.database

import androidx.room.TypeConverter
import java.sql.Time
import java.sql.Timestamp
import java.util.*

/**
 * This class converts long type into Timestamp type and
 * Timestamp type into long. This class is needed, because Timestamp
 * type instance can not be placed in the data base.
 *
 * @author Tihomir Georgiev
 */

class Converters {
    /**
     * Converts long variable into Timestamp type variable
     * @param value Variable to be converted
     * @return Timestamp type instance
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    /**
     * Converts Timestamp variable into long type variable
     * @param date Timestamp variable to be converted
     * @return Long type instance
     */
    @TypeConverter
    fun dateToTimestamp(date: Timestamp?): Long? {
        return date?.time
    }
}