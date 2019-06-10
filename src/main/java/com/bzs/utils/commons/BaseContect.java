package com.bzs.utils.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 平安上传资料时需要的实体类
 * @author: dengl
 * @create: 2019-06-10 14:50
 */
public class BaseContect {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String StrBase;
    private String ImgType;
    private Integer IsUpload;
    public String getStrBase() {
        return StrBase;
    }
    public void setStrBase(String strBase) {
        StrBase = strBase;
    }

    public String getImgType() {
        return ImgType;
    }
    public void setImgType(String imgType) {
        ImgType = imgType;
    }
    public Integer getIsUpload() {
        return IsUpload;
    }
    public void setIsUpload(Integer isUpload) {
        IsUpload = isUpload;
    }
    public BaseContect() {
        super();
    }
    public BaseContect(String strBase, String imgType, Integer isUpload) {
        super();
        StrBase = strBase;
        ImgType = imgType;
        IsUpload = isUpload;
    }

    public static List<BaseContect> getList(List<BaseContect>list){
        List<BaseContect> resultList=new ArrayList<BaseContect>();
        if(list!=null&&list.size()>0){
            for (BaseContect bc : list) {
                if(null!=bc){
                    Integer level=bc.getIsUpload();
                    if(level==1){
                        resultList.add(new BaseContect(bc.getStrBase(),bc.getImgType()+"0",0));
                        resultList.add(new BaseContect(bc.getStrBase(),bc.getImgType()+"1",0));
                    }else if(level==0){
                        resultList.add(new BaseContect(bc.getStrBase(),bc.getImgType()+"0",0));
                    }

                }

            }

        }
        return resultList;

    }

}
