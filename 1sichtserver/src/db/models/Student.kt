package db.models

/**
 * The Student class represents the model of a student in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class Student(val id: Int?, val googleId: String, val firstName: String, val lastName: String,
                   val matriculationNumber: Int)