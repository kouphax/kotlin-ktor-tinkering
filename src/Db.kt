package com.example

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Logins: IntIdTable() {
    val username = varchar("username", 255).uniqueIndex()
    val password = text("password")
}

class Login(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Login>(Logins)

    var username by Logins.username
    var password by Logins.password
}
