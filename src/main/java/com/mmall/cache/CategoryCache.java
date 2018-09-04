package com.mmall.cache;

import com.mmall.common.Const;
import com.mmall.pojo.Category;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;

import java.util.List;

public class CategoryCache {


    public static Long updateCategoryToCache(Category category) {

        //todo remove cache about this category
        Long res1 = RedisShardedPoolUtil.del(Const.CacheString.CATEGORY_CACHE + category.getId().toString());
        Long res2 = RedisShardedPoolUtil.del(Const.CacheString.CATEGORY_CACHE + "PARALLEL" + category.getId().toString());
//        Long res = RedisShardedPoolUtil.setnx(Const.CacheString.CATEGORY_CACHE+category.getId().toString(),JsonUtil.objToString(category));
        return res1 + res2;
    }

    //根据parentId获取下一层的平级节点的缓存
    public static String getChikdrenParallelCategoryFromCache(Integer parentId) {
        String res = RedisShardedPoolUtil.get(Const.CacheString.CATEGORY_CACHE + "PARALLEL" + parentId.toString());
        return res;
    }

    public static Long updateCategoryListToCache(Integer parentId, List<Category> categoryList) {
        Long res = RedisShardedPoolUtil.setnx(Const.CacheString.CATEGORY_CACHE + "PARALLEL" + parentId.toString(), JsonUtil.objToString(categoryList));
        return res;
    }

    //获取所有子节点的缓存
    public static String getCategoryAndChildrenByIdFromCache(Integer parentId) {
        String res = RedisShardedPoolUtil.get(Const.CacheString.CATEGORY_CACHE + "ALLID" + parentId.toString());
        return res;
    }

    public static Long updateCategoryAndChildrenByIdToCache(Integer parentId, List<Integer> categoryIdList) {
        Long res = RedisShardedPoolUtil.setnx(Const.CacheString.CATEGORY_CACHE + "ALLID" + parentId.toString(), JsonUtil.objToString(categoryIdList));
        return res;
    }
}
