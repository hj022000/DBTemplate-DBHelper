 package com.dwl.dbtemplate.dbutils.handlers;
 
 import java.sql.ResultSet;
 import java.sql.SQLException;

import com.dwl.dbtemplate.dbutils.RowProcessor;
 
 public class BeanMapHandler<K, V> extends AbstractKeyedHandler<K, V>
 {
   private final Class<V> type;
   private final RowProcessor convert;
   private final int columnIndex;
   private final String columnName;
 
   public BeanMapHandler(Class<V> type)
   {
     this(type, ArrayHandler.ROW_PROCESSOR, 1, null);
   }
 
   public BeanMapHandler(Class<V> type, RowProcessor convert)
   {
     this(type, convert, 1, null);
   }
 
   public BeanMapHandler(Class<V> type, int columnIndex)
   {
     this(type, ArrayHandler.ROW_PROCESSOR, columnIndex, null);
   }
 
   public BeanMapHandler(Class<V> type, String columnName)
   {
     this(type, ArrayHandler.ROW_PROCESSOR, 1, columnName);
   }
 
   private BeanMapHandler(Class<V> type, RowProcessor convert, int columnIndex, String columnName)
   {
     this.type = type;
     this.convert = convert;
     this.columnIndex = columnIndex;
     this.columnName = columnName;
   }
 
   protected K createKey(ResultSet rs)
     throws SQLException
   {
     return (K) (this.columnName == null ? 
       rs.getObject(this.columnIndex) : 
       rs.getObject(this.columnName));
   }
 
   protected V createRow(ResultSet rs) throws SQLException
   {
     return this.convert.toBean(rs, this.type);
   }
 }




