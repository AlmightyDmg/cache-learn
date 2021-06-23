package com.haizhi.dataio.job.reader;

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
public class JdbcReader {
    String username;
    String password;

    String where;
    int fetchSize;

    List<MetaColumn> column;
    List<Connection> connection;

}
