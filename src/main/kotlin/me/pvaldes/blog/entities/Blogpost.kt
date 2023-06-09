package me.pvaldes.blog.entities

import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Schema(description = "Model for a Blogpost.")
@Document
data class Blogpost(
    @Id
    val id: ObjectId = ObjectId.get(),
    val title: String?,
    val text: String?,
    val published: Boolean = false
)