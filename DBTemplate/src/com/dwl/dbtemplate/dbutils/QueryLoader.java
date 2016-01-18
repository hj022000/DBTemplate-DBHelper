 package com.dwl.dbtemplate.dbutils;
 
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Properties;
 
 public class QueryLoader
 {
   private static final QueryLoader instance = new QueryLoader();
 
   private final Map<String, Map<String, String>> queries = new HashMap();
 
   public static QueryLoader instance()
   {
     return instance;
   }
 
   public synchronized Map<String, String> load(String path)
     throws IOException
   {
     Map queryMap = (Map)this.queries.get(path);
 
     if (queryMap == null) {
       queryMap = loadQueries(path);
       this.queries.put(path, queryMap);
     }
 
     return queryMap;
   }
 
   protected Map<String, String> loadQueries(String path)
     throws IOException
   {
     InputStream in = getClass().getResourceAsStream(path);
 
     if (in == null) {
       throw new IllegalArgumentException(path + " not found.");
     }
 
     Properties props = new Properties();
     try {
       props.load(in);
     } finally {
       in.close();
     }
 
     HashMap hashMap = new HashMap(props);
     return hashMap;
   }
 
   public synchronized void unload(String path)
   {
     this.queries.remove(path);
   }
 }




