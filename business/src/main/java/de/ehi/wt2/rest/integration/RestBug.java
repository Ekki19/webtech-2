package de.ehi.wt2.rest.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.ehi.wt2.entity.DBBug;
import de.ehi.wt2.entity.DBIdentified;

public class RestBug extends DBIdentified {

	private long creatorId;
	private List<Long> commentIdList = new ArrayList<Long>();
	private String headline;
	private String description;
	private Date created;
	private boolean ispublic;
	private long affecteduserid;
	private String affectedusername;
	private RestUser creator;
	
	public RestBug(DBBug dbBug) {
		this.setId(dbBug.getId());
		this.creatorId = dbBug.getCreator().getId();
		this.commentIdList = dbBug.getCommentList().stream().map(comment -> comment.getId()).collect(Collectors.toList());
		this.headline = dbBug.getHeadline();
		this.created = dbBug.getCreated();
		this.description = dbBug.getDescription();
		this.ispublic = dbBug.isIspublic();
		
		if(dbBug.getAffecteduser() != null) {
			this.affecteduserid = dbBug.getAffecteduser().getId();
			this.affectedusername = dbBug.getAffecteduser().getUsername();
		}
		this.creator = new RestUser(dbBug.getCreator());
	}

	public RestBug() {
		
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public List<Long> getCommentListIds() {
		return commentIdList;
	}

	public void setCommentListIds(List<Long> commentListIds) {
		this.commentIdList = commentListIds;
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
	public boolean isIspublic() {
		return ispublic;
	}
	public void setIspublic(boolean ispublic) {
		this.ispublic = ispublic;
	}

	public long getAffecteduserid() {
		return affecteduserid;
	}

	public void setAffecteduserid(long affecteduserid) {
		this.affecteduserid = affecteduserid;
	}

	public String getAffectedusername() {
		return affectedusername;
	}

	public void setAffectedusername(String affectedusername) {
		this.affectedusername = affectedusername;
	}

	public RestUser getCreator() {
		return creator;
	}

	public void setCreator(RestUser creator) {
		this.creator = creator;
	}


}