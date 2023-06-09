package me.pvaldes.blog

import me.pvaldes.blog.entities.Blogpost
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests (@Autowired val restTemplate: TestRestTemplate) {

    var blogUrl = "/api/V1/blog"

    @Test
    fun `Assert add blogpost retrieves blogposts`() {

        val blogpost = Blogpost(ObjectId.get(), "title", "text")

        val headers = HttpHeaders()

        val request: HttpEntity<Blogpost> = HttpEntity<Blogpost>(blogpost, headers)
        val response = restTemplate
            .postForEntity<Blogpost>(blogUrl, request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(blogpost)
    }

    @Test
    fun `Assert get blogpost by id retrieves blogpost`() {

        val blogpost = Blogpost(ObjectId.get(), "title", "text")

        val request: HttpEntity<Blogpost> = HttpEntity<Blogpost>(blogpost, HttpHeaders())

        restTemplate
            .postForEntity<Blogpost>(blogUrl, request)

        val response = restTemplate
                .getForEntity<Blogpost>("$blogUrl/{id}", blogpost.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(blogpost)

    }

    @Test
    fun `Assert get all blogposts retrieves blogposts`() {

        val response = restTemplate
            .getForEntity<Iterable<Blogpost>>(blogUrl)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).size().isNotZero
    }

    @Test
    fun `Assert update blogpost retrieves blogposts`() {

        val blogpost = Blogpost(ObjectId.get(), "title","test")

        restTemplate
            .put(blogUrl, blogpost)
    }

    @Test
    fun `Assert delete blogpost retrieves blogposts`() {
        val blogpost = Blogpost(ObjectId.get(), "title","test")

        val request: HttpEntity<Blogpost> = HttpEntity<Blogpost>(blogpost, HttpHeaders())

        restTemplate
            .postForEntity<Blogpost>(blogUrl, request)

        restTemplate
            .delete("$blogUrl/{id}", blogpost.id)
    }

}