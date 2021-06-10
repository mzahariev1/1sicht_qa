package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.StudentDao
import edu.kit.pse.a1sicht.database.entities.Student

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class StudentAsyncTask {

    /**
     * This method executes delete all operation in [StudentDao] class on background.
     *
     * @param studentDao This is student data access object from where the operation is called
     */
    fun deleteStudent(studentDao: StudentDao){
        DeleteStudentAsyncTask(studentDao).execute()
    }

    /**
     * This method executes insert operation in [StudentDao] class on background.
     *
     * @param studentDao This is student data access object from where the operation is called
     * @param student This is the student that needs to be inserted
     */
    fun insertStudent(studentDao: StudentDao,student: Student){
        InsertStudentAsyncTask(studentDao).execute(student)
    }

    /**
     * This method executes update operation in [StudentDao] class on background.
     *
     * @param studentDao This is student data access object from where the operation is called
     * @param student This is the student that needs to be updated
     */
    fun updateEmployee(studentDao: StudentDao,student: Student){
        UpdateEmployeeAsyncTask(studentDao).execute(student)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteStudentAsyncTask constructor(private val studentDao: StudentDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            studentDao.delete()
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertStudentAsyncTask constructor(private val studentDao: StudentDao) :
        AsyncTask<Student, Void, Void>() {

        override fun doInBackground(vararg student: Student): Void? {
            studentDao.insert(student[0])
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class UpdateEmployeeAsyncTask constructor(private val studentDao: StudentDao) :
        AsyncTask<Student, Void, Void>() {

        override fun doInBackground(vararg student: Student): Void? {
            studentDao.update(student[0])
            return null
        }
    }
}