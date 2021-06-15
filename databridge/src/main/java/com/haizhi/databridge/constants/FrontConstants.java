package com.haizhi.databridge.constants;

/**
 * 业务相关配置常量，原app.yaml等取值
 */
public final class FrontConstants {
	private FrontConstants() { }

	public static final Integer FOLDER_LEVEL_LIMIT = 5;


	/********* 内部服务调用，默认参数取值范围 *********/
	// limit
	public static final Integer PARAM_LIMIT = 2000;	// 查询条数limit
	// offset
	public static final Integer PARAM_OFFSET = 0;
	// 0 or 1 的表示真假
	public static final Integer COMMON_NO = 0;
	public static final Integer COMMON_YES = 1;

	//	errorParam 定义常量 ， fe服务需要处理
	public static final String TB_TITLE = "tb_title";
	public static final String FIELD_TITLE = "field_title";
	/** ******* panrora 接口 *********/


	/** ******* overlord 接口 *********/


	/**
	 * 接口参数取值常量
	 */

	// 血缘信息，节点类别
	public static final Integer NODE_TBTYPE_NORMAL = 0;		// 工作表
	public static final Integer NODE_TBTYPE_STREAM = 1;		// 流式表
}
