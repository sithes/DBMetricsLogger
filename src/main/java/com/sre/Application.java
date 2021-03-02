package com.sre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);		
	}
	  /*HikariDataSource hikariDataSource = null;
	  
		public static void main(String[] args) {
			new Application()
					.configure(new SpringApplicationBuilder(Application.class))
					.properties(getDefaultProperties())
					.run(args);
		}

		@Bean
		public DataSource dataSource() {

			AbstractRoutingDataSource dataSource = new DBMetricsLoggerDataSource();

			File[] files = Paths.get("src/main/resources/tenants").toFile().listFiles();
	        Map<Object,Object> targetDataSources = new HashMap<>();

	        for(File propertyFile : files) {
	            Properties tenantProperties = new Properties();
	            try {
	                tenantProperties.load(new FileInputStream(propertyFile));
	                hikariDataSource = new HikariDataSource();

	                hikariDataSource.setInitializationFailTimeout(0);
	                hikariDataSource.setMaximumPoolSize(1);
	                hikariDataSource.setDataSourceClassName(tenantProperties.getProperty("datasource.driver-class-name"));
	                hikariDataSource.addDataSourceProperty("url", tenantProperties.getProperty("datasource.jdbcUrl"));
	                hikariDataSource.addDataSourceProperty("user", tenantProperties.getProperty("datasource.username"));
	                hikariDataSource.addDataSourceProperty("password", tenantProperties.getProperty("datasource.password"));
	                
	                targetDataSources.put(tenantProperties.getProperty("name"), hikariDataSource);
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
	        }

			dataSource.setTargetDataSources(targetDataSources);

			dataSource.afterPropertiesSet();

			return dataSource;
		}

		private static Properties getDefaultProperties() {
			Properties defaultProperties = new Properties();

			defaultProperties.put("spring.datasource.type", "com.zaxxer.hikari.HikariDataSource");
			defaultProperties.put("spring.datasource.jdbcUrl", "jdbc:mysql://elixir.nam.nsroot.net:3306/tenants?autoReconnect=true&useSSL=false");
			defaultProperties.put("spring.datasource.username", "root");
			defaultProperties.put("spring.datasource.password", "root");
			defaultProperties.put("spring.datasource.driver-class-name", "com.mysql.jdbc.Driver");
			return defaultProperties;
		}*/
		
		 /*File[] files = Paths.get("src/main/resources/tenants").toFile().listFiles();
	        Map<Object,Object> resolvedDataSources = new HashMap<>();

	        for(File propertyFile : files) {
	            Properties tenantProperties = new Properties();
                
                try {
                	tenantProperties.load(new FileInputStream(propertyFile));
                    tenantProperties.load(new FileInputStream(propertyFile));

                    String tenantId = tenantProperties.getProperty("name");

                    System.out.println("TenantID" +tenantId);
                    System.out.println("datasource URL" + tenantProperties.getProperty("datasource.url"));
                            
                } catch (IOException e) {
                    e.printStackTrace();

                }
	        }*/
}
