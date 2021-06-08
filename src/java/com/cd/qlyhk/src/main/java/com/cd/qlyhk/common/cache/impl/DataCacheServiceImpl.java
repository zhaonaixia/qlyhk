package com.cd.qlyhk.common.cache.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.service.impl.PubCommonServiceImpl;
import com.cd.qlyhk.vo.QlyRhDefaultCustomTableVO;
import com.cd.qlyhk.vo.QlyRhSysAreaVO;
import com.cd.qlyhk.vo.QlyRhSysCategoryVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.rdf.base.IValueObject;
import com.cd.rdf.base.dao.IBaseDAO;
import com.cd.rdf.cache.redis.RedisService;

/**
 * 数据缓存接口实现类
 * 
 * @author sailor
 *
 */
@Service(IDataCacheService.BEAN_ID)
public class DataCacheServiceImpl implements IDataCacheService {

	private static final Logger logger = LoggerFactory.getLogger(DataCacheServiceImpl.class);

	private final String mapperNamespace = PubCommonServiceImpl.class.getName();

	@Autowired
	private IBaseDAO baseDAO;

	@Autowired
	private RedisService redisService;

	@Override
	public String get(String key) {
		return redisService.get(key);
	}

	@Override
	public void set(String key, String value, int expiredSeconds) {
		redisService.set(key, value, expiredSeconds);
	}
	
	public void del(String key) {
		redisService.del(key);
	}

	@Override
	public <T extends IValueObject> List<T> getRhSysCategoryList() {
		List<QlyRhSysCategoryVO> result = null;
		// < 从缓存获取数据
		String cacheKey = DataCacheConst.CS_cache_sys_category_key;
		String cacheDataJson = null;
		cacheDataJson = redisService.get(cacheKey, DataCacheConst.cache_sec_sys_init_data);
		if (StringUtils.isNotBlank(cacheDataJson)) {
			result = (List<QlyRhSysCategoryVO>) JSON.parseArray(cacheDataJson, QlyRhSysCategoryVO.class);
		}
		// > 从缓存获取数据

		// < 从缓存找不到从数据库获取数据
		else {
			logger.debug("获取文章类别列表数据 为null，从数据库获取...");
			result = baseDAO.findListBy(mapperNamespace + ".getCategoryList");
			// < 写入缓存
			String jsonStr = JSON.toJSONString(result);
			redisService.set(cacheKey, jsonStr, DataCacheConst.cache_sec_sys_init_data);
			// >写入缓存

			logger.debug("系统文章类别列表写入缓存成功");
		}
		// > 从缓存找不到从数据库获取数据
		return (List<T>) result;
	}

	@Override
	public <T extends IValueObject> T getQlyRhUserVO(String openId) {
		QlyRhUserVO result = null;
		// < 从缓存获取数据
		String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, openId);
		String cacheDataJson = null;
		cacheDataJson = redisService.get(cacheKey, DataCacheConst.cache_sec_temp_data);
		if (StringUtils.isNotBlank(cacheDataJson)) {
			result = JSON.parseObject(cacheDataJson, QlyRhUserVO.class);
		}
		// > 从缓存获取数据

		// < 从缓存找不到从数据库获取数据
		else {
			logger.debug("获取用户信息数据 为null，从数据库获取...");
			result = baseDAO.findOne(mapperNamespace + ".getQlyRhUserVO", openId);
			
			if(result != null) {
				// < 写入缓存
				String jsonStr = JSON.toJSONString(result);
				redisService.set(cacheKey, jsonStr, DataCacheConst.cache_sec_temp_data);
				// >写入缓存

				logger.debug("用户信息写入缓存成功");
			}
			
		}
		return (T) result;
	}

	@Override
	public <T extends IValueObject> List<T> querySysAreaList() {
		List<QlyRhSysAreaVO> result = null;
		// < 从缓存获取数据
		String cacheKey = DataCacheConst.CS_cache_sys_area_key;
		String cacheDataJson = null;
		cacheDataJson = redisService.get(cacheKey, DataCacheConst.cache_sec_sys_init_data);
		if (StringUtils.isNotBlank(cacheDataJson)) {
			result = (List<QlyRhSysAreaVO>) JSON.parseArray(cacheDataJson, QlyRhSysAreaVO.class);
		}
		// > 从缓存获取数据

		// < 从缓存找不到从数据库获取数据
		else {
			logger.debug("获取系统行政区域数据 为null，从数据库获取...");
			result = baseDAO.findListBy(mapperNamespace + ".querySysAreaList");
			// < 写入缓存
			String jsonStr = JSON.toJSONString(result);
			redisService.set(cacheKey, jsonStr, DataCacheConst.cache_sec_sys_init_data);
			// >写入缓存

			logger.debug("系统行政区域列表写入缓存成功");
		}
		// > 从缓存找不到从数据库获取数据
		return (List<T>) result;
	}
	
	@Override
	public <T extends IValueObject> List<T> queryRhDefaultCustomTableList(String moduleCode) {
		List<QlyRhDefaultCustomTableVO> result = null;
		// < 从缓存获取数据
		String cacheKey = String.format(DataCacheConst.CS_cache_custom_table_key, moduleCode);
		String cacheDataJson = null;
		cacheDataJson = redisService.get(cacheKey, DataCacheConst.cache_sec_sys_init_data);
		if (StringUtils.isNotBlank(cacheDataJson)) {
			result = (List<QlyRhDefaultCustomTableVO>) JSON.parseArray(cacheDataJson, QlyRhDefaultCustomTableVO.class);
		}
		// > 从缓存获取数据

		// < 从缓存找不到从数据库获取数据
		else {
			logger.debug("获取默认自定义表格列显示数据为null，从数据库获取...");
			result = baseDAO.findListBy(mapperNamespace + ".queryDefaultCustomTable", moduleCode);
			// < 写入缓存
			String jsonStr = JSON.toJSONString(result);
			redisService.set(cacheKey, jsonStr, DataCacheConst.cache_sec_sys_init_data);
			// >写入缓存

			logger.debug("默认自定义表格列显示数据写入缓存成功");
		}
		// > 从缓存找不到从数据库获取数据
		return (List<T>) result;
	}

}
