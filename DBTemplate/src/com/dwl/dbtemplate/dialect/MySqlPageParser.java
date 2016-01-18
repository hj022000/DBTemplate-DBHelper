 package com.dwl.dbtemplate.dialect;
 
 import com.dwl.dbtemplate.util.ArrayUtils;
 
 public class MySqlPageParser
   implements PageSqlParser
 {
   public String getPageSql(String sql, boolean hasOffset)
   {
     if (hasOffset) {
       return sql + " limit ?, ?";
     }
     return sql + " limit ?";
   }
 
   public String getCountingSql(String sql)
   {
     return "select count(1) from ( " + sql + ") as __tc";
   }
 
   public Object[] attachPageParam(Object[] params, boolean hasOffset, int startIndex, int pageSize)
   {
     if (hasOffset) {
       params = ArrayUtils.add(params, Integer.valueOf(startIndex - 1));
       params = ArrayUtils.add(params, Integer.valueOf(pageSize));
     } else {
       params = ArrayUtils.add(params, Integer.valueOf(pageSize));
     }
     return params;
   }
 }




