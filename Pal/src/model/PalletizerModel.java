package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import clojure.lang.PersistentVector;
import clojure.lang.RT;
import clojure.lang.Var;

@SuppressWarnings("rawtypes")
public class PalletizerModel {
	/** Number of items to palletize. */
	private static final int NUM_ITEMS = 8;
	
	/** Main pallet. */
	private Pallet pallet;
	
	/** Clojure pallet. */
	private Object palletClj;
	
	/** Unpacked items. */
	private List<Item> unpackedItems;

	/** Clojure unpacked items. */
	private Object unpackedItemsClj;
	
	public PalletizerModel() {
		try {
			RT.loadResourceScript("pal/core.clj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Build items in Clojure.
		unpackedItems = new ArrayList<Item>();
		Var clojure = RT.var("pal.core", "build-items");
		unpackedItemsClj = clojure.invoke(NUM_ITEMS);
		for (Object item : (PersistentVector) unpackedItemsClj) {
			unpackedItems.add(new ItemAdapter((Map) item));
		}

		// Build pallet in Clojure.
		clojure = RT.var("pal.core", "pallet-builder");
		palletClj = clojure.invoke();
		
		// Pack an item into pallet.
		clojure = RT.var("pal.core", "pack-item");
		palletClj = clojure.invoke(palletClj, ((PersistentVector) unpackedItemsClj).get(0), 0, 0);
		this.pallet = new PalletAdapter((Map) palletClj);
		
		clojure = RT.var("pal.core", "echo");
		clojure.invoke(unpackedItemsClj);
	}

	public Pallet getPallet() {
		return pallet;
	}

	public List<Item> getUnpackedItems() {
		return unpackedItems;
	}
	
}
