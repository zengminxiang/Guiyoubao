package com.zmx.gyb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zmx.gyb.bean.NearbyUserPoJo;
import com.zmx.gyb.bean.Paging;
import com.zmx.gyb.bean.PersonalCenterPojo;
import com.zmx.gyb.bean.UserPojo;
import com.zmx.gyb.bean.VideoPojo;
import com.zmx.gyb.dao.UserPoJoDao;

import net.sf.json.JSONObject;

/**
 * 处理用户的
 * @author Administrator
 *
 */
public class UserServlet extends HttpServlet {

	private String TAG;// 标示
	private UserPoJoDao dao;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
				JSONObject jsonObject = new JSONObject();
				jsonObject.clear();
				
				dao = new UserPoJoDao();

				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("content-type", "text/html;charset=UTF-8");

				TAG = request.getParameter("tag");

				if (TAG != null) {
					
					//登录
					if(TAG.equals("login")){
						
						String name = request.getParameter("name");
						String paw = request.getParameter("pwd");
						
						System.out.println(name);
						System.out.println(paw);
												
						UserPojo user = dao.Login(name, paw);
						
						if(user != null){
							
							jsonObject.put("state", "200");
							jsonObject.put("user", user);
							
						}else {
							
							jsonObject.put("state", "400");
							jsonObject.put("message", "手机号或密码不对");
							
						}
						
						
					}
					
					//查询是否关注某个用户了
					else if(TAG.equals("SelectFollowUser")){
						
						String guid = request.getParameter("guid");
						String buid = request.getParameter("buid");

						System.out.println(guid);
						System.out.println(buid);
						
						int i = dao.SelectFollow(guid, buid);
						
						if(i >0){
							
							jsonObject.put("state", "200");
							jsonObject.put("message", "关注");

						}else{
							
							jsonObject.put("state", "400");
							jsonObject.put("message", "未关注");

						}
						
					}
					
					//关注某个用户
					else if(TAG.equals("AddFollowUser")){

						String guid = request.getParameter("guid");
						String buid = request.getParameter("buid");
						
						int i = dao.AddFollows(guid, buid);
						
						if(i >0){
							
							jsonObject.put("state", "200");
							jsonObject.put("message", "关注成功");

						}else{
							
							jsonObject.put("state", "400");
							jsonObject.put("message", "关注失败");

						}
						
					}
					
					//取消关注某个用户
					else if(TAG.equals("CancelFollowUser")){
						
						String guid = request.getParameter("guid");
						String buid = request.getParameter("buid");
						
						int i = dao.CancelFollows(guid, buid);
						
						if(i >0){
							
							jsonObject.put("state", "200");
							jsonObject.put("message", "取消成功");

						}else{
							
							jsonObject.put("state", "400");
							jsonObject.put("message", "取消失败");

						}
						
						
					}
					
					//查询用户的所有发表视频
					else if(TAG.equals("UserVideos")){
						
						String pageNow = request.getParameter("pagenow");
						String uid = request.getParameter("uid");

						Paging p = new Paging();
						p.setPageNow(Integer.parseInt(pageNow));
						List<VideoPojo> videos = dao.QueryAllVideo(p, uid);
						
						jsonObject.put("sum", p.getPageCount());
						jsonObject.put("videos", videos);
						
					}
					
					//查询某个用户的个人资料
					else if(TAG.equals("UserMessage")){
						
						String uid = request.getParameter("uid");
						
						UserPojo user = dao.SelectPersonal(uid);	
						
						PersonalCenterPojo pcp = new PersonalCenterPojo();
						pcp.setU_id(user.getU_id());
						pcp.setU_name(user.getU_name());
						pcp.setU_headurl(user.getU_headurl());
						pcp.setU_desc(user.getU_desc());
						pcp.setU_sex(user.getU_sex());
						pcp.setU_experience(user.getU_experience());
						pcp.setFans(dao.SelectUserLike(uid));
						pcp.setFollows(dao.SelectUserFollow(uid));
						
						jsonObject.put("state", "200");
						jsonObject.put("user", pcp);
						
					}
					
					//附近的人
					else if(TAG.equals("nearby")){
						
						String uid = request.getParameter("uid");
						String lrLatitude = request.getParameter("lrLatitude");
						String lrLongitube = request.getParameter("lrLongitube");
						String ulLatitude = request.getParameter("ulLatitude");
						String ulLongitube = request.getParameter("ulLongitube");
						
						List<NearbyUserPoJo> lists = dao.QueryNearbyUser(uid, lrLatitude, lrLongitube, ulLatitude, ulLongitube);
						
						jsonObject.put("state", "200");
						jsonObject.put("nearbyUser", lists);
						
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
