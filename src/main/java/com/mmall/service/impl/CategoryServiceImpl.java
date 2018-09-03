package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.cache.CategoryCache;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import com.mmall.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategroy(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加类品参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {

            log.info("添加品类成功");
//            return ServerResponse.createBySuccessMessage("添加品类成功");

            //todo need to cache the category
            //we need get the insert category and update to the redis,so we need to rewrite the insert sql
            //get primary key
            Integer id = category.getId();

            //get insert value
            Category insertCategory = categoryMapper.selectByPrimaryKeys(id);
            Long res = CategoryCache.updateCategoryToCache(insertCategory);
            if (res > 0){
                return ServerResponse.createBySuccessMessage("添加品类成功");
            }
            return ServerResponse.createBySuccessMessage("添加缓存失败");

        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        //根据category id来更新name
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (rowCount > 0) {
//            return ServerResponse.createBySuccess("更新品类成功");

            Category insertCategory = categoryMapper.selectByPrimaryKeys(categoryId);

            Long res = CategoryCache.updateCategoryToCache(insertCategory);
            if (res >0){
                return ServerResponse.createBySuccessMessage("更新品类成功");
            }
            return ServerResponse.createBySuccessMessage("更新品类缓存失败");

        }

        return ServerResponse.createByErrorMessage("更新品类失败");


    }

    public ServerResponse<List<Category>> getChikdrenParallelCategory(Integer integer) {

        //todo need first to get from cache ,if can't find in cache ,then find db
        List<Category> categoryList = null;
        String categoryCache = CategoryCache.getChikdrenParallelCategoryFromCache(integer);

        if (StringUtils.isNotEmpty(categoryCache)){
            categoryList = JsonUtil.strToObj(categoryCache,List.class,Category.class);
            log.info("get form cache");
            return ServerResponse.createBySuccess(categoryList);
        }


        categoryList = categoryMapper.selectCategoryChidrenByParentId(integer);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.info("未找到当前分类的子分类");
        }else {
            //更新到缓存中
            Long res = CategoryCache.updateCategoryListToCache(integer,categoryList);
            if (res > 0 ){
                log.info("getChikdrenParallelCategory更新缓存成功");
            }else {
                log.info("getChikdrenParallelCategory更新缓存失败");
            }
        }

        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点的孩子节点
     *
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        List<Integer> categoryIdList = Lists.newArrayList();

        //first get from cache ,then get from db
        String res = CategoryCache.getCategoryAndChildrenByIdFromCache(categoryId);
        if (StringUtils.isNotEmpty(res)){
            categoryIdList = JsonUtil.strToObj(res,List.class,Integer.class);
            log.info("get from cache");
            return ServerResponse.createBySuccess(categoryIdList);
        }

        findChildCategory(categorySet, categoryId);



        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }

        }

        if (!categoryIdList.isEmpty()){
            //todo need cache
            Long res1 = CategoryCache.updateCategoryAndChildrenByIdToCache(categoryId,categoryIdList);
            if (res1 > 0 ){
                log.info("add cache success ");
            }else {
                log.info("add cache fail");
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKeys(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChidrenByParentId(categoryId);
        for (Category category1 : categoryList) {
            findChildCategory(categorySet, category1.getId());
        }

        return categorySet;
    }


}
