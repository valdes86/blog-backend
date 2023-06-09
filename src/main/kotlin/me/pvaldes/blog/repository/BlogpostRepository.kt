package me.pvaldes.blog.repository

import me.pvaldes.blog.entities.Blogpost
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogpostRepository : MongoRepository<Blogpost, String> {
    fun findOneById(id: ObjectId): Blogpost
}