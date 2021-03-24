package com.example

import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun PipelineContext<Unit, ApplicationCall>.principal(): UserIdPrincipal {
    return call.principal()!!
}

@Suppress("unused")
fun Application.application() {

    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    transaction {
        SchemaUtils.create(Logins)
        Login.new {
            username = "james"
            password = Bcrypt.hash("password", 12).decodeToString()
        }

    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        authenticate {
            route("/") {
                get {
                    val user = principal()
                    call.respondHtml {
                        head {
                            title { +"Header" }
                        }
                        body {
                            h1 { +user.name }

                            a(href = "/logout") {
                                +"Logout"
                            }
                        }
                    }
                }
            }
        }
    }
}

