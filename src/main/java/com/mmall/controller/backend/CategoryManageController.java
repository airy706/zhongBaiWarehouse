package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IService.ICategoryService;
import com.mmall.service.IService.IUserService;

import com.sun.glass.ui.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value="/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增加品类节点
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value="add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue = "0") Integer parentId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //检查用户是否登录
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请重新登录");
        }
        //检查是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
        //是管理员
        //增加分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
           return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }


    /**
     * 更新品类名称
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping(value="set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,String categoryName,Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请重新登录");
        }
        //检查是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //修改品类名称业务逻辑
            return iCategoryService.updateCategoryName(categoryName,categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 获取平级子节点信息，不递归
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value="get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue = "0") Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请重新登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取平级子节点逻辑，不递归,平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 通过递归，获取本节点与子节点品类的Id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value="get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue = "0") Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录，请重新登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }
}
