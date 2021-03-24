package com.example

import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("unused")
fun Application.authentication() {

    fun auth(username: String, password: String) = transaction {
        Login.find { Logins.username eq username }.firstOrNull {
            Bcrypt.verify(password, it.password.toByteArray())
        } != null
    }

    install(Sessions) {
        cookie<UserIdPrincipal>("AUTH_COOKIE")
    }

    install(Authentication) {
        form("auth") {
            challenge("/login")
            validate { (name, password) ->
                if(auth(name, password)) UserIdPrincipal(name) else null
            }
        }

        session<UserIdPrincipal> {
            challenge("/login")
            validate { session -> session }
        }
    }

    routing {
        authenticate("auth") {
            route("/login") {
                post {
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.sessions.set(principal)
                    call.respondRedirect("/")
                }
            }
        }

        authenticate {
            route("/logout") {
                get {
                    call.sessions.clear<UserIdPrincipal>()
                    call.respondRedirect("/")
                }
            }
        }


        route("/login") {
            get {
                call.respondHtml {
                    head {
                        title { +"Header" }
                    }
                    body {
                        h1 {
                            +"Login"
                        }
                        div {
                            form("/login", method = post) {
                                div {
                                    label {
                                        +"Username: "
                                        input(name = "user")
                                    }
                                }
                                div {
                                    label {
                                        +"Password: "
                                        input(name = "password")
                                    }
                                }
                                div {
                                    input(submit) { value = "login" }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
