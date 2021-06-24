package com.haizhi.databridge.bean.domain.importdata;

import javax.persistence.AttributeOverride;
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

@ApiModel(description = "同步信息表")

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
@AttributeOverride(name = "ctime", column = @Column(name = "create_at"))
@AttributeOverride(name = "utime", column = @Column(name = "update_at"))
@Table(name = "t_database")
public class TDataBaseSourceBean extends HaizhiStandardDomainBean {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
    private Long id;

    @Column(name = "db_id", length = 40, nullable = false)
    private String dbId;

    @ApiModelProperty(value = "数据源类型")
    @Column(name = "db_type", length = 40, nullable = false)
    private String dbType;

    @NotBlank
    @ApiModelProperty(value = "配置信息")
    @Column(name = "setup", nullable = false, columnDefinition = "longtext")
    private String setup;

    @ApiModelProperty(value = "数据源名称")
    @Column(name = "ds_name", nullable = true)
    private String dsName;
    
//    @ApiModelProperty(value = "创建时间")
//    @Column(name = "create_at", nullable = true)
//    private Timestamp createAt;
//
//    @ApiModelProperty(value = "更新时间")
//    @Column(name = "update_at", nullable = true)
//    private Timestamp updateAt;

    @ApiModelProperty(value = "所有者")
    @Column(name = "owner", length = 40, nullable = true)
    private String owner;

    @ApiModelProperty(value = "options")
    @Column(name = "options", nullable = true, columnDefinition = "longtext")
    private String options;

    @ApiModelProperty(value = "exception")
    @Column(name = "exception", nullable = true, columnDefinition = "longtext")
    private String exception;

    @ApiModelProperty(value = "traceback")
    @Column(name = "traceback", nullable = true, columnDefinition = "longtext")
    private String traceback;

//    @ApiModelProperty(value = "逻辑删除")
//    @Column(name = "deleted", nullable = false, columnDefinition = "int(4)")
//    private Integer deleted;

    @ApiModelProperty(value = "remark")
    @Column(name = "remark", nullable = true, columnDefinition = "longtext")
    private String remark;

    @ApiModelProperty(value = "资源来源， 0：db, 1：streaming， 2：api")
    @Column(name = "source_type", nullable = true, columnDefinition = "int(4)")
    private Integer sourceType;
}
