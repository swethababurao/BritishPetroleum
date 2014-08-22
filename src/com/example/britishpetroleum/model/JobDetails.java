package com.example.britishpetroleum.model;

public class JobDetails {
	String job_id, job_title, role_synopsis, key_accountabilities,
			essential_education, desirable_qualification, latitude,
			longitude, is_available;

	public JobDetails(String job_id, String job_title,
			String role_synopsis, String key_accountabilities,
			String essential_education, String desirable_qualification,
			String latitude, String longitude, String is_available) {

		this.job_id = job_id;
		this.job_title = job_title;
		this.role_synopsis = role_synopsis;
		this.key_accountabilities = key_accountabilities;
		this.essential_education = essential_education;
		this.desirable_qualification = desirable_qualification;
		this.latitude = latitude;
		this.longitude = longitude;
		this.is_available = is_available;
	}

	public String getJob_id() {
		return job_id;
	}

	public String getJob_title() {
		return job_title;
	}

	public String getRole_synopsis() {
		return role_synopsis;
	}

	public String getKey_accountabilities() {
		return key_accountabilities;
	}

	public String getEssential_education() {
		return essential_education;
	}

	public String getDesirable_qualification() {
		return desirable_qualification;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getIs_available() {
		return is_available;
	}

}
