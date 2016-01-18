 package com.dwl.dbtemplate.page;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class ListPage<T> extends Page<T>
 {
   private List<T> list;
   public static final ListPage<?> EMPTY_PAGE = new ListPage();
 
   public ListPage()
   {
     this(new ArrayList(), 0, 0);
   }
 
   public ListPage(List<T> data, int start, int totalSize)
   {
     this(data, start, totalSize, 20);
   }
 
   public ListPage(List<T> data, int start, int totalSize, int pageSize)
   {
     this.list = data;
     super.init(start, data == null ? 0 : data.size(), totalSize, pageSize, this.list);
   }
 
   public List<T> getList()
   {
     return this.list;
   }
 }




