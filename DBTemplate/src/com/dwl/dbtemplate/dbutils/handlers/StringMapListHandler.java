 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.Map;

import com.dwl.dbtemplate.dbutils.StringRowProcessor;
 
 public class StringMapListHandler extends AbstractListHandler<Map<String, String>>
 {
   private final StringRowProcessor convert;
 
   public StringMapListHandler()
   {
     this(StringArrayHandler.ROW_PROCESSOR);
   }
 
   public StringMapListHandler(StringRowProcessor convert)
   {
     this.convert = convert;
   }
 
   protected Map<String, String> handleRow(ResultSet rs)
     throws SQLException
   {
     return this.convert.toMap(rs);
   }
 }




