package Daw2020v

import Daw2020v.Authentication.AuthService
import Daw2020v.Authentication.Authorized
import Daw2020v.common.ALL_PROJECTS
import Daw2020v.common.HOME
import Daw2020v.common.LOGIN
import com.fasterxml.jackson.annotation.JsonInclude.*
import com.fasterxml.jackson.databind.DeserializationFeature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {


    @Autowired
    lateinit var interceptor: Interceptor

    /*override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val converter = converters.find {
            it is MappingJackson2HttpMessageConverter
        } as MappingJackson2HttpMessageConverter
        converter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        converter.objectMapper.setSerializationInclusion(Include.NON_NULL)

        val jsonHomeConverter = MappingJackson2HttpMessageConverter()
        jsonHomeConverter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        jsonHomeConverter.objectMapper.setSerializationInclusion(Include.NON_NULL)
        jsonHomeConverter.supportedMediaTypes = listOf(MediaType("application", "json-home"))
        converters.add(jsonHomeConverter)
    }*/

    @Component
    class Interceptor @Autowired constructor(val authService: AuthService) : HandlerInterceptor {
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            if (request.method.compareTo("options", true) != 0 &&
                    request.getHeader("Authorization").isNullOrBlank()) {
                response.addHeader("WWW-Authenticate", "Basic")
                response.sendError(HttpStatus.UNAUTHORIZED.value()); return false
            }
            if(request.method.compareTo("options", true) == 0){
                response.status = 200; return true;
            }
            return (authService.verifyCredentials(request.getHeader("Authorization")))

        }
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).excludePathPatterns("$HOME/register",LOGIN)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        // TODO: Revisit this to elaborate on the CORS protocol
        registry
                .addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                //.allowCredentials(true)
    }
}

/*@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    protected fun configure(http: HttpSecurity) {
        http
                // ...
                .headers().cacheControl()
    }
}
*/

@SpringBootApplication
class Daw2020vApplication

fun main(args: Array<String>) {
    runApplication<Daw2020vApplication>(*args)
}
