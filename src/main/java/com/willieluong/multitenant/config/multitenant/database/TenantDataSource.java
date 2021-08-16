package com.willieluong.multitenant.config.multitenant.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Let’s write a class to store the database connection details for each tenant.
* We will load the connection details during server startup using @PostConstruct.
 */
public class TenantDataSource implements Serializable {

    private HashMap<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSourceConfigRepository configRepo;

    //function to get the datasource from the tenant
    public DataSource getDataSource(String name){
        if(dataSources.get(name) != null){
            return dataSources.get(name);
        }
        //if the data source is not found, then create it in the database
        DataSource dataSource = createDataSource(name);
        if(dataSource != null){
            dataSources.put(name, dataSource);
        }
        return dataSource;
    }

    //@PostConstruct
    //Method to return all data source from the database
    public Map<String, DataSource> getAll(){
        List<DataSourceConfig> configList = configRepo.findAll();
        Map<String, DataSource> result = new HashMap<>();
        for(DataSourceConfig config : configList){
            DataSource dataSource = getDataSource(config.getName());
            result.put(config.getName(), dataSource);
        }
        return result;
    }

    //Helper method to create a datasource in the table
    private DataSource createDataSource(String name){
        //find the partircular data config by the tenant name
        DataSourceConfig config = configRepo.findByName(name);
        if(config != null){
            //build out the datasource
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();
            if (config.getInitialize()) {
                initialize(ds);
            }
            return ds;
        }

        return null;
    }

    private void initialize(DataSource dataSource) {
        //ClassPathResource schemaResource = new ClassPathResource("schema.sql");
        //ClassPathResource dataResource = new ClassPathResource("data.sql");
        //ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaResource, dataResource);
        //populator.execute(dataSource);
    }


}
