 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;

import com.dwl.dbtemplate.dbutils.StringRowProcessor;
 
 public class StringArrayListHandler extends AbstractListHandler<String[]>
 {
   private final StringRowProcessor convert;
 
   public StringArrayListHandler()
   {
     this(StringArrayHandler.ROW_PROCESSOR);
   }
 
   public StringArrayListHandler(StringRowProcessor convert)
   {
     this.convert = convert;
   }
 
   protected String[] handleRow(ResultSet rs)
     throws SQLException
   {
     return this.convert.toArray(rs);
   }
 }




