 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.Map;

import com.dwl.dbtemplate.dbutils.ResultSetHandler;
import com.dwl.dbtemplate.dbutils.RowProcessor;
 
 public class MapHandler
   implements ResultSetHandler<Map<String, Object>>
 {
   private final RowProcessor convert;
 
   public MapHandler()
   {
     this(ArrayHandler.ROW_PROCESSOR);
   }
 
   public MapHandler(RowProcessor convert)
   {
     this.convert = convert;
   }
 
   public Map<String, Object> handle(ResultSet rs)
     throws SQLException
   {
     return rs.next() ? this.convert.toMap(rs) : null;
   }
 }




