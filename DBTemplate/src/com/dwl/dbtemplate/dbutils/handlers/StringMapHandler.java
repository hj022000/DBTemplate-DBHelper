 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.Map;

import com.dwl.dbtemplate.dbutils.ResultSetHandler;
import com.dwl.dbtemplate.dbutils.StringRowProcessor;
 
 public class StringMapHandler
   implements ResultSetHandler<Map<String, String>>
 {
   private final StringRowProcessor convert;
 
   public StringMapHandler()
   {
     this(StringArrayHandler.ROW_PROCESSOR);
   }
 
   public StringMapHandler(StringRowProcessor convert)
   {
     this.convert = convert;
   }
 
   public Map<String, String> handle(ResultSet rs)
     throws SQLException
   {
     return rs.next() ? this.convert.toMap(rs) : null;
   }
 }




