package com.cd.rdf.base;

import java.util.TimeZone;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * <pre>
 * 程序的中文名称。
 * </pre>
 * @author
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容: 
 * </pre>
 */
public class XstreamFactory {

	private static final String[] acceptableFormats = {"yyyy-MM-dd"};

	public static XStream newInstance() {
		XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
		//格式化日期，从UTC改为CST
		xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", acceptableFormats,TimeZone.getTimeZone("GMT+8")));
		//忽略没在javabean没注册的节点。
		xstream.ignoreUnknownElements();
		xstream.autodetectAnnotations(true);
		return xstream;
	}
	
	public static XStream newInstance(Class c){
		XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
		//格式化日期，从UTC改为CST
		xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", acceptableFormats,TimeZone.getTimeZone("GMT+8")));
		xstream.processAnnotations(c);
		//忽略没在javabean没注册的节点。
		xstream.ignoreUnknownElements();
		return xstream;
	}
	
}
