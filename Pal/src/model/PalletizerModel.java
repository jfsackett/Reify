/*
    Palletizer
    Copyright (C) 2014  Sackett Inc.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import clojure.lang.PersistentVector;
import clojure.lang.RT;
import clojure.lang.Var;

/** Main Palletizer application model. */
@SuppressWarnings("rawtypes")
public class PalletizerModel {
	
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
		Var clojure = RT.var("pal.core", "get-unpacked-items");
		unpackedItemsClj = clojure.invoke();
		for (Object item : (PersistentVector) unpackedItemsClj) {
			unpackedItems.add(new ItemAdapter((Map) item));
		}

		// Build pallet in Clojure.
		clojure = RT.var("pal.core", "get-pallet");
		palletClj = clojure.invoke();
		this.pallet = new PalletAdapter((Map) palletClj);
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
	}
	
	public void shuffleItems() {
		Var clojure = RT.var("pal.core", "shuffle-items");
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
	}
	
	public Pallet getPallet() {
		return pallet;
	}

	public List<Item> getUnpackedItems() {
		return unpackedItems;
	}
}
