package com.mmall.util;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class Util {

    public static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";
    /**
     * 判断是否具有管理员权限
     * @param request
     * @return
     */
    public static ServerResponse isAdmin(HttpServletRequest request){
        //V2.0引入redis重写
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        //从redis中获取用户序列化字符串
        String userStr = RedisPoolUtil.get(loginToken);
        //字符串转换成对象
        User user = JsonUtil.string2Obj(userStr,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录管理员帐号");
        }
        if (user!= null && user.getRole().equals(Const.Role.ROLE_ADMIN)){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("权限不足");
    }

    //joda-time
    //str -->date

    public static Date strToDate(String timeStr){
        DateTimeFormatter dateTimeFormatter =DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(timeStr);
        return dateTime.toDate();
    }

    //date-->str
    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

//    public static void main(String[] args) {
//        System.out.print(dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
//        System.out.println(strToDate("2010-01-02 12:23:41","yyyy-MM-dd HH:mm:ss"));
//    }
}
