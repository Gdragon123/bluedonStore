package com.bluedon.common.pojo;

import java.io.Serializable;

/**
 * 数控件返回的pojo
 * @author Administrator
 *
 */
public class EasyUITreeNode implements Serializable{

	private Long id; //节点的id
	private String text; //url路径
	private String state; //节点的状态  open closed
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
	
}
