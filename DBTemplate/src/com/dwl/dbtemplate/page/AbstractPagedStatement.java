 package com.dwl.dbtemplate.page;
 
 import java.sql.SQLException;

import com.dwl.dbtemplate.dialect.PageSqlParser;
import com.dwl.dbtemplate.dialect.PageSqlParserFactory;
 
 public abstract class AbstractPagedStatement<T>
 {
   public static final int MAX_PAGESIZE = 2147483647;
   protected String countSql;
   protected String querySql;
   protected int currentPage;
   protected int pageSize;
   protected int startIndex;
   protected int totalCount;
   protected boolean hasOffset;
   protected PageSqlParser pageSqlParser;
 
   public AbstractPagedStatement(String dialect, String sql, int currentPage, int pageSize)
   {
     this.currentPage = currentPage;
     this.pageSize = pageSize;
     this.startIndex = Page.getStartOfAnyPage(currentPage, pageSize);
     this.hasOffset = (currentPage > 1);
 
     this.pageSqlParser = PageSqlParserFactory.getParser(dialect);
     this.countSql = this.pageSqlParser.getCountingSql(sql);
     this.querySql = this.pageSqlParser.getPageSql(sql, this.hasOffset);
   }
 
   protected abstract Page<T> executeQuery(int paramInt)
     throws SQLException;
 }




