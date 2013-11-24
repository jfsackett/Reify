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
 * This represents a hidden node in a neural network. 
 * @author Joseph Sackett
 */
public class HiddenNode extends Node {
	/** Input synapses. */
	List<Napse> inputNapses = new ArrayList<Napse>();

	/** Output synapses. */
	List<Napse> outputNapses = new ArrayList<Napse>();

	/** Default constructor. */
	public HiddenNode() {
	}

	/**
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	public HiddenNode(double output, boolean bias) {
		super(output, bias);
	}

	/**
	 * Constructor used in clone().
	 * @param id node id.
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	private HiddenNode(double id, double output, boolean bias) {
		super(id, output, bias);
	}

	/**
	 * @return the inputNapses
	 */
	public List<Napse> getInputNapses() {
		return inputNapses;
	}

	/**
	 * @return the outputNapses
	 */
	public List<Napse> getOutputNapses() {
		return outputNapses;
	}
		
	/**
	 * Calculate the error for this hidden node.
	 * @return calculated error.
	 */
	public double calcError() {
		double weightedErrors = 0.0;
		for (Napse outputNapse : outputNapses) {
			weightedErrors += outputNapse.getWeight() * outputNapse.getOutNode().getError();
		}
		error = output * ( 1 - output ) * weightedErrors;
		return error;
	}
	
	/** 
	 * Clone this hidden node but leave its Napses empty.
	 * @return hidden node clone.
	 */
	@Override
	public HiddenNode clone() throws CloneNotSupportedException {
		HiddenNode hiddenNodeClone = new HiddenNode(this.getId(), this.output, this.isBias());
		hiddenNodeClone.error = this.error;

		return hiddenNodeClone;
	}

	/** Return string representation. */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HiddenNode [");
		for (Napse outputNapse : outputNapses) {
			builder.append(outputNapse);
		}
		builder.append("]");
		
		return builder.toString();
	}

}
