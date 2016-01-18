 package com.dwl.dbtemplate.dialect;
 
 import com.dwl.dbtemplate.util.ArrayUtils;
 
 public class OraclePageSqlParser
   implements PageSqlParser
 {
   public String getPageSql(String sql, boolean hasOffset)
   {
     StringBuilder ret = new StringBuilder(sql.length() + 100);
     if (hasOffset)
       ret.append("select * from ( select row_.*, rownum rownum_ from ( ");
     else {
       ret.append("select * from ( ");
     }
     ret.append(sql);
     if (hasOffset)
       ret.append(" ) row_ where rownum < ?) where rownum_ >= ?");
     else {
       ret.append(" ) where rownum < ?");
     }
     return ret.toString();
   }
 
   public String getCountingSql(String sql)
   {
     return "select count(1) from ( " + sql + ")";
   }
 
   public Object[] attachPageParam(Object[] params, boolean hasOffset, int startIndex, int pageSize)
   {
     if (hasOffset) {
       params = ArrayUtils.add(params, Integer.valueOf(startIndex + pageSize));
       params = ArrayUtils.add(params, Integer.valueOf(startIndex));
     } else {
       params = ArrayUtils.add(params, Integer.valueOf(startIndex + pageSize));
     }
     return params;
   }
 }




