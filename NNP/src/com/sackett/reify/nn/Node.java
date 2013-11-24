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

import java.util.List;


/** 
 * This represents a node in a neural network. 
 * @author Joseph Sackett
 */
public abstract class Node {
	/** Unique Id for this node. */
	private double id;
	
	/** Node output value. */
	protected double output = 0.0d;
	
	/** Bias node flag. Indicates this is a bias node. */
	private boolean bias = false;
	
	/** Node error. */
	protected double error = 0.0d;
	
	/** Default constructor. */
	public Node() {
		this.id = Math.random();
		this.bias = false;
	}
	
	/**
	 * Primary constructor.
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	public Node(double output, boolean bias) {
		this.id = Math.random();
		this.output = output;
		this.bias = bias;
	}

	/**
	 * Constructor for nodes without output.
	 * @param bias Bias node flag.
	 */
	public Node(boolean bias) {
		this(0.0, bias);
	}

	/**
	 * Constructor used in clone().
	 * @param id node id.
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	protected Node(double id, double output, boolean bias) {
		this.id = id;
		this.output = output;
		this.bias = bias;
	}

	/**
	 * Return the outgoing napse connections. 
	 * @return the outputNapses
	 */
	public abstract List<Napse> getOutputNapses();
	
	/**
	 * Return the inbound napse connections. 
	 * @return the inputNapses
	 */
	public abstract List<Napse> getInputNapses();
	
	@Override
	public abstract Node clone() throws CloneNotSupportedException;
	
	/**
	 * @return the id
	 */
	public double getId() {
		return id;
	}

	/**
	 * @return the output
	 */
	public double getOutput() {
		return output;
	}

	/**
	 * @return the bias
	 */
	public boolean isBias() {
		return bias;
	}

	/**
	 * @return the error
	 */
	public double getError() {
		return error;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(double output) {
		this.output = output;
	}

	/**
	 * Method hashes Id.
	 * @return hash code.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(id);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Equals method compares Ids.
	 * @param Node to compare.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (Double.doubleToLongBits(id) != Double.doubleToLongBits(other.id))
			return false;
		return true;
	}

}
