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
 * This represents an artificial neural network. 
 * @author Joseph Sackett
 */
public class NeuralNetwork {
	/** Weight adjustment coefficient. */
	private double eta;
	
	/** Weight adjustment factor to prior update. */
	private double momentum;
	
	/** Input nodes. */
	private List<InputNode> inputNodes;
	
	/** Output nodes. */
	private List<OutputNode> outputNodes;	
	
	/**
	 * Define a neural network, including weight adjustment factors.
	 * @param eta weight adjustment coefficient.
	 * @param momentum weight adjustment factor to prior update.
	 */
	public NeuralNetwork(double eta, double momentum) {
		this.eta = eta;
		this.momentum = momentum;
	}

	/**
	 * @return the eta
	 */
	public double getEta() {
		return eta;
	}

	/**
	 * @return the momentum
	 */
	public double getMomentum() {
		return momentum;
	}

	/**
	 * @return the inputNodes
	 */
	public List<InputNode> getInputNodes() {
		return inputNodes;
	}

	/**
	 * @return the outputNodes
	 */
	public List<OutputNode> getOutputNodes() {
		return outputNodes;
	}

	/**
	 * @param inputNodes the inputNodes to set
	 */
	public void setInputNodes(List<InputNode> inputNodes) {
		this.inputNodes = inputNodes;
	}

	/**
	 * @param outputNodes the outputNodes to set
	 */
	public void setOutputNodes(List<OutputNode> outputNodes) {
		this.outputNodes = outputNodes;
	}
	
	/**
	 * Initializes the input values and classifies the instance by calculating all node values, including outputs and errors. 
	 * @param inputs array of input values.
	 * @param outputs array of target output values.
	 * @return actual output values & errors.
	 */
	public ClassifyOutput classify(double[] inputs, double[] targetOutputs) {
		// Set input node values (skip bias node).
		for (int ixInput = 0 ; ixInput < inputs.length ; ixInput++) {
			inputNodes.get(ixInput+1).setOutput(inputs[ixInput]);
		}
		// Clear values of hidden nodes linked to last input node (excludes bias node).
		List<HiddenNode> hiddenNodes = getHiddenNodesFrom(inputNodes.get(inputNodes.size()-1));
		for (HiddenNode hiddenNode : hiddenNodes) {
			hiddenNode.setOutput(0.0);
		}
		// Clear output node values.
		for (OutputNode outputNode : outputNodes) {
			outputNode.setOutput(0.0);
		}
		
		// Calculate hidden node NETs.
		for (InputNode inputNode : inputNodes) {
			for (Napse napse : inputNode.getOutputNapses()) {
				Node outNode = napse.getOutNode();
				// Add weight-factored input to node value.
				outNode.setOutput(outNode.getOutput() + inputNode.getOutput() * napse.getWeight());
			}
		}

		// Apply sigmoid function to hidden node NETs.
		for (HiddenNode hiddenNode : hiddenNodes) {
			hiddenNode.setOutput(1.0 / (1.0 + Math.exp(-1.0 * hiddenNode.getOutput())));
		}

		// Get all hidden nodes, including bias.
		hiddenNodes = getHiddenNodesFrom(outputNodes.get(outputNodes.size()-1));
		
		// Calculate output node NETs.
		for (HiddenNode hiddenNode : hiddenNodes) {
			for (Napse napse : hiddenNode.getOutputNapses()) {
				Node outNode = napse.getOutNode();
				// Add weight-factored input to node value.
				outNode.setOutput(outNode.getOutput() + hiddenNode.getOutput() * napse.getWeight());
			}
		}

		// Error square sum for root mean square error calculations.
		double sumErrorsSqu = 0.0;
		// Number of discrete classification errors.
		long sumDiscreteErrors = 0;

		double[] actualOutputs = new double[outputNodes.size()-1];
		boolean first = true; int ix = 0;
		// Apply sigmoid function to output node NETs.
		for (OutputNode outputNode : outputNodes) {
			// Skip dummy bias node.
			if (first) {
				first = false;
				continue;
			}
			
			// Apply sigmoid function to accumulated NETs.
			outputNode.setOutput(1.0 / (1.0 + Math.exp(-1.0 * outputNode.getOutput())));
			// Store output for return.
			actualOutputs[ix] = outputNode.getOutput();
			
			// Accumulate square of error.
			sumErrorsSqu = sumErrorsSqu + Math.pow(targetOutputs[ix] - actualOutputs[ix], 2);
			
			// Accumulate discrete errors.
			sumDiscreteErrors = sumDiscreteErrors + Math.abs((long)targetOutputs[ix] - Math.round(actualOutputs[ix]));
			
			ix++;
		}
		
		// Calculate root mean square error.
		double rmsError = Math.sqrt(sumErrorsSqu / (double)targetOutputs.length);
		// Calculate classification error.
		double classError = (double)sumDiscreteErrors / (double)targetOutputs.length;

		
		return new ClassifyOutput(actualOutputs, rmsError, classError);
	}
	
	/**
	 * Classifies inputs, backpropagates errors based on expected outputs, and updates weights. 
	 * @param inputs array of input values.
	 * @param outputs array of target output values.
	 */
	public void backpropagate(double[] inputs, double[] outputs) {
		// Classify inputs to set all node outputs.
		classify(inputs, outputs);
		
		// Calculate output node errors (skip bias node).
		for (int ixOutput = 1 ; ixOutput < outputNodes.size() ; ixOutput++) {
			outputNodes.get(ixOutput).calcError(outputs[ixOutput-1]);
		}
		
		// Get hidden nodes, without bias node.
		List<HiddenNode> hiddenNodes = getHiddenNodesFrom(inputNodes.get(inputNodes.size()-1));
		// Calculate hidden node errors. Must be done after calculating downstream errors.
		for (HiddenNode hiddenNode : hiddenNodes) {
			hiddenNode.calcError();
		}
		
		// Loop through and update input to hidden node weights.
		for (InputNode inputNode : inputNodes) {
			for (Napse napse : inputNode.getOutputNapses()) {
				napse.updateWeight(eta, momentum);
			}
		}
		
		// Get all hidden nodes, including bias node.
		hiddenNodes = getHiddenNodesFrom(outputNodes.get(outputNodes.size()-1));
		// Loop through and update hidden to output node weights.
		for (HiddenNode hiddenNode : hiddenNodes) {
			for (Napse napse : hiddenNode.getOutputNapses()) {
				napse.updateWeight(eta, momentum);
			}
		}
	}
	
	/**
	 * Get all hidden nodes connected as outputs from an input node.
	 * @param inputNode starting point.
	 * @return connected list of hidden nodes.
	 */
	private static List<HiddenNode> getHiddenNodesFrom(InputNode inputNode) {
		List<HiddenNode> hiddenNodes = new ArrayList<HiddenNode>();
		for (Napse napse : inputNode.getOutputNapses()) {
			Node outNode = napse.getOutNode();
			// Check & safely downcast.
			if(!(outNode instanceof HiddenNode)) {
				continue;
			}
			hiddenNodes.add((HiddenNode)outNode);
		}
		
		return hiddenNodes;
	}
	
	/**
	 * Get all hidden nodes connected as inputs to an output node.
	 * @param outputNode starting point.
	 * @return connected list of hidden nodes.
	 */
	private static List<HiddenNode> getHiddenNodesFrom(OutputNode outputNode) {
		List<HiddenNode> hiddenNodes = new ArrayList<HiddenNode>();
		for (Napse napse : outputNode.getInputNapses()) {
			Node inNode = napse.getInNode();
			// Check & safely downcast.
			if(!(inNode instanceof HiddenNode)) {
				continue;
			}
			hiddenNodes.add((HiddenNode)inNode);
		}
		
		return hiddenNodes;
	}
	
	public static class ClassifyOutput {
		/** Actual output. */
		private double[] output;
		
		/** Root mean square error. */
		private double rmsError;
		
		/** Discrete classification error. */
		private double classError;
		
		/**
		 * @param output Actual outputs.
		 * @param rmsError Root mean square error.
		 * @param classError Discrete classification error.
		 */
		public ClassifyOutput(double[] output, double rmsError, double classError) {
			this.output = output;
			this.rmsError = rmsError;
			this.classError = classError;
		}
		
		/**
		 * @return the output
		 */
		public double[] getOutput() {
			return output;
		}

		/**
		 * @return the rmsError
		 */
		public double getRmsError() {
			return rmsError;
		}

		/**
		 * @return the classError
		 */
		public double getClassError() {
			return classError;
		}		
	}

}

