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
 * This represents a node in a neural network. 
 * @author Joseph Sackett
 */
public abstract class Node {
	/** Node output value. */
	protected double output = 0.0d;
	
	/** Bias node flag. Indicates this is a bias node. */
	private boolean bias = false;
	
	/** Node error. */
	protected double error = 0.0d;
	
	/** Default constructor. */
	public Node() {
	}
	
	/**
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	public Node(double output, boolean bias) {
		this.output = output;
		this.bias = bias;
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

}
