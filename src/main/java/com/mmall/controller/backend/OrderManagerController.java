package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.mmall.util.Util.isAdmin;

@Controller
@RequestMapping(value = "/manage/order/")
public class OrderManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest request,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse response = isAdmin(request);
        if (!response.isSuccess()) {
            return response;
        }
        return iOrderService.managerList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> detail(HttpServletRequest request, Long orderNo) {
        ServerResponse response = isAdmin(request);
        if (!response.isSuccess()) {
            return response;
        }
        return iOrderService.managerDetail(orderNo);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearche(HttpServletRequest request, Long orderNo,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse response = isAdmin(request);
        if (!response.isSuccess()) {
            return response;
        }
        return iOrderService.managerSearch(orderNo, pageNum, pageSize);
    }

    //发货接口
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpServletRequest request, Long orderNo) {
        ServerResponse response = isAdmin(request);
        if (!response.isSuccess()) {
            return response;
        }
        return iOrderService.managerSendGoods(orderNo);
    }
}
