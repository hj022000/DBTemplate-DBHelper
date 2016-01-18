package com.dwl.dbtemplate.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetIterator implements Iterator<Object[]> {
	private final ResultSet rs;
	private final RowProcessor convert;

	public ResultSetIterator(ResultSet rs) {
		this(rs, new BasicRowProcessor());
	}

	public ResultSetIterator(ResultSet rs, RowProcessor convert) {
		this.rs = rs;
		this.convert = convert;
	}

	public boolean hasNext() {
		try {
			return !this.rs.isLast();
		} catch (SQLException e) {
			rethrow(e);
		}
		return false;
	}

	public Object[] next() {
		try {
			this.rs.next();
			return this.convert.toArray(this.rs);
		} catch (SQLException e) {
			rethrow(e);
		}
		return null;
	}

	public void remove() {
		try {
			this.rs.deleteRow();
		} catch (SQLException e) {
			rethrow(e);
		}
	}

	protected void rethrow(SQLException e) {
		throw new RuntimeException(e.getMessage());
	}
	
	 public static Iterable<Object[]> iterable(final ResultSet rs)
	 {
//		 return new Iterable(rs)
//		 {
//		 public Iterator<Object[]> iterator()
//		 {
//		 return new ResultSetIterator(ResultSetIterator.this);
//		 }
//		 };
		return new Iterable() {
			@Override
			public Iterator iterator() {
				return new ResultSetIterator(rs);
			}
		};
	 }
}
