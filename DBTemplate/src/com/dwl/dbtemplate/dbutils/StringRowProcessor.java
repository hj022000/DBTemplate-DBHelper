package com.dwl.dbtemplate.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract interface StringRowProcessor
{
  public abstract String[] toArray(ResultSet paramResultSet)
    throws SQLException;

  public abstract Map<String, String> toMap(ResultSet paramResultSet)
    throws SQLException;
}
