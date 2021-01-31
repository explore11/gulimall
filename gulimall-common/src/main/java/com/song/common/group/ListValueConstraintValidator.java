package com.song.common.group;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/* *
 * @program: gulimall
 * @description 注解校验器
 * @author: swq
 * @create: 2021-01-30 21:55
 **/
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private final Set<Integer> sets = new HashSet<>();

    /* *
     * 初始化方法
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // 获取所有指定的值
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            sets.add(val);
        }
    }

    /* *
     * 校验是否成功
     * @param integer
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        // 判断输入的值，是否在指定的值里面
        return sets.contains(value);
    }
}
