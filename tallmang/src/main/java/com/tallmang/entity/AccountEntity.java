package com.tallmang.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Account")
@Data
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int		seq;
	@Column
	private String	id;
	@Column
	private String	password;
	@Column
	private String	salt;
	@Column
	private String	status;
	@Column
	private String 	addTime;

	@Override
	public String toString() {
		return "AccountEntity{" +
				"seq=" + seq +
				", id='" + id + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", status='" + status + '\'' +
				", addTime='" + addTime + '\'' +
				'}';
	}
}
