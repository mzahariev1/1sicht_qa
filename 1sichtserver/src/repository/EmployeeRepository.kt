package repository

import db.models.Employee
import db.models.Review
import org.jetbrains.exposed.sql.transactions.transaction
import db.tables.Employees
import db.tables.Timeslots
import db.tables.Reviews
import org.jetbrains.exposed.sql.*

class EmployeeRepository {

    /**
     * This method creates a new tuple in the Employees table in the database
     * @param employee This object holds the attributes of the newly created tuple
     */
    fun create(employee: Employee): Employee? {
        return transaction {
            val employeeId = Employees.insert {
                it[googleId] = employee.googleId
                it[firstName] = employee.firstName
                it[lastName] = employee.lastName
                it[verified] = employee.verified
            } get Employees.id

            return@transaction Employees.select { Employees.id eq employeeId }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method gets all employees from the database
     * @return A list with Employee objects representing all tuples from the Employees table in the database
     */
    fun getAll(): List<Employee> {
        return transaction {
            return@transaction Employees.selectAll()
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }
        }
    }

    /**
     * This methods fetches the employee with the given ID number from the database
     * @param employeeId An Integer representing the unique identification number of the employee
     * @return An Employee object representing the tuple with the given ID number
     * or null if such a tuple does not exist.
     */
    fun getById(employeeId: Int): Employee? {
        return transaction {
            return@transaction Employees.select { Employees.id eq employeeId }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method updates the data for an employee with the given ID number.
     * @param employeeId An Integer representing the unique identification number of the tuple to be updated
     * @param employee An Employee object holding the new attributes of the tuple
     */
    fun updateById(employeeId: Int, employee: Employee): Employee? {
        return transaction {
            Employees.update ({ Employees.id eq employeeId }) {
                it[Employees.googleId] = employee.googleId
                it[Employees.firstName] = employee.firstName
                it[Employees.lastName] = employee.lastName
            }

            return@transaction Employees.select { Employees.id eq employeeId }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()

        }
    }

    /**
     * This method deletes a tuple with the given ID number from the Employees table
     * @param employeeIdId An Integer representing the unique identification number of the tuple to be deleted
     * @return The deleted employee from the table
     */
    fun deleteById(employeeId: Int): Employee? {
        return transaction {
            val deletedEmployee = Employees.select { Employees.id eq employeeId }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()
            val deletedReview = Reviews.select { Reviews.createdBy eq employeeId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()

            Reviews.deleteWhere { Reviews.createdBy eq employeeId }
            Employees.deleteWhere { Employees.id eq employeeId }

            return@transaction deletedEmployee
        }
    }

    /**
     * This method marks the verified attribute of the tuple with the given ID number as true
     * @param employeeId An Integer representing the unique identification number of the tuple to be modified
     * @return true if the verification was successful, false otherwise
     */
    fun verifyById(employeeId: Int): Boolean {
        return transaction {
            val employeeToVerify = Employees.select { Employees.id eq employeeId }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()

            if (employeeToVerify?.verified == true) {
                return@transaction false
            } else {
                Employees.update ({ Employees.id eq employeeId }) {
                    it[Employees.verified] = true
                }

                return@transaction true
            }
        }
    }

    /**
     * This method gets all reviews created bu the given employee
     * @param employeeId An Integer representing the unique identification number of the tuple in the database
     * @return A list containing the reviews for the given employee
     */
    fun getReviewsByEmployee(employeeId: Int): List<Review> {
        return transaction {
            return@transaction Reviews.select { Reviews.createdBy eq employeeId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }
        }
    }

    /**
     * This method gets a review with a given ID number for an employee with a given ID number
     * @param employeeId An Integer representing the unique identification number of the employee in the database
     * @param reviewId An Integer representing the unique identification number of the review in the database
     * @return A Review object belonging to the given Emplyee and with the given ID number
     */
    fun getReviewByIdByEmployee(employeeId: Int, reviewId: Int): Review? {
        return transaction {
            return@transaction Reviews.select { (Reviews.createdBy eq employeeId) and (Reviews.id eq reviewId) }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()
        }
    }

    /**
     *
     */
    fun getEmployeeByReview(reviewId: Int): Employee?{
        return transaction{
            val review = Reviews.select { Reviews.id eq reviewId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()
            return@transaction Employees.select { Employees.id eq review?.createdBy?.toInt()!! }
                .map {
                    Employee(
                        id = it[Employees.id],
                        googleId = it[Employees.googleId],
                        firstName = it[Employees.firstName],
                        lastName = it[Employees.lastName],
                        verified = it[Employees.verified]
                    )
                }.firstOrNull()
        }
    }
}