 package com.dwl.dbtemplate.page;
 
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.List;
 
 public class Page<T>
   implements Serializable
 {
   private static final long serialVersionUID = -8901550401275849961L;
   public static final int MAX_PAGESIZE = 2147483647;
   public static final Page<Object> EMPTY_PAGE = new Page();
   public static final int DEFAULT_PAGESIZE = 20;
   private int myPageSize = 20;
   private int start;
   private List<T> data;
   private int curPage;
   private int avaCount;
   private int pageCount;
   private int recordCount;
 
   protected Page()
   {
     init(0, 0, 0, 20, new ArrayList());
   }
 
   protected void init(int start, int avaCount, int totalSize, int pageSize, List<T> data)
   {
     this.avaCount = avaCount;
     this.myPageSize = pageSize;
 
     this.start = start;
     this.recordCount = totalSize;
 
     this.data = data;
 
     this.curPage = ((start - 1) / pageSize + 1);
     this.pageCount = ((totalSize + pageSize - 1) / pageSize);
 
     if ((totalSize == 0) && (avaCount == 0)) {
       this.curPage = 1;
       this.pageCount = 1;
     }
   }
 
   public List<T> getData()
   {
     return this.data;
   }
 
   public int getPageSize()
   {
     return this.myPageSize;
   }
 
   public boolean hasNextPage()
   {
     return getCurPage() < getPageCount();
   }
 
   public boolean hasPreviousPage()
   {
     return getCurPage() > 1;
   }
 
   public int getStart()
   {
     return this.start;
   }
 
   public int getEnd()
   {
     int end = getStart() + getAvaCount() - 1;
     if (end < 0) {
       end = 0;
     }
     return end;
   }
 
   public int getStartOfPreviousPage()
   {
     return Math.max(this.start - this.myPageSize, 1);
   }
 
   public int getStartOfNextPage()
   {
     return this.start + this.avaCount;
   }
 
   public static int getStartOfAnyPage(int pageNo)
   {
     return getStartOfAnyPage(pageNo, 20);
   }
 
   public static int getStartOfAnyPage(int pageNo, int pageSize)
   {
     int startIndex = (pageNo - 1) * pageSize + 1;
     if (startIndex < 1) startIndex = 1;
     return startIndex;
   }
 
   public int getAvaCount()
   {
     return this.avaCount;
   }
 
   public int getRecordCount()
   {
     return this.recordCount;
   }
 
   public int getCurPage()
   {
     return this.curPage;
   }
 
   public int getPageCount()
   {
     return this.pageCount;
   }
 }




