package de.ehi.wt2.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bug", schema = "public")
public class DBBug extends DBIdentified {
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private DBUser creator;
	
	@OneToMany(mappedBy = "bug", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DBComment> commentList = new ArrayList<DBComment>();
	
	@OneToOne()
	private DBUser affecteduser;
	
	private String headline;
	private String description;
	private Date created;
	private boolean ispublic = false;
	
	public DBUser getCreator() {
		return creator;
	}
	public void setCreator(DBUser creator) {
		this.creator = creator;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public List<DBComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<DBComment> commentList) {
		this.commentList = commentList;
	}
	public boolean isIspublic() {
		return ispublic;
	}
	public void setIspublic(boolean ispublic) {
		this.ispublic = ispublic;
	}
	public DBUser getAffecteduser() {
		return affecteduser;
	}
	public void setAffecteduser(DBUser affecteduser) {
		this.affecteduser = affecteduser;
	}

}
