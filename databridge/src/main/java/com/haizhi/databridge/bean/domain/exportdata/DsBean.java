package com.haizhi.databridge.bean.domain.exportdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.haizhi.data.jpa.domain.HaizhiStandardDomainBean;

/**
 * 同步用到的真实数据库信息
 *
 * @author tools
 * @date 2020-02-12 14:15:13
 */
@ApiModel(description = "同步用到的真实数据库信息")

@Data
@FieldNameConstants
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "DS")
public class DsBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "ds_[32位uuid]")
	@Column(name = "ds_id", nullable = true, length = 35, columnDefinition = "char(35)")
	private String dsId;
	@NotBlank
	@Column(name = "`owner`", nullable = false, length = 32, columnDefinition = "varchar(32)")
	private String owner;
	@Column(name = "`name`", nullable = true, length = 64, columnDefinition = "varchar(64)")
	private String name;
	@ApiModelProperty(value = "1MYSQL|2ORACLE|3SQLSERVER|4OPEN_DS|5baidu_sem")
	@Column(name = "type", nullable = true, columnDefinition = "int(11)")
	private Integer type;
	@Column(name = "`host`", nullable = true, length = 64, columnDefinition = "varchar(64)")
	private String host;
	@Column(name = "`port`", nullable = true, columnDefinition = "int(11)")
	private Integer port;
	@Column(name = "username", nullable = true, length = 128, columnDefinition = "varchar(128)")
	private String username;
	@Column(name = "`password`", nullable = true, length = 256, columnDefinition = "varchar(256)")
	private String password;
	@Column(name = "`database`", nullable = true, length = 64, columnDefinition = "varchar(64)")
	private String database;
	@Column(name = "db_id", nullable = true, length = 35, columnDefinition = "char(35)")
	private String dbId;
//	@ApiModelProperty(value = "数据库的原始schema")
//	@Column(name = "`schema`", nullable = true, columnDefinition = "longtext")
//	private String schema;
//	@ApiModelProperty(value = "用户设置的需要同步的表")
//	@Column(name = "sync_tables", nullable = true, length = 65535, columnDefinition = "text")
//	private String syncTables;
//	@ApiModelProperty(value = "同步设置")
//	@Column(name = "sync_config", nullable = true, length = 1024, columnDefinition = "varchar(1024)")
//	private String syncConfig;
	@ApiModelProperty(value = "最后一次同步状态，状态代码用统一的状态代码")
	@Column(name = "`status`", nullable = false, columnDefinition = "tinyint(4) unsigned")
	private Integer status;
//	@Column(name = "error_msg", nullable = true, length = 65535, columnDefinition = "text")
//	private String errorMsg;
//	@Column(name = "token", nullable = true, length = 256, columnDefinition = "varchar(256)")
//	private String token;
//	@ApiModelProperty(value = "剩余配额")
//	@Column(name = "quota", nullable = true, columnDefinition = "int(11)")
//	private Integer quota;
//	@ApiModelProperty(value = "数据库同步时间偏移配置")
//	@Column(name = "config", nullable = true, length = 65535, columnDefinition = "text")
//	private String config;
//	@ApiModelProperty(value = "标签")
//	@Column(name = "tag", nullable = true, length = 50, columnDefinition = "varchar(50)")
//	private String tag;
//	@ApiModelProperty(value = "是否在排队")
//	@Column(name = "is_queue", nullable = false, columnDefinition = "int(11)")
//	private Integer isQueue;
//	@ApiModelProperty(value = "T+账套配置")
//	@Column(name = "tplus_config", nullable = true, length = 65535, columnDefinition = "text")
//	private String tplusConfig;
//	@ApiModelProperty(value = "0:不同步备注信息，1：同步备注信息")
//	@Column(name = "is_remark", nullable = true, columnDefinition = "tinyint(1)")
//	private Integer isRemark;
//	@ApiModelProperty(value = "数据源是否同步暂停  0:否 1：是")
//	@Column(name = "is_pause", nullable = true, columnDefinition = "tinyint(4)")
//	private Integer isPause;
//	@Column(name = "param", nullable = true, columnDefinition = "longtext")
//	private String param;
//	@Column(name = "flag", nullable = false, columnDefinition = "int(11)")
//	private Integer flag;
	@Column(name = "ent_id", nullable = true, length = 64, columnDefinition = "varchar(64)")
	private String entId;
	@ApiModelProperty(value = "暂停情况：0正常用户暂停 1后台自动暂停")
//	@Column(name = "suspended", nullable = true, columnDefinition = "tinyint(1)")
//	private Integer suspended;
//	@ApiModelProperty(value = "是否启用")
//	@Column(name = "is_use", nullable = false, columnDefinition = "int(2)")
//	private Integer isUse;
//	@ApiModelProperty(value = "数据源下表同步并发信息")
//	@Column(name = "tbs_multi_info", nullable = true, columnDefinition = "longtext")
//	private String tbsMultiInfo;
	@Column(name = "is_net_ssl", nullable = true, columnDefinition = "tinyint(1)")
	private Integer isNetSSL;
	@Column(name = "is_security", nullable = true, columnDefinition = "tinyint(1)")
	private Integer isSecurity;
	@ApiModelProperty(value = "数据源类型，导入还是导出")
	@Column(name = "source_type", nullable = true, columnDefinition = "tinyint(1)")
	private Integer sourceType;
	@ApiModelProperty(value = "备注")
	@Column(name = "ds_desc", nullable = true, columnDefinition = "varchar(256)")
	private String dsDesc;
	@ApiModelProperty(value = "数据库版本信息")
	@Column(name = "version_info", nullable = true, columnDefinition = "varchar(256)")
	private String versionInfo;
}
