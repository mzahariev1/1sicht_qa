package db.models

/**
 * The Employee class represents the model of an employee in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class Employee(val id: Int?, val googleId: String, val firstName: String, val lastName: String,
                    val verified: Boolean)