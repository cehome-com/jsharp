package jsharp.demo;

import java.util.Date;

import javax.persistence.Id;

import jsharp.sql.anno.Column;
import jsharp.sql.anno.Table;

@Table(name="t_document", columnUnderscoreSupport=true)
public class Document {

	@Id
	private long id;
	private String title;
	private String content;
	private Date systime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSystime() {
		return systime;
	}
	public void setSystime(Date systime) {
		this.systime = systime;
	}
}
