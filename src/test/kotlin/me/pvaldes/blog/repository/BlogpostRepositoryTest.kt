package me.pvaldes.blog.repository

import me.pvaldes.blog.entities.Blogpost
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@DataMongoTest
class BlogpostRepositoryTest (){

    companion object {

        @Container
        var mongoDBContainer = MongoDBContainer("mongo:latest")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl )
        }
    }

    @Autowired
    lateinit var blogpostRepository: BlogpostRepository
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @AfterEach
    fun cleanUp() {
        blogpostRepository.deleteAll()
    }





    @Test
    fun `When FindOneById then Return Blogpost`() {

        //given
        val blogpost = Blogpost(ObjectId.get(), "title","text")
        mongoTemplate.save(blogpost)

        //when
        val blogpostFound = blogpostRepository.findOneById(blogpost.id)

        //then
        assertEquals(blogpost, blogpostFound)
    }

    @Test
    fun `When findAll then Return List of Blogposts`() {

        //given
        val blogpost1 = Blogpost(ObjectId.get(), "title1","text1")
        val blogpost2 = Blogpost(ObjectId.get(), "title2","text2")

        mongoTemplate.save(blogpost1)
        mongoTemplate.save(blogpost2)

        //when
        val result = blogpostRepository.findAll()

        //then
        assertEquals(2, result.size)
        assertTrue(result.contains(blogpost1))
        assertTrue(result.contains(blogpost2))
    }

    @Test
    fun `When existsById then Return true`() {

        //given
        val blogpost = Blogpost(ObjectId.get(), "title","text")
        mongoTemplate.save(blogpost)

        //when
        val result = blogpostRepository.existsById(blogpost.id.toString())

        //then
        assertTrue(result)
    }

    @Test
    fun `When delete then deletes blogpost`() {

        //given
        val blogpost1 = Blogpost(ObjectId.get(), "title1","text1")
        mongoTemplate.save(blogpost1)

        val blogpost2 = Blogpost(ObjectId.get(), "title2","text2")
        mongoTemplate.save(blogpost2)

        //when
        var result = blogpostRepository.findAll()
        assertEquals(2, result.size)

        blogpostRepository.deleteById(blogpost1.id.toString())
        result = blogpostRepository.findAll()

        //then
        assertEquals(1, result.size)
        assertFalse(result.contains(blogpost1))
        assertTrue(result.contains(blogpost2))
    }
}

