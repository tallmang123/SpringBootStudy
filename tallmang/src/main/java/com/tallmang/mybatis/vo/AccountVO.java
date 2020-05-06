package com.tallmang.mybatis.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountVO {
	private int		seq;
	@NonNull
	private String	id;
	private String	password;
	@NonNull
	private String	salt;
	@NonNull
	private String	status;
	private String 	addTime;
}
