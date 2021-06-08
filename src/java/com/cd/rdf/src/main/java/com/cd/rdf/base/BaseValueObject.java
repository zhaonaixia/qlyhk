package com.cd.rdf.base;

import java.util.UUID;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thoughtworks.xstream.XStream;

/**
 * <pre>
 * VO基类
 * </pre>
 * 
 * @author awens
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class BaseValueObject implements IValueObject {

    private final static Logger logger           = LoggerFactory.getLogger(BaseValueObject.class);

    /**
     *
     */
    private static final long   serialVersionUID = 1L;


    /**
     * 值对象转换成XML类型 用于格式化输出值对象内容
     *
     * @return 值对象XML格式数据内容
     */
    @Override
    public String toXml() {
        // XStream xstream = XstreamFactory.newInstance();
        XStream xstream = new XStream();
        return xstream.toXML(this);
    }

    /**
     * 值对象转换成成Json格式字符串 用于格式化输出值对象内容
     *
     * @return 对象转换出来的字符串
     */
    @JsonIgnore
    public String toJsonStr() {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL); //  jackson 实体转json 为NULL或者为空不参加序列化

        try {
            result = mapper.writeValueAsString(this);
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * 生成UUID
     *
     * @return String UUID
     */
    public String makeUUID() {
        String result = UUID.randomUUID().toString().replace("-", "");
        return result;
    }
}
