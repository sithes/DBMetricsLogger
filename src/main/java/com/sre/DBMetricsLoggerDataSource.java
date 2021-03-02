package com.sre;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DBMetricsLoggerDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DBMetricsLoggerContext.getCurrentTenant();
    }
}
