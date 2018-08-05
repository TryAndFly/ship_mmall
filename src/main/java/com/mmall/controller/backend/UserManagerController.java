package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage/user/")
public class UserManagerController {

    @Autowired
    IUserService iUserService;

    ///manage/user/login.do
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession httpSession,
                                      HttpServletResponse servletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                CookieUtil.writeLoginToken(servletResponse, httpSession.getId());
                RedisShardedPoolUtil.setEx(httpSession.getId(), JsonUtil.objToString(response.getData()), Const.RedisCacheExtim.REDIS_SESSION_EXTIME);

                return response;
            }
            return ServerResponse.createByErrorMessage("不是管理员，无法登录");
        }
        return response;
    }

//    /manage/user/list.do

}

