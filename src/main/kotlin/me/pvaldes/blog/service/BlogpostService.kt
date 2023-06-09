package me.pvaldes.blog.service

import me.pvaldes.blog.controller.DTO.BlogpostRequest
import me.pvaldes.blog.entities.Blogpost
import me.pvaldes.blog.repository.BlogpostRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class BlogpostService (var blogRepository : BlogpostRepository){

    fun getBlogpost(id: String) = blogRepository.findOneById(ObjectId(id))
    fun getAllBlogposts():Iterable<Blogpost> = blogRepository.findAll()
    fun addBlogpost(blogpostRequest: BlogpostRequest):Blogpost =
        blogRepository.insert(Blogpost(title = blogpostRequest.title, text=blogpostRequest.text, published = false))
    fun updateBlogpost(id: String, blogpostRequest: BlogpostRequest):Blogpost?  {
        return if (blogRepository.existsById(id)){
            val blogpostUpdated = blogRepository
                    .findOneById(ObjectId(id))
                    .copy(title = blogpostRequest.title,  text = blogpostRequest.text )
            blogRepository.save(blogpostUpdated)
        } else {
            null
        }
    }
    fun deleteBlogpost(id: String) = blogRepository.deleteById(id)
}