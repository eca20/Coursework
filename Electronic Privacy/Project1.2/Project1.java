/* Ed Anderson, CS 1699 - Privacy In Electronic Society
* eca20@pitt.edu
*
* To compile:
*     javac Project1.java
* To Run:
*     java Project1
*
* What it do tho?:
*  The program will run each algorithm on 5 sets of numbers, 10 times each.
*  These values are set by ALGOLOOP and ITERATIONLOOP.
*  First, both answers (from algo A and B) are printed for each set
*   of numbers for comparison
*  Then the times of each computation are printed, along with their standard
*   deviation.
*  Finally, it prints the average of the 5 standard deviations to show
*   which algorithm was more consistent and constant with timing
*/

import java.util.Objects;
import java.math.*;
import java.util.Random;
import java.util.stream.*;


public final class Project1 {
  final static  int ALGOLOOP = 5;
  final  static int ITERATIONLOOP = 10;

  public static BigInteger divideNConquer(BigInteger base, BigInteger exp, BigInteger mod){
    BigInteger result = BigInteger.ONE;
    for (int i = exp.bitLength() - 1; i >= 0; i--){
      //always multiply result * result then mod
      result = (result.multiply(result).mod(mod));
      //if current bit is 1, also multiply result * base then mod
      if(exp.testBit(i)){
        result = (result.multiply(base)).mod(mod);
      }
    }
    return result;
  }

  public static BigInteger generateRandBigInt(){
      Random rand = new Random();
      BigInteger randomBigInt;
      randomBigInt = new BigInteger(512, rand);
      return randomBigInt;
  }

  public static double stdDev(long[] a){
    double avg = LongStream.of(a).sum() / a.length;
    double sd = 0;
    for (int i = 0; i < a.length; i++){
      sd = sd + Math.pow(a[i] - avg, 2);
    }
    return sd;
  }
  public static double avgDouble(double[] a){
    double sum = 0.0;
    for (double d : a){
      sum = Double.sum(sum,d);
    }
    double avg = sum / a.length;
    return avg;
  }

	public static void main(String[] args) {
		// Prompt user on standard output, parse standard input
    BigInteger base;
    BigInteger exponent;
    BigInteger mod;
    BigInteger two = new BigInteger("2");

    if(args.length == 0){
      base = generateRandBigInt();
      exponent = generateRandBigInt();
      mod = generateRandBigInt();
      while(mod.mod(two).equals(BigInteger.ZERO)){
        mod = generateRandBigInt();
      }
    }
    else if(args.length == 3){
	    base = new BigInteger(args[0]);
      exponent = new BigInteger(args[1]);
      mod = new BigInteger(args[2]);
    }
    else{
      System.out.println("Usage: java Project1 <base> <exponent> <mod>");
      System.out.println("\t<mod> must be an odd number greater than 3...");
      System.out.println("\tleave out any one argument to generate 3 random inputs");
      base = generateRandBigInt();
      exponent = generateRandBigInt();
      mod = generateRandBigInt();
    }

    if (!mod.testBit(0) || mod.compareTo(BigInteger.ONE) <= 0){
      System.out.println("Usage: java Project1 <base> <exponent> <mod>");
      System.out.println("\t<mod> must be an odd number greater than 3...");
    }
    else{
      BigInteger resultMont = BigInteger.ZERO;
      BigInteger result = BigInteger.ZERO;

      //[identifies which trial, 0-4][identifies which iteration, 0-9]
      //this stores the times of each trial
      long[][] montTimes = new long[ALGOLOOP][ITERATIONLOOP];
      long[][] dnqTimes = new long[ALGOLOOP][ITERATIONLOOP];
      //keeps an array of outputted standard deviations per algorithm
      //to take the average of later
      double[] montStdDev = new double[ALGOLOOP];
      double[] dnqStdDev = new double[ALGOLOOP];

      for (int j = 0; j < ALGOLOOP; j++){
        base = generateRandBigInt();
        exponent = generateRandBigInt();
        mod = generateRandBigInt();

        while(mod.mod(two).equals(BigInteger.ZERO)){
          mod = generateRandBigInt();
        }

        //create reducer with new mod, and convert new base to montgomery space
        MontgomeryModPow reducer = new MontgomeryModPow(mod);
        BigInteger baseMont = reducer.convertInput(base);

        System.out.println("Number set: " + j);
        System.out.println("Base:\n" + base);
        System.out.println("Exponent:\n" + exponent);
        System.out.println("Modulus:\n" + mod);
        System.out.println();

        for (int i = 0; i < ITERATIONLOOP; i++){

          //Run Divide and Conquer
          long startTime1 = System.nanoTime();
          result = divideNConquer(base, exponent, mod);
          long endTime1 = System.nanoTime();

          //Run Montgomery
          long startTime = System.nanoTime();
      		resultMont = reducer.power(baseMont, exponent);
          resultMont = reducer.convertOutput(resultMont);
          long endTime = System.nanoTime();

          //add total time to array for this [numberset][trial]
          montTimes[j][i] = endTime - startTime;
          dnqTimes[j][i] = endTime1 - startTime1;
        }

        System.out.println("Divide and Conquer Result: \n" + result);
        System.out.println("Montgomery Result: \n" + resultMont);
        System.out.println();
      }

      for (int j = 0; j < ALGOLOOP ; j++){
        dnqStdDev[j] = stdDev(dnqTimes[j]);
        System.out.print("Divide and Conquer Run Times on random numberset ");
        System.out.println(j +":");
        for (int i = 0; i < ITERATIONLOOP ; i++){
          System.out.println(dnqTimes[j][i]);
        }
        System.out.println("Divide and Conquer Std Deviation  " + j + " : " + dnqStdDev[j]);
        System.out.println();

      }
      System.out.println();
      System.out.println();

      for (int j = 0; j < ALGOLOOP; j++){
        montStdDev[j] = stdDev(montTimes[j]);
        System.out.print("Montgomery Run Times on random numberset ");
        System.out.println(j +":");
        for (int i = 0; i < ITERATIONLOOP; i++){
          System.out.println(montTimes[j][i]);

        }
        System.out.println("Montgomery Std Deviation " + j + " : " + montStdDev[j]);
        System.out.println();

      }
      System.out.println();
      System.out.println();

      System.out.println("Average Divide and Conquer std. deviation: " + avgDouble(dnqStdDev));
      System.out.println("Average Montgomery std. deviation: " + avgDouble(montStdDev) );
    }
	}
}
