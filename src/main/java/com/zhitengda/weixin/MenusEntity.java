package com.zhitengda.weixin;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: MenusEntity
 * @Description: 微信-菜单按钮实体类
 * @author: little.zombie.H
 * @date: 2017年5月1日 下午1:23:39
 */
@Data
public class MenusEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 菜单的响应动作类型
	 */
	private String type;
	/**
	 * 菜单标题，不超过16个字节，子菜单不超过40个字节
	 */
	private String name;
	/**
	 * 菜单KEY值，用于消息接口推送，不超过128字节, click等点击类型必须
	 */
	private String key;
	/**
	 * 网页链接，用户点击菜单可打开链接，不超过1024字节, view类型必须
	 */
	private String url;
	/**
	 * 调用新增永久素材接口返回的合法media_id，media_id类型和view_limited类型必须
	 */
	private String media_id;
	/**
	 * 小程序appid
	 */
	private String appid;
	/**
	 * 小程序页面路径
	 */
	private String pagepath;

	/**
	 * 页面跳转构造函数
	 * @param type 菜单类型
	 * @param name 菜单名称
	 * @param key key
	 * @param url 菜单地址
	 */
	public MenusEntity(String type, String name, String key, String url) {
		super();
		this.type = type;
		this.name = name;
		this.key = key;
		this.url = url;
	}

	/**
	 * 小程序跳转构造函数
	 * @param type 类型
	 * @param appid 小程序appid
	 * @param name  菜单名称
	 * @param url 地址
	 * @param pagepath 小程序页面路径
	 */
	public MenusEntity(String type,String appid,String name, String url,String pagepath) {
		super();
		this.type = type;
		this.appid = appid;
		this.name = name;
		this.url = url;
		this.pagepath = pagepath;
	}

	/**
	 * 获取菜单实体
	 * @return 返回菜单实体
	 */
	public static String menus() {
		MenusEntity entity1 = new MenusEntity("view", "wx846a32e9d62d64ab", "我要下单", "http://www.baidu.com","pages/advanceOrders/advanceOrders");
		List<Object> list1 = MenusEntity.MenusListAssemble(entity1);
		Map<String, Object> map1 = MenusEntity.MenusMapAssemble("寄件", list1);

		MenusEntity entity2 = new MenusEntity("view", "查询",  "null","http://m23j177109.iok.la/BaseWXServer/index");
		List<Object> list2 = MenusEntity.MenusListAssemble(entity2);
		Map<String, Object> map2 = MenusEntity.MenusMapAssemble("我要查询", list2);

		MenusEntity entity3 = new MenusEntity("view", "授权入口1", null, "http://m23j177109.iok.la/BaseWXServer/index");
		MenusEntity entity4 = new MenusEntity("view", "授权入口2", null, "http://m23j177109.iok.la/BaseWXServer/index");
		List list3 = MenusListAssemble(new Object[] { entity3, entity4});

		Map map3 = MenusMapAssemble("授权", list3);

		// json解析
		List<Object> list = MenusEntity.MenusListAssemble(map1, map2, map3);
		Map<String, Object> map = MenusEntity.MenusButtonAssemble(list);
		return JSONObject.toJSONString(map);
	}


	/**
	 *
	 * @param object
	 * @return
	 */
	public static List<Object> MenusListAssemble(Object... object) {
		List<Object> list1 = new ArrayList<Object>();
		for (int i = 0; i < object.length; i++) {
			list1.add(object[i]);
		}
		return list1;
	}

	/**
	 *
	 * @param name
	 * @param object
	 * @return
	 */
	public static Map<String, Object> MenusMapAssemble(String name, Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("sub_button", object);
		return map;
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	public static Map<String, Object> MenusButtonAssemble(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("button", object);
		return map;
	}


}
