package de.ehi.wt2.rest.integration;

import java.util.Date;

import de.ehi.wt2.entity.DBComment;
import de.ehi.wt2.entity.DBIdentified;

public class RestComment extends DBIdentified {
	
	private long creatorId;
	private long bugId;
	private String comment;	
	private Date published;
	private RestUser creator;
	
	public RestComment() {
		
	}
	
	public RestComment(DBComment dbComment) {
		this.setId(dbComment.getId());
		this.creatorId = dbComment.getCreator().getId();
		this.bugId = dbComment.getBug().getId();
		this.comment = dbComment.getComment();
		this.published = dbComment.getPublished();
		this.creator = new RestUser(dbComment.getCreator());
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Long getBugId() {
		return bugId;
	}
	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}

	public RestUser getCreator() {
		return creator;
	}

	public void setCreator(RestUser creator) {
		this.creator = creator;
	}
}
