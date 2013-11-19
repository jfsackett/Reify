package com.sackett.reify.bp;

public interface BinPackingElementVisitor {

	/** Visit a Bin. */
	public void visit(Bin bin);

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem);

	/** Visit an Item. */
	public void visit(Item item);
}
