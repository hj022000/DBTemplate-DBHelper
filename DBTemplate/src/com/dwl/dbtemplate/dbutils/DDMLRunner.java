package com.dwl.dbtemplate.dbutils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DDMLRunner {
	private boolean isOracle;
	
	private static DDMLRunner drun = null;
	protected static final Logger logger = Logger.getLogger(DDMLRunner.class);
	public synchronized static DDMLRunner getInstance(){
		if(drun==null){
			drun = new DDMLRunner();
		}
		return drun;
	}
	
	
	private DDMLRunner(){}
	
	private final DDLUtil du = DDLUtil.getInstance();
	
	public int update(Connection conn,String sql, List<Object> params) {
		return update(conn,sql, params.toArray(new Object[0]));
	}
	
	public int update(Connection conn, String sql, Object[] params) {
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			sql = du.adjust(this.isOracle, sql, params);
			stmt = conn.prepareStatement(sql);
			fillStatement(stmt, params);
			logger.info("sql:"+sql+"param:"+params);
			rows = stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt, conn);
		}
		return rows;
	}

	private int[] batch(Connection conn, String sql, Object[][] params) {
		PreparedStatement stmt = null;
		int[] rows = null;
		try {
			conn.setAutoCommit(false);
			sql = du.adjustSQL(this.isOracle, sql, params[0]);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				du.adjustParams(params[i]);
				fillStatement(stmt, params[i]);
				stmt.addBatch();
			}
			logger.info("sql:"+sql+"param:"+params);
			rows = stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt, conn);
		}
		return rows;
	}

	public <T> int insert(Connection conn, Class<T> cls, T bean) {
		int rows = 0;
		PreparedStatement stmt = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(cls, Object.class);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			String table = du.camel2underscore(cls.getSimpleName());
			String columns = "";
			String questionMarks = "";

//			Object[] params = new Object[pds.length];

			List<Object> so = new ArrayList<Object>();
			
//			int j = 0;
			String name="";
			Object value;
			for (PropertyDescriptor pd : pds) {
				Method getter = pd.getReadMethod();
				name = pd.getName();
				value = getter.invoke(bean, new Object[0]);
				if(value==null){
					continue;
				}
				columns = columns + du.camel2underscore(name) + ",";
				questionMarks = questionMarks + "?,";
				so.add(value);
//				params[j] = value;
//				j++;
			}
			columns = columns.substring(0, columns.length() - 1);
			questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
			String sql = String.format("insert into %s (%s) values (%s)",
					new Object[] { table, columns, questionMarks });

			sql = du.adjust(this.isOracle, sql, so.toArray(new Object[]{}));

			stmt = conn.prepareStatement(sql);
			
			fillStatement(stmt, so.toArray(new Object[]{}));
			logger.info("sql:"+sql+"params:"+so.toArray(new Object[]{}));
			try {
				rows = stmt.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt, conn);
		}
		return rows;
	}

	public <T> int[] insert(Connection conn, Class<T> cls, List<T> beans) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(cls, Object.class);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

		String table = du.camel2underscore(cls.getSimpleName());
		String columns = "";
		String questionMarks = "";

		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			columns = columns + du.camel2underscore(name) + ",";
			questionMarks = questionMarks + "?,";
		}
		columns = columns.substring(0, columns.length() - 1);
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
		String sql = String.format("insert into %s (%s) values (%s)", new Object[] { table, columns, questionMarks });
		int rows = beans.size();
		int cols = pds.length;

		Object[][] params = new Object[rows][cols];
		try {
			for (int i = 0; i < rows; i++) {
				int j = 0;
				for (PropertyDescriptor pd : pds) {
					Method getter = pd.getReadMethod();
					Object value = getter.invoke(beans.get(i), new Object[0]);
					params[i][j] = value;
					j++;
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		logger.info("sql:"+sql+"param:"+params);
		return batch(conn, sql, params);
	}

	public <T> int update(Connection conn, Class<T> cls, T bean) {
		return update(conn, cls, bean, "id");
	}

	public <T> int update(Connection conn, Class<T> cls, T bean, String primaryKey) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(cls, Object.class);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

			Object[] params = new Object[pds.length];
			primaryKey = du.underscore2camel(primaryKey);
			Object id = Integer.valueOf(0);
			String columnAndQuestionMarks = "";
			int j = 0;
			for (PropertyDescriptor pd : pds) {
				Method getter = pd.getReadMethod();
				String name = pd.getName();
				Object value = getter.invoke(bean, new Object[0]);
				if (name.equals(primaryKey)) {
					id = value;
				} else {
					columnAndQuestionMarks = columnAndQuestionMarks + du.camel2underscore(name) + "=?,";
					params[j] = value;
					j++;
				}
			}
			params[j] = id;
			String table = du.camel2underscore(cls.getSimpleName());
			columnAndQuestionMarks = columnAndQuestionMarks.substring(0, columnAndQuestionMarks.length() - 1);
			String sql = String.format("update %s set %s where %s = ?",
					new Object[] { table, columnAndQuestionMarks, du.camel2underscore(primaryKey) });
			logger.info("sql:"+sql+"param:"+params);
			return update(conn, sql, params);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> int[] update(Connection conn, Class<T> cls, List<T> beans) {
		return update(conn, cls, beans, "id");
	}

	public <T> int[] update(Connection conn, Class<T> cls, List<T> beans, String primaryKey) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(cls, Object.class);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

			primaryKey = du.underscore2camel(primaryKey);
			String columnAndQuestionMarks = "";

			for (PropertyDescriptor pd : pds) {
				String name = pd.getName();
				if (name.equals(primaryKey))
					continue;
				columnAndQuestionMarks = columnAndQuestionMarks + du.camel2underscore(name) + "=?,";
			}

			String table = du.camel2underscore(cls.getSimpleName());
			columnAndQuestionMarks = columnAndQuestionMarks.substring(0, columnAndQuestionMarks.length() - 1);
			String sql = String.format("update %s set %s where %s = ?",
					new Object[] { table, columnAndQuestionMarks, du.camel2underscore(primaryKey) });

			int rows = beans.size();
			int cols = pds.length;
			Object id = Integer.valueOf(0);
			Object[][] params = new Object[rows][cols];
			for (int i = 0; i < rows; i++) {
				int j = 0;
				for (PropertyDescriptor pd : pds) {
					Method getter = pd.getReadMethod();
					String name = pd.getName();
					Object value = getter.invoke(beans.get(i), new Object[0]);
					if (name.equals(primaryKey)) {
						id = value;
					} else {
						params[i][j] = value;
						j++;
					}
				}
				params[i][j] = id;
			}
			logger.info("sql:"+sql+"param:"+params);
			return batch(conn, sql, params);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IntrospectionException e) {
		}
		throw new RuntimeException();
	}

	public <T> int delete(Connection conn, Class<T> cls, long id) {
		String sql = String.format("delete from %s where id=?",
				new Object[] { du.camel2underscore(cls.getSimpleName()) });
		return update(conn, sql, new Object[] { Long.valueOf(id) });
	}

	private void fillStatement(PreparedStatement stmt, Object[] params) {
		if (params == null)
			return;
		try {
			for (int i = 0; i < params.length; i++) {
				if ((this.isOracle) && (params[i] == null)) {
					stmt.setNull(i + 1, 12);
				} else
					stmt.setObject(i + 1, params[i]);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void close(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			close(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void close(Connection conn) {
		try {
			if ((conn != null) && (conn.getAutoCommit()))
				conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	protected final static class DDLUtil {
		private final static DDLUtil psh = new DDLUtil();
		private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public static DDLUtil getInstance() {
			return psh;
		}

		public String adjustSQL(boolean isOracle, String sql, Object[] params) {
			if (!isOracle)
				return sql;
			int cols = params.length;
			Object[] args = new Object[cols];
			boolean found = false;
			for (int i = 0; i < cols; i++) {
				Object value = params[i];
				if ((value instanceof Date)) {
					args[i] = "to_date(?,'yyyy-mm-dd hh24:mi:ss')";
					found = true;
				} else {
					args[i] = "?";
				}
			}
			if (found) {
				String format = sql.replaceAll("\\?", "%s");
				sql = String.format(format, args);
			}
			return sql;
		}

		public void adjustParams(Object[] params) {
			int i = 0;
			for (int cols = params.length; i < cols; i++) {
				Object value = params[i];
				if (value == null)
					continue;
				if ((value instanceof Date))
					params[i] = sdf.format(value);
				else if (value.getClass().isEnum())
					params[i] = params[i].toString();
				else if ((value instanceof Boolean))
					params[i] = Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
			}
		}

		public String adjust(boolean isOracle, String sql, Object[] params) {
			int cols = params.length;
			Object[] args = new Object[cols];
			boolean found = false;
			for (int i = 0; i < cols; i++) {
				args[i] = "?";
				Object value = params[i];
				if (value == null)
					continue;
				if ((value instanceof Date)) {
					if (isOracle) {
						args[i] = "to_date(?,'yyyy-mm-dd hh24:mi:ss')";
						found = true;
					}
					params[i] = sdf.format(value);
				} else if (value.getClass().isEnum()) {
					params[i] = value.toString();
				}
			}
			if (found) {
				String format = sql.replaceAll("\\?", "%s");
				sql = String.format(format, args);
			}
			return sql;
		}

		public String camel2underscore(String camel) {
			camel = camel.replaceAll("([a-z])([A-Z])", "$1_$2");
			return camel.toLowerCase();
		}

		public String underscore2camel(String underscore) {
			if (!underscore.contains("_")) {
				return underscore;
			}
			StringBuffer buf = new StringBuffer();
			underscore = underscore.toLowerCase();
			Matcher m = Pattern.compile("_([a-z])").matcher(underscore);
			while (m.find()) {
				m.appendReplacement(buf, m.group(1).toUpperCase());
			}
			return m.appendTail(buf).toString();
		}

	}
}