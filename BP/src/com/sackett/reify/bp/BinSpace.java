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
package com.sackett.reify.bp;

/** 
 * This represents bin space available to be packed. 
 * @author Joseph Sackett
 */
public class BinSpace {
	/** Item X offset location. */
	private int xOffset;
	
	/** Item Y offset location. */
	private int yOffset;

	/** Item length. */
	private int length;
	
	/** Item width. */
	private int width;

	/** Height of left wall with packed items. */
	private int leftWall;

	/** Height of right wall with packed items. */
	private int rightWall;

	public BinSpace() {
	}

	public BinSpace(int xOffset, int yOffset, int length, int width, int leftWall, int rightWall) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.length = length;
		this.width = width;
		this.leftWall = leftWall;
		this.rightWall = rightWall;
	}

	/**
	 * Determines whether a point inside this space.
	 * @param x x coordinate of point.
	 * @param y y coordinate of point.
	 * @return true if contained, false otherwise.
	 */
	public boolean contains(int x, int y) {
		return xOffset <= x && x <= xOffset + length && yOffset <= y && y <= yOffset + width;
	}
	
	/**
	 * Determines whether another BinSpace inside this space.
	 * @param binSpace to check.
	 * @return true if contained, false otherwise.
	 */
	public boolean contains(BinSpace binSpace) {
		return contains(binSpace.getxOffset(), binSpace.getyOffset()) && contains(binSpace.getxOffset() + binSpace.getLength(), binSpace.getyOffset() + binSpace.getWidth());
	}
	
	/**
	 * @return the xOffset
	 */
	public int getxOffset() {
		return xOffset;
	}

	/**
	 * @return the yOffset
	 */
	public int getyOffset() {
		return yOffset;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the leftWall
	 */
	public int getLeftWall() {
		return leftWall;
	}

	/**
	 * @return the rightWall
	 */
	public int getRightWall() {
		return rightWall;
	}

	/**
	 * @param leftWall the leftWall to set
	 */
	public void setLeftWall(int leftWall) {
		this.leftWall = leftWall;
	}

	/**
	 * @param rightWall the rightWall to set
	 */
	public void setRightWall(int rightWall) {
		this.rightWall = rightWall;
	}

	/**
	 * @return string representation of this BinSpace.
	 */
	@Override
	public String toString() {
		return "BinSpace [xOffset=" + xOffset + ", yOffset=" + yOffset
				+ ", length=" + length + ", width=" + width + ", leftWall="
				+ leftWall + ", rightWall=" + rightWall + "]";
	}
		
}
