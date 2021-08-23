package com.willieluong.multitenant.config.multitenant.database;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.willieluong.multitenant.constant.MultiTenantConstants.DEFAULT_TENANT_ID;

/**
 *
 *
 * Spring Boot provides the AbstractRoutingDataSource
 * for determining a data source at runtime according to the tenant identifier.
 * We need to override the selectDataSource method.
 *
 * **/
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    @Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;

    private Map<String, DataSource> map = new HashMap<>();

    //Boolean to check if the tenant has been created in our database or not
    boolean init = false;
    //@PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_ID, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIndetifier) {
        if(!init){
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }
        //after init the tenant into our database, we will return its information.
        return map.get(tenantIndetifier) != null ? map.get(tenantIndetifier) : map.get(DEFAULT_TENANT_ID);
    }
}
