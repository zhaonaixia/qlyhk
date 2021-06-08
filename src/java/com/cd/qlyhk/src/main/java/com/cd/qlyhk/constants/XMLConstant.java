package com.cd.qlyhk.constants;

/**
 * XML节点常量类
 * @author sailor
 *
 */
public class XMLConstant {

    private XMLConstant() {

    }
    /**
     * 微信接收消息xml文件的属性
     */
    public static final class XmlPropeties {
        private XmlPropeties() {

        }
        /**
         * 发送方帐号（一个OpenID）
         */
        public static final String FROMUSERNAME = "FromUserName";
        /**
         * 开发者微信号
         */
        public static final String TOUSERNAME = "ToUserName";
        /**
         * 消息创建时间 （整型）
         */
        public static final String CREATETIME = "CreateTime";
        /**
         * 消息类型:text image link
         */
        public static final String MSGTYPE = "MsgType";
        /**
         * 文本消息内容
         */
        public static final String CONTENT = "Content";
        /**
         * 消息id，64位整型
         */
        public static final String MSGID = "MsgId";
        /**
         * 图片消息才有，图片链接
         */
        public static final String PICURL = "PicUrl";
        /**
         * 地理位置纬度
         */
        public static final String LOCATION_X = "Location_X";
        /**
         * 地理位置经度
         */
        public static final String LOCATION_Y = "Location_Y";
        /**
         * 地图缩放大小
         */
        public static final String SCALE = "Scale";
        /**
         * 地理位置信息
         */
        public static final String LABEL = "Label";
        /**
         * 消息标题(链接消息才有）
         */
        public static final String TITLE = "Title";
        /**
         * 消息描述(链接消息才有）
         */
        public static final String DESCRIPTION = "Description";
        /**
         * 消息标题(链接消息才有）
         */
        public static final String URL = "Url";
        /**
         * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)
         */
        public static final String EVENT = "Event";
        /**
         * 事件KEY值，与自定义菜单接口中KEY值对应
         */
        public static final String EVENTKEY = "EventKey";
        /**
         * 扫描信息
         */
        public static final String SCANCODEINFO = "ScanCodeInfo";
        /**
         * 扫描类型，一般是qrcode
         */
        public static final String SCANTYPE = "ScanType";
        /**
         * 扫描结果，即二维码对应的字符串信息
         */
        public static final String SCANRESULT = "ScanResult";
        /**
         * 媒体id，可以调用多媒体文件下载接口拉取数据。
         */
        public static final String MEDIAID = "";
        /**
         * 语音格式，如amr，speex等
         */
        public static final String FORMAT = "";
        /**
         * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
         */
        public static final String THUMBMEDIAID = "";
    }

    /**
     * 微信推送事件动作
     */
    public static final class WXMessageEvents {
        private WXMessageEvents() {

        }
        /**
         * 订阅事件
         **/
        public static final String SUBSCRIBE = "subscribe";
        /**
         * 取消订阅事件
         **/
        public static final String UN_SUBSCRIBE = "unsubscribe";
        /**
         * 自定义菜单点击事件
         **/
        public static final String CLICK = "CLICK";
        /**
         * 文本消息
         **/
        public static final String TEXT = "text";
        /**
         * 图片消息
         **/
        public static final String IMAGE = "image";
        /**
         * 语音消息
         **/
        public static final String VOICE = "voice";
        /**
         * 地图消息
         **/
        public static final String LOCATION = "location";
        /**
         * 扫码推事件
         **/
        public static final String SCANCODE_PUSH = "scancode_push";
        /**
         * 扫码推事件且弹出“消息接收中”提示框的事件
         */
        public static final String SCANCODE_WAITMSG = "scancode_waitmsg";

        /**
         * 扫描带参数二维码事件
         */
        public static final String SCAN_PARAM_CODE = "SCAN";
    }

    public static final class ReceiveMsgType {
        private ReceiveMsgType() {

        }
        /**
         * 事件消息
         */
        public static final String EVENT = "event";
    }

    /**
     * 回复消息类型
     */
    public static final class ReplyMsgType {
        private ReplyMsgType() {

        }
        /**
         * 文本消息
         */
        public static final String TEXT = "text";
        /**
         * 音乐消息
         */
        public static final String MUSIC = "music";
        /**
         * 图文消息
         */
        public static final String NEWS = "news";
        /**
         * 图片消息
         */
        public static final String IMAGE = "image";

    }

    /**
     * 图文消息个数，限制为10条以内
     **/
    public static final int PIC_TEXT_ITEMS_MAX_COUNT = 10;

    public static final class CDATAFlag {
        private CDATAFlag() {

        }
        public static final String FLAG_START = "<![CDATA[";
        public static final String FLAG_END = "]]>";
    }
}
