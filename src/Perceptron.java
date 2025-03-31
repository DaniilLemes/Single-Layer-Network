import java.util.Random;

public class Perceptron {
    private final int alpha;
    private double prog;
    private final double[] weights;
    Random rand;

    public Perceptron(int weightsVectorLength, int alpha, double prog) {
        this.alpha = alpha;
        this.prog = prog;
        weights = new double[weightsVectorLength];

        rand = new Random();
        for (int i = 0; i < weightsVectorLength; i++) {
            weights[i] = -1 + 2 * rand.nextDouble();
        }
    }

    public int compute(double[] vectorInput){
        double netInput = 0.0;
        for (int i = 0; i < vectorInput.length; i++) {
            netInput += weights[i] * vectorInput[i];
        }
        netInput -= prog;
        return netInput >= 0 ? 1 : 0;
    }

    public void learn(double[] vectorInput, int goodAnswer){
        int output = compute(vectorInput);
        int error = goodAnswer - output;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += alpha * error * vectorInput[i];
        }
        prog -= alpha * error;
    }

}
