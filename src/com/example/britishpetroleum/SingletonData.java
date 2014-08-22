package com.example.britishpetroleum;

import com.example.britishpetroleum.model.ServerResponse;

public class SingletonData {

	public static SingletonData singletonData;

	// public static Object mServerResponse;
	protected static ServerResponse mServerResponse;

	public static final SingletonData getInstance() {
		// TODO Auto-generated constructor stub

		if (singletonData == null) {
			singletonData = new SingletonData();

		}

		return singletonData;
	}

}
