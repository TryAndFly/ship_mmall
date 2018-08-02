package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategroy(HttpServletRequest request, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = Util.getUser(request);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }

        //校验权限
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            //todo
            return iCategoryService.addCategroy(categoryName, parentId);
        }
        return ServerResponse.createByErrorMessage("无权限操作");

    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategroyName(HttpServletRequest request, Integer categoryId, String categoryName) {
        User user = Util.getUser(request);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //todo
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //    /manage/category/get_category.do
    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getCategory(HttpServletRequest request,
                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = Util.getUser(request);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //todo get child node
            return iCategoryService.getChikdrenParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //    /manage/category/get_deep_category.do
    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(HttpServletRequest request,
                                          @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = Util.getUser(request);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //todo get this node and his child node
            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
