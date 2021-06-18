package com.haizhi.databridge.bean.domain.importdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
 * @author duanxiaoyi
 * @Description 同步的表，与同步任务的映射关系
 * @createTime 2021年06月04日 20:46:18
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
@Table(name = "t_trans_task_rel")
public class TblTransTaskRelBean extends HaizhiStandardDomainBean {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "SCHEDULER的ID")
    @NotBlank
    @Column(name = "job_id", nullable = false, length = 40, columnDefinition = "varchar(40)")
    private String jobId;

    @ApiModelProperty(value = "源表ID")
    @NotBlank
    @Column(name = "from_table_id", nullable = false, length = 40, columnDefinition = "varchar(40)")
    private String fromTableId;

    @ApiModelProperty(value = "目的表ID")
    @NotBlank
    @Column(name = "to_table_id", nullable = false, length = 40, columnDefinition = "varchar(40)")
    private String toTableId;

    @ApiModelProperty(value = "实际执行的任务ID")
    @NotNull
    @Column(name = "trans_task_id", nullable = false, length = 40, columnDefinition = "varchar(40)")
    private String transTaskId;

    @ApiModelProperty(value = "操作人")
    @NotBlank
    @Column(name = "owner", nullable = false, length = 50, columnDefinition = "varchar(50)")
    private String owner;
}
