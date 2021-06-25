package com.haizhi.dataio.job.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareOperator extends SqlOperator {
    public static final String COMPARE_PATTERN = " %s %s %s%s%s ";
    String fieldName;
    String compareOp;
    String fieldType;
    String value;

    @Override
    public String generate() {
        String quota = getQuot(fieldType);
        return String.format(COMPARE_PATTERN, compareOp, fieldName, quota, value, quota);
    }

    public String getQuot(String type) {
        if ("string".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type)) {
            return "'";
        }

        return "";
    }
}
