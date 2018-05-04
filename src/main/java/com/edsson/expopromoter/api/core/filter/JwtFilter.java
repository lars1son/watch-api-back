package com.edsson.expopromoter.api.core.filter;

import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        Set<String> allowedPaths = new TreeSet<>();
        allowedPaths.add("/users/create");
        allowedPaths.add("/login");
        allowedPaths.add("/auth/registration");
        allowedPaths.add("/auth/test");
//        allowedPaths.add("/swagger-ui.html");
//        allowedPaths.add("/webjars/springfox-swagger-ui/springfox.css");
//        allowedPaths.add("/webjars/springfox-swagger-ui/springfox.js");
//        allowedPaths.add("/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js");
//        allowedPaths.add("/swagger-resources/configuration/ui");
//        allowedPaths.add("/swagger-resources/configuration/security");
//        allowedPaths.add("/swagger-resources");
//        if(httpRequest.getRequestURI().contains("swagger")){
//            return;
//        }
        if (allowedPaths.contains(httpRequest.getRequestURI()) ) {
            filterChain.doFilter(httpRequest, res);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && !authHeader.equals("")) {
            try {
                Claims c = jwtUtil.parseToken(authHeader);
                httpRequest.setAttribute("claims", c);
                LinkedHashMap user =  c.get("user", LinkedHashMap.class);
                httpRequest.setAttribute("user_roles", user.get("roles"));

                User u = userService.findOneById((String) user.get("id"));

                httpRequest.setAttribute("user", u);

                filterChain.doFilter(httpRequest, res);
            } catch ( JwtException e ) {
                res.reset();
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            res.reset();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
