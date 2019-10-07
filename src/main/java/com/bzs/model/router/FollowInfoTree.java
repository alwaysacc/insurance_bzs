package com.bzs.model.router;

import com.bzs.model.FollowInfo;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "follow_info")
public class FollowInfoTree<T> implements Serializable {
    private static final long serialVersionUID = -3327478146308500708L;

    private String date;

    private List<FollowInfo> children;

    public void initChildren(){
        this.children = new ArrayList<>();
    }

}