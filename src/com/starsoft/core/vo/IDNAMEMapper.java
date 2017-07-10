package com.starsoft.core.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("rawtypes")
public class IDNAMEMapper implements RowMapper {
	@Override
	public IdName mapRow(ResultSet rs, int arg1) throws SQLException {
		return new IdName(rs.getString("id"),rs.getString("tname"));
	}
}
