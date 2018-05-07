/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlp;

/**
 *
 * @author toni
 */
import java.util.ArrayList;
import java.util.Random;
class Perceptron
    {
        ArrayList<Layer> layers;

        public Perceptron(int[] neuronsPerlayer)
        {
            layers = new ArrayList<Layer>();
            Random r = new Random();

            for (int i = 0; i < neuronsPerlayer.length; i++)
            {
                layers.add(new Layer(neuronsPerlayer[i], i == 0 ? neuronsPerlayer[i] : neuronsPerlayer[i - 1], r));
            }
        }
        public double[] Activate(double[] inputs)
        {
            double[] outputs = new double[0];
            for (int i = 1; i < layers.size(); i++)
            {
                outputs = layers.get(i).Activate(inputs);
                inputs = outputs;
            }
            return outputs;
        }
        double IndividualError(double[] realOutput, double[] desiredOutput)
        {
            double err = 0;
            for (int i = 0; i < realOutput.length; i++)
            {
                err += Math.pow(realOutput[i] - desiredOutput[i], 2);
            }
            return err;
        }
        double GeneralError(ArrayList<double[]> input, ArrayList<double[]> desiredOutput)
        {
            double err = 0;
            for (int i = 0; i < input.size(); i++)
            {
                err += IndividualError(Activate(input.get(i)), desiredOutput.get(i));
            }
            return err;
        }
        ArrayList<String> log;
        public boolean Learn(ArrayList<double[]> input, ArrayList<double[]> desiredOutput, double alpha, double maxError, int maxIterations)
        {
            double err = 99999;
            log = new ArrayList<String>();
            int it = maxIterations;
            while (err > maxError)
            {
                ApplyBackPropagation(input, desiredOutput, alpha);
                err = GeneralError(input, desiredOutput);

                
                 System.out.println(err + " iterations: " + (it - maxIterations));
                log.add(String.valueOf(err));                
                maxIterations--;

  /*              if (Console.KeyAvailable)
                {
                    System.IO.File.WriteAllLines(@"C:\Users\ASUS\LogTail.txt", log.ToArray());
                    return true;
                }

                if (maxIterations <= 0)
                {
                    Console.WriteLine("MINIMO LOCAL");
                    System.IO.File.WriteAllLines(@"C:\Users\ASUS\LogTail.txt", log.ToArray());
                    return false;
                }
*/
            }
            
            //System.IO.File.WriteAllLines(@"C:\Users\ASUS\LogTail.txt", log.ToArray());
            return true;
        }

        ArrayList<double[]> sigmas;
        ArrayList<double[]> deltas;
        double[][] sigmaMatriz;
        void SetSigmas(double[] desiredOutput)
        {
            sigmas = new ArrayList<double[]>();
            sigmaMatriz = new double[layers.size()][layers.size()];
            for (int i = 0; i < layers.size(); i++)
            {
                sigmas.add(new double[layers.get(i).numberOfNeurons]);
            }
            for (int i = layers.size() - 1; i >= 0; i--)
            {
                for (int j = 0; j < layers.get(i).numberOfNeurons; j++)
                {
                    if (i == layers.size() - 1)
                    {
                        double y = layers.get(i).neurons.get(i).lastActivation;
                        sigmaMatriz[i][j] = (Neuron.Sigmoid(y) - desiredOutput[j]) * Neuron.SigmoidDerivated(y)
                        sigmas.add( sigmaMatriz[i][j]);
                    }
                    else
                    {
                        double sum = 0;
                        for (int k = 0; k < layers[i + 1].numberOfNeurons; k++)
                        {
                            sum += layers[i + 1].neurons[k].weights[j] * sigmas[i + 1][k];
                        }
                        sigmas[i][j] = Neuron.SigmoidDerivated(layers[i].neurons[j].lastActivation) * sum;
                    }
                }
            }
        }
        void SetDeltas()
        {
            deltas = new List<double[,]>();
            for (int i = 0; i < layers.Count; i++)
            {
                deltas.Add(new double[layers[i].numberOfNeurons, layers[i].neurons[0].weights.Length]);
            }
        }
        void AddDelta()
        {
            for (int i = 1; i < layers.Count; i++)
            {
                for (int j = 0; j < layers[i].numberOfNeurons; j++)
                {
                    for (int k = 0; k < layers[i].neurons[j].weights.Length; k++)
                    {
                        deltas[i][j, k] += sigmas[i][j] * Neuron.Sigmoid(layers[i - 1].neurons[k].lastActivation);
                    }
                }
            }
        }
        void UpdateBias(double alpha)
        {
            for (int i = 0; i < layers.Count; i++)
            {
                for (int j = 0; j < layers[i].numberOfNeurons; j++)
                {
                    layers[i].neurons[j].bias -= alpha * sigmas[i][j];
                }
            }
        }
        void UpdateWeights(double alpha)
        {
            for (int i = 0; i < layers.Count; i++)
            {
                for (int j = 0; j < layers[i].numberOfNeurons; j++)
                {
                    for (int k = 0; k < layers[i].neurons[j].weights.Length; k++)
                    {
                        layers[i].neurons[j].weights[k] -= alpha * deltas[i][j, k];
                    }
                }
            }
        }
        void ApplyBackPropagation(List<double[]> input, List<double[]> desiredOutput, double alpha)
        {
            SetDeltas();
            for (int i = 0; i < input.Count; i++)
            {
                Activate(input[i]);
                SetSigmas(desiredOutput[i]);
                UpdateBias(alpha);
                AddDelta();
            }
            UpdateWeights(alpha);

        }
    }

 