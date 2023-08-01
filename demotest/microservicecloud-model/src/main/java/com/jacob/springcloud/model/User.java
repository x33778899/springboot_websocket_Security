package com.jacob.springcloud.model;

import java.io.Serializable;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users") // This annotation specifies the name of the database table
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Use int for the id field as the primary key
    
	@NonNull
	@Column(name = "account", unique = true, nullable = false, length = 50)
    private String account;
	@NonNull
	@Column(name = "password", unique = false, nullable = false, length = 50)
    private String password;
	@NonNull
	@Column(name = "role", unique = false, nullable = false, length = 10)
    private String role;
    private static final long serialVersionUID = 1L; 



    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}



	
}
