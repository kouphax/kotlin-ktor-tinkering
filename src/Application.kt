package com.example

import com.example.routes.*
import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused")
fun Application.application() {

    initDatabase()
    initAuth()

    install(ContentNegotiation) {
        json()
    }
    
    routing {
        authenticate {
            route("/api") {
                get("/message") {
                    call.respond(mapOf("message" to UUID.randomUUID().toString()))
                }
            }
        }

        logout()
        loginForm()
        login()
    }
}

fun Application.initAuth() {
    install(Sessions) {
        cookie<UserIdPrincipal>("AUTH_COOKIE")
    }

    install(Authentication) {
        form("auth") {
            challenge {
                call.respond(HttpStatusCode.Forbidden)
            }
            validate { (name, password) ->
                val authenticatedUser = transaction {
                    Login.find { Logins.username eq name }.firstOrNull {
                        Bcrypt.verify(password, it.password.toByteArray())
                    }
                }

                if (authenticatedUser != null) UserIdPrincipal(name) else null
            }
        }

        session<UserIdPrincipal> {
            challenge {
                call.respond(HttpStatusCode.Forbidden)
            }
            validate { session -> session }
        }
    }
}

