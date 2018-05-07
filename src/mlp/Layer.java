/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author toni
 */

      class Layer
    {
        public ArrayList<Neuron> neurons;
        public int numberOfNeurons;
        public double[] output;

        public Layer(int _numberOfNeurons, int numberOfInputs, Random r)
        {
            numberOfNeurons = _numberOfNeurons;
            neurons = new ArrayList<Neuron>();
            for (int i = 0; i < numberOfNeurons; i++)
            {
                neurons.add(new Neuron(numberOfInputs, r));
            }
        }

        public double[] Activate(double[] inputs)
        {
           double[] outputs = new double[inputs.length];
            
            for (int i = 0; i < numberOfNeurons; i++)
            {
                outputs[i] = neurons.get(i).Activate(inputs);
            }
            
            //output = outputs;  //esto se pasaba a toArray()
            
            return outputs;
        }

    }
