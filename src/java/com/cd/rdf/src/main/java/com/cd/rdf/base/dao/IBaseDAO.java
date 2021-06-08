package com.cd.rdf.base.dao;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * <pre>
 * 数据库通用操作接口。
 * </pre>
 * 
 * @author frb
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public interface IBaseDAO {

    public static final String BEAN_ID = "baseDAO";


    /**
     * 获取SqlSessionTemplate
     * @return
     */
    public SqlSessionTemplate getSqlSessionTemplate();

    /**
     * 插入map数据
     * @param statement
     * @param map
     * @return
     */
    public int add(String statement, Map<String, Object> map);

    /**
     * 插入对象
     * @param statement
     * @param obj
     * @return
     */
    public int add(String statement, Object obj);

    /**
     * 查找
     * @param statement
     * @return
     */
    public <T> T findOne(String statement);

    /**
     * 查找
     * @param statement
     * @param param
     * @return
     */
    public <T> T findOne(String statement, Object param);

    /**
     * 查找
     * @param statement
     * @param map
     * @return
     */
    public <T> T findOne(String statement, Map<String, Object> map);

    /**
     * 查找
     * @param statement
     * @return List<T>
     */
    public <T> List<T> findListBy(String statement);

    /**
     * 查找
     * @param statement
     * @param param
     * @return
     */
    public <T> List<T> findListBy(String statement, String param);

    /**
     * 查找
     * @param statement
     * @param map
     * @return
     */
    public <T> List<T> findListBy(String statement, Map<String, Object> map);

    /**
     * 范围查找(用于分页查询,适配MySQL的limit语法)
     * @param statement String
     * @param param Map<String, Object> 参数
     * @param startIndex Integer 开始行下标,从0开始(注意:不是从1开始)
     * @param limitRows Integer 返回记录行的最大数目
     * @return List<T>
     */
    public <T> List<T> findListByRange(String statement, Map<String, Object> param, Integer startIndex, Integer limitRows);

    /**
     * 查找
     * @param statement
     * @param obj
     * @return
     */
    public <T> List<T> findListBy(String statement, Object obj);

    /**
     * 查找
     * @param statement
     * @param map
     * @return
     */
    public Map<?, ?> findMapBy(String statement, Map<String, Object> map);

    /**
     * 查找
     * @param statement
     * @param param
     * @return
     */
    public Map<?, ?> findMapBy(String statement, String param);

    /**
     * 查找
     * @param statement
     * @param obj
     * @param paramkey
     * @return
     */
    public Map<?, ?> findMapBy(String statement, Object obj, String paramkey);

    /**
     * 删除
     * @param statement
     * @param param
     * @return
     */
    public int delete(String statement, String param);

    /**
     * 删除
     * @param statement
     * @param map
     * @return
     */
    public int delete(String statement, Map<String, Object> map);

    /**
     * 删除对象
     * @param statement
     * @param t
     * @return
     */
    public <T> int delete(String statement, T t);

    /**
     * 修改数据
     * @param statement
     * @param param
     * @return
     */
    public int update(String statement, String param);

    /**
     * 修改数据
     * @param statement
     * @param map
     * @return
     */
    public int update(String statement, Map<String, Object> map);

    /**
     * 修改对象
     * @param statement
     * @param t
     * @return
     */
    public <T> int update(String statement, T t);

    /**
     * 分页报表，测试
     * @param cls
     * @param param
     * @param start
     * @param limit
     * @return
     */
    // public PagedResult<Object> queryByParam(Class<?> cls,Map<String, Object> param, int start, int limit);
}
