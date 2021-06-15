package com.haizhi.databridge.bean.domain.importdata;

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
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月21日 20:22:25
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
@Table(name = "t_job_distjob_rel")
public class JobRelBean extends HaizhiStandardDomainBean {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Databridge内部的JobId")
    @NotBlank
    @Column(name = "job_id", nullable = false, length = 50, columnDefinition = "varchar(50)")
    private String jobId;

    @ApiModelProperty(value = "xxl-job的id，格式为{id}_{jobDesc}")
    @NotBlank
    @Column(name = "dist_job_id", nullable = false, length = 50, columnDefinition = "varchar(50)")
    private String distJobId;

    @ApiModelProperty(value = "操作人")
    @NotBlank
    @Column(name = "owner", nullable = false, length = 50, columnDefinition = "varchar(50)")
    private String owner;
}
