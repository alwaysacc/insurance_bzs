package com.bzs.utils.treeUtil;

import com.bzs.model.router.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 树形数据工具类
 * @author: dengl
 * @create: 2019-06-11 17:48
 */
public class TreeUtil {
    protected TreeUtil() {

    }

    private final static String TOP_NODE_ID = "0";

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pid = node.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null)
                        n.initChildren();
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });


        Tree<T> root = new Tree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        root.setText("root");
        return root;
    }


    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter<T>>
     */
    public static <T> ArrayList<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        ArrayList list = new ArrayList<>();

        List<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();
        router.setName("系统主页");
        router.setPath("/home");
        router.setComponent("HomePageView");
        router.setIcon("home");
        router.setChildren(null);
        //router.setMeta(new RouterMeta(false, true));
        topRoutes.add(router);
     /*   routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
                route.setMeta(new RouterMeta(route.getName(),route.getIcon()));
                Parent parent=new Parent();
                parent.setAlwaysShow(true);
                parent.setComponent(route.getComponent());
                parent.setName(route.getName());
                parent.setPath(route.getPath());
                parent.setRedirect("noredirect");
                parent.setMeta(new RouterMeta(route.getName(),route.getIcon()));
                list.add(parent);
                return;
            }
            for (VueRouter<T> p : routes) {
                String id = p.getId();
                if (id != null && id.equals(parentId)) {
                    if (p.getChildren() == null)
                        p.initChildren();
                    p.getChildren().add(route);
                    Children children=new Children();
                    children.setComponent(p.getComponent());
                    children.setMeta(new RouterMeta(p.getName(),p.getIcon()));
                    children.setName(p.getName());
                    children.setPath(p.getPath());
                    p.getChildren().add(children);
                    p.setAlwaysShow(true);
                    //route.setHasParent(true);
                    p.setHasParent(true);
                    return;
                }
            }
        });*/
        routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
                route.setMeta(new RouterMeta(route.getName(),route.getIcon()));
                route.setAlwaysShow(true);
                route.setRedirect("noredirect");
          /*      Parent parent=new Parent();
                parent.setAlwaysShow(true);
                parent.setComponent(route.getComponent());
                parent.setName(route.getName());
                parent.setPath(route.getPath());
                parent.setRedirect("noredirect");
                parent.setMeta(new RouterMeta(route.getName(),route.getIcon()));*/
                list.add(route);
                return;
            }
            for (VueRouter<T> p : routes) {
                String id = p.getId();
                if (id != null && id.equals(parentId)) {
                    if (p.getChildren() == null)
                        p.initChildren();
                    p.getChildren().add(route);
                    p.setAlwaysShow(true);
                    //route.setHasParent(true);
                    p.setHasParent(true);
                    return;
                }
            }
        });
        router = new VueRouter<>();
        router.setPath("/profile");
        router.setName("个人中心");
        router.setComponent("personal/Profile");
        router.setIcon("none");
        //router.setMeta(new RouterMeta(true, false));
        topRoutes.add(router);


        return list;
    }
}
