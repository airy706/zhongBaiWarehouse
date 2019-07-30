package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IService.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value="/order/")
public class OrderController {
    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService iOrderService;
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path=request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }
    //支付宝的回调会把参数放在request中
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
       Map params= Maps.newHashMap();
       Map requestParams=request.getParameterMap();
       for(Iterator iter=requestParams.keySet().iterator();iter.hasNext();){
           String name= (String) iter.next();
           String[] values= (String[]) requestParams.get(name);
           String valueStr="";
           for(int i=0;i<values.length;i++){
               valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
           }
           params.put(name,valueStr);
       }
       logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
    //验证回调的正确性，确认是否为支付宝发的，并且过滤掉重复通知
        params.remove("sign_type");
        try {
            Boolean alipayRSAcheckV2=AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSAcheckV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝回调验证异常",e);
        }
        //todo 验证数据

        //todo 各种业务逻辑
        ServerResponse serverResponse=iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AliCallback.RESPONSE_SUCCESS;
        }
        return Const.AliCallback.RESPONSE_FAILED;
    }
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,Long orderNo){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse=iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        return ServerResponse.createBySuccessData(serverResponse.isSuccess());
    }
}