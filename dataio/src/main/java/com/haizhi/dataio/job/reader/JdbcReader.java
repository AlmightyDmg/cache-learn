package com.haizhi.dataio.job.reader;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataio.job.column.MetaColumn;
import com.haizhi.dataio.job.column.ReaderConnection;

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
    List<ReaderConnection> connection;

}
