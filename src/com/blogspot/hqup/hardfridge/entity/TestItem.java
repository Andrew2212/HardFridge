package com.blogspot.hqup.hardfridge.entity;

public class TestItem {

	private String name = "TestItem";
	private String token = "token_test";
	private int age = 33;
	private int weight = 99;
	
	public TestItem() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "TestItem [name=" + name + ", token=" + token + ", age=" + age
				+ ", weight=" + weight + "]";
	}

}
