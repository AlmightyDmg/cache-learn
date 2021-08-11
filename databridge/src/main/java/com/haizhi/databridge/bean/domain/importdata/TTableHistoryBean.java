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
@Table(name = "t_table_history")
public class TTableHistoryBean  extends HaizhiBaseDomainBean {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "int(11) unsigned")
    private Long id;

    @Column(name = "task_id", length = 50, nullable = false)
    private String taskId;

    @Column(name = "owner", length = 70, nullable = false)
    private String owner;

    @Column(name = "tb_name", length = 1024, nullable = false)
    private String tbName;

    @Column(name = "table_id", length = 40, nullable = false)
    private String tableId;

    @Column(name = "size", nullable = false, columnDefinition = "int(11)")
    private Integer size;

    @Column(name = "count", nullable = false, columnDefinition = "int(11)")
    private Integer count;

    @Column(name = "elapse", nullable = false, columnDefinition = "int(11)")
    private Integer elapse;

    @Column(name = "start_at")
    private Timestamp startAt;

    @Column(name = "status", length = 16)
    private String status;

    @Column(name = "exception", columnDefinition = "longtext")
    private String exception;

    @Column(name = "traceback", columnDefinition = "longtext")
    private String traceback;

    @Column(name = "extra", columnDefinition = "longtext")
    private String extra;

    @Column(name = "toucher", length = 16, nullable = false)
    private String toucher;

    @Column(name = "toucher_type", nullable = false, columnDefinition = "int(1)")
    private Integer toucherType;
}
