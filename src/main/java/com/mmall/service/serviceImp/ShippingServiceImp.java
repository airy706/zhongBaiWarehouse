package com.mmall.service.serviceImp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IService.IShippingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service(value="iShippingService")
public class ShippingServiceImp implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        //主键回填
        int rowCount=shippingMapper.insert(shipping);
        if(rowCount>0){
            Map result= Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccessMessageData("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }
    public ServerResponse delete(Integer userId,Integer shippingId){
        //防止横向越权
        int rowCount=shippingMapper.deleteByUserIdShippingId(userId,shippingId);
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }
    public ServerResponse update(Integer userId,Shipping shipping){
        //不使用shipping中的userId,使用session中获取到的userId,防止横向越权
        shipping.setUserId(userId);
        int rowCount =shippingMapper.updateByShipping(shipping);
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
        //防止横向越权，不加userId的话会查阅到别人的记录
        Shipping shipping=shippingMapper.selectByUserIdShippingId(userId,shippingId);
        if(shipping==null){
            return ServerResponse.createByErrorMessage("无法查阅到该地址");
        }
        return ServerResponse.createBySuccessMessageData("查找地址成功",shipping);
    }
    public ServerResponse<PageInfo<Shipping>> list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        if(CollectionUtils.isNotEmpty(shippingList)){
            PageInfo pageInfo=new PageInfo(shippingList);
            return ServerResponse.createBySuccessData(pageInfo);
        }
        return ServerResponse.createByErrorMessage("无法查阅到地址列表");
    }
}
