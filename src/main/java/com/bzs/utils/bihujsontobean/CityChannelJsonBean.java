package com.bzs.utils.bihujsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 城市渠道续保期JsonBean
 * @author: dengl
 * @create: 2019-06-10 10:02
 */
public class CityChannelJsonBean  extends BaseJsonBean{
    private List<CityChannelItem> Items;

    public List<CityChannelItem> getItems() {
        return Items;
    }

    public void setItems(List<CityChannelItem> items) {
        Items = items;
    }
}
