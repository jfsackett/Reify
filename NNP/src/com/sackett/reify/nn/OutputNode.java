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

import java.util.ArrayList;
import java.util.List;

/** 
 * This represents an output node in a neural network. 
 * @author Joseph Sackett
 */
public class OutputNode extends Node {
	/** Input synapses. */
	List<Napse> inputNapses = new ArrayList<Napse>();

	/** Default constructor. */
	public OutputNode() {
	}

	/**
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	public OutputNode(double output, boolean bias) {
		super(output, bias);
	}

	/**
	 * Constructor used in clone().
	 * @param id node id.
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	private OutputNode(double id, double output, boolean bias) {
		super(id, output, bias);
	}

	/**
	 * @return the inputNapses
	 */
	public List<Napse> getInputNapses() {
		return inputNapses;
	}
	
	/**
	 * Calculate the error for this output node.
	 * @param target classification target.
	 * @return calculated error.
	 */
	public double calcError(double target) {
		error = output * ( 1 - output ) * ( target - output );
		return error;
	}
	
	/** 
	 * Clone this output node but leave its Napses empty.
	 * @return output node clone.
	 */
	@Override
	public OutputNode clone() throws CloneNotSupportedException {
		OutputNode outputNodeClone = new OutputNode(this.getId(), this.output, this.isBias());
		outputNodeClone.error = this.error;

		return outputNodeClone;
	}

}
