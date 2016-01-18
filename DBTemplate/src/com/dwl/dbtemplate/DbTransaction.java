package com.dwl.dbtemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.dwl.dbtemplate.dbutils.DbUtils;
import com.dwl.dbtemplate.dbutils.DQLRunner;
import com.dwl.dbtemplate.dbutils.handlers.BeanHandler;
import com.dwl.dbtemplate.dbutils.handlers.BeanListHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayListHandler;

public class DbTransaction {
	private static final Logger logger;
	private static final Logger sqllogger;
	private Connection conn = null;
	private boolean isAutoCommit;
	private DQLRunner run = null;

	static {
		logger = Logger.getLogger(DbTransaction.class);

		sqllogger = Logger.getLogger("com.dbtemplate.SQL");
	}

	public DbTransaction(DataSource dataSource) {
		this.run = new DQLRunner(dataSource);
	}

	public void connect() throws SQLException {
		try {
			this.conn = this.run.getDataSource().getConnection();
		} catch (SQLException ex) {
			logger.error("get connection error!", ex);
			throw ex;
		}
	}

	public void begin() throws SQLException {
		try {
			this.isAutoCommit = this.conn.getAutoCommit();
			this.conn.setAutoCommit(false);
		} catch (SQLException ex) {
			logger.error("begin transaction error!", ex);
			throw ex;
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void commit() throws SQLException {
		try {
			this.conn.commit();
			this.conn.setAutoCommit(this.isAutoCommit);
		} catch (SQLException ex) {
			logger.error("commit transaction error!", ex);
			throw ex;
		}
	}

	public void rollback() {
		try {
			this.conn.rollback();
			this.conn.setAutoCommit(this.isAutoCommit);
		} catch (SQLException ex) {
			logger.error("rollback transaction error!", ex);
		}
	}

	public boolean getAutoCommit() throws SQLException {
		boolean result;
		try {
			result = this.conn.getAutoCommit();
		} catch (SQLException ex) {
			logger.error("get auto commit status error!", ex);
			throw ex;
		}

		return result;
	}

	public int execute(String sql) throws SQLException {
		return execute(sql, null);
	}

	public int execute(String sql, Object param) throws SQLException {
		return execute(sql, new Object[] { param });
	}

	public int execute(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return this.run.update(this.conn, sql, params);
	}

	public int[] batch(String sql, List<Object[]> params) throws SQLException {
		debug(sql, new Object[] { "batch sql, count:" + (params == null ? -1 : params.size()) });
		assert (params != null);
		Object[][] p = new Object[params.size()][];
		for (int i = 0; i < params.size(); i++) {
			p[i] = ((Object[]) params.get(i));
		}
		return this.run.batch(this.conn, sql, p);
	}

	public String[] find(String sql) throws SQLException {
		return find(sql, new Object());
	}

	public String[] find(String sql, Object param) throws SQLException {
		return find(sql, new Object[] { param });
	}

	public String[] find(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (String[]) this.run.query(this.conn, sql, new StringArrayHandler(), params);
	}

	public <T> T find(String sql, Class<T> type) throws SQLException {
		return find(sql, type, null);
	}

	public <T> T find(String sql, Class<T> type, Object param) throws SQLException {
		return find(sql, type, new Object[] { param });
	}

	public <T> T find(String sql, Class<T> type, Object[] params) throws SQLException {
		debug(sql, params);
		return (T) this.run.query(this.conn, sql, new BeanHandler(type, DbTemplate.BEAN_ROW_PROCESSOR), params);
	}

	public List<String[]> query(String sql) throws SQLException {
		return query(sql, new Object());
	}

	public List<String[]> query(String sql, Object param) throws SQLException {
		return query(sql, new Object[] { param });
	}

	public List<String[]> query(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) this.run.query(this.conn, sql, new StringArrayListHandler(), params);
	}

	public <T> List<T> query(String sql, Class<T> type) throws SQLException {
		return query(sql, type, null);
	}

	public <T> List<T> query(String sql, Class<T> type, Object param) throws SQLException {
		return query(sql, type, new Object[] { param });
	}

	public <T> List<T> query(String sql, Class<T> type, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) this.run.query(this.conn, sql, new BeanListHandler(type, DbTemplate.BEAN_ROW_PROCESSOR), params);
	}

	public void close() {
		DbUtils.closeQuietly(this.conn);
		this.conn = null;
	}

	private static void debug(String sql, Object[] params) {
		if (sqllogger.isDebugEnabled()) {
			if (params == null) {
				params = new Object[0];
			}
			StringBuilder sb = new StringBuilder(400);
			sb.append(sql);
			if (params.length != 0) {
				sb.append(" [params:");
				for (Object obj : params) {
					sb.append(obj == null ? "<null>" : obj.toString()).append(", ");
				}
				sb.setLength(sb.length() - 2);
				sb.append("]");
			}
			sqllogger.info(sb.toString());
		}
	}

	public static void main(String[] args) throws Exception {
	}
}
