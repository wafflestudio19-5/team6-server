package waffle.team6.global.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter

@Configuration
class OpenEntityManagerConfig {
    @Bean
    fun openEntityManagerInViewFilter(): FilterRegistrationBean<OpenEntityManagerInViewFilter> {
        val openEntityManagerFilterRegistrationBean = FilterRegistrationBean<OpenEntityManagerInViewFilter>()
        openEntityManagerFilterRegistrationBean.filter = OpenEntityManagerInViewFilter()
        openEntityManagerFilterRegistrationBean.order = Int.MIN_VALUE
        return openEntityManagerFilterRegistrationBean
    }
}