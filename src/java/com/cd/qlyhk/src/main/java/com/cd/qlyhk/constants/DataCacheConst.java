package com.cd.qlyhk.constants;

/**
 * <pre>
 * 缓存服务常量定义
 * </pre>
 * 
 * @author sailor
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public abstract class DataCacheConst {
	
    /** 微信访问接口token主键格式:qlyhk:sys:access_token */
    public static final String CS_cache_sys_token_key             = "qlyhk:sys:access_token";
    
    /** 微信访问接口ticket主键格式:qlyhk:sys:jsapi_ticket */
    public static final String CS_cache_sys_ticket_key            = "qlyhk:sys:jsapi_ticket";
    
    /** 千里眼系统财务软件主键格式:qlyhk:sys:category */
    public static final String CS_cache_sys_category_key      	  = "qlyhk:sys:category";
    
    /** 千里眼系统行政区域主键格式:qlyhk:sys:area */
    public static final String CS_cache_sys_area_key              = "qlyhk:sys:area";
    
    /** 千里眼用户信息主键格式:qlyhk:t:user:+[openId] */
    public static final String CS_cache_temp_user_key          	  = "qlyhk:user:%s";
    
    /** 千里眼系统财务软件主键格式:qlyhk:custom:table:%s */
    public static final String CS_cache_custom_table_key      	  = "qlyhk:custom:table:%s";
    
    /** 微信访问接口信息缓存时长(秒)，缓存2小时 */
    public static final int    cache_sec_weixin_token             = 60 * 60 * 2;
    
    /** 千里眼通用临时数据缓存时长(秒)，缓存2小时 */
    public static final int    cache_sec_temp_data                = 60 * 60 * 2;
    
    /** 千里眼系统初始化基础数据缓存时长(秒)，缓存12小时 */
    public static final int    cache_sec_sys_init_data            = 60 * 60 * 12;

    /** 千里眼后台管理系统用户SESSION会话主键格式:qlyhk:user:session:+[token] */
    public static final String CS_cache_user_session_key          	  = "qlyhk:user:session:%s";
    
    /** 千里眼后台管理系统用户SESSION会话缓存时长(秒)，缓存12小时 */
    public static final int    cache_sec_user_session_data            = 60 * 60 * 12;
}
