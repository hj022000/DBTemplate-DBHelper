package com.dwl.dbtemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.dwl.dbtemplate.dbutils.BasicRowProcessor;
import com.dwl.dbtemplate.dbutils.CamelBeanProcessor;
import com.dwl.dbtemplate.dbutils.DDMLRunner;
import com.dwl.dbtemplate.dbutils.DQLRunner;
import com.dwl.dbtemplate.dbutils.DbUtils;
import com.dwl.dbtemplate.dbutils.RowProcessor;
import com.dwl.dbtemplate.dbutils.handlers.ArrayHandler;
import com.dwl.dbtemplate.dbutils.handlers.ArrayListHandler;
import com.dwl.dbtemplate.dbutils.handlers.BeanHandler;
import com.dwl.dbtemplate.dbutils.handlers.BeanListHandler;
import com.dwl.dbtemplate.dbutils.handlers.MapHandler;
import com.dwl.dbtemplate.dbutils.handlers.MapListHandler;
import com.dwl.dbtemplate.dbutils.handlers.ScalarHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringArrayListHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringMapHandler;
import com.dwl.dbtemplate.dbutils.handlers.StringMapListHandler;
import com.dwl.dbtemplate.page.ListPage;
import com.dwl.dbtemplate.page.ListPagedStatement;

public class DbTemplate{
	protected static final Logger logger = Logger.getLogger(DbTemplate.class);

	private DataSource dataSource;
	protected String dialect;
	public static final RowProcessor BEAN_ROW_PROCESSOR = new BasicRowProcessor(new CamelBeanProcessor());

	private static DQLRunner qrun = null;
	
	private static DDMLRunner drun = DDMLRunner.getInstance();;
	
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		qrun= new DQLRunner(dataSource);
	}
	
//	public DbTemplate(DataSource ds){
//		this.dataSource=ds;
//	}
//	
	//init dialect 
	public void InitDialect() {
		try {
			Connection conn = this.dataSource.getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			String databaseName = databaseMetaData.getDatabaseProductName();
			logger.info("got data source to: " + databaseMetaData.getDatabaseProductName() + " ("
					+ databaseMetaData.getDatabaseProductVersion() + ") through driver: "
					+ databaseMetaData.getDriverName() + " (" + databaseMetaData.getDriverVersion() + ")");
			if (databaseName.equalsIgnoreCase("postgresql")) {
				logger.info("selecting postgres implementation.");
				this.dialect = "postgresql";
			} else if (databaseName.equalsIgnoreCase("mysql")) {
				logger.info("selecting mysql implementation.");
				this.dialect = "mysql";
			} else if (databaseName.equalsIgnoreCase("oracle")) {
				logger.info("selecting oracle implementation.");
				this.dialect = "oracle";
			} else if (databaseName.equalsIgnoreCase("hsql database engine")) {
				logger.info("selecting hsql implementation.");
				this.dialect = "hsql";
			} else {
				logger.warn("unknown database '" + databaseName
						+ "', will use generic driver, which does not support paging result sets. All data will be read on all select statements!");
				this.dialect = "oracle";
			}
		} catch (SQLException e) {
			throw new RuntimeException("could not instantiate database dialect", e);
		}
	}

	public DbTransaction getDbTransaction() {
		return new DbTransaction(this.dataSource);
	}

	private synchronized Connection getConn() {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void closeConn(Connection conn) {
		DbUtils.closeQuietly(conn);
	}

	public String queryScalar(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		Object obj = qrun.query(sql, new ScalarHandler(1), params);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	public int[] batch(String sql, List<Object[]> params) throws SQLException {
		debug(sql, new Object[] { "batch sql, count:" + (params == null ? -1 : params.size()) });
		if (params == null) {
			throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
		}
		Object[][] o = new Object[params.size()][];
		for (int i = 0; i < params.size(); i++) {
			o[i] = ((Object[]) params.get(i));
		}
		return qrun.batch(sql, o);
	}

	public Map<String, String> getMap(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (Map) qrun.query(sql, new StringMapHandler(), params);
	}

	public Map<String, Object> getNativeMap(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		
		return (Map) qrun.query(sql, new MapHandler(), params);
	}

	public String[] getArray(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		
		return (String[]) qrun.query(sql, new StringArrayHandler(), params);
	}

	public Object[] getNativeArray(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		
		return (Object[]) qrun.query(sql, new ArrayHandler(), params);
	}

	public <T> T getBean(String sql, Class<T> type, Object[] params) throws SQLException {
		debug(sql, params);
		
		return (T) qrun.query(sql, new BeanHandler(type, BEAN_ROW_PROCESSOR), params);
	}

	public List<Map<String, String>> getMapList(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) qrun.query(sql, new StringMapListHandler(), params);
	}

	public List<Map<String, Object>> getNativeMapList(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) qrun.query(sql, new MapListHandler(), params);
	}

	public List<String[]> getArrayList(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) qrun.query(sql, new StringArrayListHandler(), params);
	}

	public List<Object[]> getNativeArrayList(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) qrun.query(sql, new ArrayListHandler(), params);
	}

	public <T> List<T> getBeanList(String sql, Class<T> type, Object[] params) throws SQLException {
		debug(sql, params);
		return (List) qrun.query(sql, new BeanListHandler(type, BEAN_ROW_PROCESSOR), params);
	}

	public int execute(String sql, Object[] params) throws SQLException {
		debug(sql, params);
		return qrun.update(sql, params);
	}

	public ListPage<String[]> getArrayPage(String sql) throws SQLException {
		return getArrayPage(sql, 1, 20, null);
	}

	public ListPage<String[]> getArrayPage(String sql, int curPage) throws SQLException {
		return getArrayPage(sql, curPage, 20, null);
	}

	public ListPage<String[]> getArrayPage(String sql, int curPage, int pageSize) throws SQLException {
		return getArrayPage(sql, curPage, pageSize, null);
	}

	public ListPage<String[]> getArrayPage(String sql, int curPage, int pageSize, Object[] params) throws SQLException {
		ListPagedStatement pst = new ListPagedStatement(this.dataSource, this.dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(0);
	}

	public ListPage<Map<String, Object>> getNativeMapPage(String sql) throws SQLException {
		return getNativeMapPage(sql, 1, 20, null);
	}

	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage) throws SQLException {
		return getNativeMapPage(sql, curPage, 20, null);
	}

	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage, int pageSize) throws SQLException {
		return getNativeMapPage(sql, curPage, pageSize, null);
	}

	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage, int pageSize, Object[] params)
			throws SQLException {
		ListPagedStatement pst = new ListPagedStatement(this.dataSource, this.dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(2);
	}

	public <T> ListPage<T> getPage(String sql, Class<T> clazz) throws SQLException {
		return getPage(sql, clazz, 1, 20, null);
	}

	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage) throws SQLException {
		return getPage(sql, clazz, curPage, 20, null);
	}

	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage, int pageSize) throws SQLException {
		return getPage(sql, clazz, curPage, pageSize, null);
	}

	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage, int pageSize, Object[] params)
			throws SQLException {
		ListPagedStatement pst = new ListPagedStatement(this.dataSource, this.dialect, sql, curPage, pageSize, params);
		pst.setClazz(clazz);
		return pst.executeQuery(-1);
	}

	public ListPage<Map<String, String>> getMapPage(String sql) throws SQLException {
		return getMapPage(sql, 1, 20, null);
	}

	public ListPage<Map<String, String>> getMapPage(String sql, int curPage) throws SQLException {
		return getMapPage(sql, curPage, 20, null);
	}

	public ListPage<Map<String, String>> getMapPage(String sql, int curPage, int pageSize) throws SQLException {
		return getMapPage(sql, curPage, pageSize, null);
	}

	public ListPage<Map<String, String>> getMapPage(String sql, int curPage, int pageSize, Object[] params)
			throws SQLException {
		ListPagedStatement pst = new ListPagedStatement(this.dataSource, this.dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(1);
	}

	protected void debug(String sql, Object[] params) {
		if (true) {
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
			logger.info(sb.toString());
		}
	}
	
	public int update(String sql, List<Object> params) {
		
		return drun.update(getConn(), sql, params);
	}

	public int update(String sql, Object[] params) {
		
		return drun.update(getConn(), sql, params);
	}
	
	public <T> int insert(Class<T> cls, T bean) {
		
		return drun.insert(getConn(), cls, bean);
	}

	
	public <T> int[] insert(Class<T> cls, List<T> beans) {
		
		return drun.insert(getConn(), cls, beans);
	}

	public <T> int update(Class<T> cls, T bean) {
		
		return drun.update(getConn(), cls, bean);
	}
	
	public <T> int update(Class<T> cls, T bean, String primaryKey) {
		
		return drun.update(getConn(),cls, bean, primaryKey);
	}
	
	public <T> int[] update(Class<T> cls, List<T> beans) {
		
		return drun.update(getConn(),cls, beans);
	}
	
	public <T> int[] update(Class<T> cls, List<T> beans, String primaryKey) {
		
		return drun.update(getConn(),cls, beans, primaryKey);
	}
	
	public <T> int delete(Connection conn, Class<T> cls, long id) {
		
		return drun.delete(getConn(), cls, id);
	}
}

