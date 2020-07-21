package com.zzz.demo.search;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchDatabase {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> resErrInfo() {
        Map<String, Object> backObject = new HashMap();
        backObject.put("code", 10001);
        backObject.put("data", "请传入用户ID");
        return backObject;
    }


    @ResponseBody
    @GetMapping("/query")
    public Object list(String index) {
        System.out.println(index);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * FROM user");
        System.out.println("list的长度为：" + list.size());
        try {
            int idx = Integer.parseInt(index);
            if (idx >= 0 && idx < list.size()) {
                return list.get(idx);
            } else {
                return list;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return list;
        }
    }

    @ResponseBody
    @RequestMapping("/insertData")
    public JSONObject insertData(String name, String sex) {

        if (name != null && sex != null) {

            String sql = "insert into user (name, sex) values (?,?)";

            List<Object[]> batchArgs = new ArrayList<>();
            batchArgs.add(new Object[]{name, sex});
            jdbcTemplate.batchUpdate(sql, batchArgs);
            JSONObject backObject = new JSONObject();
            backObject.put("code", 10000);
            backObject.put("data", "用户信息保存成功");
            return backObject;
        } else {
            JSONObject backObject = new JSONObject();
            backObject.put("code", 10001);
            backObject.put("data", "请传入正确的参数");
            return backObject;
        }
    }


    @ResponseBody
    @RequestMapping("/updateData")
    public Map<String, Object> updateData(String id, String name, String sex) {
        if (id == "") {
            return this.resErrInfo();
        }
        try {
            String sql = "select * from user where id=" + id;
            System.out.println(sql);
            Map<String, Object> obj = jdbcTemplate.queryForMap(sql);
            System.out.println(obj);
            if (name != null && sex != null) {
                String updateSql = "update user set name=?,sex=? where id=?";
                System.out.println(updateSql);
                jdbcTemplate.update(updateSql, new Object[]{name, sex, id});
                Map<String, Object> backObject = new HashMap();
                Map<String, Object> res = jdbcTemplate.queryForMap(sql);
                backObject.put("code", 10000);
                backObject.put("data", res);
                backObject.put("msg", "用户信息修改成功");
                return backObject;
            } else {
                Map<String, Object> backObject = new HashMap();
                backObject.put("code", 10001);
                backObject.put("data", obj);
                backObject.put("msg", "请传入正确的参数");
                return backObject;
            }
        } catch (IllegalStateException e) {
            return this.resErrInfo();
        } catch (EmptyResultDataAccessException e) {
            Map<String, Object> backObject = new HashMap();
            backObject.put("code", 10001);
            backObject.put("data", "未获取到用户信息，无法修改");
            return backObject;

        }
    }


    @ResponseBody
    @RequestMapping("/delData")
    public Map<String, Object> delData(String id) {
        System.out.println(id);

        if (id == "") {
            return this.resErrInfo();
        }
        try {
            String sql = "select * from user where id=" + id;
            System.out.println(sql);
            Map<String, Object> obj = jdbcTemplate.queryForMap(sql);
            System.out.println(obj);
            String delSql = "delete from user where id=?";
            System.out.println(delSql);
            jdbcTemplate.update(delSql, new Object[]{id});
            Map<String, Object> backObject = new HashMap();
            backObject.put("code", 10000);
            backObject.put("msg", "用户信息删除成功");
            return backObject;

        } catch (IllegalStateException e) {
            return this.resErrInfo();
        } catch (EmptyResultDataAccessException e) {
            Map<String, Object> backObject = new HashMap();
            backObject.put("code", 10001);
            backObject.put("data", "未获取到用户信息，无法删除");
            return backObject;

        }
    }
}
