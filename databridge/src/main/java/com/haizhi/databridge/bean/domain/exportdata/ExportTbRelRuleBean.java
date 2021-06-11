package com.haizhi.databridge.bean.domain.exportdata;

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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.haizhi.data.jpa.domain.HaizhiStandardDomainBean;

/**
 * 导出表和规则的关联关系
 * 
 * @author tools
 * @date 2020-02-12 14:15:20
 */
@ApiModel(description = "导出数据源表")

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
@Table(name = "EXPORT_TB_REL_RULE")
public class ExportTbRelRuleBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
	private Long id;
	@ApiModelProperty(value = "关联关系id")
	@Column(name = "rel_id", nullable = false, length = 1024, columnDefinition = "varchar(127)")
	private String relaId;
	@NotBlank
	@ApiModelProperty(value = "xtb_3444441234 ,虚拟表的id")
	@Column(name = "xtb_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String xtbId;
	@ApiModelProperty(value = "字段名称")
	@Column(name = "field_name", nullable = true, length = 40, columnDefinition = "char(40)")
	private String fieldName;
	@NotBlank
	@ApiModelProperty(value = "规则id")
	@Column(name = "rule_id", nullable = false, length = 127, columnDefinition = "char(40)")
	private String ruleId;
	@NotBlank
	@ApiModelProperty(value = "操作人")
	@Column(name = "operator", nullable = false, length = 1024, columnDefinition = "varchar(127)")
	private String operator;

}
