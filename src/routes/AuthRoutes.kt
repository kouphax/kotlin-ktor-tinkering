package com.example.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*

fun Route.logout() {
    authenticate {
        get("/logout") {
            call.sessions.clear<UserIdPrincipal>()
            call.respondRedirect("/")
        }
    }
}

fun Route.loginForm() {
    get("/login") {
        call.respondHtml {
            head {
                title { +"Header" }
            }
            body {
                h1 {
                    +"Login"
                }
                div {
                    form("/login", method = FormMethod.post) {
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
                            input(InputType.submit) { value = "login" }
                        }
                    }
                }
            }
        }
    }
}

fun Route.login() {
    authenticate("auth") {
        post("/login") {
            val principal = call.principal<UserIdPrincipal>()!!
            call.sessions.set(principal)
            call.respondRedirect("/")
        }
    }
}