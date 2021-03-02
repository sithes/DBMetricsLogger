package com.model;

public class Log {

	private String APP_SERVER_DATETIME;
	private String USER_NAME;
	private String DEVICE_TYPE;
	private String REQ_URI;
	private String FUNC_TYPE;
	private String FUNC_SUB_TYPE;
	private String API1_FUNCCODE;
	private String STATUS_CODE;
	private String STATUS_MSG;
	private String APP_SERVER_INSTANCE_ID;
	private String GM_JFP_SESSION_ID;
	private String JFX_INSTANCE_ID;
	private String JFX_SESSION_ID;	
	private String GM_TTIMEMS;
	private String API_TTIMEMS;
	private String ESB_TTIMEMS;
	private String DEVICE_SID;
	private String DEVICE_ID;
	private String CUSTOMER_ROLE;
	private String REQ_LOG_DATA;
	private String RES_LOG_DATA;
			
	/**
	 * 
	 */
	public Log() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aPP_SERVER_DATETIME
	 * @param uSER_NAME
	 * @param dEVICE_TYPE
	 * @param rEQ_URI
	 * @param fUNC_TYPE
	 * @param fUNC_SUB_TYPE
	 * @param aPI1_FUNCCODE
	 * @param sTATUS_CODE
	 * @param sTATUS_MSG
	 * @param aPP_SERVER_INSTANCE_ID
	 * @param gM_JFP_SESSION_ID
	 * @param jFX_INSTANCE_ID
	 * @param jFX_SESSION_ID
	 * @param gM_TTIMEMS
	 * @param aPI_TTIMEMS
	 * @param eSB_TTIMEMS
	 * @param dEVICE_SID
	 * @param dEVICE_ID
	 * @param cUSTOMER_ROLE
	 * @param rEQ_LOG_DATA
	 * @param rES_LOG_DATA
	 */
	public Log(String aPP_SERVER_DATETIME, String uSER_NAME, String dEVICE_TYPE, String rEQ_URI, String fUNC_TYPE,
			String fUNC_SUB_TYPE, String aPI1_FUNCCODE, String sTATUS_CODE, String sTATUS_MSG,
			String aPP_SERVER_INSTANCE_ID, String gM_JFP_SESSION_ID, String jFX_INSTANCE_ID, String jFX_SESSION_ID,
			String gM_TTIMEMS, String aPI_TTIMEMS, String eSB_TTIMEMS, String dEVICE_SID, String dEVICE_ID,
			String cUSTOMER_ROLE, String rEQ_LOG_DATA, String rES_LOG_DATA) {
		APP_SERVER_DATETIME = aPP_SERVER_DATETIME;
		USER_NAME = uSER_NAME;
		DEVICE_TYPE = dEVICE_TYPE;
		REQ_URI = rEQ_URI;
		FUNC_TYPE = fUNC_TYPE;
		FUNC_SUB_TYPE = fUNC_SUB_TYPE;
		API1_FUNCCODE = aPI1_FUNCCODE;
		STATUS_CODE = sTATUS_CODE;
		STATUS_MSG = sTATUS_MSG;
		APP_SERVER_INSTANCE_ID = aPP_SERVER_INSTANCE_ID;
		GM_JFP_SESSION_ID = gM_JFP_SESSION_ID;
		JFX_INSTANCE_ID = jFX_INSTANCE_ID;
		JFX_SESSION_ID = jFX_SESSION_ID;
		GM_TTIMEMS = gM_TTIMEMS;
		API_TTIMEMS = aPI_TTIMEMS;
		ESB_TTIMEMS = eSB_TTIMEMS;
		DEVICE_SID = dEVICE_SID;
		DEVICE_ID = dEVICE_ID;
		CUSTOMER_ROLE = cUSTOMER_ROLE;
		REQ_LOG_DATA = rEQ_LOG_DATA;
		RES_LOG_DATA = rES_LOG_DATA;
	}

	/**
	 * @return the aPP_SERVER_DATETIME
	 */
	public String getAPP_SERVER_DATETIME() {
		return APP_SERVER_DATETIME;
	}

	/**
	 * @param aPP_SERVER_DATETIME the aPP_SERVER_DATETIME to set
	 */
	public void setAPP_SERVER_DATETIME(String aPP_SERVER_DATETIME) {
		APP_SERVER_DATETIME = aPP_SERVER_DATETIME;
	}

	/**
	 * @return the uSER_NAME
	 */
	public String getUSER_NAME() {
		return USER_NAME;
	}

	/**
	 * @param uSER_NAME the uSER_NAME to set
	 */
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	/**
	 * @return the dEVICE_TYPE
	 */
	public String getDEVICE_TYPE() {
		return DEVICE_TYPE;
	}

	/**
	 * @param dEVICE_TYPE the dEVICE_TYPE to set
	 */
	public void setDEVICE_TYPE(String dEVICE_TYPE) {
		DEVICE_TYPE = dEVICE_TYPE;
	}

	/**
	 * @return the rEQ_URI
	 */
	public String getREQ_URI() {
		return REQ_URI;
	}

	/**
	 * @param rEQ_URI the rEQ_URI to set
	 */
	public void setREQ_URI(String rEQ_URI) {
		REQ_URI = rEQ_URI;
	}

	/**
	 * @return the fUNC_TYPE
	 */
	public String getFUNC_TYPE() {
		return FUNC_TYPE;
	}

	/**
	 * @param fUNC_TYPE the fUNC_TYPE to set
	 */
	public void setFUNC_TYPE(String fUNC_TYPE) {
		FUNC_TYPE = fUNC_TYPE;
	}

	/**
	 * @return the fUNC_SUB_TYPE
	 */
	public String getFUNC_SUB_TYPE() {
		return FUNC_SUB_TYPE;
	}

	/**
	 * @param fUNC_SUB_TYPE the fUNC_SUB_TYPE to set
	 */
	public void setFUNC_SUB_TYPE(String fUNC_SUB_TYPE) {
		FUNC_SUB_TYPE = fUNC_SUB_TYPE;
	}

	/**
	 * @return the aPI1_FUNCCODE
	 */
	public String getAPI1_FUNCCODE() {
		return API1_FUNCCODE;
	}

	/**
	 * @param aPI1_FUNCCODE the aPI1_FUNCCODE to set
	 */
	public void setAPI1_FUNCCODE(String aPI1_FUNCCODE) {
		API1_FUNCCODE = aPI1_FUNCCODE;
	}

	/**
	 * @return the sTATUS_CODE
	 */
	public String getSTATUS_CODE() {
		return STATUS_CODE;
	}

	/**
	 * @param sTATUS_CODE the sTATUS_CODE to set
	 */
	public void setSTATUS_CODE(String sTATUS_CODE) {
		STATUS_CODE = sTATUS_CODE;
	}

	/**
	 * @return the sTATUS_MSG
	 */
	public String getSTATUS_MSG() {
		return STATUS_MSG;
	}

	/**
	 * @param sTATUS_MSG the sTATUS_MSG to set
	 */
	public void setSTATUS_MSG(String sTATUS_MSG) {
		STATUS_MSG = sTATUS_MSG;
	}

	/**
	 * @return the aPP_SERVER_INSTANCE_ID
	 */
	public String getAPP_SERVER_INSTANCE_ID() {
		return APP_SERVER_INSTANCE_ID;
	}

	/**
	 * @param aPP_SERVER_INSTANCE_ID the aPP_SERVER_INSTANCE_ID to set
	 */
	public void setAPP_SERVER_INSTANCE_ID(String aPP_SERVER_INSTANCE_ID) {
		APP_SERVER_INSTANCE_ID = aPP_SERVER_INSTANCE_ID;
	}

	/**
	 * @return the gM_JFP_SESSION_ID
	 */
	public String getGM_JFP_SESSION_ID() {
		return GM_JFP_SESSION_ID;
	}

	/**
	 * @param gM_JFP_SESSION_ID the gM_JFP_SESSION_ID to set
	 */
	public void setGM_JFP_SESSION_ID(String gM_JFP_SESSION_ID) {
		GM_JFP_SESSION_ID = gM_JFP_SESSION_ID;
	}

	/**
	 * @return the jFX_INSTANCE_ID
	 */
	public String getJFX_INSTANCE_ID() {
		return JFX_INSTANCE_ID;
	}

	/**
	 * @param jFX_INSTANCE_ID the jFX_INSTANCE_ID to set
	 */
	public void setJFX_INSTANCE_ID(String jFX_INSTANCE_ID) {
		JFX_INSTANCE_ID = jFX_INSTANCE_ID;
	}

	/**
	 * @return the jFX_SESSION_ID
	 */
	public String getJFX_SESSION_ID() {
		return JFX_SESSION_ID;
	}

	/**
	 * @param jFX_SESSION_ID the jFX_SESSION_ID to set
	 */
	public void setJFX_SESSION_ID(String jFX_SESSION_ID) {
		JFX_SESSION_ID = jFX_SESSION_ID;
	}

	/**
	 * @return the gM_TTIMEMS
	 */
	public String getGM_TTIMEMS() {
		return GM_TTIMEMS;
	}

	/**
	 * @param gM_TTIMEMS the gM_TTIMEMS to set
	 */
	public void setGM_TTIMEMS(String gM_TTIMEMS) {
		GM_TTIMEMS = gM_TTIMEMS;
	}

	/**
	 * @return the aPI_TTIMEMS
	 */
	public String getAPI_TTIMEMS() {
		return API_TTIMEMS;
	}

	/**
	 * @param aPI_TTIMEMS the aPI_TTIMEMS to set
	 */
	public void setAPI_TTIMEMS(String aPI_TTIMEMS) {
		API_TTIMEMS = aPI_TTIMEMS;
	}

	/**
	 * @return the eSB_TTIMEMS
	 */
	public String getESB_TTIMEMS() {
		return ESB_TTIMEMS;
	}

	/**
	 * @param eSB_TTIMEMS the eSB_TTIMEMS to set
	 */
	public void setESB_TTIMEMS(String eSB_TTIMEMS) {
		ESB_TTIMEMS = eSB_TTIMEMS;
	}

	/**
	 * @return the dEVICE_SID
	 */
	public String getDEVICE_SID() {
		return DEVICE_SID;
	}

	/**
	 * @param dEVICE_SID the dEVICE_SID to set
	 */
	public void setDEVICE_SID(String dEVICE_SID) {
		DEVICE_SID = dEVICE_SID;
	}

	/**
	 * @return the dEVICE_ID
	 */
	public String getDEVICE_ID() {
		return DEVICE_ID;
	}

	/**
	 * @param dEVICE_ID the dEVICE_ID to set
	 */
	public void setDEVICE_ID(String dEVICE_ID) {
		DEVICE_ID = dEVICE_ID;
	}

	/**
	 * @return the cUSTOMER_ROLE
	 */
	public String getCUSTOMER_ROLE() {
		return CUSTOMER_ROLE;
	}

	/**
	 * @param cUSTOMER_ROLE the cUSTOMER_ROLE to set
	 */
	public void setCUSTOMER_ROLE(String cUSTOMER_ROLE) {
		CUSTOMER_ROLE = cUSTOMER_ROLE;
	}

	/**
	 * @return the rEQ_LOG_DATA
	 */
	public String getREQ_LOG_DATA() {
		return REQ_LOG_DATA;
	}

	/**
	 * @param rEQ_LOG_DATA the rEQ_LOG_DATA to set
	 */
	public void setREQ_LOG_DATA(String rEQ_LOG_DATA) {
		REQ_LOG_DATA = rEQ_LOG_DATA;
	}

	/**
	 * @return the rES_LOG_DATA
	 */
	public String getRES_LOG_DATA() {
		return RES_LOG_DATA;
	}

	/**
	 * @param rES_LOG_DATA the rES_LOG_DATA to set
	 */
	public void setRES_LOG_DATA(String rES_LOG_DATA) {
		RES_LOG_DATA = rES_LOG_DATA;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Log [APP_SERVER_DATETIME=" + APP_SERVER_DATETIME + ", USER_NAME=" + USER_NAME + ", DEVICE_TYPE="
				+ DEVICE_TYPE + ", REQ_URI=" + REQ_URI + ", FUNC_TYPE=" + FUNC_TYPE + ", FUNC_SUB_TYPE=" + FUNC_SUB_TYPE
				+ ", API1_FUNCCODE=" + API1_FUNCCODE + ", STATUS_CODE=" + STATUS_CODE + ", STATUS_MSG=" + STATUS_MSG
				+ ", APP_SERVER_INSTANCE_ID=" + APP_SERVER_INSTANCE_ID + ", GM_JFP_SESSION_ID=" + GM_JFP_SESSION_ID
				+ ", JFX_INSTANCE_ID=" + JFX_INSTANCE_ID + ", JFX_SESSION_ID=" + JFX_SESSION_ID + ", GM_TTIMEMS="
				+ GM_TTIMEMS + ", API_TTIMEMS=" + API_TTIMEMS + ", ESB_TTIMEMS=" + ESB_TTIMEMS + ", DEVICE_SID="
				+ DEVICE_SID + ", DEVICE_ID=" + DEVICE_ID + ", CUSTOMER_ROLE=" + CUSTOMER_ROLE + ", REQ_LOG_DATA="
				+ REQ_LOG_DATA + ", RES_LOG_DATA=" + RES_LOG_DATA + "]";
	}

}