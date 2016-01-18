 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;

import com.dwl.dbtemplate.dbutils.BasicStringRowProcessor;
import com.dwl.dbtemplate.dbutils.ResultSetHandler;
import com.dwl.dbtemplate.dbutils.StringRowProcessor;
 
 public class StringArrayHandler
   implements ResultSetHandler<String[]>
 {
   static final StringRowProcessor ROW_PROCESSOR = new BasicStringRowProcessor();
   private final StringRowProcessor convert;
 
   public StringArrayHandler()
   {
     this(ROW_PROCESSOR);
   }
 
   public StringArrayHandler(StringRowProcessor convert)
   {
     this.convert = convert;
   }
 
   public String[] handle(ResultSet rs)
     throws SQLException
   {
     return rs.next() ? this.convert.toArray(rs) : null;
   }
 }




