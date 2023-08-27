package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.SMSUtils;
import com.example.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.datatransfer.StringSelection;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        // 获取手机号
        String phone = user.getPhone();
        // 生成随机的4位验证码
        if(StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("对应手机号{},生成了验证码：{}",phone, code);
            // 调用aliyun api 完成发送短信
            //SMSUtils.sendMessage("take-out", "", phone, code);
            // 存在session中
            httpSession.setAttribute(phone, code);
            return R.success("发送验证码短信成功");
        }
        return R.error("发送验证码短信成失败");
    }

    @RequestMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession) {
        log.info(map.toString());
        //map中获取手机号以及验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //从session获取保存的验证码， 进行比对，如果成功就登陆成功
        Object codeInSession = httpSession.getAttribute(phone);
        if(codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if(user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
