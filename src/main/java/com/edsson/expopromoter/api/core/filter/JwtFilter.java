package com.edsson.expopromoter.api.core.filter;

import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
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

    @Transactional
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        Set<String> allowedPaths = new TreeSet<>();
//        allowedPaths.add("/users/create");
        allowedPaths.add("/auth/login");
        allowedPaths.add("/auth/registration");
allowedPaths.add("/auth/reset_password");
        allowedPaths.add("/auth/update_password");

//        allowedPaths.add("/event/");
        if (allowedPaths.contains(httpRequest.getRequestURI())) {
            filterChain.doFilter(httpRequest, res);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
//Todo:here must be token checking

        if (authHeader != null && !authHeader.equals("")) {
            try {
                authHeader = jwtUtil.updateToken(authHeader);
                httpResponse.setHeader("Token", authHeader);

                Claims c = jwtUtil.parseToken(authHeader);
                httpRequest.setAttribute("claims", c);
                LinkedHashMap user = c.get("user", LinkedHashMap.class);
                httpRequest.setAttribute("user_roles", user.get("roles"));

                User u = userService.findOneById(Long.valueOf((Integer) user.get("id")));
                httpRequest.setAttribute("user", u);
//                httpResponse.setHeader("Token", );
                filterChain.doFilter(httpRequest, res);
            } catch (JwtException e) {
                res.reset();
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            res.reset();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
