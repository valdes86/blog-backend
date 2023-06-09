package me.pvaldes.blog.repository

import me.pvaldes.blog.entities.Blogpost
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class BlogpostRepositoryTest {
    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var blogpostRepository: BlogpostRepository

    @Test
    fun `When FindOneById then Return Blogpost`() {
        val blogpost = Blogpost(ObjectId.get(), "titlw","text")
        entityManager.persist(blogpost)
        entityManager.flush()
        val blogpostFound = blogpostRepository.findOneById(blogpost.id)
        assertThat(blogpostFound == blogpost)
    }

}