package com.cd.qlyhk.reptile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cd.qlyhk.constants.ArticleConstants;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.SpringUtils;
import com.cd.qlyhk.vo.QlyRhCompanyArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleDetailVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileQueueVO;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class WeixinArticePipeline implements Pipeline {

	private static final Logger logger = LoggerFactory.getLogger(WeixinArticePipeline.class);
	
    final String uploadPath = "upload/image";
    
    String openId;
    
    String filePath;
    
    String fileUrlPath;
    
    String shareText;
    
    Integer companyId = null;
    
    Map<String, Object> param;

    final ConcurrentHashMap<String, String> contentMap = new ConcurrentHashMap<>();

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    IArticleService articleService = null;
    
    IMessageCenterService messageCenterService = null;
    
    IPubCommonService pubCommonService = null;
    
//    IDataCacheService dataCacheService = null;
    
    public WeixinArticePipeline(Map<String, Object> param) {
    	this.param = param;
    }
    
    @Override
    public void process(ResultItems resultItems, Task task) {
    	logger.info("*****************************resultItems**************************");
//    	logger.info(openId);
//    	logger.info(filePath);
//    	logger.info(fileUrlPath);
        String title = resultItems.get("title");
        final String source = resultItems.get("source");
//        final String editDate = resultItems.get("editDate");
        final String content = resultItems.get("content");
//        final String contentSnap = resultItems.get("contentSnap");

        //文章地址
        final String articeUrl = resultItems.getRequest().getUrl();
        contentMap.put(articeUrl, content);

        if(param.get("openId") != null) {
        	openId = (String) param.get("openId");
        }
        
        if(param.get("filePath") != null) {
        	filePath = (String) param.get("filePath");
        }
        
        if(param.get("fileUrlPath") != null) {
        	fileUrlPath = (String) param.get("fileUrlPath");
        }
        
        if(param.get("companyId") != null) {
        	companyId = Integer.parseInt(param.get("companyId").toString());
        }
        
        if(param.get("shareText") != null) {
        	shareText = (String) param.get("shareText");
        }
        
        //图片地址
        final List<String> imageUrls = resultItems.get("imageUrls");
        List<String> convertImgUrls = new ArrayList<String>();
        String coverPhotoUrl = fileUrlPath + "images/qlyfmtp.jpg";
        
        if(imageUrls != null && imageUrls.size() > 0) {
        	imageUrls.forEach(item -> {
                try {
                    String newContent = conventContent(item, filePath, fileUrlPath, contentMap.get(articeUrl), convertImgUrls);
                    if(StringUtils.isNotBlank(newContent)){
                        contentMap.put(articeUrl, newContent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        
        if(convertImgUrls.size() > 0) {
        	coverPhotoUrl = convertImgUrls.get(0);
        }
        
        /*
        //视频地址
        final List<String> videoUrls = resultItems.get("videoUrls");
        logger.info(videoUrls + "");
        videoUrls.forEach(item -> {
            try {
                String newContent = conventVideoContent(item, filePath, fileUrlPath, contentMap.get(articeUrl));
                if(StringUtils.isNotBlank(newContent)){
                    contentMap.put(articeUrl, newContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
        
//        logger.info(convertImgUrls + "");
        //替换后的内容从contentMap中获取
        // 测试使用
//        String filename = filePath + System.currentTimeMillis() + ".txt";
//		Test.WriteStringToFile5(filename, contentMap.get(articeUrl));
        
        if(articleService == null) {
        	articleService = (IArticleService) SpringUtils.getObject(IArticleService.BEAN_ID);
        }
        
        if(messageCenterService == null) {
        	messageCenterService = (IMessageCenterService) SpringUtils.getObject(IMessageCenterService.BEAN_ID);
        }
        
        if(pubCommonService == null) {
        	pubCommonService = (IPubCommonService) SpringUtils.getObject(IPubCommonService.BEAN_ID);
        }
//        logger.info("*********************articleService2**********************" + articleService);
        int categoryId = Constants.ARTICLE_DEFAULT_CATEGORYID;// 推荐
        String ispublic = "1";
        String collect_date = DateUtil.formatDate(new Date());
        String audit_status = ArticleConstants.ARTICLE_STATUS_PENDING;
        if(StringUtils.isNotBlank(openId)) {
        	ispublic = "0";
        }
        QlyRhReptileQueueVO rqVO = articleService.getQlyRhReptileQueueVO(articeUrl);
        if(rqVO != null) {
        	categoryId = rqVO.getCategory_id();
        	rqVO.setContent_url(articeUrl);
            rqVO.setIsload("1");
            articleService.updateQlyRhReptileQueueVO(rqVO);
        }
        
        if(companyId != null) {
        	//02表示品牌类型
        	categoryId = pubCommonService.getCategoryIdByType("02");
        	audit_status = ArticleConstants.ARTICLE_STATUS_SUCCESS;
        }
        
		/*
		 * if(StringUtils.isBlank(title) && StringUtils.isNotBlank(openId)) { //
		 * 没有获取到文章标题的，默认设置成来自xxx的分享 if(dataCacheService == null) { dataCacheService =
		 * (IDataCacheService) SpringUtils.getObject(IDataCacheService.BEAN_ID); }
		 * 
		 * QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId); title = "来自" +
		 * userVO.getUser_name() + "的分享"; }
		 */
        
        QlyRhReptileArticleVO articleVO = new QlyRhReptileArticleVO();
        articleVO.setCategory_id(categoryId);
        articleVO.setUuid(UUID.randomUUID().toString());
        articleVO.setArticle_title(title);
        articleVO.setArticle_url(articeUrl);
        articleVO.setPic_url(coverPhotoUrl);
        articleVO.setCreate_user(openId);
        articleVO.setAudit_status(audit_status);
        articleVO.setIspublic(ispublic);
        articleVO.setSource(source);
        articleVO.setCollect_date(collect_date);
        articleVO.setIstop(ArticleConstants.ARTICLE_IS_NOTTOP);
        articleVO.setShare_text(shareText);
        articleService.insertQlyRhReptileArticleVO(articleVO);
        
        QlyRhReptileArticleDetailVO articleDetailVO = new QlyRhReptileArticleDetailVO();
        articleDetailVO.setArticle_uuid(articleVO.getUuid());
        articleDetailVO.setArticle_content(contentMap.get(articeUrl));
        articleService.insertQlyRhReptileArticleDetailVO(articleDetailVO);
        
        List<Map<String, Object>> addCategorys = new ArrayList<Map<String, Object>>();
        Map<String, Object> addParam = new HashMap<String, Object>();
        addParam.put("article_id", articleVO.getId());
        addParam.put("category_id", categoryId);
        addCategorys.add(addParam);
        articleService.batchInsertArticleAndCategory(addCategorys);
        
        // 个人创建的文章
        if(StringUtils.isNotBlank(openId) && companyId == null) {
        	// 推送创建文章成功消息
        	messageCenterService.sendCreateArticleMsg(openId, articleVO.getUuid(), articleVO.getArticle_title());
        }
        
        if(companyId != null) {
        	QlyRhCompanyArticleVO companyArticleVO = new QlyRhCompanyArticleVO();
        	companyArticleVO.setCompany_id(companyId);
        	companyArticleVO.setArticle_id(articleVO.getId());
        	articleService.insertQlyRhCompanyArticleVO(companyArticleVO);
        }
        
        //保存到数据库成功后台从Map中移除
        contentMap.remove(articeUrl);
    }

    String conventContent(String url, String picPath, String imgUrlPath, String content, List<String> convertImgUrls) throws Exception {
    	Date dt = new Date();
    	String currentDate = DateUtil.formatDateByFormat(dt, "yyyyMMdd");
    	picPath += "images/article" + File.separator;
    	imgUrlPath += "images/article" + File.separator;
    	
    	picPath += currentDate + File.separator;
        File file = new File(picPath);
        if(!file.exists()) file.mkdirs();

        String imgSuffix = url.substring(url.length()-3);
        if(!isImageSuffix(imgSuffix)) {
        	return content;
        }
        if("peg".equals(imgSuffix)) imgSuffix = "jpg";
        // 文件名
        String fileName = UUID.randomUUID() + "." + imgSuffix;
        // 上传文件目录完整路径
        String filePath = picPath + fileName;
        // 访问图片的url
        String urlPath = imgUrlPath + currentDate + File.separator + fileName;
        
        String newContent = null;
        byte[] data = new byte[10240];
        int len = 0;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection http = null;
        try {
            URL urlGet = new URL(url);
            http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            inputStream = http.getInputStream();

            fileOutputStream = new FileOutputStream(filePath);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
//            System.out.println(filePath);
//            File uploadFile = new File(filePath);
//            String newContent = content.replace(url, AliyunOssUtils.upload(uploadFile, _getFileKey(filePath)));
            convertImgUrls.add(urlPath);
            newContent = content.replace(url, urlPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(http != null){
                http.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return newContent;
    }
    
    String conventVideoContent(String url, String videoPath, String videoUrlPath, String content) throws Exception {
    	Date dt = new Date();
    	String currentDate = DateUtil.formatDateByFormat(dt, "yyyyMMdd");
    	videoPath += "video/article" + File.separator;
    	videoUrlPath += "video/article" + File.separator;
    	
    	videoPath += currentDate + File.separator;
        File file = new File(videoPath);
        if(!file.exists()) file.mkdirs();
        
        String videoSuffix = "mp4";
        if(url.indexOf("?") > -1) {
        	url = url.substring(0, url.indexOf("?"));
        	System.out.println(url);
        	videoSuffix = url.substring(url.length()-3);
        }
        // 文件名
        String fileName = UUID.randomUUID() + "." + videoSuffix;
        // 上传文件目录完整路径
        String filePath = videoPath + fileName;
        // 访问视频的url
        String urlPath = videoUrlPath + currentDate + File.separator + fileName;
        
        String newContent = null;
        byte[] data = new byte[10240];
        int len = 0;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection http = null;
        try {
            URL urlGet = new URL(url);
            http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            inputStream = http.getInputStream();

            fileOutputStream = new FileOutputStream(filePath);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            
            newContent = content.replace(url, urlPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(http != null){
                http.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return newContent;
    }

    private String _getFileKey(String filename){

        String uuid= UUID.randomUUID().toString();
//        synchronized (WeixinArticePipeline.class) {
//            uuid = String.valueOf(new Date().getTime());
//        }

        return _processPath(new StringBuilder().append(uploadPath)
                .append(File.separator).append(dateFormat.format(new Date())).append(File.separator).append(uuid)
                .append(getSuffix(filename)).toString());
    }

    private String _processPath(String imgUrl){
        return imgUrl.replaceAll("\\\\", "/");
    }

    private String getSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    private boolean isImageSuffix(String suffix) {
    	if("bmp".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix)
    			|| "png".equalsIgnoreCase(suffix) || "gif".equalsIgnoreCase(suffix) || "peg".equalsIgnoreCase(suffix)) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
