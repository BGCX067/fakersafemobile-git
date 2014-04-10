package com.faker.mobilesafe.deal;

/**
 * 存放配置信息
 * 
 * @author Administrator
 * 
 */
public class ConstConfig {

	public static final String SPNAME = "config"; // SharedPrefence文件名
	public static final String ISUPDATE = "update"; // 是否检查更新
	public static final String APKURL = "http://115.200.123.173/mobilesafe/update.xml"; // apk更新xml文件地址
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public static final String ISINITDB = "is_init_db"; // 是否初始化归属地数据库
	public static final String ISOPENADRESS = "is_address_open"; // 是否开启归属地服务
	public static final String ISINTECPTER = "is_intecpter"; // 是否开启拦截服务
	public static final String ISAPPLOCK = "is_applock"; // 是否开启拦截服务
	public static final String ISLOSTFIRST = "is_lost_first"; // 是否是第一次登陆手机防盗
	public static final String ISAUTOIP = "is_auto_ip"; // 是否开启ip自动拨号
	public static final String IPNUMBER = "ipnumber"; // 是否开启ip自动拨号
	public static final String ISLOSTSETUP = "is_lost_setup"; // 是否完成手机防盗设置
	public static final String SAFENUMBER = "safenumber"; // 安全号码
	public static final String ISBOUNDSIM = "isboundsim"; // 是否绑定sim卡
	public static final String ISOPENLOST = "isopenlost"; // 是否开启手机防盗
	public static final String SIMSERIAL = "simSerial"; // SIM卡唯一标识
	public static final String ISSHOW = "isshow"; // 是否隐藏手机防盗模块
	public static final String LOCATION_COMMAND = "#*location*#"; // 获取手机位置指令
	public static final String DELETE_COMMAND = "#*delete*#"; // 恢复出厂设置指令
	public static final String LOCK_COMMAND = "#*lockscreen*#"; // 锁定手机屏幕指令
	public static final String ALARM_COMMAND = "#*alarm*#"; // 发出警报指令
	public static final String RELOAD_COMMAND = "20142014"; // 重新进入模块指令
	public static final String MODULE_NAME = "moudle_name"; // 修改后的模块名称
	public static final String LOCKSCREEN_PWD = "lock_pwd"; // 锁屏解说密码
	
	public static final String LOCK_PATTERN = "is_lock_pattern"; // 锁屏解说密码

	public static final String FLOATVIEW_X = "floatview_x"; // 来电归属地悬浮窗口位置
	public static final String FLOATVIEW_Y = "floatview_y"; // 来电归属地悬浮窗口位置

    public static final String TRAFFIC_SET = "traffic_set";
    public static final String TRAFFIC_MOBILE_TOTAL = "mobile_total";
    public static final String ISTRAFFIC = "is_traffic_service";

}
