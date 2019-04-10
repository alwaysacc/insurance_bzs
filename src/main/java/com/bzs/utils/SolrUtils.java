package com.bzs.utils;


import io.swagger.annotations.Api;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 优化：抽取 Id、text 为一个 JavaBean
 * @author linhongcun
 *
 */
@Component
public class SolrUtils {
    @Autowired
    private SolrClient client;

    /**
     * 1、增
     * @param message
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    public String insert(String message) throws IOException, SolrServerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateString = sdf.format(new Date());
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("id", dateString);
            doc.setField("text", message);

            /*
             * 如果 spring.data.solr.host 里面配置到 core了, 那么这里就不需要传 collection1 这个参数 下面都是一样的 即
             * client.commit();
             */

            client.add("alwaysacc", doc);
            client.commit("alwaysacc");
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 2、查 id
     * @param id
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public String getDocumentById(@PathVariable String id) throws SolrServerException, IOException {
        SolrDocument document = client.getById("alwaysacc", id);
        System.out.println(document);
        return document.toString();

    }

    /**
     * 3、删 id
     * @return
     */
    public String getAllDocuments(@PathVariable String id) {
        try {
            client.deleteById(id);
            client.commit();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 4、删 all
     * @return
     */
    public String deleteAll() {
        try {

            client.deleteByQuery( "*:*");
            client.commit();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 5、改
     * @param message
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    public String update(String id, String message) throws IOException, SolrServerException {
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("id", id);
            doc.setField("text", message);

            /*
             * 如果 spring.data.solr.host 里面配置到 core了, 那么这里就不需要传 itaem 这个参数 下面都是一样的 即
             * client.commit();
             */
            client.add("alwaysacc", doc);
            client.commit("alwaysacc");
            return doc.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 6、全：还没实现，也感觉没有必要实现
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public Map<String, Object> getAll()
            throws SolrServerException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    /**
     * 7、查  ++:关键字、高亮、分页  ✔
     * @return
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public  Map<String, Object> select( String q,  Integer page, Integer size,String userId)
            throws SolrServerException, IOException {
        SolrQuery params = new SolrQuery();

        // 查询条件
        params.set("q","*"+q+"*");
        params.set("fq","userId:"+userId);
        // 排序
        params.addSort("dates", SolrQuery.ORDER.desc);

        // 分页
        params.setStart(page);
        params.setRows(size);

        // 默认域
        params.set("df", "skeywords");

        // 只查询指定域
        //params.set("fl", "*");

        // 开启高亮
        params.setHighlight(true);
        // 设置前缀
        params.setHighlightSimplePre("<span style='color:red'>");
        // 设置后缀
        params.setHighlightSimplePost("</span>");

        // solr数据库是
        QueryResponse queryResponse = client.query(params);
        SolrDocumentList results = queryResponse.getResults();

        // 数量，分页用
        long total = results.getNumFound();// JS 使用 size=MXA 和 data.length 即可知道长度了（但不合理）

        // 获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
        Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("data", results);
        return map;
    }
    public void save(SolrInputDocument document) throws IOException, SolrServerException {
        client.add(document);
        client.commit();
    }
}
