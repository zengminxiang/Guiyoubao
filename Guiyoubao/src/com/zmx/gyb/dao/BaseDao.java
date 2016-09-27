package com.zmx.gyb.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//DAO基类
public abstract class BaseDao {

	// 连接对象
	protected Connection conn = null;
	protected ResultSet res = null;
	protected PreparedStatement ps = null;

	// c3p0连接池工厂对�?
	public C3p0Connection factory = C3p0Connection.getInstance();

	// �?��连接
	public Connection DBConnect() {

		try {
			// 如果连接对象conn是空或�?连接对象是关闭的
			if (this.conn == null || conn.isClosed()) {

				// 到c3p0连接池取
				conn = factory.getConn();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}

	// 关闭连接
	public String DBclose() {

		try {
			// 如果数据库连接不为空且没有被关闭

			if (conn != null && !conn.isClosed()) {
				// 关闭连接
				conn.close();
				return "关闭成功";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "关闭失败";
	}

	// 执行前操�?
	public void DBPs(String sql, Object... ob) {

		// 建立连接
		this.DBConnect();

		try {

			ps = this.conn.prepareStatement(sql);

			// 判断参数是否为空
			if (ob != null) {

				for (int i = 0; i < ob.length; i++) {

					ps.setObject(i + 1, ob[i]);

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 执行前操�?
	public void DBPs1(String sql, Object... ob) {

		try {
			ps = this.conn.prepareStatement(sql);

			// 判断参数是否为空
			if (ob != null) {

				for (int i = 0; i < ob.length; i++) {

					ps.setObject(i + 1, ob[i]);

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 执行更新操作
	public int DBUpdate(String sql, Object... ob) {

		int i = 0;

		// 调用执行前方�?
		this.DBPs(sql, ob);

		try {

			i = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 关闭连接
		this.DBclose();
		return i;

	}

	// 执行更新操作1
	public int DBUpdate1() {

		int i = 0;

		try {

			i = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;

	}

	// 执行查询操作
	public ResultSet DBSelect(String sql, Object... ob) {

		// 调用执行前方�?
		this.DBPs(sql, ob);

		try {

			return res = ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 关闭连接
		this.DBclose();
		return res;

	}

	// 用户在DAO中自动封装pojo属�?值的方法
	public Object autoSetter(Object daoIpml, ResultSet rs) {

		// 通过反射取得实现Dao自己的名�?
		String daoName = daoIpml.getClass().getName();

		// 通过Dao名称取得pojo名称(命名规范)
		String pojoName = daoName.replaceFirst("dao", "bean")
				.replace("Dao", "");

		// 对应的pojo对象
		Object pojo = null;

		try {
			// Class clz =Class.forName(pojoName);
			pojo = Class.forName(pojoName).newInstance();

			// 去调查pojo的所有属�?
			Field fields[] = pojo.getClass().getDeclaredFields();

			// 通过属�?名称 去构造setter方法
			String setterName = null;

			for (Field f : fields) {

				// role_desc "set"+"R"+"ole_desc"
				setterName = "set" + f.getName().substring(0, 1).toUpperCase()
						+ f.getName().substring(1);

				// 找到相应的方�?
				Method setter = pojo.getClass().getDeclaredMethod(setterName,
						f.getType());

				// 动�?执行setter方法 /// getObject方法 忽略参数类型直接取得Object对象
				setter.invoke(pojo, rs.getObject(f.getName()));

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pojo;
	}

	// 查询返回�?��pojo的方�?
	public Object findOne(Object daoIpml, String sql, Object... params) {

		ResultSet rs = this.DBSelect(sql, params);

		Object obj = null;

		try {
			if (rs.next()) {

				obj = this.autoSetter(daoIpml, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 关闭
		this.DBclose();
		return obj;
	}

	// 返回�?��集合List
	public List findSome(Object daoIpml, String sql, Object... params) {

		ResultSet rs = this.DBSelect(sql, params);

		List list = new ArrayList();
		Object obj = null;

		try {
			while (rs.next()) {

				obj = this.autoSetter(daoIpml, rs);
				list.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
