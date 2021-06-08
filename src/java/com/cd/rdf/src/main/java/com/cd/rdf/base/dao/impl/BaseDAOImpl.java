package com.cd.rdf.base.dao.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;

import com.cd.rdf.base.dao.IBaseDAO;

/**
 * <pre>
 * 数据库通用操作接口实现
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
public class BaseDAOImpl implements IBaseDAO {

    private SqlSessionTemplate sqlSessionTemplate;


    /**
     * 设置 sqlSessionTemplate。
     * 
     * @param sqlSessionTemplate 设置 sqlSessionTemplate。
     */
    //  @Override
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * 返回 sqlSessionTemplate。
     * 
     * @return 返回 sqlSessionTemplate。
     */
    @Override
    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.sqlSessionTemplate;
    }

    public int add(String statement, Map<String, Object> map) {
        return this.sqlSessionTemplate.insert(statement, map);
    }

    public int add(String statement, Object obj) {
        return this.sqlSessionTemplate.insert(statement, obj);
    }

    /**
     * 查找
     * @param statement
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T findOne(String statement) {
        return (T) this.sqlSessionTemplate.selectOne(statement);
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(String statement, Object param) {
        return (T) this.sqlSessionTemplate.selectOne(statement, param);
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(String statement, Map<String, Object> map) {
        return (T) this.sqlSessionTemplate.selectOne(statement, map);
    }

    /**
     * 查找
     * @param statement
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findListBy(String statement) {
        return (List<T>) this.sqlSessionTemplate.selectList(statement);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListBy(String statement, String param) {
        return (List<T>) this.sqlSessionTemplate.selectList(statement, param);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListBy(String statement, Map<String, Object> map) {
        return (List<T>) this.sqlSessionTemplate.selectList(statement, map);
    }

    /**
     * 范围查找(用于分页查询,适配MySQL的limit语法)
     * @param statement String
     * @param param Map<String, Object> 参数
     * @param startIndex Integer 开始行下标,从0开始(注意:不是从1开始)
     * @param limitRows Integer 返回记录行的最大数目
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findListByRange(String statement, Map<String, Object> param, Integer startIndex, Integer limitRows) {
        return (List<T>) this.sqlSessionTemplate.selectList(statement, param, new RowBounds(startIndex, limitRows));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListBy(String statement, Object obj) {
        return (List<T>) this.sqlSessionTemplate.selectList(statement, obj);
    }

    @Override
    public Map<?, ?> findMapBy(String statement, Map<String, Object> map) {

        return this.sqlSessionTemplate.selectOne(statement, map);
    }

    @Override
    public Map<?, ?> findMapBy(String statement, String param) {

        return this.sqlSessionTemplate.selectOne(statement, param);
    }

    @Override
    public Map<?, ?> findMapBy(String statement, Object obj, String paramkey) {
        return (Map<?, ?>) this.sqlSessionTemplate.selectMap(statement, obj, paramkey);
    }

    @Override
    public int delete(String statement, String param) {
        return this.sqlSessionTemplate.delete(statement, param);
    }

    @Override
    public <T> int delete(String statement, T t) {
        return this.sqlSessionTemplate.delete(statement, t);
    }

    @Override
    public int update(String statement, String param) {
        return this.sqlSessionTemplate.update(statement, param);
    }

    @Override
    public <T> int update(String statement, T t) {
        return this.sqlSessionTemplate.update(statement, t);
    }

    @Override
    public int delete(String statement, Map<String, Object> map) {
        return this.sqlSessionTemplate.delete(statement, map);
    }

    @Override
    public int update(String statement, Map<String, Object> map) {
        return this.sqlSessionTemplate.update(statement, map);

    }
}

