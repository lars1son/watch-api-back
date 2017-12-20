package com.edsson.expopromoter.api.core.filter;

import com.edsson.expopromoter.api.user.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Strict a class or method for specific user role.
 * for example: @RolesAllowed(roles = {RoleEnum.ADMIN, RoleEnum.USER})
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    Role[] roles();
}
