package me.pvaldes.blog.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter(var properties: JwtAuthConverterProperties) : Converter<Jwt?, AbstractAuthenticationToken?> {
    private val jwtGrantedAuthoritiesConverter: JwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()


    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
            ).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

     fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName = JwtClaimNames.SUB
        if (properties.principalAttribute!= null) {
            claimName = properties.principalAttribute!!
        }
        return jwt.getClaim(claimName)
    }

     fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess: Map<String, Any> = jwt.getClaim("resource_access")
        val resource: Map<String?, Any?> = emptyMap()
        val resourceRoles: Collection<String> = emptyList()
        return if (
            (resourceAccess[properties.resourceId] == null)
            || (resource["roles"] == null)
        ) {
            emptySet()
        } else resourceRoles
            .stream()
            .map { role: String ->
                SimpleGrantedAuthority(
                    "ROLE_$role"
                )
            }
            .collect(Collectors.toSet())
    }
}