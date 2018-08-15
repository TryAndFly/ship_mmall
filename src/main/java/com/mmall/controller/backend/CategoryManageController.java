package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategroy(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return iCategoryService.addCategroy(categoryName, parentId);
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategroyName(Integer categoryId, String categoryName) {
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    //    /manage/category/get_category.do
    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.getChikdrenParallelCategory(categoryId);
    }

    //    /manage/category/get_deep_category.do
    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.selectCategoryAndChildrenById(categoryId);

    }
}
