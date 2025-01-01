package com.example.mate.auth.presentation.support;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this")
public @interface OptionalUser {
} 