package com.task.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Numbers {
	
	@Id
	@GeneratedValue
	private long id;
	
	private long number;
	
	@Version
	private long version;

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}
}
