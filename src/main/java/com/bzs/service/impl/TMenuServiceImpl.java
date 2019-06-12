package com.bzs.service.impl;

import com.bzs.dao.TMenuMapper;
import com.bzs.model.FebsConstant;
import com.bzs.model.TMenu;
import com.bzs.model.router.Tree;
import com.bzs.service.TMenuService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.treeUtil.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by dl on 2019/06/11.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TMenuServiceImpl extends AbstractService<TMenu> implements TMenuService {
    @Resource
    private TMenuMapper tMenuMapper;

    @Override
    public List<TMenu> findUserPermissions(String username) {
        return this.tMenuMapper.findUserPermissions(username);
    }

    @Override
    public List<TMenu> findUserMenus(String username) {
        return this.tMenuMapper.findUserMenus(username);
    }

    @Override
    public Map<String, Object> findMenus(TMenu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            Condition example=new Condition(TMenu.class);
            Example.Criteria  criteria= example.createCriteria();
            if (StringUtils.isNotBlank(menu.getMenuName()))
                criteria.andCondition("menu_name=", menu.getMenuName());
            if (StringUtils.isNotBlank(menu.getType()))
                criteria.andCondition("type=", Long.valueOf(menu.getType()));
            if (StringUtils.isNotBlank(menu.getCreateTimeFrom()) && StringUtils.isNotBlank(menu.getCreateTimeTo())) {
                criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') >=", menu.getCreateTimeFrom());
                criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') <=", menu.getCreateTimeTo());
            }
            example.setOrderByClause("order_num");
            List<TMenu> menus = mapper.selectByCondition(example);
            List<Tree<TMenu>> trees = new ArrayList<>();
            List<String> ids = new ArrayList<>();
            buildTrees(trees, menus, ids);

            result.put("ids", ids);
            if (StringUtils.equals(menu.getType(), FebsConstant.TYPE_BUTTON)) {
                result.put("rows", trees);
            } else {
                Tree<TMenu> menuTree = TreeUtil.build(trees);
                result.put("rows", menuTree);
            }

            result.put("total", menus.size());
        } catch (NumberFormatException e) {
           // log.error("查询菜单失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }

    @Override
    public List<TMenu> findMenuList(TMenu menu) {
      //  Example example = new Example(TMenu.class);
        Condition condition=new Condition(TMenu.class);
        Example.Criteria  criteria= condition.createCriteria();
       // Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(menu.getMenuName()))
            criteria.andCondition("menu_name=", menu.getMenuName());
        if (StringUtils.isNotBlank(menu.getType()))
            criteria.andCondition("type=", Long.valueOf(menu.getType()));
        if (StringUtils.isNotBlank(menu.getCreateTimeFrom()) && StringUtils.isNotBlank(menu.getCreateTimeTo())) {
            criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') >=", menu.getCreateTimeFrom());
            criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') <=", menu.getCreateTimeTo());
        }
       // example.setOrderByClause("menu_id");
        condition.setOrderByClause("menu_id");
        return mapper.selectByCondition(condition);
    }

    @Override
    @Transactional
    public void createMenu(TMenu menu) {
        menu.setCreateTime(new Date());
        if (menu.getParentId() == null)
            menu.setParentId(0L);
        if (TMenu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
        }
        this.save(menu);

    }

    @Override
    @Transactional
    public void updateMenu(TMenu menu) throws Exception {

    }

    @Override
    @Transactional
    public void deleteMeuns(String[] menuIds) throws Exception {

    }

    private void buildTrees(List<Tree<TMenu>> trees, List<TMenu> menus, List<String> ids) {
        menus.forEach(menu -> {
            ids.add(menu.getMenuId().toString());
            Tree<TMenu> tree = new Tree<>();
            tree.setId(menu.getMenuId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            tree.setTitle(tree.getText());
            tree.setIcon(menu.getIcon());
            tree.setComponent(menu.getComponent());
            tree.setCreateTime(menu.getCreateTime());
            tree.setModifyTime(menu.getModifyTime());
            tree.setPath(menu.getPath());
            tree.setOrder(menu.getOrderNum());
            tree.setPermission(menu.getPerms());
            tree.setType(menu.getType());
            trees.add(tree);
        });
    }
}
