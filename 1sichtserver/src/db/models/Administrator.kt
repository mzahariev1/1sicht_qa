package db.models

/**
 * The Administrator class represents the model of an administrator in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class Administrator(val id: Int?, val googleId: String, val firstName: String, val lastName: String)