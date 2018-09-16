package com.shoebox.android.repository;

public class RepositoryData<T> {

	public T data;
	public String error;

	public RepositoryData() {
	}

	public RepositoryData(T data) {
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
