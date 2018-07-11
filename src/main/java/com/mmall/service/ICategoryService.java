package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse addCategroy(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChikdrenParallelCategory(Integer integer);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
