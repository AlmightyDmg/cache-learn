package com.haizhi.databridge.bean.domain.exportdata;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
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
 * 
 * @author tools
 * @date 2020-02-12 14:15:16
 */
@ApiModel(description = "")

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
@Table(name = "JOB")
public class JobBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;
	@NotBlank
	@Column(name = "job_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String jobId;
	@Column(name = "ds_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String dsId;
	@NotBlank
	@Column(name = "user_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String userId;
	@NotBlank
	@Column(name = "ent_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String entId;
	@NotBlank
	@Column(name = "config", nullable = false, columnDefinition = "longtext")
	private String config;
	@NotNull
	@Column(name = "`type`", nullable = false, columnDefinition = "int(2)")
	private Integer type;
	@Column(name = "crontab", nullable = true, length = 256, columnDefinition = "varchar(256)")
	private String crontab;
	@Column(name = "rela_id", nullable = true, length = 40, columnDefinition = "char(40)")
	private String relaId;
	@Column(name = "`desc`", nullable = true, columnDefinition = "longtext")
	private String desc;
	@Column(name = "export_mode", nullable = true, columnDefinition = "longtext")
	private String exportMode;
	@Column(name = "export_failure_strategy", nullable = false, columnDefinition = "int(2)")
	private Integer exportFailureStrategy;
	@Column(name = "xtb_id", nullable = true, columnDefinition = "char(40)")
	private String xtbId;
	@Column(name = "execute_mode", nullable = false, columnDefinition = "int(2)")
	private Integer executeMode;
	@Column(name = "`count`", nullable = true, columnDefinition = "text")
	private String count;
	// CREATE = 0; SYNCING = 1; NORMAL = 2; ERROR = 3; QUEUE = 4; STOP = 5
	@Column(name = "status", nullable = false, columnDefinition = "int(2)")
	private Integer status;
	@Column(name = "error_msg", nullable = true, columnDefinition = "longtext")
	private String errorMsg;
	@Column(name = "ctime", nullable = true, columnDefinition = "datetime")
	private Timestamp ctime;
	@Column(name = "utime", nullable = true, columnDefinition = "datetime")
	private Timestamp utime;
	@Column(name = "etime", nullable = true, columnDefinition = "datetime")
	private Timestamp etime;
	@Column(name = "sync_config_back", nullable = true, length = 256, columnDefinition = "varchar(256)")
	private String syncConfigBack;
	@Column(name = "job_source", nullable = false, columnDefinition = "int(2)")
	private Integer jobSource;
}
