package waffle.team6.global.config

import feign.Logger
import feign.codec.Encoder
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean

class KakaoClientConfig {

    @Bean
    fun feignFormEncoder(converters: ObjectFactory<HttpMessageConverters?>?): Encoder {
        return SpringFormEncoder(SpringEncoder(converters))
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}