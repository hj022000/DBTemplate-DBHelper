package com.dwl.dbtemplate.dialect;

public abstract interface PageSqlParser
{
  public abstract String getPageSql(String paramString, boolean paramBoolean);

  public abstract String getCountingSql(String paramString);

  public abstract Object[] attachPageParam(Object[] paramArrayOfObject, boolean paramBoolean, int paramInt1, int paramInt2);
}

