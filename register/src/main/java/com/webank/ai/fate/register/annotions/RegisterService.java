package com.webank.ai.fate.register.annotions;

import com.webank.ai.fate.register.common.RouterModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterService {

    String serviceName() ;

    String  version() default "";

    boolean  useDynamicEnvironment()  default   false;

    RouterModel routerModel() default   RouterModel.ALL_ALLOWED;


//    String threadPoolKey() default "";
//
//    String fallbackMethod() default "";
//
//    HystrixProperty[] commandProperties() default {};
//
//    HystrixProperty[] threadPoolProperties() default {};
//
//    Class<? extends Throwable>[] ignoreExceptions() default {};
//
//    ObservableExecutionMode observableExecutionMode() default ObservableExecutionMode.EAGER;
//
//    HystrixException[] raiseHystrixExceptions() default {};
//
//    String defaultFallback() default "";


}
