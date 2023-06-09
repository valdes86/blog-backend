package me.pvaldes.blog.repository

import me.pvaldes.blog.entities.UserLogin
import org.springframework.data.mongodb.repository.MongoRepository

interface UserLoginRepository : MongoRepository<UserLogin, String>