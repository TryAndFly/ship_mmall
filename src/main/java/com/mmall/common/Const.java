package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER="current_user";
    public static final String EMAIL="email";
    public static final String USERNAME="username";

    public static final String TOKEN_PREFIX = "token_";

    public interface RedisCacheExtim{
        int REDIS_SESSION_EXTIME = 60*30;//30分钟
    }

    public interface Cart{
        int CHECKED =1;//选中状态
        int UNCHECKED = 0;//

        String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
    }

    public interface Role{
        int ROLE_CUSTOMER= 0;
        int ROLE_ADMIN=1;
    }

    public interface AlipayCallBack{
        String TRADE_WAIT_BUYER_PAY= "WAIT_BUYER_PAY";
        String TRADE_TRADE_SUCCESS= "TRADE_SUCCESS";
        String TRADE_FINISHED= "TRADE_FINISHED";
        String TRADE_CLOSED= "TRADE_CLOSED";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";

    }

    public interface CacheString{
        String CATEGORY_CACHE = "CATEGORY_CACHE";
        String PRODUCT_DETAIL_CACHE = "PRODUCT_DETAIL_CACHE";

    }

    public enum OrderStatusEnum{
        CANCEL("已取消",0),
        NO_PAY("未支付",10),
        PAID("已付款",20),
        SHIPPED("已发货",40),
        ORDER_SUCCESS("订单已完成",50),
        ORDER_CLOSE("订单已关闭",60)
        ;
        private String value;
        private int code;

        OrderStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }
        public static OrderStatusEnum codeOf(int code){
            for (OrderStatusEnum orderStatusEnum:values()){
                if (orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("未找到对应的枚举");
        }
        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PayPlatformEnum{
        ALIPAY("支付宝",1);
        private String value;
        private int code;

        PayPlatformEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum  ProductStatusEnum{
        ON_SALE("在线",1);
        private String value;
        private int code;

        ProductStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");

    }

    public enum PaymentTypeEnum{
        ON_LINE_PAY("在线支付",1)
        ;
        private String value;
        private int code;

        PaymentTypeEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code){
            for (PaymentTypeEnum paymentTypeEnum:values()){
                if (paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("未找到对应的枚举");
        }
    }

    public interface REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
    }
}
