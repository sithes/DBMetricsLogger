package com.sre.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.model.Log;
import com.wrapper.LogMISResponse;

@Service
public class DBMetricsLoggerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DataSourceConfig dataSourceConfig;
	
	@Value("${log.display.count}")
	private int displayRowCount;
	
	private int page;
	private int total;
	private int records;
	
	// DBMetricsLoggerService Related functions
	
	public int execute(String query) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService execute with query={}", query);
		Connection con = null;
		Statement st = null;
		int results = 0;
		try {
			con = dataSourceConfig.routeDataSource().getConnection();	
			st = con.createStatement();
			results = st.executeUpdate(query);
		} catch (SQLException ex) {
			logger.error("DBMetricsLoggerService execute SQLException", ex);
		} finally {
			logger.debug("DBMetricsLoggerService execute calling resource cleanup");
			if (st != null)
				st.close();
			if (con != null)
				con.close();
			logger.debug("DBMetricsLoggerService execute resource cleanup done");
		}
		logger.debug("Leaving DBMetricsLoggerService execute return with {}", results);
		return results;
	}
	
	public Map<String, String> getRegion() throws SQLException {
		logger.debug("Entering DBMetricsLoggerService getRegion");
		Connection getCon = null;
		PreparedStatement getPS = null;
		ResultSet getRS = null;
		
		Map<String, String> regionList = new HashMap<String, String>();
		
		try {
			getCon = dataSourceConfig.routeDataSource().getConnection();
			getPS = getCon.prepareStatement("select distinct region FROM dbinfo where isActive='Yes'");
			
			getRS = getPS.executeQuery();
			
			while (getRS.next()) {
				regionList.put(getRS.getString(1), getRS.getString(1));
			}
		} catch (SQLException e) {
			logger.error("DBMetricsLoggerService getRegion SQLException", e);
		}
		finally {
			logger.debug("DBMetricsLoggerService getRegion calling resource cleanup");
			//Release connection, RS and PS
			if (getRS != null)
				getRS.close();
			if (getPS != null)
				getPS.close();
			if (getCon != null)
				getCon.close();
			logger.debug("DBMetricsLoggerService getRegion resource cleanup done");
		}
		
		logger.debug("Leaving DBMetricsLoggerService getRegion Region List {}", regionList.toString());
		return regionList;
	}
	
	public Map<String, String> getCountry(String region) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService getCountry with region={}",region);
		Connection getCon = null;
		PreparedStatement getPS = null;
		ResultSet getRS = null;
		
		Map<String, String> countryList = new HashMap<String, String>();
		
		try {
			getCon = dataSourceConfig.routeDataSource().getConnection();
			getPS = getCon.prepareStatement("select distinct country FROM dbinfo where region=? and isActive='Yes'");
			getPS.setString(1, region);
			
			getRS = getPS.executeQuery();
			
			while (getRS.next()) {
				countryList.put(getRS.getString(1), getRS.getString(1));
			}
		} catch (SQLException e) {
			logger.error("DBMetricsLoggerService getCountry SQLException", e);
		}
		finally {
			logger.debug("DBMetricsLoggerService getCountry calling resource cleanup");
			//Release connection, RS and PS
			if (getRS != null)
				getRS.close();
			if (getPS != null)
				getPS.close();
			if (getCon != null)
				getCon.close();
			logger.debug("DBMetricsLoggerService getCountry resource cleanup done");
		}
		
		logger.debug("Leaving DBMetricsLoggerService getCountry Country List {}", countryList.toString());
		return countryList;
	}
	
	public Map<String, String> getEnv(String region, String country) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService getEnv with region={} and country={}",region, country);
		Connection getCon = null;
		PreparedStatement getPS = null;
		ResultSet getRS = null;
		
		Map<String, String> envList = new HashMap<String, String>();
		
		try {
			getCon = dataSourceConfig.routeDataSource().getConnection();
			getPS = getCon.prepareStatement("select distinct env FROM dbinfo where region=? and country=? and isActive='Yes'");
			getPS.setString(1, region);
			getPS.setString(2, country);
			getRS = getPS.executeQuery();
			
			while (getRS.next()) {
				envList.put(getRS.getString(1), getRS.getString(1));
			}
		} catch (SQLException e) {
			logger.error("DBMetricsLoggerService getEnv SQLException", e);
		}	
		finally {
			logger.debug("DBMetricsLoggerService getEnv calling resource cleanup");
			//Release connection, RS and PS
			if (getRS != null)
				getRS.close();
			if (getPS != null)
				getPS.close();
			if (getCon != null)
				getCon.close();
			logger.debug("DBMetricsLoggerService getEnv resource cleanup done");
		}
		logger.debug("Leaving DBMetricsLoggerService getEnv Env List {}", envList.toString());
		return envList;
	}
	
	public List<Log> getLogList(Map<String, String> filter, int rows, int page, String sidx, String sord, String loadAll) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService getLogList with filter={} rows={} page={} sidx={} sord={} loadAll={}",filter, rows, page, sidx, sord, loadAll);
		List<Log> logList = new ArrayList<Log>();
		boolean flag = true;
		int count=0,total_pages=0, start=0, limit=displayRowCount;
		Connection getLogCon = null;
		PreparedStatement getLogPS = null;
		ResultSet getLogRS = null;
		try {
			getLogCon = dataSourceConfig.routeDataSource().getConnection();	
			
			//count
			if (filter.get("today").equals("Today")) {//SGMBOLGM.GMP_MONT_LOGS
				getLogPS = getLogCon.prepareStatement("select COUNT(*) as count FROM "+filter.get("schema")+".GMP_MONT_LOGS where APP_SERVER_DATETIME >= trunc(sysdate) And APP_SERVER_DATETIME < trunc(sysdate) + 1 "+filter.get("addFilter"));
			}
			else {
				getLogPS = getLogCon.prepareStatement("select COUNT(*) as count FROM "+filter.get("schema")+".GMP_MONT_LOGS where APP_SERVER_DATETIME BETWEEN TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') AND TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') "+filter.get("addFilter"));
				getLogPS.setString(1, filter.get("from"));
				getLogPS.setString(2, filter.get("to"));
			}
			
			getLogRS = getLogPS.executeQuery();	
			logger.debug("select COUNT(*) as count FROM {}.GMP_MONT_LOGS where APP_SERVER_DATETIME BETWEEN TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') AND TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') {}",filter.get("schema"), filter.get("from"), filter.get("to"),filter.get("addFilter")); 
			if (getLogRS.next()) {
				count = getLogRS.getInt("count");
				logger.debug("Count of {} records",count); 
			}
			if( count > 0 ) {
				total_pages = (count/limit);
			} else {
				total_pages = 0;
			}
			getLogRS.close();
			getLogPS.close();
			getLogCon.close(); 
			if (page > total_pages) page = total_pages;
			
			start = limit*page - limit; // do not put limit*(page - 1)
			if(start < 0) start = 0;
			
			logger.debug("Total pages {} ",page);
			
			getLogCon = dataSourceConfig.routeDataSource().getConnection();
			//if("".equals(loadAll) || loadAll == null || !"N".equals(loadAll)) {
			if(filter.get("today").equals("Today")) {
				
				if("N".equals(loadAll)) {
					getLogPS = getLogCon.prepareStatement("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from "+filter.get("schema")+".GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME >= trunc(sysdate) And APP_SERVER_DATETIME < trunc(sysdate) + 1 and ROWNUM <=? "+filter.get("addFilter")+" ) f  where r >= ?");
					if (start == 0) {
						getLogPS.setInt(1, limit);
						logger.debug(" start if start={} limit={}", start,limit);
					}
					else {					
						limit = start +limit;
						getLogPS.setInt(1, limit);
						logger.debug(" start else start={} limit={}", start,limit);
					}
					getLogPS.setInt(2, start);
					logger.debug("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA {}.GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME >= trunc(sysdate) And APP_SERVER_DATETIME < trunc(sysdate) + 1 and ROWNUM <={} "+filter.get("addFilter")+" ) f  where r >={}",filter.get("schema"),limit,start);
				}
				else{
					getLogPS = getLogCon.prepareStatement("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from "+filter.get("schema")+".GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME >= trunc(sysdate) And APP_SERVER_DATETIME < trunc(sysdate) + 1 "+filter.get("addFilter")+" ) f ");
					logger.debug("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA {}.GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME >= trunc(sysdate) And APP_SERVER_DATETIME < trunc(sysdate) + 1 "+filter.get("addFilter")+" ) f ",filter.get("schema"));
				}
			}
			else {
				if("N".equals(loadAll)) {
					getLogPS = getLogCon.prepareStatement("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from "+filter.get("schema")+".GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME BETWEEN TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') AND TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') and ROWNUM <=? "+filter.get("addFilter")+" ) f  where r >= ?");
					
					getLogPS.setString(1, filter.get("from"));
					getLogPS.setString(2, filter.get("to"));
					if (start == 0) {
						getLogPS.setInt(3, limit);
						logger.debug(" start if start={} limit={}", start,limit);
					}
					else {					
						limit = start +limit;
						getLogPS.setInt(3, limit);
						logger.debug(" start else start={} limit={}", start,limit);
					}
					getLogPS.setInt(4, start);
					logger.debug("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from {}.GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME BETWEEN TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') AND TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') and ROWNUM <={} {}) f  where r >= {}",filter.get("schema"),filter.get("from"), filter.get("to"), limit, filter.get("addFilter"), start);
				}
				else {
					getLogPS = getLogCon.prepareStatement("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from "+filter.get("schema")+".GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME BETWEEN TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') AND TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') "+filter.get("addFilter")+" ) f ");
					
					getLogPS.setString(1, filter.get("from"));
					getLogPS.setString(2, filter.get("to"));
					
					logger.debug("select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA, ROWNUM as r from (select CLIENT_IPADDRESS, APP_SERVER_DATETIME, USER_NAME,DEVICE_TYPE, REQ_URI, FUNC_TYPE, FUNC_SUB_TYPE, API1_FUNCCODE, STATUS_CODE, STATUS_MSG, APP_SERVER_INSTANCE_ID, GM_JFP_SESSION_ID, JFX_INSTANCE_ID, JFX_SESSION_ID, GM_TTIMEMS, API_TTIMEMS, ESB_TTIMEMS, DEVICE_SID, DEVICE_ID, CUSTOMER_ROLE, REQ_LOG_DATA, RES_LOG_DATA from {}.GMP_MONT_LOGS ORDER BY APP_SERVER_DATETIME desc) t where APP_SERVER_DATETIME BETWEEN TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') AND TO_DATE('{}','yyyy/mm/dd hh24:mi:ss') {}) f ",filter.get("schema"),filter.get("from"), filter.get("to"), filter.get("addFilter"));
				}
			}
			getLogRS = getLogPS.executeQuery();
			
			while (getLogRS.next()) {
				flag = false;
				Log epp = new Log(getLogRS.getString("APP_SERVER_DATETIME"),getLogRS.getString("USER_NAME"),getLogRS.getString("DEVICE_TYPE"),getLogRS.getString("REQ_URI"),getLogRS.getString("FUNC_TYPE"),getLogRS.getString("FUNC_SUB_TYPE"),getLogRS.getString("API1_FUNCCODE"),getLogRS.getString("STATUS_CODE"),getLogRS.getString("STATUS_MSG"),getLogRS.getString("APP_SERVER_INSTANCE_ID"),getLogRS.getString("GM_JFP_SESSION_ID"),getLogRS.getString("JFX_INSTANCE_ID"),getLogRS.getString("JFX_SESSION_ID"),getLogRS.getString("GM_TTIMEMS"),getLogRS.getString("API_TTIMEMS"),getLogRS.getString("ESB_TTIMEMS"),getLogRS.getString("DEVICE_SID"),getLogRS.getString("DEVICE_ID"),getLogRS.getString("CUSTOMER_ROLE"),getLogRS.getString("REQ_LOG_DATA"),getLogRS.getString("RES_LOG_DATA"));
				logList.add(epp);
				//logger.debug("USER_NAME's [{}] CLIENT_IPADDRESS is =[{}]",getLogRS.getString("USER_NAME"), getLogRS.getString("CLIENT_IPADDRESS"));
			}
			if (flag) {
				logger.debug("Invalid input parameters");
			}
			else{
				this.page = page;
				this.total = total_pages;
				this.records = count;
				logger.debug("Pagination data has been added");
			}
		} catch (SQLException ex) {
			logger.error("DBMetricsLoggerService getLogList SQLException", ex);
		} finally {
			logger.debug("DBMetricsLoggerService getLogList calling resource cleanup");
			//Release connection, RS and PS
			if (getLogRS != null)
				getLogRS.close();
			if (getLogPS != null)
				getLogPS.close();
			if (getLogCon != null)
				getLogCon.close();
			logger.debug("DBMetricsLoggerService getLogList resource cleanup done");
		}
		logger.debug("Leaving DBMetricsLoggerService getLogList return with [{}]", logList.toString());
		return logList;
	}
	
	public List<LogMISResponse> getEPPMISData(String fromDate, String toDate, String ip) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService getEPPMISData with fromDate={} toDate={} ip={}",fromDate, toDate, ip);
		List<LogMISResponse> misRes = new ArrayList<LogMISResponse>();
		Connection getMISListCon = null;
		PreparedStatement getMISListPS = null;
		ResultSet getMISListRS = null;
		try {
			getMISListCon = dataSourceConfig.routeDataSource().getConnection();

			if (fromDate != null && toDate == null && ip == null) { /* Fetch Today data */
				getMISListPS = getMISListCon.prepareStatement("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, hit FROM hitcount where DATE_FORMAT(updatedOn,'%Y-%m-%d')=?");
				getMISListPS.setString(1, fromDate);
				logger.debug("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, hit FROM hitcount where DATE_FORMAT(updatedOn,'%Y-%m-%d')='{}'",fromDate);
			} 
			else if (fromDate != null && toDate == null && ip != null) { /* Fetch from and ip based filter data */
				getMISListPS = getMISListCon.prepareStatement("select updatedOn, ip, hit FROM hitcount where DATE_FORMAT(updatedOn,'%Y-%m-%d') =? and ip=?");
				getMISListPS.setString(1, fromDate);
				getMISListPS.setString(2, ip);
				
				logger.debug("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, hit FROM hitcount where DATE_FORMAT(updatedOn,'%Y-%m-%d') ='{}' and ip='{}')",fromDate,ip);
			} 
			else if (fromDate != null && toDate != null && ip == null) { /* Fetch from and to date based filter data */
				getMISListPS = getMISListCon.prepareStatement("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, sum(hit) as hit FROM hitcount where (DATE_FORMAT(updatedOn,'%Y-%m-%d') between ? and ?) group by ip, updatedOn");
				getMISListPS.setString(1, fromDate);
				getMISListPS.setString(2, toDate);
				
				logger.debug("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, sum(hit) as hit FROM hitcount where (DATE_FORMAT(updatedOn,'%Y-%m-%d') between '{}' and '{}') group by ip, updatedOn",fromDate,toDate);
			} 
			else if (fromDate != null && toDate != null && ip != null) { /* Fetch from, to and ip based filter data */
				getMISListPS = getMISListCon.prepareStatement("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, sum(hit) as hit FROM hitcount where (DATE_FORMAT(updatedOn,'%Y-%m-%d') between ? and ?) and ip=? group by ip, updatedOn");
				getMISListPS.setString(1, fromDate);
				getMISListPS.setString(2, toDate);
				getMISListPS.setString(3, ip);
				
				logger.debug("select DATE_FORMAT(updatedOn,'%d-%m-%Y') as updatedOn, ip, sum(hit) as hit FROM hitcount where (DATE_FORMAT(updatedOn,'%Y-%m-%d') between '{}' and '{}') and ip='{}' group by ip, updatedOn",fromDate,toDate,ip);
			} 
			
			getMISListRS = getMISListPS.executeQuery();
			while (getMISListRS.next()) {
				misRes.add(new LogMISResponse(getMISListRS.getString("ip"),getMISListRS.getString("hit"),getMISListRS.getString("updatedOn")));
			}
		} catch (SQLException ex) {
			logger.error("DBMetricsLoggerService getEPPMISData SQLException", ex);
		} finally {
			logger.debug("DBMetricsLoggerService getEPPMISData calling resource cleanup");
			//Release connection, RS and PS
			if (getMISListRS != null)
				getMISListRS.close();
			if (getMISListPS != null)
				getMISListPS.close();
			if (getMISListCon != null)
				getMISListCon.close();
			logger.debug("DBMetricsLoggerService getEPPMISData resource cleanup done");
		}
		logger.debug("Leaving DBMetricsLoggerService getEPPMISData return with [{}]", misRes.toString());
		return misRes;
	}
	
	public int hasEPPHitCount(String ip, String curDate) throws SQLException {
		logger.debug("Entering DBMetricsLoggerService hasEPPHitCount with ip={} curDate={}", ip, curDate);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			con = dataSourceConfig.routeDataSource().getConnection();	
			ps = con.prepareStatement("select id FROM hitcount where ip='"+ip+"' and DATE_FORMAT(updatedOn,'%Y-%m-%d')='"+curDate+"'");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt("id");
			}
		} catch (SQLException ex) {
			logger.error("DBMetricsLoggerService hasEPPHitCount SQLException", ex);
		} finally {
			logger.debug("DBMetricsLoggerService hasEPPHitCount calling resource cleanup");
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
			logger.debug("DBMetricsLoggerService hasEPPHitCount resource cleanup done");
		}
		logger.debug("Leaving DBMetricsLoggerService hasEPPHitCount return with {}", result);
		return result;
	}
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the records
	 */
	public int getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(int records) {
		this.records = records;
	}
	
}
