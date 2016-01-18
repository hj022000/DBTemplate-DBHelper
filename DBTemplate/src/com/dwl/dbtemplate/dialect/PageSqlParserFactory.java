 package com.dwl.dbtemplate.dialect;

public class PageSqlParserFactory
 {
   public static PageSqlParser getParser(String dialect)
   {
     if ("mysql".equals(dialect)) {
       return new MySqlPageParser();
     }
     return new OraclePageSqlParser();
   }
 }




