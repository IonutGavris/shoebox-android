package com.shoebox.android.repository;

public class RepositoryData {

	public Object data;
	public String error;

	public RepositoryData() {
	}

	public RepositoryData(Object data) {
		this.data = data;
	}

	public RepositoryData(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "RepositoryData{" +
				"data=" + data +
				", error='" + error + '\'' +
				'}';
	}
}
