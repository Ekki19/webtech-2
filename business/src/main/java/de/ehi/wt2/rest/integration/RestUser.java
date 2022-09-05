package de.ehi.wt2.rest.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.ehi.wt2.entity.DBIdentified;
import de.ehi.wt2.entity.DBUser;

public class RestUser extends DBIdentified {
	
	private String username;
	private String email;
	private String firstname;
	private String lastname;
	private Date registered;
	private List<Long> bugIdList = new ArrayList<Long>();
	private List<Long> commentIdList = new ArrayList<Long>();
	private String role;
	
	public RestUser(DBUser dbUser) {
		this.setId(dbUser.getId());
		this.username = dbUser.getUsername();
		this.firstname = dbUser.getFirstname();
		this.lastname = dbUser.getLastname();
		this.email = dbUser.getEmail();
		this.registered = dbUser.getRegistered();
		this.bugIdList = dbUser.getBugList().stream().map(bug -> bug.getId()).collect(Collectors.toList());
		this.commentIdList = dbUser.getCommentList().stream().map(comment -> comment.getId()).collect(Collectors.toList());	
		this.role = dbUser.getRole();
	}
	
	public RestUser() {
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	public List<Long> getBugIdList() {
		return bugIdList;
	}
	public void setBugIdList(List<Long> bugIdList) {
		this.bugIdList = bugIdList;
	}
	public List<Long> getCommentIdList() {
		return commentIdList;
	}
	public void setCommentIdList(List<Long> commentIdList) {
		this.commentIdList = commentIdList;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
