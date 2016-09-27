package com.zmx.gyb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.zmx.gyb.bean.Paging;
import com.zmx.gyb.bean.ReplyCommentPojo;
import com.zmx.gyb.bean.VideoCommentPojo;
import com.zmx.gyb.bean.VideoLikePojo;
import com.zmx.gyb.bean.VideoPojo;
import com.zmx.gyb.bean.VideoMessage;
import com.zmx.gyb.servlet.ViedoServlet;

/**
 * 
 * @author Administrator 视频处理数据库的dao
 */
public class VideoPojoDao extends BaseDao {

	/**
	 * 发表视频
	 */
	public int PublicVideo(VideoMessage video) {

		String sql = "insert into z_video(v_id,v_videourl,v_videoimgurl,v_time,v_browse_number,uid,v_content,v_addre) values (?,?,?,?,?,?,?,?)";

		return DBUpdate(sql, video.getV_id(), video.getV_videourl(),
				video.getV_videoimgurl(), video.getV_time(),
				video.getV_browse_number(), video.getUid(),
				video.getV_content(), video.getV_addre());

	}
	
	/**
	 * 分页查询视频列表
	 * @param p
	 */
	public List<VideoPojo> QueryAllVideo(Paging p){
		
		List<VideoPojo> jsons = new ArrayList<VideoPojo>();
		

		// 查询多少条记录
		String sqlA = "SELECT COUNT(*) from z_video";

		int i = 0;

		ResultSet resA = this.DBSelect(sqlA);

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
		
		
		String sql = "select u.u_name,u.u_headurl,v.* from z_user u, z_video v WHERE u.u_id = v.uid ORDER BY v.v_time DESC limit ?,?";

		ResultSet res = this.DBSelect(sql,p.getPageitem(), p.getPageSize());

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
	
	
	/**
	 * 添加评论
	 * @param vid  视频的id
	 * @param uid  用户的id
	 * @param context  评论的内容
	 * @return
	 */
	public int addComments(String vid, String uid, String context) {

		String sql = "insert into z_video_comment(v_id,u_id,vc_content,vc_time) values (?,?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return DBUpdate(sql, vid, uid, context, format.format(new Date()));

	}
	
	
	/**
	 * 回复评论
	 * @param vcid 子评论的id
	 * @param huid 回复的用户
	 * @param buname 被回复的用户名称
	 * @param vr_context 回复的内容
	 * @return
	 */
	public int AddcommentComment(String vid,String vcid,String huid,String buname,String vr_context){

		String sql = "insert into z_video_reply(v_id,vc_id,hu_id,bu_name,vr_content,vr_time) values (?,?,?,?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return DBUpdate(sql,vid, vcid, huid, buname,vr_context, format.format(new Date()));
		
	}
	
	/**
	 * 查询用户最新插入的那条评论的id
	 * 
	 * @param uid
	 *            用户的id
	 * @return
	 */
	public VideoCommentPojo selectOneComment(String uid) {
		

		VideoCommentPojo pojo = new VideoCommentPojo();

		String sql = "SELECT b.vc_id,a.u_id,a.u_name,a.u_headurl,a.u_experience,b.vc_content,b.vc_time FROM z_user a , z_video_comment b WHERE b.u_id = ? and a.u_id = b.u_id ORDER BY vc_time DESC LIMIT 1";

		ResultSet res = this.DBSelect(sql, uid);

		try {

			while (res.next()) {

				pojo.setVc_id(res.getString(1));
				pojo.setU_id(res.getString(2));
				pojo.setU_name(res.getString(3));
				pojo.setU_head(res.getString(4));
				pojo.setU_experience(res.getString(5));
				pojo.setVc_content(res.getString(6));
				pojo.setVc_time(res.getString(7));
				List<ReplyCommentPojo> ReplyLists = QueryVideoReplyComment(res.getString(1));
				pojo.setReplys(ReplyLists);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return pojo;

	}
	
	/**
	 * 查询某条评论
	 * @param vcid 评论的id
	 */
	public VideoCommentPojo QueryViedoOneComment(String vcid){
		
		VideoCommentPojo pojo = new VideoCommentPojo();
		
		String sql = "SELECT b.vc_id,a.u_id,a.u_name,a.u_headurl,a.u_experience,b.vc_content,b.vc_time FROM z_user a , z_video_comment b WHERE b.vc_id = ? and b.u_id=a.u_id";
		
		ResultSet res = this.DBSelect(sql, vcid);

		try {

			while (res.next()) {

				pojo.setVc_id(res.getString(1));
				pojo.setU_id(res.getString(2));
				pojo.setU_name(res.getString(3));
				pojo.setU_head(res.getString(4));
				pojo.setU_experience(res.getString(5));
				pojo.setVc_content(res.getString(6));
				pojo.setVc_time(res.getString(7));
				List<ReplyCommentPojo> ReplyLists = QueryVideoReplyComment(res.getString(1));
				pojo.setReplys(ReplyLists);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return pojo;

	}

	
	
	/**
	 * 分页查询某个视频下的评论
	 * @param vid  视频的id
	 * @return
	 */
	public List<VideoCommentPojo> QueryVideoComment(Paging p,String vid){
		
		// 查询多少条记录
				String sqlA = "SELECT COUNT(*) from z_video_comment WHERE v_id = ?";

				int i = 0;

				ResultSet resA = this.DBSelect(sqlA,vid);

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
		
		
		List<VideoCommentPojo> lists = new ArrayList<VideoCommentPojo>();
		
		//ORDER BY b.vc_time DESC 
		String sql = "SELECT b.vc_id,a.u_id,a.u_name,a.u_headurl,a.u_experience,b.vc_content,b.vc_time FROM z_user a , z_video_comment b WHERE b.v_id = ? and a.u_id = b.u_id limit ?,?";
		
		ResultSet res = this.DBSelect(sql, vid,p.getPageitem(), p.getPageSize());

		try {

			while (res.next()) {

				VideoCommentPojo pojo = new VideoCommentPojo();
				pojo.setVc_id(res.getString(1));
				pojo.setU_id(res.getString(2));
				pojo.setU_name(res.getString(3));
				pojo.setU_head(res.getString(4));
				pojo.setU_experience(res.getString(5));
				pojo.setVc_content(res.getString(6));
				pojo.setVc_time(res.getString(7));
				
				List<ReplyCommentPojo> ReplyLists = QueryVideoReplyComment(res.getString(1));
				pojo.setReplys(ReplyLists);
								
				lists.add(pojo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		return lists;
		
	}
	
	
	
	/**
	 * 查询回复的评论
	 * @param vcid 主评论id
	 * @return
	 */
  public List<ReplyCommentPojo> QueryVideoReplyComment(String vcid){
	  
	  List<ReplyCommentPojo> lists = new ArrayList<ReplyCommentPojo>();
	  
	  String sql="SELECT r.*,u.u_name from z_user u,z_video_reply r WHERE r.vc_id = ? and r.hu_id=u.u_id";

		ResultSet res = this.DBSelect(sql, vcid);

		try {

			while (res.next()) {
				
				ReplyCommentPojo reply = new ReplyCommentPojo();
				reply.setVr_id(res.getString(1));
				reply.setV_id(res.getString(2));
				reply.setVc_id(res.getString(3));
				reply.setHu_id(res.getString(4));
				reply.setBu_name(res.getString(5));
				reply.setVr_content(res.getString(6));
				reply.setVr_time(res.getString(7));
				reply.setHu_name(res.getString(8));
				lists.add(reply);
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
	return lists;
	  
  }
	
  /**
   * 点赞
   * @param vid
   * @param uid
   * @return
   */
	public int ClickLike(String vid,String uid){
		
		String sql = "insert into z_video_like(v_id,u_id,vl_time) values (?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return DBUpdate(sql, vid, uid, format.format(new Date()));
		
	}
	
	/**
	 * 查询是否已经点赞了
	 * @param vid
	 * @param uid
	 * @return
	 */
	public int WhetherLike(String vid,String uid){
		
		String sql = "SELECT * FROM z_video_like WHERE v_id = ? and u_id = ?";
		ResultSet res = this.DBSelect(sql, vid,uid);
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
	 * 取消点赞
	 * @param vid
	 * @param uid
	 * @return
	 */
	public int CancelLike(String vid,String uid){
		
		String sql = "DELETE FROM z_video_like WHERE v_id = ? and u_id = ?";
		
		return DBUpdate(sql,vid,uid);
		
	}
	
	
	/**
	 * 分页查询点赞列表
	 * @param vid 视频id
	 * @return
	 */
	public List<VideoLikePojo> QueryLike(Paging p,String vid){
		
		// 查询多少条记录
		String sqlA = "SELECT COUNT(*) from z_video_like WHERE v_id = ?";

		int i = 0;

		ResultSet resA = this.DBSelect(sqlA,vid);

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

		
		List<VideoLikePojo> lists = new ArrayList<VideoLikePojo>();
		
		String sql="SELECT l.*,u.u_headurl from z_user u,z_video_like l WHERE v_id = ? AND l.u_id = u.u_id limit ?,?";
		
		ResultSet res = this.DBSelect(sql,vid,p.getPageitem(), p.getPageSize());

		try {

			while (res.next()) {
				
				VideoLikePojo like = new VideoLikePojo();
				like.setVl_id(res.getString(1));
				like.setV_id(res.getString(2));
				like.setU_id(res.getString(3));
				like.setVl_time(res.getString(4));
				like.setU_headurl(res.getString(5));
				lists.add(like);
							
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	  
		
		return lists;
		
	}
	
//	SELECT * FROM z_city c WHERE ulLongitube < c.c_longitude and c.c_longitude < lrLongitube and ulLatitude >  c.c_latitude and c.c_latitude > lrLatitude

	

}
