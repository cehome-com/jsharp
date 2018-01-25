package jsharp.test;
import java.util.Date;

import javax.persistence.Id;

import jsharp.sql.Entity;
import jsharp.sql.anno.Column;
import jsharp.sql.anno.Table;

@Table(name="t_document")
public class Document extends Entity {

	@Id
    private long id;
	private String title;
	private String content;
	private Date createTime;
	private int browseCount;
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(nullable=false,defaultValue = "0")
	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}
}
