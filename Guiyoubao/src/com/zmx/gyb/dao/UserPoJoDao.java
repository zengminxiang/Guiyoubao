package com.zmx.gyb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zmx.gyb.bean.NearbyUserPoJo;
import com.zmx.gyb.bean.Paging;
import com.zmx.gyb.bean.UserPojo;
import com.zmx.gyb.bean.VideoLikePojo;
import com.zmx.gyb.bean.VideoPojo;

/**
 * 处理用户的dao
 * 
 * @author Administrator
 * 
 */
public class UserPoJoDao extends BaseDao {

	
	/**
	 * 登录
	 * @param name 手机号
	 * @param paw 密码
	 * @return
	 */
	public UserPojo Login(String name, String paw) {

		UserPojo u = null;

		String sql = "select * from z_user where u_phone=? and u_pwd=?";

		ResultSet res = this.DBSelect(sql, name, paw);

		try {

			if (res.next()) {

				u = new UserPojo();
				u.setU_id(res.getString(1));
				u.setU_phone(res.getString(2));
				u.setU_pwd(res.getString(3));
				u.setU_name(res.getString(4));
				u.setU_headurl(res.getString(5));
				u.setU_desc(res.getString(6));
				u.setU_sex(res.getString(7));
				u.setU_time(res.getString(8));
				u.setU_number(res.getString(9));
				u.setU_experience(res.getString(10));

			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}

		return u;

	}

	/**
	 * 查询附近的用户
	 * 
	 * @param uid
	 *            查询用户的id可为空
	 * @param lrLatitude
	 * @param lrLongitube
	 * @param ulLatitude
	 * @param ulLongitube
	 * @return
	 */
	public List<NearbyUserPoJo> QueryNearbyUser(String uid, String lrLatitude,
			String lrLongitube, String ulLatitude, String ulLongitube) {

		List<NearbyUserPoJo> lists = new ArrayList<NearbyUserPoJo>();

		String sql;
		ResultSet res;

		if (uid == "0" || uid.equals("0") || uid == null) {

			sql = "SELECT u.u_id,u.u_headurl,c.c_longitude,c.c_latitude FROM z_user u,z_city c WHERE u.u_id = c.u_id and ? < c.c_longitude and c.c_longitude < ? and ? >  c.c_latitude and c.c_latitude > ?";
			res = this.DBSelect(sql, ulLongitube, lrLongitube, ulLatitude,
					lrLatitude);

		} else {

			sql = "SELECT u.u_id,u.u_headurl,c.c_longitude,c.c_latitude FROM z_user u,z_city c WHERE u.u_id = c.u_id and c.u_id != ? and ? < c.c_longitude and c.c_longitude < ? and ? >  c.c_latitude and c.c_latitude > ?";
			res = this.DBSelect(sql, uid, ulLongitube, lrLongitube, ulLatitude,
					lrLatitude);

		}

		try {

			while (res.next()) {

				NearbyUserPoJo usr = new NearbyUserPoJo();
				usr.setU_id(res.getString(1));
				usr.setU_headurl(res.getString(2));
				usr.setC_longitude(res.getString(3));
				usr.setC_latitude(res.getString(4));

				lists.add(usr);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;

	}
	
	/**
	 * 查询用户资料
	 * @param uid
	 */
	public UserPojo SelectPersonal(String uid){
		
		UserPojo u = null;

		String sql = "select * from z_user where u_id=?";

		ResultSet res = this.DBSelect(sql, uid);

		try {

			if (res.next()) {

				u = new UserPojo();
				u.setU_id(res.getString(1));
				u.setU_phone(res.getString(2));
				u.setU_pwd(res.getString(3));
				u.setU_name(res.getString(4));
				u.setU_headurl(res.getString(5));
				u.setU_desc(res.getString(6));
				u.setU_sex(res.getString(7));
				u.setU_time(res.getString(8));
				u.setU_number(res.getString(9));
				u.setU_experience(res.getString(10));

			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}

		return u;
	}
	
	
	/**
	 * 查询粉丝
	 * @param uid
	 * @return
	 */
	public int SelectUserLike(String uid){
		
		String sql = "SELECT COUNT(*) from z_follow WHERE bu_id = ?";
		
		int i = 0;

		ResultSet resA = this.DBSelect(sql,uid);

		try {

			if (resA.next()) {

				i = resA.getInt(1);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				resA.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return i;
		
	}
	
	/**
	 * 查询关注的数量
	 * @param uid
	 * @return
	 */
	public int SelectUserFollow(String uid){

		String sql = "SELECT COUNT(*) from z_follow WHERE gu_id = ?";
		
		int i = 0;

		ResultSet resA = this.DBSelect(sql,uid);

		try {

			if (resA.next()) {

				i = resA.getInt(1);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				resA.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return i;
		
	}
	
	/**
	 * 查询是否关注某个用户了
	 * @param guid
	 * @param buid
	 * @return
	 * 
	 */
	public int SelectFollow(String guid,String buid){
		
		String sql = "SELECT * FROM z_follow WHERE gu_id = ? and bu_id = ?";
		ResultSet res = this.DBSelect(sql, guid,buid);
		int i = 0;
		
		try {
			
			while (res.next()) {
				
				i = 1;
								
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return i;
		
	}
	
	
	/**
	 * 加关注
	 * @param guid
	 * @param buid
	 */
	public int AddFollows(String guid,String buid){
		
		String sql = "insert into z_follow(gu_id,bu_id,f_time) values (?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return DBUpdate(sql, guid, buid,format.format(new Date()));
		
	}
	
	
	
	/**
	 * 取消关注
	 * @param guid
	 * @param buid
	 * @return
	 */
	public int CancelFollows(String guid,String buid){
		
	String sql = "DELETE FROM z_follow WHERE gu_id = ? and bu_id = ?";
		
		return DBUpdate(sql,guid,buid);
		
	}
	
	/**
	 * 分页查询视频列表
	 * @param p
	 */
	public List<VideoPojo> QueryAllVideo(Paging p,String uid){
		
		List<VideoPojo> jsons = new ArrayList<VideoPojo>();
		

		// 查询多少条记录
		String sqlA = "SELECT COUNT(*) from z_video WHERE uid = ?";

		int i = 0;

		ResultSet resA = this.DBSelect(sqlA,uid);

		try {

			if (resA.next()) {

				i = resA.getInt(1);

			}

			p.setRowCount(i);// 设置总页数

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				resA.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		String sql = "select u.u_name,u.u_headurl,v.* from z_user u, z_video v WHERE u.u_id = v.uid and v.uid = ? ORDER BY v.v_time DESC limit ?,?";

		ResultSet res = this.DBSelect(sql,uid,p.getPageitem(), p.getPageSize());

		try {

			while (res.next()) {
				
				VideoPojo video = new VideoPojo();
				video.setU_name(res.getString(1));
				video.setU_headurl(res.getString(2));
				video.setV_id(res.getInt(3));
				video.setV_videourl(res.getString(4));
				video.setV_videoimgurl(res.getString(5));
				video.setV_time(res.getString(6));
				video.setV_browse_number(res.getInt(7));
				video.setUid(res.getInt(8));
				video.setV_content(res.getString(9));
				video.setV_addre(res.getString(10));
				video.setCount_comment(SelectCountComment(res.getInt(3)+"")+"");
				video.setCount_like(SelectLike(res.getInt(3)+"")+"");
				
				jsons.add(video);
			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return jsons;
		
	}
	
	/**
	 * 查询视频有多少个评论
	 * @param vid
	 * @return
	 */
	public int SelectCountComment(String vid){
		
		int counts = 0;//评论总数
		String sql = "select (select count(*) from z_video_comment where v_id = ?) + (select count(*) from z_video_reply where v_id = ?)";
		ResultSet res = this.DBSelect(sql,vid,vid);
		try {

			if (res.next()) {

				counts = res.getInt(1);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return counts;
	}
	
	/**
	 * 查询视频点赞书
	 * @param vid
	 * @return
	 */
	public int SelectLike(String vid){
		
		String sql = "SELECT COUNT(*) from z_video_like WHERE v_id = ?";

		int counts = 0;

		ResultSet res = this.DBSelect(sql,vid);

		try {

			if (res.next()) {

				counts = res.getInt(1);

			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return counts;
		
	}
	
	

}
