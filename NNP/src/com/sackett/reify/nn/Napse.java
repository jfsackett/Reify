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
	 * Updates weights based on upstream value & dowstream error, factored by eta & momentum.
	 * @param eta adjustment coefficient.
	 * @param momentum adjustment factor to prior update.
	 */
	public void updateWeight(double eta, double momentum) {
		weight = weight + eta * inNode.getOutput() * outNode.getError();
	}

}
