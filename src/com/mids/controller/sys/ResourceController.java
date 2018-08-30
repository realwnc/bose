package com.mids.controller.sys;

import com.mids.common.Result;
import com.mids.mybatis.model.Resource;
import com.mids.mybatis.model.User;
import com.mids.service.ResourceService;
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
 * @description：资源管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/sys/resource")
public class ResourceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    /**
     * 菜单树
     *
     * @return
     */
    @RequestMapping(value = "/showTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Tree> showTree() {
        User currentUser = getCurrentUser();
        List<Tree> tree = resourceService.findTree(currentUser);
        return tree;
    }

    /**
     * 资源管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/sys/admin/resource";
    }

    /**
     * 资源管理列表
     *
     * @return
     */
    @RequestMapping(value = "/findTreeGrid", method = RequestMethod.POST)
    @ResponseBody
    public List<Resource> findTreeGrid() {
        List<Resource> treeGrid = resourceService.findAll();
        return treeGrid;
    }

    /**
     * 添加资源页
     *
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage() {
        return "/sys/admin/resourceAdd";
    }

    /**
     * 添加资源
     *
     * @param resource
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result add(Resource resource) {
        Result result = new Result();
        try {
        	resource.setInsertTime(new Date());
        	resource.setUpdateTime(new Date());
            resourceService.add(resource);
            result.setSuccess(true);
            result.setMsg("添加成功！");
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("添加资源失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 二级资源树
     *
     * @return
     */
    @RequestMapping("/showAllTree")
    @ResponseBody
    public List<Tree> showAllTree() {
    	List<Tree> list = resourceService.findAllTree();
        return list;
    }

    /**
     * 三级资源树
     *
     * @return
     */
    @RequestMapping(value = "/showAllTrees", method = RequestMethod.POST)
    @ResponseBody
    public List<Tree> showAllTrees() {
        return resourceService.findAllTrees();
    }

    /**
     * 编辑资源页
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, Integer id) {
        Resource resource = resourceService.findById(id);
        request.setAttribute("resource", resource);
        return "/sys/admin/resourceEdit";
    }

    /**
     * 编辑资源
     *
     * @param resource
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result edit(Resource resource) {
        Result result = new Result();
        try {
        	resource.setUpdateTime(new Date());
            resourceService.update(resource);
            result.setSuccess(true);
            result.setMsg("编辑成功！");
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("编辑资源失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result delete(Integer id) {
        Result result = new Result();
        try {
            resourceService.deleteById(id);
            result.setMsg("删除成功！");
            result.setSuccess(true);
            return result;
        } catch (RuntimeException e) {
        	LOGGER.error("删除资源失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

}
