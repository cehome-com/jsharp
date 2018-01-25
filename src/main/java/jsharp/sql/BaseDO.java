package jsharp.sql;
import java.util.Date;
import com.alibaba.fastjson.JSON;
import jsharp.sql.Entity;
import jsharp.sql.anno.Column;
public class BaseDO extends Entity {
	private Date createTime;
	private Date updateTime;

	public BaseDO(){
		this.setSqlValue("createTime", "now()");
		this.setSqlValue("updateTime", "now()");
	}

	@Column(updatable=false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}