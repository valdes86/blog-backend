package me.pvaldes.blog.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.pvaldes.blog.controller.DTO.BlogpostRequest
import me.pvaldes.blog.entities.Blogpost
import me.pvaldes.blog.repository.BlogpostRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BlogpostServiceTests {

    private var blogpostRepository : BlogpostRepository = mockk()
    private val blogpostService : BlogpostService = BlogpostService(blogpostRepository)

    private val blogpost = Blogpost(ObjectId.get(), "title", "text")
    private val blogpostRequest = BlogpostRequest("title", "text")
    private val blogpostUpdate = blogpost.copy(title = "textUpdate")
    private val blogpostUpdateRequest = blogpostRequest.copy(title= "textUpdate")
    private val blogposts = emptyList<Blogpost>()

    @Test
    fun `When get blogposts then return list of Blogposts`() {

        //given
        every { blogpostRepository.findAll() } returns blogposts

        //when
        val result = blogpostService.getAllBlogposts()

        //then
        verify(exactly = 1) { blogpostRepository.findAll()}
        assertEquals(blogposts, result)
    }

    @Test
    fun `When get blogpost then return Blogpost`() {

        //given
        every { blogpostRepository.findOneById(blogpost.id) } returns blogpost

        //when
        val result = blogpostService.getBlogpost(blogpost.id.toString())

        //then
        verify(exactly = 1) { blogpostRepository.findOneById(blogpost.id)}
        assertEquals(blogpost, result)
    }

    @Test
    fun `When add blogpost then return added blogpost`() {

        //given
        every { blogpostRepository.save(any()) } returns blogpost

        //when
        val result = blogpostService.addBlogpost(blogpostRequest)

        //then
        verify(exactly = 1) { blogpostRepository.save(any()) }
        assertEquals(blogpost, result)
    }

    @Test
    fun `When update blogpost and already exists then return blogpost`() {

        //given
        every { blogpostRepository.existsById(blogpost.id.toString())} returns true
        every { blogpostRepository.findOneById(blogpost.id) } returns blogpost
        every { blogpostRepository.save(blogpostUpdate) } returns blogpostUpdate

        //when
        val result = blogpostService.updateBlogpost(blogpost.id.toString(), blogpostUpdateRequest)

        //then
        verify(exactly = 1) { blogpostRepository.save(blogpostUpdate)}
        assertEquals(result, blogpostUpdate)
        assertNotEquals(result, blogpost)
    }

    @Test
    fun `When update blogpost and not exists then return null`() {

        //given
        every { blogpostRepository.existsById(any())} returns false

        //when
        val result = blogpostService.updateBlogpost(blogpost.id.toString(), blogpostRequest)

        //then
        verify(exactly = 0) { blogpostRepository.save(blogpostUpdate)}
        assertNull(result)
    }

    @Test
    fun `When delete blogpost then return deleted blogpost`() {

        //given
        every { blogpostRepository.existsById(any())} returns true
        every { blogpostRepository.deleteById(blogpost.id.toString()) } returns Unit

        //when
        val result = blogpostService.deleteBlogpost(blogpost.id.toString())

        //then
        verify(exactly = 1) { blogpostRepository.deleteById(blogpost.id.toString()) }
        assertEquals(blogpost.id.toString(), result)
    }

    @Test
    fun `When delete blogpost that not exists then return deleted blogpost`() {

        //given
        every { blogpostRepository.existsById(any())} returns false
        every { blogpostRepository.deleteById(blogpost.id.toString()) } returns Unit

        //when
        val result = blogpostService.deleteBlogpost(blogpost.id.toString())

        //then
        verify(exactly = 0) { blogpostRepository.deleteById(blogpost.id.toString())}
        assertNull(result)
    }
}