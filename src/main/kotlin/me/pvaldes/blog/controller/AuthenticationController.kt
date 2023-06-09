package me.pvaldes.blog.controller

import me.pvaldes.blog.entities.UserLogin
import me.pvaldes.blog.service.LoginService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*


@Controller
@RequestMapping("/api/V1/authentication")
class AuthenticationController(var loginService: LoginService) {

    @GetMapping("/me")
    fun getUserExtra(token: JwtAuthenticationToken): UserLogin? {
        return loginService.validateAndGetUserExtra(token.name)
    }

    @PostMapping("/me")
    fun saveUserExtra(
        @RequestBody updateUserExtraRequest: UserLogin ,
        token: JwtAuthenticationToken
    ): UserLogin? {
        val userExtra: UserLogin = loginService.getUserExtra(token.name)
        return loginService.saveUserExtra(userExtra)
    }

}