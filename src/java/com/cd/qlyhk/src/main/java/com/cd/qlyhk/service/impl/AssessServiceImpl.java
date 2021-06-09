package com.cd.qlyhk.service.impl;


import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.AssessService;
import com.cd.rdf.base.dao.IBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(AssessService.BEAN_ID)
public class AssessServiceImpl implements AssessService {
    private final String mapperNamespace = AssessServiceImpl.class.getName();


    @Autowired
    private IBaseDAO iBaseDAO;

    @Override
    public Response finalByArticleAndStatistics(Map map) {
        List list = iBaseDAO.findListBy(mapperNamespace + ".finalByArticleAndStatistics", map);
        Response response = Response.getDefaulTrueInstance();
        if (list.size()!=0){
            response.setMessage("查询成功");
            response.setData(list);
            return response;
        }
        response.setSuccess(false);
        response.setMessage("查询结果为空");
        return response;
    }

    @Override
    public Response finalByMorning(Map map) {
        List list = iBaseDAO.findListBy(mapperNamespace + ".finalByMorning", map);
        Response response = Response.getDefaulTrueInstance();
        if (list.size()!=0){
            response.setMessage("查询成功");
            response.setData(list);
            return response;
        }
        response.setSuccess(false);
        response.setMessage("查询结果为空");
        return response;
    }

    @Override
    public Response finalByUser(Map map) {
        List list = iBaseDAO.findListBy(mapperNamespace + ".finalByUser", map);
        Response response = Response.getDefaulTrueInstance();
        if (list.size()!=0){
            response.setMessage("查询成功");
            response.setData(list);
            return response;
        }
        response.setSuccess(false);
        response.setMessage("查询结果为空");
        return response;
    }

    @Override
    public Response finalByArticle(Map map) {
        List list = iBaseDAO.findListBy(mapperNamespace + ".finalByArticle", map);
        Response response = Response.getDefaulTrueInstance();
        if (list.size()!=0){
            response.setMessage("查询成功");
            response.setData(list);
            return response;
        }
        response.setSuccess(false);
        response.setMessage("查询结果为空");
        return response;
    }
}
