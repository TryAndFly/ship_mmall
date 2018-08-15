package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        //false则不返回controller
        //解析handlerMethod
        HandlerMethod handlerMethod = (HandlerMethod)o;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();


        //解析参数
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            //request 的参数map返回的value是个字符串数组
            Object obj = entry.getValue();
            if (obj instanceof String []){
                String [] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }

            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }


        if (StringUtils.equals(className,"UserManageController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截的请求,className{},methodName:{}",className,methodName);
            //如果拦截到登录请求不打印密码
            return true;
        }

        log.info("权限拦截器拦截的请求,className{},methodName:{},param:{}",className,methodName,requestParamBuffer);


        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr,User.class);
        }
        if (user == null || user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            //返回false不调用constroller
            //需要reset否则报getWritter异常
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");//设置返回值类型为json
            PrintWriter out = httpServletResponse.getWriter();


            //上传富文本需要特定的返回数据
            if (user == null){
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtext_img_upload")) {
                    Map resMap = Maps.newHashMap();
                    resMap.put("success",false);
                    resMap.put("msg","请登录管理员");
                    out.print(JsonUtil.objToString(resMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else {
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtext_img_upload")) {
                    Map resMap = Maps.newHashMap();
                    resMap.put("success",false);
                    resMap.put("msg","无权限操作");
                    out.print(JsonUtil.objToString(resMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限")));
                }
            }
            out.flush();
            out.close();

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //controller执行后调用
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //最后调用
        log.info("afterHandle");

    }
}
