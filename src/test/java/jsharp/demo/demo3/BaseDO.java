package jsharp.demo.demo3;

import java.util.Date;

import jsharp.sql.Entity;
import jsharp.sql.anno.Column;

public class BaseDO extends Entity {

	private Date gmtCreate;
	private Date gmtModified;
	public BaseDO(){
		this.setSqlValue("gmtCreate", "now()");
		this.setSqlValue("gmtModified", "now()");
	}
	
	 public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	@Column(updatable=false)
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	
}
