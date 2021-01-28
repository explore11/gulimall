package com.song.gulimall.product.exception;


import com.song.common.exception.BizCode;
import com.song.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/* *
 * @program: gulimall
 * @description 统一异常处理类
 * @author: swq
 * @create: 2021-01-25 21:19
 **/
@Slf4j
@RestControllerAdvice(basePackages = "com.song.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型{}", e.getMessage(), e.getClass());
        Map<String, String> map = new HashMap<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(fieldError -> {
            //变量
            String field = fieldError.getField();
            //信息
            String message = fieldError.getDefaultMessage();
            map.put(field, message);
        });
        return R.error(BizCode.VALID_ERROR_EXCEPTION.getCode(), BizCode.VALID_ERROR_EXCEPTION.getMsg()).put("data", map);
    }


    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        return R.error(BizCode.UN_KNOW_SYSTEM_EXCEPTION.getCode(), BizCode.UN_KNOW_SYSTEM_EXCEPTION.getMsg());
    }
}
