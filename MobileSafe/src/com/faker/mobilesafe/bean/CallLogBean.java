package com.faker.mobilesafe.bean;

public class CallLogBean {

	private String name;
	private String time;
	private String address;
	private String number;

	public CallLogBean(String name, String time, String address, String number) {
		super();
		this.name = name;
		this.time = time;
		this.address = address;
		this.number = number;
	}

	public CallLogBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "CallLogBean [name=" + name + ", time=" + time + ", address="
				+ address + ", number=" + number + "]";
	}

}
