package repository

import db.models.Administrator
import db.tables.Administrators
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * The AdminRepository class contains the methods which manipulate the data in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
class AdminRepository {

    /**
     * This method creates a new tuple in the Administrators table in the database
     * @param administrator This object holds the attributes of the newly created tuple
     */
    fun create(administrator: Administrator): Administrator? {
        return transaction {
            val adminId = Administrators.insert {
                it[googleId] = administrator.googleId
                it[firstName] = administrator.firstName
                it[lastName] = administrator.lastName
            } get Administrators.id

            return@transaction Administrators.select { Administrators.id eq adminId }
                .map {
                    Administrator(
                        id = it[Administrators.id],
                        googleId = it[Administrators.googleId],
                        firstName = it[Administrators.firstName],
                        lastName = it[Administrators.lastName]
                    )
                }.firstOrNull()

        }
    }

    /**
     * This method gets all administrators from the database
     * @return A list with Administrator objects representing all tuples from the Administrators table in the database
     */
    fun getAll(): List<Administrator> {
        return transaction {
            return@transaction Administrators.selectAll()
                .map {
                    Administrator(
                        id = it[Administrators.id],
                        googleId = it[Administrators.googleId],
                        firstName = it[Administrators.firstName],
                        lastName = it[Administrators.lastName]
                    )
                }
        }
    }

    /**
     * This methods fetches the administrator with the given ID number from the database
     * @param adminId An Integer representing the unique identification number of the administrator
     * @return An @see<Administrator> object representing the tuple with the given ID number
     * or null if such a tuple does not exist.
     */
    fun getById(adminId: Int): Administrator? {
        return transaction {
            return@transaction Administrators.select { Administrators.id eq adminId }
                .map {
                    Administrator(
                        id = it[Administrators.id],
                        googleId = it[Administrators.googleId],
                        firstName = it[Administrators.firstName],
                        lastName = it[Administrators.lastName]
                    )
                }.firstOrNull()

        }
    }

    /**
     * This method updates the data for an administrator with the given ID number.
     * @param adminId An Integer representing the unique identification number of the tuple to be updated
     * @param administrator An Administrator object holding the new attributes of the tuple
     */
    fun updateById(adminId: Int, administrator: Administrator): Administrator? {
        return transaction {
            Administrators.update({ Administrators.id eq adminId }) {
                it[firstName] = administrator.firstName
                it[lastName] = administrator.lastName
                //it[googleId] = administrator.googleId
            }

            return@transaction Administrators.select { Administrators.id eq adminId }
                .map {
                    Administrator(
                        id = it[Administrators.id],
                        googleId = it[Administrators.googleId],
                        firstName = it[Administrators.firstName],
                        lastName = it[Administrators.lastName]
                    )
                }.firstOrNull()

        }
    }

    /**
     * This method deletes a tuple with the given ID number from the Administrators table
     * @param adminId An Integer representing the unique identification number of the tuple to be deleted
     */
    fun deleteById(adminId: Int): Administrator? {
        return transaction {
            val deletedAdmin = Administrators.select { Administrators.id eq adminId }
                .map {
                    Administrator(
                        id = it[Administrators.id],
                        googleId = it[Administrators.googleId],
                        firstName = it[Administrators.firstName],
                        lastName = it[Administrators.lastName]
                    )
                }.firstOrNull()

            Administrators.deleteWhere { Administrators.id eq adminId }

            return@transaction deletedAdmin
        }
    }
}