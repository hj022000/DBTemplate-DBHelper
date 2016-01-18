 package com.dwl.dbtemplate.dbutils;
 
 import java.beans.PropertyDescriptor;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.Arrays;

import com.dwl.dbtemplate.util.WordUtils;
 
 public class CamelBeanProcessor extends BeanProcessor
 {
   protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props)
     throws SQLException
   {
     int cols = rsmd.getColumnCount();
     int[] columnToProperty = new int[cols + 1];
     Arrays.fill(columnToProperty, -1);
 
     for (int col = 1; col <= cols; col++) {
       String columnName = rsmd.getColumnLabel(col);
       if ((columnName == null) || (columnName.length() == 0)) {
         columnName = rsmd.getColumnName(col);
       }
 
       for (int i = 0; i < props.length; i++) {
         if (formatColName(columnName).equalsIgnoreCase(props[i].getName())) {
           columnToProperty[col] = i;
           break;
         }
         if (columnName.equalsIgnoreCase(props[i].getName())) {
           columnToProperty[col] = i;
           break;
         }
       }
     }
 
     return columnToProperty;
   }
 
   private String formatColName(String name)
   {
     if ((name == null) || ("".equals(name))) {
       return "";
     }
     String rstr = name.toLowerCase();
     rstr = WordUtils.uncapitalize(WordUtils.capitalize(rstr, "_".toCharArray()));
     rstr = rstr.replaceAll("_", "");
     return rstr;
   }
 }



 
 