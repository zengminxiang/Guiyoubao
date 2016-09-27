package com.zmx.gyb.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//数据库连接池的工�?
public class C3p0Connection {

	// 配置文件的属�?
	private static String DBdriver; // 加载驱动
	private static String DBurl; // connection连接（ip地址、端口�?数据库名�?
	private static String DBuser; // 用户�?
	private static String DBped; // 密码

	// 加载驱动 只要加载�?�� 读取属�?配置文件
	static {

		Properties prop = new Properties();

		try {
			prop.load(new InputStreamReader(BaseDao.class.getClassLoader()
					.getResourceAsStream("zeng.properties"), "UTF-8"));

			// 读取配置信息
			DBdriver = prop.getProperty("DBdriver");
			DBurl = prop.getProperty("DBurl");
			DBuser = prop.getProperty("DBuser");
			DBped = prop.getProperty("DBped");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 连接�?
	com.mchange.v2.c3p0.ComboPooledDataSource ds = null;

	private static C3p0Connection factory = new C3p0Connection();

	// 构�?器私有化
	private C3p0Connection() {
		// 初始化数据源
		ds = new ComboPooledDataSource();

		try {
			// 配置数据库的基本信息
			ds.setDriverClass(DBdriver);
			ds.setJdbcUrl(DBurl);
			ds.setUser(DBuser);
			ds.setPassword(DBped);

			// 设置连接池的信息
			ds.setInitialPoolSize(50);// 初始化大�?
			ds.setMaxPoolSize(200); // �?��的连接数
			ds.setMinPoolSize(20);// �?���?
			ds.setMaxIdleTime(60);// 连接池连接超时时�?
			ds.setMaxStatements(500); // �?��的状态数

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	public static C3p0Connection getInstance() {

		return factory;
	}

	// 取得连接
	public Connection getConn() {

		try {
			return this.ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
