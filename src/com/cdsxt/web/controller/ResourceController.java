package com.cdsxt.web.controller;

import com.cdsxt.interceptor.annotation.Authorize;
import com.cdsxt.po.Resource;
import com.cdsxt.service.ResourceService;
import com.cdsxt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // 查询资源表, 全部查询
    @Authorize(value = "SYS_RESOURCE_VIEW")
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(ModelMap model, @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
        List<Resource> resourceList = resourceService.queryAllResourceWithoutMenu();
        PageUtil page = new PageUtil(resourceList.size(), curPage);
        int startRow = page.getStartRow();
        int pageCount = page.getPageRow();
        List<Resource> resourceListOnePage = resourceService.queryResourceOnePageWithoutMenu(startRow, pageCount);

        model.addAttribute("page", page);
        model.addAttribute("resourceList", resourceListOnePage);
        return "resources/index";
    }

    /*
    资源管理
     */

    // 添加资源
    @Authorize(value = "SYS_RESOURCE_SAVE")
    @RequestMapping(value = "addResource", method = RequestMethod.POST)
    public String addResource(@Validated @ModelAttribute("resource") Resource resource, BindingResult result) {
        if (result.hasErrors()) {
            // 有错误
            return "redirect:/resources/index";
        }
        // 数据库中插入记录
        resourceService.addResource(resource);
        return "redirect:/resources/index";
    }

    // 删除资源
    @Authorize(value = "SYS_RESOURCE_DELETE")
    @RequestMapping("deleteResource/{id}")
    public String deleteResource(@PathVariable("id") Integer id) {
        Resource cr = this.resourceService.queryResourceById(id);
        resourceService.deleteResource(cr);
        return "redirect:/resources/index";
    }

    // 修改资源: 查询资源并返回 json 对象
    @Authorize(value = "SYS_RESOURCE_UPDATE")
    @RequestMapping(value = "modifyResource/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Resource modifyResource(@PathVariable("id") Integer id) {
        // 根据 id 查询用户, 返回 json 对象给页面
        return resourceService.queryResourceById(id);
    }

    // 修改资源: 修改数据库
    @Authorize(value = "SYS_RESOURCE_UPDATE")
    @RequestMapping(value = "modifyResource", method = RequestMethod.POST)
    public String modifyResource(Resource resource) {
        resourceService.modifyResource(resource);
        return "redirect:/resources/index";
    }

}
