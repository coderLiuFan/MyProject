package com.lite.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {
        // 将java类型转换为数据库需要的类型
        long time = date.getTime();
        preparedStatement.setLong(i,time);
    }

    /**
     *
     * @param resultSet 查询出的结果集
     * @param s 要转换的字段名称
     * @return
     * @throws SQLException
     */
    @Override
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {
        // 将数据库中的类型转换为java的类型
        // String参数 要转换的字段的名称
        // 获得结果集中需要的数据（long）转换成Date类型 返回
        long aLong = resultSet.getLong(s);
        Date date = new Date(aLong);
        return date;
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        // 将数据库中的类型转换为java的类型
        long aLong = resultSet.getLong(i);
        Date date = new Date(aLong);
        return date;
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        // 将数据库中的类型转换为java的类型
        long aLong = callableStatement.getLong(i);
        Date date = new Date(aLong);
        return date;
    }
}
