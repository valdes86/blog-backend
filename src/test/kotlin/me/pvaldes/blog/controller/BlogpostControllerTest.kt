package me.pvaldes.blog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.pvaldes.blog.controller.DTO.BlogpostRequest
import me.pvaldes.blog.entities.Blogpost
import me.pvaldes.blog.service.BlogpostService
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class BlogpostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var blogpostService: BlogpostService

    var blogUrl = "/api/V1/blogpost"

    @Test
    fun `Assert not logged in trying to add a blogpost gets 401 error`() {

        //Given
        val blogpostRequest = BlogpostRequest("title", "text")
        val blogpost = Blogpost(ObjectId.get(),"title", "text")

        given(blogpostService.addBlogpost(blogpostRequest))
            .willReturn(blogpost)

        //when
        val response = mockMvc.perform(post(blogUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(blogpostRequest)))

        //then
        response.andExpect(status().isUnauthorized)
    }

    @Test
    fun `Assert add blogpost retrieves blogposts`() {

        //Given
        val blogpostRequest = BlogpostRequest("title", "text")
        val blogpost = Blogpost(ObjectId.get(),"title", "text")

        given(blogpostService.addBlogpost(blogpostRequest))
            .willReturn(blogpost)

        //when
        val response = mockMvc.perform(post(blogUrl)
            .with(SecurityMockMvcRequestPostProcessors.jwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(blogpostRequest)))

        //then
        response.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("title"))
            .andExpect(jsonPath("$.text").value("text"))
            .andExpect(jsonPath("$.published").value(false))
    }

    @Test
    fun `Assert get blogpost by id retrieves blogpost`() {

        //Given
        val id = ObjectId.get()
        val blogpost = Blogpost(id, "title", "text")

        given(blogpostService.getBlogpost(id.toString()))
            .willReturn(blogpost)

        //When
        val response = mockMvc.perform(get("$blogUrl/${id}")
            .contentType(MediaType.APPLICATION_JSON))

        //Then
        response.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("title"))
            .andExpect(jsonPath("$.text").value("text"))
    }

    @Test
    fun `Assert get all blogposts retrieves blogposts`() {

        //Given
        val allBlogposts: Iterable<Blogpost> = listOf(
            Blogpost(ObjectId.get(),"title1", "text1"),
            Blogpost(ObjectId.get(),"title2", "text2")
        )

        given(blogpostService.getAllBlogposts())
            .willReturn(allBlogposts)

        //When
        val response = mockMvc.perform(get(blogUrl)
            .contentType(MediaType.APPLICATION_JSON))

        //Then
        response.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].title").value("title1"))
            .andExpect(jsonPath("$[0].text").value("text1"))
            .andExpect(jsonPath("$[1].id", notNullValue()))
            .andExpect(jsonPath("$[1].title").value("title2"))
            .andExpect(jsonPath("$[1].text").value("text2"))
    }

    @Test
    fun `Assert not logged in trying to update a blogpost gets 401 error`() {

        //Given
        val id = ObjectId.get()
        val blogpost = Blogpost(ObjectId.get(), "title","test")
        val update = BlogpostRequest("updated title", "updated text")

        given(blogpostService.updateBlogpost(id.toString(), update))
            .willReturn(blogpost.copy(title= update.title, text=update.text))

        //When
        val response = mockMvc.perform(put("$blogUrl/${id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(update)))

        //then
        response.andExpect(status().isUnauthorized)
    }

    @Test
    fun `Assert update blogpost retrieves updated blogpost`() {

        //Given
        val id = ObjectId.get()
        val blogpost = Blogpost(ObjectId.get(), "title","test")
        val update = BlogpostRequest("updated title", "updated text")

        given(blogpostService.updateBlogpost(id.toString(), update))
            .willReturn(blogpost.copy(title= update.title, text=update.text))

        //When
        val response = mockMvc.perform(put("$blogUrl/${id}")
            .with(SecurityMockMvcRequestPostProcessors.jwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(update)))

        //Then
        response.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("updated title"))
            .andExpect(jsonPath("$.text").value("updated text"))
    }

    @Test
    fun `Assert if trying to update non existing blogpost retrieves null`() {

        //Given
        val id = ObjectId.get()
        val update = BlogpostRequest("updated title", "updated text")

        given(blogpostService.updateBlogpost(id.toString(), update))
            .willReturn(null)

        //When
        val response = mockMvc.perform(put("$blogUrl/${id}")
            .with(SecurityMockMvcRequestPostProcessors.jwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(update)))

        //Then
        response.andExpect(status().isNotFound)
    }

    @Test
    fun `Assert not logged in trying to delete a blogpost gets 401 error`() {

        //Given
        val id = ObjectId.get()

        given(blogpostService.deleteBlogpost(id.toString()))
            .willReturn(id.toString())

        //When
        val response = mockMvc.perform((delete("$blogUrl/${id}"))
        )

        //then
        response.andExpect(status().isUnauthorized)
    }
    @Test
    fun `Assert delete blogpost retrieves blogposts`() {
        //Given
        val id = ObjectId.get()

        given(blogpostService.deleteBlogpost(id.toString()))
            .willReturn(id.toString())

        //When
        val response = mockMvc.perform((delete("$blogUrl/${id}"))
            .with(SecurityMockMvcRequestPostProcessors.jwt())
        )

        //Then
        response.andExpect((status().isOk))
    }
}