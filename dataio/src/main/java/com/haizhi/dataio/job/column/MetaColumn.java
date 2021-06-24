package com.haizhi.dataio.job.column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaColumn {
    private String name;
    private String type;
    private String value;
}
