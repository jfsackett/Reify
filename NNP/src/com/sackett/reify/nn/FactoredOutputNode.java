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
 * This represents an output node who's activation is hit with factor. 
 * @author Joseph Sackett
 */
public class FactoredOutputNode extends OutputNode {
	/** Factor to apply to output. */
	double factor = 1.0;

	/** Default constructor. */
	public FactoredOutputNode() {
		super();
	}

	/**
	 * @param factor output factor.
	 * @param bias Bias node flag.
	 */
	public FactoredOutputNode(double factor, boolean bias) {
		super(bias);
		this.factor = factor;
	}

	/**
	 * Constructor used in clone().
	 * @param id node id.
	 * @param factor output factor.
	 * @param bias Bias node flag.
	 */
	protected FactoredOutputNode(double id, double factor, boolean bias) {
		super(id, bias);
		this.factor = factor;
	}

	/**
	 * @return the factored output
	 */
	@Override
	public double getFactoredOutput() {
		return output * factor;
	}

	/**
	 * Calculate the error for this factored output node.
	 * @param target classification target.
	 * @return calculated error.
	 */
	public double calcError(double target) {
		double factoredOutput = getFactoredOutput();
		error = factoredOutput * ( factor - factoredOutput ) * ( target - factoredOutput );
		return error;
	}
	
	/** 
	 * Clone this factored output node but leave its Napses empty.
	 * @return output node clone.
	 */
	@Override
	public FactoredOutputNode clone() throws CloneNotSupportedException {
		FactoredOutputNode outputNodeClone = new FactoredOutputNode(this.getId(), factor, this.isBias());
		outputNodeClone.error = this.error;

		return outputNodeClone;
	}

}
