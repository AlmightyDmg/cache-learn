package com.haizhi.databridge.bean.domain.importdata;

import java.sql.Timestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@AttributeOverride(name = "isDel", column = @Column(name = "deleted"))
@Table(name = "t_scheduler_history")
public class TSchedulerHistory extends HaizhiBaseDomainBean {

	private static final long serialVersionUID =  8807656451897447487L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   	@Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
	private Long id;

   	@Column(name = "owner", length = 70, nullable = false)
	private String owner;

	@Column(name = "scheduler_id", length = 40, nullable = false)
	private String schedulerId;

	@Column(name = "scheduler_name", length = 64, nullable = false)
	private String schedulerName;

	@Column(name = "task_id", length = 50, nullable = false)
	private String taskId;

	@Column(name = "elapse", nullable = false, columnDefinition = "int(11)")
	private Integer elapse;

   	@Column(name = "start_at")
	private Timestamp startAt;

   	@Column(name = "params", columnDefinition = "longtext")
	private String params;

   	@Column(name = "status", length = 32)
	private String status;

   	@Column(name = "exception", columnDefinition = "longtext")
	private String exception;

   	@Column(name = "traceback", columnDefinition = "longtext")
	private String traceback;

}
