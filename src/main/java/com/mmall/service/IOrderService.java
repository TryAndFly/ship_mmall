package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse<String> cancelOrder(Integer userId,Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetial(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(int pageNum, int pageSize, Integer userId);

    ServerResponse<PageInfo> managerList(int pageNum,int pageSize);

    ServerResponse<OrderVo> managerDetail(Long orderNo);

    ServerResponse<PageInfo> managerSearch(Long orderNo,int pageNum,int pageSize);

    ServerResponse<String> managerSendGoods(Long orderNo);
}
