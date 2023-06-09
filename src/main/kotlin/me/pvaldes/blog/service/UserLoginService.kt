package me.pvaldes.blog.service

import me.pvaldes.blog.entities.UserLogin
import me.pvaldes.blog.repository.UserLoginRepository
import org.springframework.stereotype.Service


@Service
class LoginService( var userLoginRepository: UserLoginRepository) {

    fun validateAndGetUserExtra(username: String?): UserLogin = getUserExtra(username)

    fun getUserExtra(username: String?): UserLogin = userLoginRepository.findById(username!!).get()

    fun saveUserExtra(userExtra: UserLogin?): UserLogin = userLoginRepository.save(userExtra!!)
}