package model;

import java.io.IOException;
import java.util.Map;

import clojure.lang.RT;
import clojure.lang.Var;

public class PalletizerModel {

	public PalletizerModel() {
		try {
			RT.loadResourceScript("pal/core.clj");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public PalletAdapter getPalletAdapter() {
		Var pal = RT.var("pal.core", "item-builder");
		return new PalletAdapter((Map) pal.invoke());
	}

	
}
