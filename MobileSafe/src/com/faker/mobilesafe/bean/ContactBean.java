package com.faker.mobilesafe.bean;

public class ContactBean {

	private String name;
	private String phoneNumber;

	public ContactBean(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public ContactBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "ContactBean [name=" + name + ", phoneNumber=" + phoneNumber
				+ "]";
	}

}
