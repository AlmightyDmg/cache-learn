package com.haizhi.databridge.bean.domain.importdata;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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

import com.haizhi.data.jpa.domain.HaizhiBaseDomainBean;

/**
 * @Description  
 * @Author  Sencheng
 * @Date 2021-06-02 
 */

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
@Table(name = "t_table")
public class TTableBean extends HaizhiBaseDomainBean {

	private static final long serialVersionUID =  9076000672977972062L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   	@Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
	private Long id;

	@NotBlank
   	@Column(name = "table_id", length = 40, nullable = false)
	private String tableId;

	@NotBlank
   	@Column(name = "db_id", length = 40, nullable = false)
	private String dbId;

   	@Column(name = "scheduler_id", length = 40, nullable = true)
	private String schedulerId;

   	@Column(name = "tb_name", length = 1024, nullable = false)
	private String tbName;

   	@Column(name = "sync_config", columnDefinition = "longtext")
	private String syncConfig;

   	@Column(name = "owner", length = 1024, nullable = false)
	private String owner;

   	@Column(name = "status", nullable = true)
	private String status;

   	@Column(name = "exception", columnDefinition = "longtext")
	private String exception;

   	@Column(name = "traceback", columnDefinition = "longtext")
	private String traceback;

   	@Column(name = "posted", nullable = true, columnDefinition = "int(11)")
	private Integer posted;

   	@Column(name = "fetched", nullable = false, columnDefinition = "int(11)")
	private Integer fetched;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "create_at")
	private Timestamp createAt;

   	@Column(name = "modify_at")
	private Timestamp modifyAt;

   	@Column(name = "start_at")
	private Timestamp startAt;

   	@Column(name = "finish_at")
	private Timestamp finishAt;

   	@Column(name = "deleted", nullable = false, columnDefinition = "int(4)")
	private Integer deleted;

   	@Column(name = "remark", columnDefinition = "longtext")
	private String remark;

}
