package com.edsson.expopromoter.api.core.filter;

import com.edsson.expopromoter.api.model.Role;
import com.edsson.expopromoter.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Intercept http request and validate permission annotation against user roles.
 */
public class PermissionHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=====================================================================================");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /*
         * User is attached to request attribute inside the JwtFilter
         * User object attached to request should not be altered or be used beside getting general
         * information about the user such as the roles
         */
        User currentUser = (User) request.getAttribute("user");

        HandlerMethod method = (HandlerMethod) handler;
        RolesAllowed classAnnotation = method.getBeanType().getAnnotation(RolesAllowed.class);
        RolesAllowed methodAnnotation = method.getMethodAnnotation(RolesAllowed.class);

        // If no permission annotation specify, continue to controller.
        if (classAnnotation == null && methodAnnotation == null) {
            return true;
        }

        if (currentUser == null) {
            response.sendError(HttpStatus.FORBIDDEN.value());
            return false;
        }

        //Todo: discover next code
//        Set<Role> currentUserRoles = new HashSet<>();
//        currentUserRoles.add(currentUser.getRole().getRole());

        // If permission annotation is on class level.
//        if (classAnnotation != null) {
//            if (!hasAtLeastOneRole(currentUserRoles, classAnnotation.roles())) {
//                response.sendError(HttpStatus.FORBIDDEN.value(), "Unavailable request for role: " + currentUser.getRoles());
//                return false;
//            }
//        }
//
//        // If permission annotation is on method level.
//        if (methodAnnotation != null) {
//            if (!hasAtLeastOneRole(currentUserRoles, methodAnnotation.roles())) {
//                response.sendError(HttpStatus.FORBIDDEN.value(), "Unavailable request for role: " + currentUser.getRoles());
//                return false;
//            }
//        }

        // else continue to controller.
        return true;
    }

    /**
     * Check if the current user roles match the annotated roles.
     *
     * @param currentUserRoles          - the current user roles.
     * @param methodOrClassAllowedRoles - the annotated roles.
     * @return return true if role exists or false if not.
     */
    private boolean hasAtLeastOneRole(Set currentUserRoles, Role[] methodOrClassAllowedRoles) {
        if (methodOrClassAllowedRoles == null || methodOrClassAllowedRoles.length == 0) {
            return true;
        } else if (currentUserRoles == null || currentUserRoles.isEmpty()) {
            //There is at least one required role on the method or class but the user roles are empty
            return false;
        }

        for (Role role : methodOrClassAllowedRoles) {
            if (currentUserRoles.contains(role)) {
                return true;
            }
        }

        return false;
    }
}