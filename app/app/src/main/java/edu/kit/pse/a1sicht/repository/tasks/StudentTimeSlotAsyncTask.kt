package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.StudentTimeslotDao
import edu.kit.pse.a1sicht.database.entities.StudentTimeslot

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class StudentTimeSlotAsyncTask {

    /**
     * This method executes delete operation in [StudentTimeslotDao] class on background.
     *
     * @param stTimeSlotDao This is student time slot data access object from where the operation is called
     * @param stTimeSlot This is the student time slot that needs to be deleted
     */
    fun deleteStudentTimeslot(stTimeSlotDao: StudentTimeslotDao, stTimeSlot: StudentTimeslot) {
        DeleteStudentTimeslotAsyncTask(stTimeSlotDao).execute(stTimeSlot)
    }

    /**
     * This method executes insert operation in [StudentTimeslotDao] class on background.
     *
     * @param stTimeSlotDao This is student time slot data access object from where the operation is called
     * @param stTimeSlot This is the student time slot that needs to be inserted
     */
    fun insertStudentTimeslot(stTimeSlotDao: StudentTimeslotDao, stTimeSlot: StudentTimeslot) {
        InsertStudentTimeslotAsyncTask(stTimeSlotDao).execute(stTimeSlot)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteStudentTimeslotAsyncTask constructor(private val stTimeSlotDao: StudentTimeslotDao) :
        AsyncTask<StudentTimeslot?, Void, Void>() {

        override fun doInBackground(vararg stTimeSlot: StudentTimeslot?): Void? {
            stTimeSlotDao.delete(stTimeSlot[0]!!)
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertStudentTimeslotAsyncTask constructor(private val stTimeSlotDao: StudentTimeslotDao) :
        AsyncTask<StudentTimeslot, Void, Void>() {

        override fun doInBackground(vararg stTimeSlot: StudentTimeslot): Void? {
            stTimeSlotDao.insert(stTimeSlot[0])
            return null
        }
    }
}