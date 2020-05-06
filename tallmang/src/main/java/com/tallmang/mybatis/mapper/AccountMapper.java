package com.tallmang.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tallmang.mybatis.vo.AccountVO;

@Mapper
public interface AccountMapper {
	
	AccountVO getUser();
}
