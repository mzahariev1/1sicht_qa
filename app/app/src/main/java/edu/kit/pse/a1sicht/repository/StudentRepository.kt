package edu.kit.pse.a1sicht.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.repository.tasks.AdministratorAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.EmployeeAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.StudentAsyncTask
import kotlin.Exception

/**
 * The [StudentRepository] class gives read and write access to Student objects to the local and server database
 *
 * @author Maximilian Ackermann, Stanimir Bozhilov, Martin Zahariev
 */
class StudentRepository(localDatabase: LocalDatabase, private val studentService: StudentService) {

    private val adminDao = localDatabase.adminDao()
    private val employeeDao = localDatabase.employeeDao()
    private val studentDao = localDatabase.studentDao()

    private val employeeAsyncTask: EmployeeAsyncTask = EmployeeAsyncTask()
    private val administratorAsyncTask: AdministratorAsyncTask = AdministratorAsyncTask()
    private val studentAsyncTask: StudentAsyncTask = StudentAsyncTask()

    /**
     * Creates a new Student object.
     * Inserts the Student via Dao and service classes into the local and server database.
     * @param [googleID] A String object representing the unique Google ID of the student.
     * @param [firstName] A String object representing the first name of the student.
     * @param [lastName] A String object representing the last name of the student.
     * @param [matriculationNumber] An Integer representing the matriculation number of the student
     * @return [Student] The newly created Student
     */
    fun createStudent(
        googleID: String,
        firstName: String,
        lastName: String,
        matriculationNumber: Int
    ): Student? {

        administratorAsyncTask.deleteAdministrator(adminDao)
        employeeAsyncTask.deleteEmployee(employeeDao)
        studentAsyncTask.deleteStudent(studentDao)

        var newStudent = Student(0, googleID, firstName, lastName, matriculationNumber)

        newStudent = studentService.create(newStudent).blockingFirst()
        studentAsyncTask.insertStudent(studentDao, newStudent)

        return newStudent
    }

    /**
     * This method returns a student user from the local database.
     * @return Live data object of type student
     */
    fun getStudent(): LiveData<Student> {
        return studentDao.get()
    }

    /**
     * This method inserts a student user in local database.
     * @param student The student to be inserted
     */
    fun insertStudent(student: Student) {
        studentAsyncTask.insertStudent(studentDao, student)
    }

    /**
     * This method delete all student users in the local database.
     */
    fun deleteStudent() {
        studentAsyncTask.deleteStudent(studentDao)
    }

    /**
     * This method gets all students from the remote database
     * @return [List<Student>] containing all students on the server.
     */
    fun getAllStudents(): List<Student>? {
        return try {
            studentService.getAll().blockingFirst()
        } catch (error: Exception) {
            //Return empty list on error
            listOf()
        }
    }

    /**
     * This method gets a student by a given ID number from the remote database
     * @param [studentId] The unique ID number of the student
     * @return [Student] with the given ID number
     */
    fun getStudentById(studentId: Int): Student? {
        return try {
            getAllStudents()?.firstOrNull { it.id == studentId }
        } catch (error: Exception) {
            //Return empty list on error
            null
        }
    }

    /**
     * This method gets all students which have signed up for a review with a given ID number
     * @param [reviewId] The unique ID number of the review
     * @return [List<Student>] containing all students which have signed up for this review
     */
    fun getStudentsByReview(reviewId: Int): List<Student>? {
        return try {
            studentService.getForReview(reviewId).blockingFirst()
        } catch (error: java.lang.Exception) {
            //Return empty list on error
            listOf()
        }
    }

    /**
     * This method gets all students which have signed up for a timeslot with a given ID number
     * @param [timeslotId] The unique ID number of the timeslot
     * @return [List<Student>] containing all students which have signed up for this timeslot
     */
    fun getStudentsByTimeslot(timeslotId: Int): List<Student>? {
        return try {
            studentService.getForTimeslot(timeslotId).blockingFirst()
        } catch (error: java.lang.Exception) {
            //Return empty list on error
            listOf()
        }
    }

    /**
     * This method updates a student by a given ID number
     * @param [studentId] The unique ID number of the student which is to be updated
     * @param [student] A Student object holding the new data.
     * @return [true] if the update was successful, [false] otherwise
     */
    @SuppressLint("CheckResult")
    fun updateStudentById(studentId: Int, student: Student): Boolean {
        return try {
            studentService.updateById(studentId, student).blockingFirst()
            studentAsyncTask.updateEmployee(studentDao, student)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Deletes a Student that has the given [studentId].
     * @param [studentId] The unique ID number of the student which is to be deleted
     * @return [Student] The deleted Student
     */
    fun deleteStudentById(studentId: Int): Student? {
        return if (studentDao.get().value?.id == studentId) {
            null
        } else {
            studentService.deleteById(studentId).blockingFirst()
        }
    }
}
