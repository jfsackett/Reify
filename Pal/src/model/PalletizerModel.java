package model;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.SwingPropertyChangeSupport;

import clojure.lang.PersistentVector;
import clojure.lang.RT;
import clojure.lang.Var;

@SuppressWarnings("rawtypes")
public class PalletizerModel {
	// Property change constants.
	public static final String PALLET = "PALLET";
	public static final String UNPACKED_ITEMS = "UNPACKED_ITEMS";
	
	/** Main pallet. */
	private Pallet pallet;
	
	/** Clojure pallet. */
	private Object palletClj;
	
	/** Unpacked items. */
	private List<Item> unpackedItems;

	/** Clojure unpacked items. */
	private Object unpackedItemsClj;
	
	/** Change event generator. */
    private SwingPropertyChangeSupport propChangeEventGen;

	public PalletizerModel() {
		propChangeEventGen = new SwingPropertyChangeSupport(this);
		
		try {
			RT.loadResourceScript("pal/core.clj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Build items in Clojure.
		unpackedItems = new ArrayList<Item>();
//		Var clojure = RT.var("pal.core", "build-items");
//		unpackedItemsClj = clojure.invoke(NUM_ITEMS);
		Var clojure = RT.var("pal.core", "get-unpacked-items");
		unpackedItemsClj = clojure.invoke();
		for (Object item : (PersistentVector) unpackedItemsClj) {
			unpackedItems.add(new ItemAdapter((Map) item));
		}

		// Build pallet in Clojure.
		clojure = RT.var("pal.core", "get-pallet");
//		clojure = RT.var("pal.core", "pallet-builder");
		palletClj = clojure.invoke();
		this.pallet = new PalletAdapter((Map) palletClj);
		
		// Pack an item into pallet.
//		clojure = RT.var("pal.core", "pack-item");
//		palletClj = clojure.invoke(palletClj, ((PersistentVector) unpackedItemsClj).get(0), 0, 0);
		
//		clojure = RT.var("pal.core", "pack-items");
//		palletClj = clojure.invoke();
//		this.pallet = new PalletAdapter((Map) palletClj);
		
//		clojure = RT.var("pal.core", "echo");
//		clojure.invoke(unpackedItemsClj);
	}

	public void newItems() {
		Var clojure = RT.var("pal.core", "reset-all");
		clojure.invoke();
		
		unpackedItems.clear();
		clojure = RT.var("pal.core", "get-unpacked-items");
		unpackedItemsClj = clojure.invoke();
		for (Object item : (PersistentVector) unpackedItemsClj) {
			unpackedItems.add(new ItemAdapter((Map) item));
		}
		
		clojure = RT.var("pal.core", "get-pallet");
		palletClj = clojure.invoke();
		this.pallet = new PalletAdapter((Map) palletClj);
		
//		propChangeEventGen.firePropertyChange(PALLET, priorComplexity, complexity);
	}
	
	public void packItems() {
		Var clojure = RT.var("pal.core", "pack-items");
		palletClj = clojure.invoke();
		this.pallet = new PalletAdapter((Map) palletClj);
		
		unpackedItems.clear();
		clojure = RT.var("pal.core", "get-unpacked-items");
		unpackedItemsClj = clojure.invoke();
		for (Object item : (PersistentVector) unpackedItemsClj) {
			unpackedItems.add(new ItemAdapter((Map) item));
		}
//		propChangeEventGen.firePropertyChange(PALLET, priorComplexity, complexity);
	}
	
	public Pallet getPallet() {
		return pallet;
	}

	public List<Item> getUnpackedItems() {
		return unpackedItems;
	}
	
	/** Adds a listener to model change events. */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeEventGen.addPropertyChangeListener(listener);
	}
}
