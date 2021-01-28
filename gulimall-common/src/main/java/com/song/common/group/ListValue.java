package com.song.common.group;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/* *
 * @program: gulimall
 * @description 自定义注解
 * @author: swq
 * @create: 2021-01-25 22:54
 **/
@Documented
@Constraint(
        validatedBy = {}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {
    String message() default "{com.song.common.group.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
