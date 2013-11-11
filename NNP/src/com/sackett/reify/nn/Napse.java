/*
    Adaptive Neural Network
    Copyright (C) 2013  Sackett Inc.

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
package com.sackett.reify.nn;

import java.text.DecimalFormat;

/** 
 * This represents a synapse between two nodes in neural network.
 * @author Joseph Sackett
 */
public class Napse {
	/** Inbound node. */
	private Node inNode;
	
	/** Outbound node. */
	private Node outNode;
	
	/** Connection Weight. */
	private double weight;

	/**
	 * @param inNode Input node.
	 * @param outNode Output node.
	 */
	public Napse(Node inNode, Node outNode, double weight) {
		this.inNode = inNode;
		this.outNode = outNode;
		this.weight = weight;
	}

	/**
	 * @return the inNode
	 */
	public Node getInNode() {
		return inNode;
	}

	/**
	 * @return the outNode
	 */
	public Node getOutNode() {
		return outNode;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * @param inNode the inNode to set
	 */
	void setInNode(Node inNode) {
		this.inNode = inNode;
	}

	/**
	 * @param outNode the outNode to set
	 */
	void setOutNode(Node outNode) {
		this.outNode = outNode;
	}

	/**
	 * @param weight the weight to set
	 */
	void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Updates weights based on upstream value & dowstream error, factored by eta & momentum.
	 * @param eta adjustment coefficient.
	 * @param momentum adjustment factor to prior update.
	 */
	public void updateWeight(double eta, double momentum) {
		weight = weight + eta * inNode.getOutput() * outNode.getError();
	}

	/**
	 * Clone this exactly, including former Nodes.
	 * @return napse clone.
	 */
	@Override
	public Napse clone() throws CloneNotSupportedException {
		return new Napse(inNode, outNode, weight);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inNode == null) ? 0 : ((Double)inNode.getId()).hashCode());
		result = prime * result + ((outNode == null) ? 0 : ((Double)outNode.getId()).hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Napse other = (Napse) obj;
		if (inNode == null) {
			if (other.inNode != null)
				return false;
		} else if (!inNode.equals(other.inNode))
			return false;
		if (outNode == null) {
			if (other.outNode != null)
				return false;
		} else if (!outNode.equals(other.outNode))
			return false;
		return true;
	}

	private static DecimalFormat decFormat = new DecimalFormat("#.######");

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + decFormat.format(weight) + " -> " + decFormat.format(outNode.getId()) + "]";
	}

}
