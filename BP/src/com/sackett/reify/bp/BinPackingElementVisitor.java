package com.sackett.reify.bp;

import com.sackett.reify.bp.Bin.SpaceMap;

public interface BinPackingElementVisitor {

	/** Visit a Bin. */
	public void visit(Bin bin);

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem);

	/** Visit an Item. */
	public void visit(Item item);
	
	/** Visit a SpaceMap. */
	public void visit(SpaceMap spaceMap);	
}
