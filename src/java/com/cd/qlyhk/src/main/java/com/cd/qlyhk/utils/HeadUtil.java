package com.cd.qlyhk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HeadUtil {


	public static List<Map<String, String>> getHeadList(String[] heads) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		String[] headKeys = heads[0].split(";");
		String[] headNames = heads[1].split(";");
		String[] headHides = heads[2].split(";");
		
		for(int i = 0; i < headKeys.length; i++) {
			String headkey = headKeys[i];
			String headname = headNames[i];
			String headhide = headHides[i];
			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("head_key", headkey);
			resMap.put("head_name", headname);
			resMap.put("head_hide", headhide);
			dataList.add(resMap);
		}
		
		return dataList;
	}
	
	public static JSONArray getDefaulArticleHead() {
		JSONArray headArr = new JSONArray();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("head_key", "uuid");// 字段名
		jsonObj.put("head_name", "文章ID");// 字段名称
		jsonObj.put("ishow", false);// 是否显示列 true 表示显示  false 表示隐藏
		jsonObj.put("ischecked", false);// 是否勾选列 true 表示勾选  false 表示不勾选
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "article_title");
		jsonObj.put("head_name", "文章标题");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "article_url");
		jsonObj.put("head_name", "文章URL");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", false);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "pic_url");
		jsonObj.put("head_name", "封面图片");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", false);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "audit_status");
		jsonObj.put("head_name", "状态");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "audit_user");
		jsonObj.put("head_name", "审核人");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "source");
		jsonObj.put("head_name", "来源");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "edit_date");
		jsonObj.put("head_name", "原文创建日期");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "istop");
		jsonObj.put("head_name", "是否置顶");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "collect_date");
		jsonObj.put("head_name", "采集日期");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		jsonObj = new JSONObject();
		jsonObj.put("head_key", "category_name");
		jsonObj.put("head_name", "归属");
		jsonObj.put("ishow", true);
		jsonObj.put("ischecked", true);
		headArr.add(jsonObj);
		
		return headArr;
	}
}
