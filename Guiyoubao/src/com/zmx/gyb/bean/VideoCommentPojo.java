package com.zmx.gyb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 评论的pojo
 * @author Administrator
 *
 */
public class VideoCommentPojo implements Serializable{

	
	private String vc_id; //评论的id
	private String u_id;  //评论的用户id
	private String u_name; //评论的用户名
	private String u_head;//用户头像
	private String u_experience;//用户的经验值
	private String vc_content; //评论的内容
	private String vc_time;  //评论的时间
	
	private List<ReplyCommentPojo> replys;//回复的评论
	
	
	public String getVc_id() {
		return vc_id;
	}
	public void setVc_id(String vc_id) {
		this.vc_id = vc_id;
	}
	
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_experience() {
		return u_experience;
	}
	public void setU_experience(String u_experience) {
		this.u_experience = u_experience;
	}
	public String getVc_content() {
		return vc_content;
	}
	public void setVc_content(String vc_content) {
		this.vc_content = vc_content;
	}
	public String getVc_time() {
		return vc_time;
	}
	public void setVc_time(String vc_time) {
		this.vc_time = vc_time;
	}
	public String getU_head() {
		return u_head;
	}
	public void setU_head(String u_head) {
		this.u_head = u_head;
	}
	
	
	public List<ReplyCommentPojo> getReplys() {
		return replys;
	}
	public void setReplys(List<ReplyCommentPojo> replys) {
		this.replys = replys;
	}
	
	
	

}
