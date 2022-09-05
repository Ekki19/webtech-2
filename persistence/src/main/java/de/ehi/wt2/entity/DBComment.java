package de.ehi.wt2.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comment", schema = "public")
public class DBComment extends DBIdentified{
		
	private String comment;	
	private Date published;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private DBUser creator;
	
	@ManyToOne
	@JoinColumn(name = "bug_id")
	private DBBug bug;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public DBUser getCreator() {
		return creator;
	}
    public void setCreator(DBUser user) {
		this.creator = user;
	}
	
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}
	public DBBug getBug() {
		return bug;
	}
	public void setBug(DBBug bug) {
		this.bug = bug;
	}
}
