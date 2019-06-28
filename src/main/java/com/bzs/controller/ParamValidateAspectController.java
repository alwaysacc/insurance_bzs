package com.bzs.controller;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-26 15:23
 */

import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.aspect.AspectInterface;
import com.bzs.utils.aspect.validate.ValidateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 切面实现入参校验
 */
@RestController
public class ParamValidateAspectController {
    @GetMapping(value = "/validate")
    @AspectInterface
    public Result validate(ValidateRequest request) {
        return ResultGenerator.genSuccessResult();
    }
}
