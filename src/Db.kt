package com.example

import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object Logins: IntIdTable() {
    val username = varchar("username", 255).uniqueIndex()
    val password = text("password")
}

class Login(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Login>(Logins)

    var username by Logins.username
    var password by Logins.password
}

fun Application.initDatabase(): Database {
    val db = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    transaction {
        SchemaUtils.create(Logins)
        Login.new {
            username = "james"
            password = Bcrypt.hash("password", 12).decodeToString()
        }
    }

    return db
}