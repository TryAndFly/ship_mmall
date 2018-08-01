package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody//自动解析成json
    public ServerResponse<User> login(String username, String password,
                                      HttpSession httpSession, HttpServletResponse httpServletResponse) {

        //service--mybatis--dao
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
//            V1.0版本，直接设置session
//            httpSession.setAttribute(Const.CURRENT_USER, response.getData());
//            StandardSession[C61220CC73DD5D33B5D15BE8EB43C2D5]
//            JSESSIONID=C61220CC73DD5D33B5D15BE8EB43C2D5
//            V2.0版本引入redis,解决单点登录问题
            CookieUtil.writeLoginToken(httpServletResponse, httpSession.getId());
            RedisPoolUtil.setEx(httpSession.getId(), JsonUtil.objToString(response.getData()), Const.RedisCacheExtim.REDIS_SESSION_EXTIME);

        }
        return response;
    }

    /**
     * 退出登录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        httpSession.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    /**
     * 注册
     * username,password,email,phone,question,answer
     *
     * @param
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(String username, String password, String email,
                                           String phone, String question, String answer) {

        User user = new User(username, password, email, phone, question, answer);
        return iUserService.register(user);
    }

    /**
     * 检查用户名是否有效
     * str,type
     * str可以是用户名也可以是email。对应的type是 username 和 email
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
//        V1.0使用session获取
//        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        //V2.0引入redis重写
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        //从redis中获取用户序列化字符串
        String userStr = RedisPoolUtil.get(loginToken);
        //字符串转换成对象
        User user = JsonUtil.string2Obj(userStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        } else {
            user.setPassword("");
            return ServerResponse.createBySuccess(user);
        }
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    //    forget_check_answer.do
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    //    /user/forget_reset_password.do
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    //    /user/reset_password.do
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpServletRequest request) {
        //make sure was login
        //V2.0引入redis重写
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        //从redis中获取用户序列化字符串
        String userStr = RedisPoolUtil.get(loginToken);
        //字符串转换成对象
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    // /user/update_information.do
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpServletRequest request, User user) {
        //V2.0引入redis重写
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        //从redis中获取用户序列化字符串
        String userStr = RedisPoolUtil.get(loginToken);
        //字符串转换成对象
        User current_user = JsonUtil.string2Obj(userStr, User.class);

        if (current_user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(current_user.getId());
        user.setUsername(current_user.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
//            httpSession.setAttribute(Const.CURRENT_USER, response.getData());
            response.getData().setUsername(current_user.getUsername());
            RedisPoolUtil.setEx(loginToken, JsonUtil.objToString(response.getData()), Const.RedisCacheExtim.REDIS_SESSION_EXTIME);
        }

        return response;
    }

    ///user/get_information.do
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest request) {
        //V2.0引入redis重写
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        //从redis中获取用户序列化字符串
        String userStr = RedisPoolUtil.get(loginToken);
        //字符串转换成对象
        User current_user = JsonUtil.string2Obj(userStr, User.class);
        if (current_user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需强制登录");
        }

        return iUserService.getInformation(current_user.getId());
    }

}

