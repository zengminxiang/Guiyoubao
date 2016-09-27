package com.zmx.gyb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.zmx.gyb.bean.Paging;
import com.zmx.gyb.bean.VideoCommentPojo;
import com.zmx.gyb.bean.VideoLikePojo;
import com.zmx.gyb.bean.VideoPojo;
import com.zmx.gyb.bean.VideoMessage;
import com.zmx.gyb.dao.VideoPojoDao;

/**
 * 
 * @author Administrator 视频模块
 * 
 */
public class ViedoServlet extends HttpServlet {

	private VideoMessage videomessage;// 发表视频说说的数据
	private VideoPojoDao dao = new VideoPojoDao();// 处理数据库的dao
	private String TAG;// 标示

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// json
		JSONObject jsonObject = new JSONObject();
		jsonObject.clear();

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");

		TAG = request.getParameter("tag");

		if (TAG != null) {

			// 发表
			if (TAG.equals("publish")) {

				videomessage = new VideoMessage();
				videomessage.setUid(request.getParameter("uid"));
				videomessage.setV_addre(request.getParameter("addre"));
				videomessage.setV_browse_number(0);
				videomessage.setV_content(request.getParameter("content"));

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String time = format.format(new Date());
				videomessage.setV_time(time);

				videomessage.setV_videoimgurl(request
						.getParameter("videoimgurl"));
				videomessage.setV_videourl(request.getParameter("videourl"));

				int i = dao.PublicVideo(videomessage);

				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("message", "发送成功");

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("message", "发送失败");

				}

			}
			// 查询全部视频列表
			else if (TAG.equals("queryAll")) {

				List<VideoPojo> lists;
				String pageNow = request.getParameter("pagenow");

				Paging p = new Paging();
				p.setPageNow(Integer.parseInt(pageNow));

				lists = dao.QueryAllVideo(p);

				jsonObject.put("sum", p.getPageCount());
				jsonObject.put("video", lists);

			}

			// 添加评论
			else if (TAG.equals("AddComment")) {

				// 评论
				String uid = request.getParameter("uid");
				String vid = request.getParameter("vid");
				String context = request.getParameter("comment");

				int i = dao.addComments(vid, uid, context);

				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("comment", dao.selectOneComment(uid));

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("value", "评论失败");

				}

			}
			
			//回复评论
			else if(TAG.equals("ReplyComment")){

				String vcid = request.getParameter("vcid"); //主评论id
				String huid = request.getParameter("huid"); //回复用户的id
				String buname = request.getParameter("buname"); //被回复的用户名
				String vr_content = request.getParameter("vrcontent"); //回复的内容
				String vid = request.getParameter("vid");//视频的id
				
				int i = dao.AddcommentComment(vid,vcid, huid, buname, vr_content);
				
				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("value", "回复成功");

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("value", "回复失败");

				}
				
			}

			// 查询评论
			else if (TAG.equals("QueryComment")) {

				String vid = request.getParameter("vid");
				String pageNow = request.getParameter("pagenow");

				Paging p = new Paging();
				p.setPageSize(10);
				p.setPageNow(Integer.parseInt(pageNow));
				
				List<VideoCommentPojo> lists = dao.QueryVideoComment(p,vid);

				jsonObject.put("sum", p.getPageCount());
				jsonObject.put("comments", lists);

			}
			
			//查询某条评论
			else if(TAG.equals("QueryOneComment")){
				
				String vcid = request.getParameter("vcid");
				
				jsonObject.put("state", "200");
				jsonObject.put("comment", dao.QueryViedoOneComment(vcid));

				
			}
			
			//点赞视频
			else if(TAG.equals("like")){
				
				String vid = request.getParameter("vid");
				String uid = request.getParameter("uid");
				
				int i = dao.ClickLike(vid, uid);
				
				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("value", "点赞成功");

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("value", "点赞失败");

				}
				
				
			}
			
			//查询是否已经点赞了
			else if(TAG.equals("WhetherLike")){
				
				String vid = request.getParameter("vid");
				String uid = request.getParameter("uid");
				
				int i = dao.WhetherLike(vid, uid);
				
				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("value", "true");

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("value", "false");

				}
				
			}
			
			//取消点赞
			else if(TAG.equals("CancelLike")){
				
				String vid = request.getParameter("vid");
				String uid = request.getParameter("uid");
				
				int i = dao.CancelLike(vid, uid);
				if (i > 0) {

					jsonObject.put("state", "200");
					jsonObject.put("value", "取消成功");

				} else {

					jsonObject.put("state", "400");
					jsonObject.put("value", "取消失败");

				}
				
				
				
			}
			
			//查询点赞列表
			else if(TAG.equals("SelectLike")){
				
				String vid = request.getParameter("vid");
				String pageNow = request.getParameter("pagenow");

				Paging p = new Paging();
				p.setPageSize(8);
				
				System.out.println(pageNow+"sssss");
				
				p.setPageNow(Integer.parseInt(pageNow));
				
				List<VideoLikePojo> lists = dao.QueryLike(p, vid);
				
				jsonObject.put("sum", p.getPageCount());
				jsonObject.put("likes", lists);
				
			}
			
			// 输出
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toString());
			out.flush();
			out.close();

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);

	}

}
