package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IService.IFileService;
import com.mmall.service.IService.IProductService;
import com.mmall.service.IService.IUserService;
import com.mmall.utils.PropertiesUtil;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.MalformedServerReplyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value="/manage/product/")
public class ProductManageController{
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;
    /**
     * 新增或更新产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value="save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
           //更新或者保存产品的逻辑
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");
    }

    /**
     * 修改产品销售状态
     * @param session
     * @param productId
     * @param productStatus
     * @return
     */
    @RequestMapping(value="set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer productStatus){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //更改产品销售状态
            return iProductService.setSaleStatus(productId,productStatus);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");
    }

    /**
     * 产品信息
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value="detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //增加业务
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");
    }

    /**
     * 产品列表并分页
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
                                  @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //分页
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");
    }

    /**
     * 根据ID和产品名称搜索产品
     * @param session
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="search.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session,Integer productId,String productName,@RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
                                 @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //增加搜索业务
            return iProductService.searchProduct(productId, productName, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");
    }
    @RequestMapping(value="upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value="upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map<String,String> fileMap= Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccessData(fileMap);
        }
        return ServerResponse.createByErrorMessage("无权限操作，请登录管理员");

    }
    @RequestMapping(value="richtext-img-upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value="upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //富文本对于返回值有自己的要求，我们是用的是simditor,故按照simditor的格式返回
        if(user==null){
            resultMap.put("success",false);
            resultMap.put("msg","用户未登录，请登录管理员");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }
        resultMap.put("success",false);
        resultMap.put("msg","无权限操作，请登录管理员");
        return resultMap;

    }
}
