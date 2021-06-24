package com.haizhi.dataio.job.writer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataio.job.column.Connection;
import com.haizhi.dataio.job.column.MetaColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JdbcWriter {
    String username;
    String password;

    List<MetaColumn> column;
    List<Connection> connection;

    String writeMode;
    String insertSqlMode;
    List<String> preSql;
}
