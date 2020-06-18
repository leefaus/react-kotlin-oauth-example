package io.kubikl.omniscient.security

import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.lang.annotation.*

@Target(AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Documented
@AuthenticationPrincipal
annotation class CurrentUser