package de.ehi.wt2.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "user", schema = "public")
public class DBUser extends DBIdentified {
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private Date registered;
	private String role;
	
	@OneToMany(mappedBy = "creator", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DBBug> bugList = new ArrayList<DBBug>();
	
	@OneToMany(mappedBy = "creator", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DBComment> commentList = new ArrayList<DBComment>();
	
	public List<DBBug> getBugList() {
		return bugList;
	}
	public void setBugList(List<DBBug> bugList) {
		this.bugList= bugList;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE_TIME)
	public Date getRegistered() {
		return registered;
	}
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	public List<DBComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<DBComment> commentList) {
		this.commentList = commentList;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}


}
