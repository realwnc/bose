package com.mids.controller.sys;

//import com.google.common.collect.Maps;
import com.mids.common.Result;
import com.mids.mybatis.model.Role;
import com.mids.service.RoleService;
import com.mids.util.MyFastjsonUtil;
import com.mids.mybatis.util.PageHelper;
import com.mids.mybatis.vo.Tree;

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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description：权限管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 权限管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/sys/admin/role";
    }

    /**
     * 权限列表
     *
     * @param role
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping("findRolePage")
	public void findUserPage(HttpServletResponse response, Role role,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			role.setName("");
			PageHelper.Page<Role> rolePage = roleService.findPage(role, page, rows, "");
			// 得到套餐产品所有的国家
			
			map.put("total", rolePage.getTotal());
			map.put("rows", rolePage.getResult());
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", "");
			LOGGER.error("===>获取角色分页数据出错 :", e);
		}
		MyFastjsonUtil.sendJsonToResponse(map, response);
	}

    /**
     * 权限树
     *
     * @return
     */
    @RequestMapping(value = "/showTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Tree> showTree() {
        return roleService.findTree();
    }

    /**
     * 添加权限页
     *
     * @return
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String addPage() {
        return "/sys/admin/roleAdd";
    }

    /**
     * 添加权限
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result add(Role role) {
        Result result = new Result();
        try {
        	role.setInsertTime(new Date());
        	role.setUpdateTime(new Date());
            roleService.add(role);
            result.setSuccess(true);
            result.setMsg("添加成功！");
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("添加角色失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result delete(Integer id) {
        Result result = new Result();
        try {
            roleService.deleteById(id);
            result.setMsg("删除成功！");
            result.setSuccess(true);
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("删除角色失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 编辑权限页
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, Integer id) {
        Role role = roleService.findById(id);
        request.setAttribute("role", role);
        return "/sys/admin/roleEdit";
    }

    /**
     * 删除权限
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result edit(Role role) {
        Result result = new Result();
        try {
        	role.setUpdateTime(new Date());
            roleService.update(role);
            result.setSuccess(true);
            result.setMsg("编辑成功！");
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("编辑角色失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 授权页面
     *
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/grantPage")
    public String grantPage(HttpServletRequest request, Long id, Model model) {
        model.addAttribute("id", id);
        return "/sys/admin/roleGrant";
    }

    /**
     * 授权页面页面根据角色查询资源
     *
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/findResourceIdListByRoleId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findResourceByRoleId(HttpServletRequest request, Integer id, Model model) {
        Result result = new Result();
        try {
            List<Integer> resources = roleService.findResourceIdListByRoleId(id);
            result.setSuccess(true);
            result.setObj(resources);
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("角色查询资源失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    @RequestMapping(value = "/grant", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result grant(Integer id, String resourceIds) {
        Result result = new Result();
        try {
            roleService.updateResource(id, resourceIds);
            result.setMsg("授权成功！");
            result.setSuccess(true);
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("授权失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

}
