package com.haizhi.databridge.bean.domain.exportdata;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import com.haizhi.data.jpa.domain.HaizhiBaseDomainBean;
/**
 * job的更新记录
 * 
 * @author tools
 * @date 2020-02-12 14:15:14
 */
@ApiModel(description = "导出任务的更新记录")

@Data
@FieldNameConstants
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "EXPORT_LOG")
public class ExportLogBean extends HaizhiBaseDomainBean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
	private Long id;
	@NotBlank
	@Column(name = "job_id", nullable = false, length = 35, columnDefinition = "varchar(35)")
	private String jobId;
	@ApiModelProperty(value = "0成功 1失败")
	@Column(name = "status", nullable = true, columnDefinition = "tinyint(2) unsigned")
	private Integer status;
	@NotBlank
	@Column(name = "count", nullable = false, columnDefinition = "text")
	private String count;
	@NotBlank
	@Column(name = "error_msg", nullable = false, columnDefinition = "longtext")
	private String errorMsg;
	@Column(name = "start_time", nullable = false, columnDefinition = "timestamp")
	private Timestamp startTime;
	@Column(name = "end_time", nullable = false, columnDefinition = "timestamp")
	private Timestamp endTime;
	@Column(name = "cost_time", nullable = false, columnDefinition = "int(11) unsigned")
	private Integer costTime;
}
