package com.zmx.gyb.bean;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 * 视频类
 */
public class VideoMessage implements Serializable{
	
	private String v_id;//视频id
	private String v_videourl;//视频路径
	private String v_videoimgurl;//视频第一帧图片
	private String v_time;//发表的时间
	private int v_browse_number;//浏览量
	private String uid;//发表用户的id
	private String v_content;//发表的内容
	private String v_addre;//发表的地址
	
	
	public String getV_id() {
		return v_id;
	}
	public void setV_id(String v_id) {
		this.v_id = v_id;
	}
	public String getV_videourl() {
		return v_videourl;
	}
	public void setV_videourl(String v_videourl) {
		this.v_videourl = v_videourl;
	}
	public String getV_videoimgurl() {
		return v_videoimgurl;
	}
	public void setV_videoimgurl(String v_videoimgurl) {
		this.v_videoimgurl = v_videoimgurl;
	}
	public String getV_time() {
		return v_time;
	}
	public void setV_time(String v_time) {
		this.v_time = v_time;
	}
	public int getV_browse_number() {
		return v_browse_number;
	}
	public void setV_browse_number(int v_browse_number) {
		this.v_browse_number = v_browse_number;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getV_content() {
		return v_content;
	}
	public void setV_content(String v_content) {
		this.v_content = v_content;
	}
	public String getV_addre() {
		return v_addre;
	}
	public void setV_addre(String v_addre) {
		this.v_addre = v_addre;
	}
	
	
	
	
	

}
