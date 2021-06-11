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
 * 虚拟数据库
 *
 * @author tools
 * @date 2020-02-12 14:15:12
 */
@ApiModel(description = "虚拟数据库")

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
@Table(name = "EXPORT_TB_RULE")
public class ExportTbRuleBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;
	@Column(name = "rule_id", nullable = false, length = 35, columnDefinition = "char(35)")
	private String ruleId;
	@NotBlank
	@Column(name = "operator", nullable = false, length = 32, columnDefinition = "varchar(32)")
	private String owner;
	@Column(name = "rule_name", nullable = true, length = 128, columnDefinition = "varchar(128)")
	private String ruleName;
	@Column(name = "cond", nullable = true, length = 128, columnDefinition = "varchar(128)")
	private String cond;
	@ApiModelProperty(value = "1:内置|2:自定义")
	@Column(name = "type", nullable = true, columnDefinition = "int(11)")
	private Integer type;
	@Column(name = "ent_id", nullable = true, length = 128, columnDefinition = "varchar(128)")
	private String entId;
}
