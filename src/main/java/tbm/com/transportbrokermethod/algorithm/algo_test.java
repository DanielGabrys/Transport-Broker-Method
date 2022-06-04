package tbm.com.transportbrokermethod.algorithm;

public class algo_test
{
    public static void main(String args[])
    {
        /*
        int provider[] = new int[3];
        int recipient[] = new int[1];
        float profits[][] = new float[3][1];
        provider[0] = 13;
        provider[1] = 54;
        provider[2] = 10;
        recipient[0] = 61;
        profits[0][0] = 3.F;
        profits[1][0] = 4.F;
        profits[2][0] = 7.F;
        */

        int provider[] = new int[2];
        int recipient[] = new int[3];
        float profits[][] = new float[2][3];
        provider[0] = 20;
        provider[1] = 30;
        recipient[0] = 10;
        recipient[1] = 28;
        recipient[2] = 27;
        profits[0][0] = 12.F;
        profits[0][1] = 1.F;
        profits[0][2] = 3.F;
        profits[1][0] = 6.F;
        profits[1][1] = 4.F;
        profits[1][2] = -1.F;


        int[][] result = tbm.com.algorithm.TBMAlgorithm.compute(provider, recipient, profits);
    }
}
