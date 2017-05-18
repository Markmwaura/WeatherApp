package com.markmwaura.weatherapp.Utils;

import java.util.ArrayList;

;

public class DummyContent {

	public static ArrayList<DummyModel> getDummyModelList() {
		ArrayList<DummyModel> list = new ArrayList<>();

		list.add(new DummyModel(0, "", "", 0));

		return list;
	}
}
