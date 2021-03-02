package com.sre;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sre.service.DBMetricsLoggerService;
import com.model.Log;
import com.wrapper.LogMISResponse;
import com.wrapper.LogResponse;

@Controller
//@RestController
public class DBMetricsLoggerController {
    
	private final  Logger logger = LoggerFactory.getLogger(this.getClass());
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
    @Autowired
	DBMetricsLoggerService dbMetricsLoggerService;

    @Value("${log.display.count}")
	private int displayRowCount;
    
	@Value("${filter.column}")
	private String filterColumn;
	
    @RequestMapping("/")
	public String index(Model model) {
		initModelList(model);//loading of the lists
		return "index";
	}
	
	@RequestMapping("/log")
	public String otp(Model model) {
		initModelList(model);//loading of the lists
		return "index";
	}
	
	private void initModelList(Model model) {
		logger.debug("Entering DBMetricsLoggerController initModelList"); 
		try {
			DBMetricsLoggerContext.setCurrentTenant("loganalyzer");
			
			Map<String, String> regionList = dbMetricsLoggerService.getRegion();
			Map<String, String> countryList = null;
			Map<String, String> envList = null;
			
			model.addAttribute("region", regionList);
			
			if (!CollectionUtils.isEmpty(regionList))
				countryList = dbMetricsLoggerService.getCountry(regionList.values().toArray()[0].toString());
			model.addAttribute("country", countryList);
			
			if (!CollectionUtils.isEmpty(regionList) && !CollectionUtils.isEmpty(countryList))
				envList = dbMetricsLoggerService.getEnv(regionList.values().toArray()[0].toString(), countryList.values().toArray()[0].toString());
			model.addAttribute("env", envList);
			model.addAttribute("displayRowCount", displayRowCount);
			
			
		} catch (SQLException e) {
			logger.error("DBMetricsLoggerController initModelList Exception", e.getMessage()); 
		}
		logger.debug("Leaving DBMetricsLoggerController initModelList"); 
	}
	
	@RequestMapping(value="/dropListPopulation", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<?> dropListPopulation(@RequestParam(value = "region", required = false) String region,@RequestParam(value = "country", required = false) String country,@RequestParam(value = "environment", required = false) String environment) throws Exception {
		logger.debug("Entering DBMetricsLoggerController dropListPopulation method with region={} country={} environment={}",region,country,environment);
		DBMetricsLoggerContext.setCurrentTenant("loganalyzer");
		Map<String, String> list = null;
		if("".equals(region) && "".equals(country)){
			list = dbMetricsLoggerService.getRegion();
		}
		else if(!"".equals(region) && "".equals(country)){
			list = dbMetricsLoggerService.getCountry(region);
		}
		else if(!"".equals(region) && !"".equals(country) && "".equals(environment)){
			list = dbMetricsLoggerService.getEnv(region,country);
		}
		
		logger.debug("Leaving DBMetricsLoggerController dropListPopulation method with {}",list.toString());
		return new ResponseEntity<Map<String, String>>(list, HttpStatus.OK);
	}
    
	@RequestMapping(value="/logSearch", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<?> logSearch(HttpServletRequest request,@RequestParam(value = "region", required = true) String region, @RequestParam(value = "country", required = true) String country, @RequestParam(value="environment", required=true) String environment, @RequestParam(value = "filter", required = true) String filter, @RequestParam(value = "fromDate", required = true) String fromDate, @RequestParam(value = "toDate", required = true) String toDate, @RequestParam(value = "rows", required = false) int rows, @RequestParam(value = "page", required = false) int page, @RequestParam(value = "sidx", required = false) String sidx, @RequestParam(value = "sord", required = false) String sord, @RequestParam(value = "loadAll", required = false) String loadAll,@RequestParam Map<String, String> params) throws Exception {
		logger.debug("Entering DBMetricsLoggerController logSearch method with region={} country={} environment={} filter={} fromDate={} toDate={} rows={} page={} sidx={} sord={} loadAll={} params={}",region,country,environment,filter,fromDate, toDate, rows, page, sidx, sord, loadAll,params.toString());
		Map<String,String> filterMap = new HashMap<String, String>();
		StringBuffer addFilter = new StringBuffer();
		boolean flag = true;
		//Log Hit count in DB
		hitCounter(request);
				
		//V.Imp context switch
		String contextName = region+"_"+country+"_"+environment;
		DBMetricsLoggerContext.setCurrentTenant(contextName);
				
		List<Log> eppLog = null;
		if("".equals(sidx)) 
			sidx = "1";
		//reset value
		reset(dbMetricsLoggerService);
		
		for (Map.Entry<String, String> paramMap : params.entrySet()) {
		    if(filterColumn.toLowerCase().contains(paramMap.getKey().toLowerCase())) {
		    	logger.debug("filterColumn.toLowerCase().contains(paramMap.getKey().toLowerCase())={}",paramMap.getKey().toLowerCase());
		    	if (flag) {
		    		addFilter.append(paramMap.getKey());
		    		addFilter.append(" LIKE '%");
		    		addFilter.append(paramMap.getValue());
		    		addFilter.append("%'");
					flag=false;
				}
				else {
					addFilter.append(" and ");
					addFilter.append(paramMap.getKey());
					addFilter.append(" LIKE '%");
		    		addFilter.append(paramMap.getValue());
		    		addFilter.append("%'");
				}	
		    }//if
		}//for
		logger.debug("DBMetricsLoggerController logSearch method addFilter {} addFilter.length()={}",addFilter,addFilter.length());
		if(addFilter.length() > 1) {
			filterMap.put("addFilter", " and "+addFilter);
		}
		else 
			filterMap.put("addFilter", "");
		
		filterMap.put("today", filter);
		filterMap.put("schema", country+"GM");
		filterMap.put("from", fromDate);
		filterMap.put("to", toDate);
		
		eppLog = dbMetricsLoggerService.getLogList(filterMap, rows, page, sidx, sord, loadAll);
		logger.debug("Leaving DBMetricsLoggerController logSearch method with {}",eppLog.toString());
		LogResponse eppResponse = new LogResponse(eppLog, dbMetricsLoggerService.getPage(), dbMetricsLoggerService.getTotal(), dbMetricsLoggerService.getRecords());
		
	    return new ResponseEntity<LogResponse>(eppResponse, HttpStatus.OK);
    }
	
	private void reset(DBMetricsLoggerService logAnalyzerService) {
		logAnalyzerService.setTotal(0);
		logAnalyzerService.setRecords(0);
	}
	
	/**************************************** Log/MIS ***********************************************/
	@RequestMapping("log/mis")
	public String eppMis() {
		return "mis/logmis";
	}
	
	@RequestMapping(value="/logMisSearch", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<?> logMisSearch(HttpServletRequest request, @RequestParam(value = "filter", required = false) String filter,@RequestParam(value = "fromDate", required = false) String fromDate,@RequestParam(value = "toDate", required = false) String toDate,@RequestParam(value = "ip", required = false) String ip) throws Exception {
		logger.debug("Entering DBMetricsLoggerController logMisSearch method with filter={} fromDate={} toDate={} IP={}",filter,fromDate,toDate,ip);
		List<LogMISResponse> misRes = null;
		//Log Hit count in DB
		hitCounter(request);
		if("Today".equals(filter) && "".equals(fromDate)  && "".equals(toDate)  && "".equals(ip)) {
			misRes = dbMetricsLoggerService.getEPPMISData(df.format(new Date()), null, null);
		}
		else if("Today".equals(filter) && "".equals(fromDate)  && "".equals(toDate)  && !"".equals(ip)) {
			misRes = dbMetricsLoggerService.getEPPMISData(df.format(new Date()), null, ip);
		}
		else if("Between".equals(filter) && !"".equals(fromDate)  && !"".equals(toDate)  && "".equals(ip)) {
			misRes = dbMetricsLoggerService.getEPPMISData(fromDate, toDate, null);
		}
		else if("Between".equals(filter) && !"".equals(fromDate)  && !"".equals(toDate)  && !"".equals(ip)) {
			misRes = dbMetricsLoggerService.getEPPMISData(fromDate, toDate, ip);
		}
		else {
			//TODO
			//Invalid input has to be handled
		}
		
		logger.debug("Leaving DBMetricsLoggerController logMisSearch method with {}",misRes.toString());
		return new ResponseEntity<List<LogMISResponse>>(misRes, HttpStatus.OK);
	}
	
	private void hitCounter(HttpServletRequest request) throws SQLException {
		logger.debug("Entering DBMetricsLoggerController hitCounter method with {}",request);
        String remoteAddr = "";

        DBMetricsLoggerContext.setCurrentTenant("loganalyzer");
        
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            df.setTimeZone(TimeZone.getTimeZone("Asia/Singapore")); 
            String currDate = df.format(new Date());
            
            int id = dbMetricsLoggerService.hasEPPHitCount(remoteAddr, currDate);
           
            logger.debug("DBMetricsLoggerController hitCounter method - remoteAddr=[{}] CurrentDate=[{}]", remoteAddr, currDate);
            
            if(id > 0) {//update
            	dbMetricsLoggerService.execute("update hitcount set hit=hit+1 where ip='"+remoteAddr+"' and date(updatedOn)='"+currDate+"'");
            }
            else{//insert
            	dbMetricsLoggerService.execute("insert into hitcount (ip, updatedOn, hit) values('"+remoteAddr+"','"+currDate+"',1)");
            }
        }
        logger.debug("Leaving DBMetricsLoggerController hitCounter method");
    }
	/**************************************** Log/MIS ***********************************************/
	
}
