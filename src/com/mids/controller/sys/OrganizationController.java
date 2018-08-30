package com.mids.controller.sys;

import com.mids.common.Result;
import com.mids.mybatis.model.Organization;
import com.mids.service.OrganizationService;
import com.mids.mybatis.vo.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

/**
 * @description：部门管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/sys/organization")
public class OrganizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    /**
     * 部门管理主页
     *
     * @return
     */
    @RequestMapping("/manager")
    public String manager() {
        return "/sys/admin/organization";
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequestMapping(value = "/showTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Tree> showTree() {
        List<Tree> trees = organizationService.findTree();
        return trees;
    }

    /**
     * 部门列表
     *
     * @return
     */
    @RequestMapping("/findTreeGrid")
    @ResponseBody
    public List<Organization> findTreeGrid() {
        List<Organization> treeGrid = organizationService.findTreeGrid();
        return treeGrid;
    }

    /**
     * 添加部门页
     *
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage() {
        return "/sys/admin/organizationAdd";
    }

    /**
     * 添加部门
     *
     * @param organization
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result add(Organization organization) {
        Result result = new Result();
        try {
        	organization.setInsertTime(new Date());
        	organization.setUpdateTime(new Date());
            organizationService.add(organization);
            result.setSuccess(true);
            result.setMsg("添加成功！");
            return result;
        } catch (RuntimeException e) {
            LOGGER.info("添加部门失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 编辑部门页
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, Integer id) {
        Organization organization = organizationService.findById(id);
        request.setAttribute("organization", organization);
        return "/sys/admin/organizationEdit";
    }

    /**
     * 编辑部门
     *
     * @param organization
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result edit(Organization organization) {
        Result result = new Result();
        try {
        	organization.setUpdateTime(new Date());
            organizationService.update(organization);
            result.setSuccess(true);
            result.setMsg("编辑成功！");
            return result;
        } catch (RuntimeException e) {
            LOGGER.info("编辑部门失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result delete(Integer id) {
        Result result = new Result();
        try {
            organizationService.deleteById(id);
            result.setMsg("删除成功！");
            result.setSuccess(true);
            return result;
        } catch (RuntimeException e) {
            LOGGER.info("删除部门失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }
}
