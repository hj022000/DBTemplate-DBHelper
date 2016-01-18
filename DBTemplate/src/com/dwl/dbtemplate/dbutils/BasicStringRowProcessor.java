 package com.dwl.dbtemplate.dbutils;
 
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Locale;
 import java.util.Map;
 import java.util.Map.Entry;
 
 public class BasicStringRowProcessor
   implements StringRowProcessor
 {
   private static final BasicStringRowProcessor instance = new BasicStringRowProcessor();
 
   @Deprecated
   public static BasicStringRowProcessor instance()
   {
     return instance;
   }
 
   public String[] toArray(ResultSet rs)
     throws SQLException
   {
     ResultSetMetaData meta = rs.getMetaData();
     int cols = meta.getColumnCount();
     String[] result = new String[cols];
 
     for (int i = 0; i < cols; i++) {
       result[i] = trimToEmpty(rs.getString(i + 1));
     }
 
     return result;
   }
 
   public Map<String, String> toMap(ResultSet rs)
     throws SQLException
   {
     Map result = new CaseInsensitiveHashMap();
     ResultSetMetaData rsmd = rs.getMetaData();
     int cols = rsmd.getColumnCount();
 
     for (int i = 1; i <= cols; i++) {
       result.put(rsmd.getColumnName(i), trimToEmpty(rs.getString(i)));
     }
 
     return result;
   }
 
   private static String trimToEmpty(String str)
   {
     return str != null ? str.trim() : "";
   }
 
   private static class CaseInsensitiveHashMap extends HashMap<String, String>
   {
     private final Map<String, String> lowerCaseMap = new HashMap();
     private static final long serialVersionUID = -2848101435296897392L;
 
     public boolean containsKey(Object key)
     {
       Object realKey = this.lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
       return super.containsKey(realKey);
     }
 
     public String get(Object key)
     {
       Object realKey = this.lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
       return (String)super.get(realKey);
     }
 
     public String put(String key, String value)
     {
       Object oldKey = this.lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
       String oldValue = (String)super.remove(oldKey);
       super.put(key, value);
       return oldValue;
     }
 
     public void putAll(Map<? extends String, ? extends String> m)
     {
       for (Map.Entry entry : m.entrySet()) {
         String key = (String)entry.getKey();
         String value = (String)entry.getValue();
         put(key, value);
       }
     }
 
     public String remove(Object key)
     {
       Object realKey = this.lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
       return (String)super.remove(realKey);
     }
   }
 }
