package com.faker.mobilesafe.bean;

public class MsgRecordBean {

	private String number;
	private String content;
	private String time;

	public MsgRecordBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MsgRecordBean(String number, String content, String time) {
		super();
		this.number = number;
		this.content = content;
		this.time = time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "MsgRecordBean [number=" + number + ", address=" + content
				+ ", content=" + ", time=" + time + "]";
	}

}
