package com.cd.rdf.constants;

/**
 * 
 * <pre>
 * 用于存储bovms内部常量信息。
 * 从com.cd.fbrp.util.constants.BaseConstants复制过来
 * </pre>
 * 
 * @author Awens
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class BaseConstant {

  /**
   * 登录ID。
   */
  public static final String LOGIN_ID = "loginId";
  /**
   * 登录密码。
   */
  public static final String PASSWD = "passwd";
  /**
   * 逻辑应用ID。
   */
  public static final String APPID = "appId";
  /**
   * 逻辑应用名称。
   */
  public static final String APPNAME = "appName";
  /**
   * 选择逻辑应用前。
   */
  public static final String BEFORE_SEL_APP = "beforeSelApp";
  /**
   * 标识用户。
   */
  public static final String USER_PROFILE = "UserProfile";
  /**
   * 报表业务类型。
   */
  public static final String REPORT_BIZ_TYPE = "report_biz_type";
  /**
   * 标识员工。
   */
  public static final String STAFFVO = "StaffVO";

  /**
   * 菜单类型。
   */
  public static final String MENU_TYPE = "menu_type";
  /**
   * 排在左边菜单风格。
   */
  public static final String MENU_TYPE_LEFT = "left";
  /**
   * 排在顶端菜单风格。
   */
  public static final String MENU_TYPE_TOP = "top";

  /**
   * 表删除标识（Y）。
   */
  public static final String TABLE_DELFLAG_Y = "y";
  /**
   * 表删除标识（N）。
   */
  public static final String TABLE_DELFLAG_N = "n";
  /**
   * 表状态（0）。
   */
  public static final String TABLE_STATUS_0 = "0";
  /**
   * 表状态（1）。
   */
  public static final String TABLE_STATUS_1 = "1";
  /**
   * URL类型的资源。
   */
  public static final Integer RESOURCE_TYPE_URL = Integer.valueOf(0);
  /**
   * 菜单类型的资源。
   */
  public static final Integer RESOURCE_TYPE_MENU = Integer.valueOf(1);

  /**
   * 日期格式。
   */
  public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
  /**
   * 日期格式。
   */
  public static final String DATE_FORMAT_STR = "yyyy-MM-dd";
  /**
   * 时间格式。
   */
  public static final String TIME_FORMAT_STR = "HH:mm:ss";

  /**
   * 默认菜单ID。
   */
  public static final String DEFAULT_MENU_APPID = "0";

  /**
   * 超级管理员所在逻辑应用ID。
   */
  public static final String DEFAULT_ADMIN_APPID = "101";

  /**
   * 标识最大登录失败次数。
   */
  public static final String MAX_FAILED_LOGIN_COUNT = "auth.password.maxFailedLoginCoun";
  /**
   * 默认最大登录失败次数。
   */
  public static final int DEFAULT_MAX_FAILED_LOGIN_COUNT = 5;
  /**
   * 标识用户默认密码。
   */
  public static final String DEFAILT_PASSWORD = "auth.password.defaultPassword";
  /**
   * 用户默认密码。
   */
  public static final String DEFAULT_DEFAULT_PASSWORD = "111111";
  /**
   * 标识超级管理员角色代码。
   */
  public static final String ADMIN_ROLECODE = "auth.admin.rolecode";
  /**
   * 超级管理员角色代码。
   */
  public static final String DEFAULT_ADMIN_ROLECODE = "20012";
  /**
   * 标识超级管理员员工ID。
   */
  public static final String ADMIN_STAFFID = "auth.admin.staffid";
  /**
   * 超级管理员员工ID。
   */
  public static final String DEFAULT_ADMIN_STAFFID = "SUPADMIN";
  /**
   * 超级管理员默认登录账号。
   */
  public static final String DEFAULT_ADMIN_LOGINID = "supadmin";
  /**
   * 标识密码有效时间。
   */
  public static final String PASSWORD_EXPIRE_DAYS = "auth.password.expire_days";
  /**
   * 密码有效时间。
   */
  public static final int DEFAULT_PASSWORD_EXPIRE_DAYS = 30;

  /**
   * 固定参数。
   */
  public static final Long PARAM_FIX_VALUE_TYPE = Long.valueOf(1);
  /**
   * SQL参数。
   */
  public static final Long PARAM_SQL_VALUE_TYPE = Long.valueOf(2);

  /**
   * 资源服务器类型。
   */
  public static final String RES_SERVER_TYPE = "res_server_type";
  /**
   * 资源服务器认证类型。
   */
  public static final String RES_SERVER_AUTH_TYPE = "res_server_auth_type";

  /**
   * 参数控制类型。
   */
  public static final String PARM_CONTROL_TYPE = "control_type";
  /**
   * 参数值类型。
   */
  public static final String PARM_VALUE_TYPE = "value_type";

  /**
   * Boolean类型。
   */
  public static final String YESORNO_TYPE = "yesorno_type";
  /**
   * YES类型。
   */
  public static final String YESORNO_TYPE_YES = "1";
  /**
   *  NO类型。
   */
  public static final String YESORNO_TYPE_NO = "0";

  /**
   * 标识性别。
   */
  public static final String SEX_TYPE = "sex_type";

  /**
   * 链接类型。
   */
  public static final String LINK_TARGET_TYPE = "link_target_type";
  /**
   * 菜单Icon对应的URL。
   */
  public static final String MENU_ICON_URL = "menu_icon_url";

  /**
   * 下拉框默认选项。
   */
  public static final String DEFAULT_ITEMLABEL = "--请选择--";

  /**
   * 认证适配器。
   */
  public static final String AUTH_ADAPTERPROXY = "fbrp_security_authenticationAdapterProxy";
  /** 认证类型。 */
  public static final String AUTH_TYPE = "auth_type";
  /** 本地资料库认证。 */
  public static final String AUTH_localDataBase = "fbrp_security_localDBAuthAp";
  /** 第三方数据库认证。 */
  public static final String AUTH_rdbmsAuthenticationAdapter = "fbrp_security_rdbmsAuthenticationAdapter";
  /** LDAP认证。 */
  public static final String AUTH_ldapAuthenticationAdapter = "fbrp_security_ldapAuthenticationAdapter";
  /** CAS认证。 */
  public static final String AUTH_casAuthenticationAdapter = "fbrp_security_casAuthenticationAdapter";
  /** WebService系统标识(标识第三方集成FBRP)。 */
  public static final String AUTH_webServiceAuthenticationAdapter = "fbrp_security_webServiceAuthenticationAdapter";

  /**
   * FTP默认端口。
   */
  public static final String DEFAULT_FTP_PORT = "21";
  /**
   * 公告类型。
   */
  public static final String NOTICE_TYPE = "notice_type";
  /**
   * 布局类型。
   */
  public static final String LAYOUT_TYPE = "layout_type";
  /**
   * 安全策略。
   */
  public static final String SECURITY_STRATEGY_TYPE = "security_strategy";

  // 系统自定义的数据源类型
  /** 数据库类型_Oracle。 */
  public static final String DATASOURSE_TYPE_ORACLE = "ORACLE";
  /** 数据库类型_Informix。 */
  public static final String DATASOURSE_TYPE_INFORMIX = "INFORMIX";
  /** 数据库类型_MicroSoft SQL Server。 */
  public static final String DATASOURSE_TYPE_MSSQLSERVER = "SQLSERVER";
  /** 数据库类型_MicroSoft SQL Server 2005。 */
  public static final String DATASOURSE_TYPE_MSSQLSERVER2005 = "SQLSERVER2005";
  /** 数据库类型_MySQL。 */
  public static final String DATASOURSE_TYPE_MYSQL = "MYSQL";
  /** 数据库类型_Sybase SQL。 */
  public static final String DATASOURSE_TYPE_SYBASESQL = "SYBASE";
  /** 数据库类型_DB2。 */
  public static final String DATASOURSE_TYPE_DB2 = "DB2";
  /** 数据库类型_Teradata。 */
  public static final String DATASOURSE_TYPE_TERADATA = "TERADATA";
  /** 数据库类型_不支持的类型。 */
  public static final String DATASOURSE_TYPE_NONSUPPORT = "NONSUPPORT";
  /**
   * 数据库类型_JNDI。
   */
  public static final String DATASOURSE_JNDI = "JNDI";

  // 单行记录的数据库操作标记
  /** 数据库操作,无更新。 */
  public static final int DB_OPERATE_NONE = 0;
  /** 数据库操作,修改。 */
  public static final int DB_OPERATE_UPDATE = 1;
  /** 数据库操作,增加。 */
  public static final int DB_OPERATE_INSERT = 2;
  /** 数据库操作,删除。 */
  public static final int DB_OPERATE_DELETE = 3;
  /** 数据库操作描述。 */
  public static final String[] DB_OPERATE_REMARK = { "无更新 ", "修改", "增加", "删除" };

  /**
   * 日志操作类型。
   */
  public static final String LOG_TYPE = "log_type";
  /**
   * 日志业务模块。
   */
  public static final String BIZ_TYPE = "biz_type";
  /**
   * 作用范围类型。
   */
  public static final String RANGE_TYPE = "range_type";
  /**
   * 被允许上传的文件类型。
   */
  public static final String ACCEPTED_TYPE = "accepted_type";

  /** 定制Cognos分析报表的打开动作。 */
  public static final String CUSTOM_COGNOS_OPEN_OPERATION = "custom_cognos_open_operation";
  /** Cognos分析报表的打开方式:"edit"/"view"。 */
  public static final String CUSTOM_COGNOS_OPEN_EDIT_MODE = "editMode";
  /** 是否隐藏菜单:"true"/"false"。 */
  public static final String CUSTOM_COGNOS_OPEN_HIDE_MENU = "hideMenu";
  /** 过滤不显示的工具按钮:IDS_TOOLBAR_OPEN,IDS_TOOLBAR_NEW,...多个以逗号隔开。 */
  public static final String CUSTOM_COGNOS_OPEN_FILTER_TOOLITEMS = "filterToolItems";

  /**
   * 读取权限的代码 。
   */
  public static final int GRANT_TYPE_READ = 1;
  /**
   * 写权限的代码。
   */
  public static final int GRANT_TYPE_WRITE = 2;
  /**
   * 创建权限的代码。
   */
  public static final int GRANT_TYPE_CREATE = 4;
  /**
   * 删除权限的代码。
   */
  public static final int GRANT_TYPE_DELETE = 8;
  /**
   * 管理权限的代码。
   */
  public static final int GRANT_TYPE_ADMIN = 16;

  /**
   * 树状权限展示方式。
   */
  public static final String GRANT_TYPE_TREE = "TREE";

  /**
   * 网格状权限展示方式。
   */
  public static final String GRANT_TYPE_GRID = "GRID";
  
  
  /**
   * 默认编码。
   */
  public static final String DEFAULT_ENCODEING = "UTF-8";
  /**
   * 
   * */
  public static final String DZSWJ_QTSFDJ_INTERFACE_SESSSIOIN_INFO = "dzswj_session_id_";
  /**
   * WINDOWS编码。
   */
  public static final String WINDOWS_ENCODEING = "GBK";
  
  /**
   * 当前应用软件id ThreadLocal变量。
   */
  public static final ThreadLocal<String> applicationId = new ThreadLocal<String>();

  /**
   * 当前应用软件id ThreadLocal变量。
   */
  public static final ThreadLocal<String> systemMessage = new ThreadLocal<String>();
  public static final String LOGIN_SESSION_NAME = "LOGIN_SESSION_NAME";
  

}
