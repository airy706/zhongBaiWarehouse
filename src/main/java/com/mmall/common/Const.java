package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER="currentUser";
    public static final String EMAIL="email";
    public static final String USERNAME="username";
    //内部接口类的属性实现类似于枚举的作用
    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }
    public interface Cart{
        int CHECKED=1;//选中
        int UN_CHECKED=0;//未选中

        String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
    }
    public interface ProductListOrderBy{
        Set<String> PRICR_ASC_DESC= Sets.newHashSet("price_asc","price_desc");
    }
    public enum ProductStatusEnum{
        ON_SALE("在线",1);
        private String value;
       private int code;
       ProductStatusEnum(String value,int code){
           this.code=code;
           this.value=value;
       }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单成功"),
        ORDER_CLOSED(60,"订单关闭");
        private Integer code;
        private String value;
        OrderStatusEnum(Integer code,String value){
            this.code=code;
            this.value=value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
    public interface  AliCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";
        String RESPONSE_SUCCESS="success";
        String RESPONSE_FAILED="failed";
    }
    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");
        private Integer code;
        private String value;
        PayPlatformEnum(Integer code,String value){
            this.code=code;
            this.value=value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
