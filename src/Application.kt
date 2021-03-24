package com.example

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.HttpStatusCode.Companion.ResetContent
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.application() {
    routing {
        authenticate {
            route("/") {
                get {
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.respondHtml {
                        head {
                            title { +"Header" }
                        }
                        body {
                            h1 {
                                +"Yay ${principal.name}"
                            }
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

