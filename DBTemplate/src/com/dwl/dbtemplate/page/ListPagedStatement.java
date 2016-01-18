 package com.dwl.dbtemplate.page;
 
 import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.dwl.dbtemplate.DbTemplate;
import com.dwl.dbtemplate.dbutils.DQLRunner;
import com.dwl.dbtemplate.dbutils.handlers.BeanListHandler;
import com.dwl.dbtemplate.dbutils.handlers.MapListHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayListHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringMapListHandler;
import com.dwl.dbtemplate.util.ArrayUtils;
 
 public class ListPagedStatement<T> extends AbstractPagedStatement<T>
 {
   private static final Logger pagesqllogger = Logger.getLogger(ListPagedStatement.class);
 
   protected Object[] params = new Object[0];
 
   protected Class<T> clazz = null;
   protected List<T> list;
   protected ListPage<T> listPage;
   private DataSource dataSource;
 
   public ListPagedStatement(DataSource dataSource, String dialect, String sql, int pageNo, int pageSize, Object[] params)
   {
     super(dialect, sql, pageNo, pageSize);
     if (params != null) {
       this.params = params;
     }
     this.dataSource = dataSource;
   }
 
   public ListPage<T> executeQuery(int type)
     throws SQLException
   {
     debug(0);
 
     DQLRunner run = new DQLRunner(this.dataSource);
     String[] count = (String[])run.query(this.countSql, new StringArrayHandler(), this.params);
     if (count != null)
       this.totalCount = Integer.valueOf(count[0]).intValue();
     else {
       this.totalCount = 0;
     }
     if (this.totalCount < 1)
     {
       return (ListPage<T>) ListPage.EMPTY_PAGE;
     }
 
     this.params = this.pageSqlParser.attachPageParam(this.params, this.hasOffset, this.startIndex, this.pageSize);
 
     debug(1);
 
     if (this.clazz == null)
       switch (type) {
       case 0:
         this.list = ((List)run.query(this.querySql, new StringArrayListHandler(), this.params));
         break;
       case 1:
         this.list = ((List)run.query(this.querySql, new StringMapListHandler(), this.params));
         break;
       default:
         this.list = ((List)run.query(this.querySql, new MapListHandler(), this.params));
         break;
       }
     else {
       this.list = ((List)run.query(this.querySql, new BeanListHandler(this.clazz, DbTemplate.BEAN_ROW_PROCESSOR), this.params));
     }
 
     this.listPage = new ListPage(this.list, this.startIndex, this.totalCount, this.pageSize);
     return this.listPage;
   }
 
   public void addParam(Object param)
   {
     this.params = ArrayUtils.add(this.params, param);
   }
 
   public void addParam(Object[] params)
   {
     this.params = ArrayUtils.addAll(this.params, params);
   }
 
   public void setClazz(Class<T> clazz)
   {
     this.clazz = clazz;
   }
 
   public List<T> getList()
   {
     return this.list;
   }
 
   public ListPage<T> getListPage()
   {
     return this.listPage;
   }
 
   private void debug(int type)
   {
     if (pagesqllogger.isDebugEnabled()) {
       StringBuilder sb = new StringBuilder(400);
       switch (type) {
       case 0:
         sb.append(this.countSql);
         if ((this.params == null) || (this.params.length <= 0)) break;
         sb.append(" [params:");
         for (Object obj : this.params) {
           sb.append(obj == null ? "<null>" : obj.toString()).append(", ");
         }
         sb.setLength(sb.length() - 2);
         sb.append("]");
 
         break;
       default:
         sb.append(this.querySql);
         if ((this.params != null) && (this.params.length > 0)) {
           sb.append(" [params:");
           for (Object obj : this.params) {
             sb.append(obj == null ? "<null>" : obj.toString()).append(", ");
           }
           sb.setLength(sb.length() - 2);
           sb.append("]");
         }
         sb.append(" [totalCount:").append(this.totalCount).append("]");
         sb.append(" [currentPage:").append(this.currentPage).append("]");
         sb.append(" [pageSize:").append(this.pageSize).append("]");
       }
 
       pagesqllogger.debug(sb.toString());
     }
   }
 }




