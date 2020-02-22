package com.sinco.mybatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * json 字段处理
 * @author: dengwei
 * @date: 2018年10月31日 下午4:25:12
 *
 */
@MappedTypes({JSONObject.class})
public class JSONObjectTypeHandler extends NotNullResultTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toJSONString());
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        if(jsonSource == null) {
        	return null;
        }
        return JSON.parseObject(jsonSource);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        if(jsonSource == null) {
        	return null;
        }
        return JSON.parseObject(jsonSource);
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        if(jsonSource == null) {
        	return null;
        }
        return JSON.parseObject(jsonSource);
    }
}