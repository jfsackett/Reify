package com.sackett.reify.bp;

public interface BinPackingElement {
	/** Visitor accept method. */
	public void accept(BinPackingElementVisitor visitor);
}
