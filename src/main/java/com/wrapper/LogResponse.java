package com.wrapper;

import java.util.List;

import com.model.Log;

public class LogResponse {

	private int page;
	private int total;
	private int records;
	private List<Log> rows;

	  public LogResponse(List<Log> rows, int page, int total, int records){
	    	this.rows = rows;  
	    	this.page = page;
	    	this.total = total;
	    	this.records = records;

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

		/**
		 * @return the rows
		 */
		public List<Log> getRows() {
			return rows;
		}

		/**
		 * @param rows the rows to set
		 */
		public void setRows(List<Log> rows) {
			this.rows = rows;
		}

}