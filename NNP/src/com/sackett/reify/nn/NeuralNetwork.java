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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	/** Hidden nodes. */
	private List<HiddenNode> hiddenNodes;
	
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
	 * @return the hiddenNodes
	 */
	public List<HiddenNode> getHiddenNodes() {
		return hiddenNodes;
	}
	
	/**
	 * Hidden nodes accessor with option to include or skip bias nodes.
	 * @param withBias flag indicating whether bias nodes returned or not.
	 * @return the hiddenNodes
	 */
	public List<HiddenNode> getHiddenNodes(boolean withBias) {
		if (withBias) {
			return hiddenNodes;
		}
		
		List<HiddenNode> hiddenNodesNoBias = new ArrayList<HiddenNode>();
		for (HiddenNode hiddenNode : hiddenNodes) {
			if (!hiddenNode.isBias()) {
				hiddenNodesNoBias.add(hiddenNode);
			}
		}
		
		return hiddenNodesNoBias;
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
	 * @param hiddenNodes the hiddenNodes to set
	 */
	public void setHiddenNodes(List<HiddenNode> hiddenNodes) {
		this.hiddenNodes = hiddenNodes;
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
		// Set input node values (excluding bias node).
		for (int ixInput = 0 ; ixInput < inputs.length ; ixInput++) {
			inputNodes.get(ixInput+1).setOutput(inputs[ixInput]);
		}
		// Clear values of hidden nodes linked to last input node (excluding bias node).
		List<HiddenNode> hiddenNodesNoBias = getHiddenNodes(false);
		for (HiddenNode hiddenNode : hiddenNodesNoBias) {
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
		long sumActualOutput = 0;

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
			actualOutputs[ix] = outputNode.getFactoredOutput();
			
			// Accumulate square of error.
			sumErrorsSqu += Math.pow(targetOutputs[ix] - actualOutputs[ix], 2);
			
			// Accumulate discrete errors.
//			sumDiscreteErrors += Math.abs((long)targetOutputs[ix] - Math.round(actualOutputs[ix]));
			sumDiscreteErrors += (Math.abs((long)targetOutputs[ix] - Math.round(actualOutputs[ix])) > 0) ? 1 : 0; 
			
//			sumActualOutput += actualOutputs[ix];
			
			ix++;
		}
		
		// Calculate root mean square error.
		double rmsError = Math.sqrt(sumErrorsSqu / (double)targetOutputs.length);
		// Calculate classification error.
		double classError = (double)sumDiscreteErrors / (double)targetOutputs.length;
//		double classError = (double)sumDiscreteErrors / (double)sumActualOutput; //targetOutputs.length;
		
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
		
		// Calculate output node errors (excluding bias node).
		for (int ixOutput = 1 ; ixOutput < outputNodes.size() ; ixOutput++) {
			outputNodes.get(ixOutput).calcError(outputs[ixOutput-1]);
		}
		
		// Get hidden nodes (excluding bias node).
		List<HiddenNode> hiddenNodesNoBias = getHiddenNodes(false);
		// Calculate hidden node errors. Must be done after calculating downstream errors.
		for (HiddenNode hiddenNode : hiddenNodesNoBias) {
			hiddenNode.calcError();
		}
		
		// Loop through and update input to hidden node weights (including bias node).
		for (InputNode inputNode : inputNodes) {
			for (Napse napse : inputNode.getOutputNapses()) {
				napse.updateWeight(eta, momentum);
			}
		}
		
		// Loop through and update hidden to output node weights (including bias node).
		for (HiddenNode hiddenNode : hiddenNodes) {
			for (Napse napse : hiddenNode.getOutputNapses()) {
				napse.updateWeight(eta, momentum);
			}
		}
	}
	
	private List<Napse> discInputToHiddenNapses = new ArrayList<Napse>();
	private List<Napse> discHiddenToOutputNapses = new ArrayList<Napse>();
	
	/**
	 * Updates the neighborhood (napse connections & weights) by factor.
	 * @param updateFactor update factor.
	 */
	public void updateNeighborhood(double updateFactor) {
		// Maps of nodes for resolving Napse connections.
		Map<Double,InputNode> inputNodesMap = new HashMap<Double,InputNode>();
		Map<Double,HiddenNode> hiddenNodesMap = new HashMap<Double,HiddenNode>();
		Map<Double,OutputNode> outputNodesMap = new HashMap<Double,OutputNode>();
		
		// Loop through and input to hidden napses and alter layer connections & weights.
		List<Napse> removedInputToHiddenNapses = new ArrayList<Napse>();
		for (InputNode inputNode : inputNodes) {
			// Place in map for indexing to resolve Napse connections.
			inputNodesMap.put(inputNode.getId(), inputNode);
			for (Napse napse : inputNode.getOutputNapses()) {
				// Check whether to remove this napse.
//				if (checkRandom(updateFactor)) {
//					// Save for disconnection & possible use later.
//					removedInputToHiddenNapses.add(napse);
//				}
//				else 
				if (checkRandom(updateFactor)) {
					// Update napse weight.
					napse.setWeight(factorWeight(napse.getWeight(), updateFactor));
				}
			}
		}
		
		// Loop through removed napses to disconnect and save for later use.
		for (Napse napse : removedInputToHiddenNapses) {
			// Remove from in node.
			((InputNode)napse.getInNode()).getOutputNapses().remove(napse);
			// Remove from out node.
			((HiddenNode)napse.getOutNode()).getInputNapses().remove(napse);
			// Save for potential rese later.
			discInputToHiddenNapses.add(napse);
		}

		// Loop through and hidden to output napses and alter layer connections & weights.
		List<Napse> removedHiddenToOutputNapses = new ArrayList<Napse>();
		for (HiddenNode hiddenNode : hiddenNodes) {
			// Place in map for indexing to resolve Napse connections.
			hiddenNodesMap.put(hiddenNode.getId(), hiddenNode);
			for (Napse napse : hiddenNode.getOutputNapses()) {
				// Check whether to remove this napse.
//				if (checkRandom(updateFactor)) {
//					// Save for disconnection & possible use later.
//					removedHiddenToOutputNapses.add(napse);
//				}
//				else 
				if (checkRandom(updateFactor)) {
					// Update napse weight.
					napse.setWeight(factorWeight(napse.getWeight(), updateFactor));
				}
			}
		}
		
//		// Loop through removed napses to disconnect and save for later use.
//		for (Napse napse : removedHiddenToOutputNapses) {
//			// Remove from in node.
//			((HiddenNode)napse.getInNode()).getOutputNapses().remove(napse);
//			// Remove from out node.
//			((OutputNode)napse.getOutNode()).getInputNapses().remove(napse);
//			// Save for potential rese later.
//			discHiddenToOutputNapses.add(napse);
//		}
//
//		// Loop through output nodes.
//		for (OutputNode outputNode : outputNodes) {
//			// Place in map for indexing to resolve Napse connections.
//			outputNodesMap.put(outputNode.getId(), outputNode);
//		}
		
		// Loop through previously removed input to hidden napses and check probability to add them back.
//		List<Napse> reinstateNapses = new ArrayList<Napse>();
//		for (Napse napse : discInputToHiddenNapses) {
//			if (checkRandom(updateFactor)) {
//				reinstateNapses.add(napse);
//				// Update weights.
//				napse.setWeight(factorWeight(napse.getWeight(), updateFactor));
//				// Get current nodes to connect since they've since been cloned.
//				InputNode inputNode = inputNodesMap.get(napse.getInNode().getId());
//				HiddenNode hiddenNode = hiddenNodesMap.get(napse.getOutNode().getId());
//				// Set napse to point at current nodes.
//				napse.setInNode(inputNode);
//				napse.setOutNode(hiddenNode);
//				// Add napse to output of hidden node and input of output node.
//				inputNode.getOutputNapses().add(napse);
//				hiddenNode.getInputNapses().add(napse);
//			}
//		}
//		discInputToHiddenNapses.removeAll(reinstateNapses);
//		
//		// Loop through previously removed hidden to output napses and check probability to add them back.
//		reinstateNapses = new ArrayList<Napse>();
//		for (Napse napse : discHiddenToOutputNapses) {
//			if (checkRandom(updateFactor)) {
//				reinstateNapses.add(napse);
//				// Update weights.
//				napse.setWeight(factorWeight(napse.getWeight(), updateFactor));
//				// Get current nodes to connect since they've since been cloned.
//				HiddenNode hiddenNode = hiddenNodesMap.get(napse.getInNode().getId());
//				OutputNode outputNode = outputNodesMap.get(napse.getOutNode().getId());
//				// Set napse to point at current nodes.
//				napse.setInNode(hiddenNode);
//				napse.setOutNode(outputNode);
//				// Add napse to output of hidden node and input of output node.
//				hiddenNode.getOutputNapses().add(napse);
//				outputNode.getInputNapses().add(napse);
//			}
//		}
//		discHiddenToOutputNapses.removeAll(reinstateNapses);
	}
	
	/** 
	 * Check for random occurrence.
	 * @param probability chance of random occurrence.
	 * @return indicator of occurrence.
	 */
	private static boolean checkRandom(double probability) {
		return Math.random() < probability;
	}
	
	/**
	 * Generate random weight update from source & factor.
	 * @param weight input weight.
	 * @param factor update factor.
	 * @return random weight
	 */
	private static double factorWeight(double weight, double factor) {
//		return weight + 2 * factor * Math.random() - factor;
		return weight + 2 * Math.random() - 1;
	}
	
	/**
	 * Represents the classification of a test instance.
	 */
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
		 * Clone the whole neural network.
		 */
		public double getClassError() {
			return classError;
		}		
	}

	/** 
	 * Clone the whole neural network.
	 * @return neural network clone.
	 */
	@Override
	public NeuralNetwork clone() throws CloneNotSupportedException {
		NeuralNetwork clone = new NeuralNetwork(this.eta, this.momentum);
		
		// Maps of cloned nodes for resolving Napse connections.
		Map<Double,InputNode> inputNodesCloneMap = new HashMap<Double,InputNode>();
		Map<Double,HiddenNode> hiddenNodesCloneMap = new HashMap<Double,HiddenNode>();
		Map<Double,OutputNode> outputNodesCloneMap = new HashMap<Double,OutputNode>();
		
		// Clone input nodes.
		List<InputNode> inputNodesClone = new ArrayList<InputNode>();
		for (InputNode inputNode : inputNodes) {
			InputNode inputNodeClone = inputNode.clone();
			// Add to cloned list.
			inputNodesClone.add(inputNodeClone);
			// Place in map for indexing to resolve Napse connections.
			inputNodesCloneMap.put(inputNode.getId(), inputNodeClone);
		}
		clone.setInputNodes(inputNodesClone);
		
		// Clone hidden nodes.
		List<HiddenNode> hiddenNodesClone = new ArrayList<HiddenNode>();
		for (HiddenNode hiddenNode : hiddenNodes) {
			HiddenNode hiddenNodeClone = hiddenNode.clone();
			// Add to cloned list.
			hiddenNodesClone.add(hiddenNodeClone);
			// Place in map for indexing to resolve Napse connections.
			hiddenNodesCloneMap.put(hiddenNode.getId(), hiddenNodeClone);
		}
		clone.setHiddenNodes(hiddenNodesClone);
		
		// Clone output nodes.
		List<OutputNode> outputNodesClone = new ArrayList<OutputNode>();
		for (OutputNode outputNode : outputNodes) {
			OutputNode outputNodeClone = outputNode.clone();
			// Add to cloned list.
			outputNodesClone.add(outputNodeClone);
			// Place in map for indexing to resolve Napse connections.
			outputNodesCloneMap.put(outputNode.getId(), outputNodeClone);
		}
		clone.setOutputNodes(outputNodesClone);
		
		// Loop through input nodes, clone and connect Napses.
		for (InputNode inputNode : inputNodes) {
			// Get cloned input node from map.
			InputNode inputNodeClone = inputNodesCloneMap.get(inputNode.getId());
			List<Napse> napses = inputNode.getOutputNapses();
			for (Napse napse : napses) {
				// Get connected, cloned hidden node from map.
				HiddenNode hiddenNodeClone = hiddenNodesCloneMap.get(napse.getOutNode().getId());
				
				// Clone napse.
				Napse napseClone = napse.clone();
				napseClone.setInNode(inputNodeClone);
				napseClone.setOutNode(hiddenNodeClone);

				// Place cloned napse in cloned input node outputs.
				inputNodeClone.getOutputNapses().add(napseClone);

				// Place cloned napse in cloned hidden node inputs.
				hiddenNodeClone.getInputNapses().add(napseClone);
			}
		}
		
		// Loop through hidden nodes, clone and connect Napses.
		for (HiddenNode hiddenNode : hiddenNodes) {
			// Get cloned hidden node from map.
			HiddenNode hiddenNodeClone = hiddenNodesCloneMap.get(hiddenNode.getId());
			List<Napse> napses = hiddenNode.getOutputNapses();
			for (Napse napse : napses) {
				// Get connected, cloned output node from map.
				OutputNode outputNodeClone = outputNodesCloneMap.get(napse.getOutNode().getId());
				
				// Clone napse.
				Napse napseClone = napse.clone();
				napseClone.setInNode(hiddenNodeClone);
				napseClone.setOutNode(outputNodeClone);

				// Place cloned napse in cloned hidden node outputs.
				hiddenNodeClone.getOutputNapses().add(napseClone);

				// Place cloned napse in cloned output node inputs.
				outputNodeClone.getInputNapses().add(napseClone);
			}
		}
		
		// Retain disconnected nodes.
		clone.discInputToHiddenNapses = discInputToHiddenNapses;
		clone.discHiddenToOutputNapses = discHiddenToOutputNapses;

		return clone;
	}

	/**
	 * Hash code for this neural network (incomplete).
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hiddenNodes == null) ? 0 : hiddenNodes.hashCode());
		result = prime * result
				+ ((inputNodes == null) ? 0 : inputNodes.hashCode());
		result = prime * result
				+ ((outputNodes == null) ? 0 : outputNodes.hashCode());
		return result;
	}

	/**
	 * Compares equivalence.
	 * @param obj to compare.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NeuralNetwork other = (NeuralNetwork) obj;
		if (hiddenNodes == null) {
			if (other.hiddenNodes != null)
				return false;
		}		
		if (inputNodes == null) {
			if (other.inputNodes != null)
				return false;
		} 
		if (outputNodes == null) {
			if (other.outputNodes != null)
				return false;
		}
		
		// Confirm input nodes exist in other.
		if (!(other.inputNodes.containsAll(inputNodes) || inputNodes.containsAll(other.inputNodes))) {
			return false;
		}
		// Confirm hidden nodes exist in other.
		if (!(other.hiddenNodes.containsAll(hiddenNodes) || hiddenNodes.containsAll(other.hiddenNodes))) {
			return false;
		}
		// Confirm output nodes exist in other.
		if (!(other.outputNodes.containsAll(outputNodes) || outputNodes.containsAll(other.outputNodes))) {
			return false;
		}
		
		return true;
	}

}

