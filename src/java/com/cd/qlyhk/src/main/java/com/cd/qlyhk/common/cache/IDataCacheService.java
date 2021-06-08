package com.cd.qlyhk.common.cache;

import java.util.List;

import com.cd.rdf.base.IValueObject;

/**
 * 数据缓存接口定义
 * @author sailor
 *
 */
public interface IDataCacheService {

	final String BEAN_ID = "dataCacheService";
	
	/**
     *	 读取缓存数据
     * @param key String key
     * @return String 数据
     */
    public String get(String key);
    
    /**
     * 	写入缓存数据
     * @param key String key
     * @param value String 数据
     * @param expiredSeconds int 有效时间，重置数据缓存有效时间
     * @return String 数据
     */
    public void set(String key, String value, int expiredSeconds);
    
    /**
     *	 删除缓存数据
     * @param key String key
     * @return String 数据
     */
    public void del(String key);
    
	/**
     * 	获取系统定义的文章类别列表
     * @return List<QlyRhSysCategoryVO>
     */
    public <T extends IValueObject> List<T> getRhSysCategoryList();
    
    /**
     	* 根据企业UD获取企业及账套信息
     * @param openId String 用户的唯一标识
     * @return QlyRhUserVO对象
     */
    public <T extends IValueObject> T getQlyRhUserVO(String openId);

    /**
                  * 获取行政区域列表
     * @return
     */
	public <T extends IValueObject> List<T> querySysAreaList();
	
	/**
     * 	获取默认的自定义表格列显示列表
     * @return List<QlyRhDefaultCustomTableVO>
     */
    public <T extends IValueObject> List<T> queryRhDefaultCustomTableList(String moduleCode);
}
