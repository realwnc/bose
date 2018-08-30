package com.mids.controller.sys;

//import com.google.common.collect.Lists;
import com.mids.common.Result;
import com.mids.mybatis.model.User;
import com.mids.mybatis.model.UserRole;
import com.mids.mybatis.util.PageHelper;
import com.mids.service.UserService;
import com.mids.util.MyFastjsonUtil;
import com.mids.mybatis.vo.UserVo;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description：用户管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/sys/admin/user";
    }

    /**
     * 用户管理列表
     *
     * @param userVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    
    @RequestMapping("/findUserPage")
	public void findUserPage(HttpServletResponse response, UserVo userVo,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//userVo.setUsername("");
			PageHelper.Page<UserVo> userVoPage = userService.findVoPage(userVo, page, rows, "");
			// 得到套餐产品所有的国家
			
			map.put("total", userVoPage.getTotal());
			map.put("rows", userVoPage.getResult());
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", "");
			LOGGER.error("===>获取用户分页数据出错 :", e);
		}
		MyFastjsonUtil.sendJsonToResponse(map, response);
	}
    /**
     * 添加用户页
     *
     * @return
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String addPage() {
        return "/sys/admin/userAdd";
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result add(UserVo userVo) {
        Result result = new Result();
        User u = userService.findByUsername(userVo.getUsername());
        if (u != null) {
            result.setMsg("用户名已存在!");
            return result;
        }
        try {
            userVo.setPassword(DigestUtils.md5Hex(userVo.getUsername()+userVo.getPassword()));
            userService.add(userVo);
            result.setSuccess(true);
            result.setMsg("添加成功");
            return result;
        } catch (RuntimeException e) {
            LOGGER.error("添加用户失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 编辑用户页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Integer id, Model model) {
        UserVo userVo = userService.findVoById(id);
        List<UserRole> userRoleList = userService.findListByUserId(userVo.getId());//userVo.getUserRoleList();
        List<Integer> ids = new ArrayList<Integer>();
        for (UserRole userRole : userRoleList) {
            ids.add(userRole.getRoleId());
        }
        model.addAttribute("roleIds", ids);
        model.addAttribute("user", userVo);
        return "/sys/admin/userEdit";
    }
	
    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result edit(UserVo userVo) {
        Result result = new Result();
        User user = userService.findByUsername(userVo.getUsername());
        if (user != null && user.getId().intValue() != userVo.getId().intValue()) 
        {
            result.setMsg("用户名已存在!");
            return result;
        }
        try {
        	if(StringUtils.isBlank(userVo.getPassword()) == false)
        	{
        		userVo.setPassword(DigestUtils.md5Hex(userVo.getUsername()+userVo.getPassword()));
        	}
        	else
        	{
        		userVo.setPassword(user.getPassword());
        	}
            userService.update(userVo);
            result.setSuccess(true);
            result.setMsg("修改成功！");
            return result;
        } catch (RuntimeException e) {
            LOGGER.error("修改用户失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 修改密码页
     *
     * @return
     */
    @RequestMapping(value = "/editPwdPage", method = RequestMethod.GET)
    public String editPwdPage() {
        return "/sys/admin/userEditPwd";
    }

    /**
     * 修改密码
     *
     * @param request
     * @param oldPwd
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/editUserPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result editUserPwd(HttpServletRequest request, String oldPwd, String pwd) {
        Result result = new Result();
        if (!getCurrentUser().getPassword().equals(DigestUtils.md5Hex(getCurrentUser().getUsername()+oldPwd))) {
            result.setMsg("老密码不正确!");
            return result;
        }

        try {
            userService.updatePwdById(getUserId(), DigestUtils.md5Hex(getCurrentUser().getUsername()+pwd));
            result.setSuccess(true);
            result.setMsg("密码修改成功！");
            return result;
        } catch (Exception e) {
            LOGGER.error("修改密码失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result delete(Integer id) {
        Result result = new Result();
        try {
            userService.deleteById(id);
            result.setMsg("删除成功！");
            result.setSuccess(true);
            return result;
        } catch (RuntimeException e) {
            LOGGER.error("删除用户失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }
}
