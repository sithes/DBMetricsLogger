package com.sre.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.sre.DBMetricsLoggerDataSource;
import com.sre.util.AdvancedEncryptionStandard;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
 
	private final  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${db.config.path}")
	private String dbConfigPath;
	
	@Autowired
	AdvancedEncryptionStandard advancedEncryptionStandard;
	
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource defaultDataSource() {
    	logger.debug("DataSourceConfig dataSource returing defaultDataSource");
        return DataSourceBuilder.create().build();
    }
 
    @Bean
    @Primary
    public DataSource routeDataSource() {
    	logger.debug("DataSourceConfig dataSource returing routeDataSource");
    	Map<Object,Object> targetDataSources = new HashMap<>();
        File[] files = Paths.get(dbConfigPath).toFile().listFiles();//

        /*try {
			logger.debug("DataSourceConfig dataSource advancedEncryptionStandard {}",advancedEncryptionStandard.encrypt("Sithes"));
		} catch (Exception e) {
			logger.error("DataSourceConfig routeDataSource IOException {}",e);
            return null;
		}*/
        
        for(File propertyFile : files) {
            Properties tenantProperties = new Properties();       

            try {
                tenantProperties.load(new FileInputStream(propertyFile));    
                
                HikariDataSource     hikariDataSource = new HikariDataSource();
                
                hikariDataSource.setPoolName(tenantProperties.getProperty("name"));
                hikariDataSource.setMaximumPoolSize(Integer.valueOf(tenantProperties.getProperty("datasource.maximumPoolSize")));
                hikariDataSource.setMinimumIdle(Integer.valueOf(tenantProperties.getProperty("datasource.minimumIdle")));
                hikariDataSource.setMaxLifetime(Long.valueOf(tenantProperties.getProperty("datasource.maxLifetime")));
                hikariDataSource.setConnectionTimeout(Long.valueOf(tenantProperties.getProperty("datasource.connectionTimeout")));
                hikariDataSource.setIdleTimeout(Long.valueOf(tenantProperties.getProperty("datasource.idleTimeout")));
                
                hikariDataSource.addDataSourceProperty("type", tenantProperties.getProperty("datasource.type"));
                hikariDataSource.setDriverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
                hikariDataSource.setJdbcUrl(tenantProperties.getProperty("datasource.jdbcUrl"));
                hikariDataSource.setUsername(tenantProperties.getProperty("datasource.username"));
                hikariDataSource.setPassword(advancedEncryptionStandard.decrypt(tenantProperties.getProperty("datasource.password")));
                
                targetDataSources.put(tenantProperties.getProperty("name"), hikariDataSource);
                
            } catch (Exception e) {                
            	logger.error("DataSourceConfig routeDataSource IOException {}",e);
                return null;
            }
        }//for
        
        DBMetricsLoggerDataSource routeDataSource = new DBMetricsLoggerDataSource();
        routeDataSource.setDefaultTargetDataSource(defaultDataSource());
       
        targetDataSources.put("tenants", defaultDataSource()); 
        routeDataSource.setTargetDataSources(targetDataSources); 
        
        logger.debug("DataSourceConfig routeDataSource initialized successfully.");
        
        return routeDataSource;
        }
    }
