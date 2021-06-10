package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.kit.pse.a1sicht.database.daos.*
import edu.kit.pse.a1sicht.database.entities.*

/**
 * Data base class extends the [RoomDatabase] class and creates a
 * singleton instance for local data base. The local data base contains
 * six entities: [Employee], [Administrator], [Student], [Review],
 * [Timeslot] and [StudentTimeslot].
 *
 * @author Tihomir Georgiev
 */
@Database(
    entities = [Employee::class, Administrator::class, Student::class, Review::class,
        Timeslot::class, StudentTimeslot::class],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    /**
     * Employee data access object for the [Employee] entity
     */
    abstract fun employeeDao(): EmployeeDao

    /**
     * Administrator data access object for the [Administrator] entity
     */
    abstract fun adminDao(): AdministratorDao

    /**
     * Student data access object for the [Student] entity
     */
    abstract fun studentDao(): StudentDao

    /**
     * Review data access object for the [Review] entity
     */
    abstract fun reviewDao(): ReviewDao

    /**
     * Time slot data access object for [Timeslot] entity
     */
    abstract fun timeslotDao(): TimeslotDao

    /**
     * Student time slot data access object for [StudentTimeslot] entity
     */
    abstract fun studentTimeslotDao(): StudentTimeslotDao

    /**
     * Companion object for the class, so the [getInstance]
     * method can be called without an instance of the class
     */
    companion object {
        /**
         * Instance of the local data base class
         */
        @Volatile
        private var INSTANCE: edu.kit.pse.a1sicht.database.LocalDatabase? = null

        /**
         * Gets an instance of the data base class
         * @param context [Context] type variable
         * @return Instance of the local data base
         */
        fun getInstance(context: Context): edu.kit.pse.a1sicht.database.LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "Word_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
