package com.faker.mobilesafe.bean;

public class CallRecordBean {

	private String id;
	private String number;
	private String address;
	private String type;
	private String time;

	@Override
	public String toString() {
		return "CallRecordBean [id=" + id + ", number=" + number + ", name="
				+ address + ", type=" + type + ", time=" + time + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String name) {
		this.address = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
