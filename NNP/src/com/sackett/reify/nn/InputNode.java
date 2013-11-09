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
 * This represents an input node in a neural network. 
 * @author Joseph Sackett
 */
public class InputNode extends Node {
	/** Output synapses. */
	List<Napse> outputNapses = new ArrayList<Napse>();

	/**
	 * Default constructor.
	 */
	public InputNode() {
		super();
	}

	/**
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	public InputNode(double output, boolean bias) {
		super(output, bias);
	}

	/**
	 * Constructor used in clone().
	 * @param id node id.
	 * @param output default output.
	 * @param bias Bias node flag.
	 */
	private InputNode(double id, double output, boolean bias) {
		super(id, output, bias);
	}

	/**
	 * @return the outputNapses
	 */
	public List<Napse> getOutputNapses() {
		return outputNapses;
	}

	/** 
	 * Clone this input node but leave its Napses empty.
	 * @return input node clone.
	 */
	@Override
	public InputNode clone() throws CloneNotSupportedException {
		InputNode inputNodeClone = new InputNode(this.getId(), this.output, this.isBias());
		inputNodeClone.error = this.error;

		return inputNodeClone;
	}

}
