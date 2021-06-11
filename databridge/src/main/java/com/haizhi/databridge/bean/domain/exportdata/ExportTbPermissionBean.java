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
 * 虚拟数据表
 * 
 * @author tools
 * @date 2020-02-12 14:15:20
 */
@ApiModel(description = "导出数据源表")

@Data
@DynamicInsert
@DynamicUpdate
@FieldNameConstants
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "EXPORT_TB_PERMISSION")
public class ExportTbPermissionBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;

	@NotBlank
	@ApiModelProperty(value = "基础表id")
	@Column(name = "xtb_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String xtbId;
	@NotBlank
	@ApiModelProperty(value = "数据源id")
	@Column(name = "ds_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String dsId;
	@NotBlank
	@ApiModelProperty(value = "角色id")
	@Column(name = "role_id", nullable = false, length = 128, columnDefinition = "char(128)")
	private String roleId;
	@ApiModelProperty(value = "角色类型，0: 用户 1: 用户组")
	@Column(name = "role_type", nullable = false, columnDefinition = "tinyint(4)")
	private Integer roleType;
	@NotBlank
	@ApiModelProperty(value = "操作人")
	@Column(name = "operator", nullable = false, length = 128, columnDefinition = "char(128)")
	private String operator;
}
