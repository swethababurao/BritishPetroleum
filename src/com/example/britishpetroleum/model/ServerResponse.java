package com.example.britishpetroleum.model;

import java.util.ArrayList;

public class ServerResponse {

	boolean status;
	ArrayList<JobDetails> jobs = new ArrayList<JobDetails>();

	public boolean isStatus() {
		return status;
	}

	public ArrayList<JobDetails> getJobs() {
		return jobs;
	}

}
