
package com.jake.mall.config.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToMallUser {

    /**
     * The name of the current user in Request
     *
     * @return
     */
    String value() default "user";

}
