package com.bzs.utils.bihujsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-10 10:51
 */
public class NewCarVehicleInfoJsonBean extends BaseJsonBean {
    List<NewCarVehicleInfoItem> Items;

    public List<NewCarVehicleInfoItem> getItems() {
        return Items;
    }

    public void setItems(List<NewCarVehicleInfoItem> items) {
        Items = items;
    }
}
