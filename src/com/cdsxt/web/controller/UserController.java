package com.cdsxt.web.controller;

import com.cdsxt.po.User;
import com.cdsxt.service.RoleService;
import com.cdsxt.service.UserService;
import com.cdsxt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // 查询用户表, 全部查询
    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    public String index(ModelMap modelMap, @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
        List<User> userList = userService.queryAll();

        PageUtil page = new PageUtil(userList.size(), curPage);
        int startRow = page.getStartRow();
        int pageCount = page.getPageRow();
        List<User> userListOnePage = userService.queryOnePage(startRow, pageCount);

        modelMap.addAttribute("page", page);
        modelMap.addAttribute("userList", userListOnePage);
        return "users/index";
    }

    /*
    用户管理
     */

    // 添加用户
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public String addUser(User user) {
        // 数据库中插入记录
        userService.addUser(user);
        return "redirect:/users/index";
    }

    // 删除用户
    @RequestMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        User user = new User();
        user.setId(id);
        userService.deleteUser(user);
        return "redirect:/users/index";
    }

    // 修改用户: 查询用户并返回 json 对象
    @RequestMapping(value = "modifyUser/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User modifyUser(@PathVariable("id") Integer id) {
        // 根据 id 查询用户, 返回 json 对象给页面
        return userService.queryUserById(id);
    }

    // 修改用户: 修改数据库
    @RequestMapping(value = "modifyUser", method = RequestMethod.POST)
    public String modifyUser(User user) {
        userService.modifyUser(user);
        return "redirect:/users/index";
    }

    // 为用户分配角色
    @RequestMapping(value = "allocateRole/{id}", method = RequestMethod.GET)
    public String allocateRole(@PathVariable("id") Integer id, Model model) {
        // 先查询出原有的课程, 并返回到页面上
        User user = this.userService.queryUserById(id);
        if (Objects.nonNull(user)) {
            // 用户存在
            model.addAttribute("user", user);
            // 将所有可选角色一并返回
            model.addAttribute("allRoles", this.roleService.queryAllRole());
            return "users/allocateRole";
        } else {
            // todo 用户不存在, 如何处理?
            return null;
        }
    }

    /**
     * 为用户分配角色
     * @param id 当前用户的 id
     * @param roles 该用户所选中的角色列表
     * @return 返回到用户主页面
     */
    @RequestMapping(value = "allocateRole/{id}", method = RequestMethod.POST)
    public String allocateRole(@PathVariable("id") Integer id, Integer[] roles) {
        this.userService.allocateRole(id, roles);
        return "redirect:/users/index";
    }

}