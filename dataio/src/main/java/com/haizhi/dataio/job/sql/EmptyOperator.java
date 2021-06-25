package com.haizhi.dataio.job.sql;

public class EmptyOperator extends SqlOperator {
    @Override
    public String generate() {
        return " (1=1) ";
    }
}
