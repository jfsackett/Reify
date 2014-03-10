/*
    Bin Packing System
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


/** Interface for visitor to bin packing elements. Part of the visitor pattern. */
public interface ElementVisitor {

	/** Visit a Bin. */
	public void visit(PalletAdapter bin);

	/** Visit a Packed Item. */
//	public void visit(PackedItem packedItem);
//
//	/** Visit an Item. */
//	public void visit(Item item);
//	
//	/** Visit a SpaceMap. */
//	public void visit(SpaceMap spaceMap);	
}
