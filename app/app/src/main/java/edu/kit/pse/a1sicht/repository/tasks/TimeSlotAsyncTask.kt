package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.TimeslotDao
import edu.kit.pse.a1sicht.database.entities.Timeslot

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class TimeSlotAsyncTask {

    /**
     * This method executes delete operation in [TimeslotDao] class on background.
     *
     * @param timeSlotDao This is time slot data access object from where the operation is called
     */
    fun deleteTimeslot(timeSlotDao: TimeslotDao) {
        DeleteTimeslotAsyncTask(timeSlotDao).execute()
    }

    /**
     * This method executes insert operation in [TimeslotDao] class on background.
     *
     * @param timeSlotDao This is time slot data access object from where the operation is called
     * @param timeSlot This is the time slot that needs to be inserted
     */
    fun insertTimeslot(timeSlotDao: TimeslotDao, timeSlot: Timeslot) {
        InsertTimeslotAsyncTask(timeSlotDao).execute(timeSlot)
    }

    /**
     * This method executes delete by id operation in [TimeslotDao] class on background.
     *
     * @param timeSlotDao This is time slot data access object from where the operation is called
     * @param id This is the id of the time slot that needs to be deleted
     */
    fun deleteTimeSlotById(timeSlotDao: TimeslotDao, id: Int) {
        DeleteTimeslotByIdAsyncTask(timeSlotDao).execute(id)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteTimeslotAsyncTask constructor(private val timeSlotDao: TimeslotDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg param: Void?): Void? {
            timeSlotDao.delete()
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteTimeslotByIdAsyncTask constructor(private val timeSlotDao: TimeslotDao) :
        AsyncTask<Int, Void, Void>() {

        override fun doInBackground(vararg id: Int?): Void? {
            timeSlotDao.deleteById(id[0]!!)
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertTimeslotAsyncTask constructor(private val timeSlotDao: TimeslotDao) :
        AsyncTask<Timeslot, Void, Void>() {

        override fun doInBackground(vararg timeSlot: Timeslot): Void? {
            timeSlotDao.insert(timeSlot[0])
            return null
        }
    }

}