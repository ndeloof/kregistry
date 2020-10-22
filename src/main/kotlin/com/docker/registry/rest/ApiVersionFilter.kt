package com.docker.registry.rest

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiVersionFilter(): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        response.addHeader("Docker-Distribution-Api-Version", "registry/2.0")
        filterChain.doFilter(request, response)
    }
}