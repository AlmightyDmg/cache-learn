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
 * 虚拟数据表
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
@Table(name = "EXPORT_DS_TB")
public class ExportDsTbBean extends HaizhiStandardDomainBean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
	private Long id;
	@NotBlank
	@ApiModelProperty(value = "tb_3444441234 ,虚拟表的id")
	@Column(name = "xtb_id", nullable = false, length = 40, columnDefinition = "char(40)")
	private String xtbId;
	@ApiModelProperty(value = "所属数据源")
	@Column(name = "ds_id", nullable = true, length = 40, columnDefinition = "char(40)")
	private String dsId;
	@NotBlank
	@ApiModelProperty(value = "表的所有者")
	@Column(name = "owner", nullable = false, length = 127, columnDefinition = "char(127)")
	private String owner;
	@ApiModelProperty(value = "表所属的企业域id")
	@Column(name = "ent_id", nullable = true, length = 128, columnDefinition = "char(128)")
	private String entId;
	@NotBlank
	@ApiModelProperty(value = "表名，不可修改")
	@Column(name = "name", nullable = false, length = 1024, columnDefinition = "varchar(1024)")
	private String name;
	@ApiModelProperty(value = "表的别名，可以修改")
	@Column(name = "title", nullable = true, length = 1024, columnDefinition = "varchar(1024)")
	private String title;
//	@Column(name = "comment", nullable = true, length = 255, columnDefinition = "varchar(255)")
//	private String comment;
}
