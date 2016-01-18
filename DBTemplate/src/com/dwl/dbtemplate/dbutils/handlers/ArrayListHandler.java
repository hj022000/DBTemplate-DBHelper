 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;

import com.dwl.dbtemplate.dbutils.RowProcessor;
 
 public class ArrayListHandler extends AbstractListHandler<Object[]>
 {
   private final RowProcessor convert;
 
   public ArrayListHandler()
   {
     this(ArrayHandler.ROW_PROCESSOR);
   }
 
   public ArrayListHandler(RowProcessor convert)
   {
     this.convert = convert;
   }
 
   protected Object[] handleRow(ResultSet rs)
     throws SQLException
   {
     return this.convert.toArray(rs);
   }
 }




