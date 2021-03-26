package com.example.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.*

fun Route.homePage() {
    authenticate {
        get("/") {
            val user = call.principal<UserIdPrincipal>()!!
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
