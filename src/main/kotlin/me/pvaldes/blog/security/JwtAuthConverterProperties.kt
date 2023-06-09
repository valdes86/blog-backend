package me.pvaldes.blog.security

import org.springframework.context.annotation.Configuration

@Configuration
class JwtAuthConverterProperties {
    val resourceId: String? = null
    val principalAttribute: String? = null
}