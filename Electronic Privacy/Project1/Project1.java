import java.math.*;

public class Project1 {

  public static long loopMultiply(int a, int b){
    int absValB = Math.abs(b);
    long result = a;
    for(int i = 1; i < absValB; i++){
      result += a;
    }
    return (b < 0) ? -result : result;
  }
  public static long loopMultiply(long a, int b){
    int absValB = Math.abs(b);
    long result = a;
    for(int i = 1; i < absValB; i++){
      result += a;
    }
    return (b < 0) ? -result : result;
  }
  /**
  * Algorithm A
  */

  public static long javaAlgo(int base, int exponent, int mod){
      long answer;
      answer = ((int)Math.pow(base, exponent)) % mod;
      return answer;

  }
  public static long squareAndMult(int base, int exponent, int mod){
    long result;
    result = 1;
    long base2 = base;
    base = base % mod;
    while (exponent > 0) {
      if ((exponent & 1) == 1) {
        result = (loopMultiply(result, base)) % mod;
      }
      exponent = exponent >> 1;
      base2 = (loopMultiply(base, base)) % mod;
    }
    return result;
  }
  /**
  * Algorithm B
  */
  public static long montgomeryMult(int base, int exponent, int mod){
    int digitBaseMin = 10;
    int powerCount = 1;
    long xPrime, yPrime;
    long answer;
    //Find R
    while(base > digitBaseMin){
      digitBaseMin *= 10;
      powerCount +=1;
    }
    xPrime = (base * digitBaseMin) % mod;
    yPrime = (exponent * digitBaseMin) % mod;
    answer = xPrime * yPrime;
    System.out.println("answer thus far = " + answer);

    answer = answer + (4*mod);
    System.out.println("answer thus far = " + answer);

    answer = answer + (20*mod);
    System.out.println("answer thus far = " + answer);

    answer = answer / digitBaseMin;
    System.out.println("answer thus far = " + answer);

    answer = (((1/digitBaseMin) % mod)) % mod;
    System.out.println("answer thus far = " + answer);




    return answer;
  }

  public static void main(String[] args){
    long answer;
    int b = 0, e = 0, m = 0;

    if(args.length < 2 ){
      System.out.println("\tUsage: java Project1 <base> <exponent> <mod> <algorithm>");
      System.exit(-1);
    }
    try{
      b = Integer.parseInt(args[0]);
      e = Integer.parseInt(args[1]);
      m = Integer.parseInt(args[2]);
    }
    catch(Exception e1){
      System.out.println("\t <base> <exponent> and <mod> must be integer values");
      System.exit(-1);
    }
    System.out.println("\tbase: " + b + " exponent: " + e + " mod: " + m);

    /**
    * Square and Multiply
    */
    System.out.println("\tRunning modular exponentiation with square and multiply, loop multiplication...");
    long startTime2 = System.nanoTime();

      answer = squareAndMult(b, e, m);

    long endTime2 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tSquare and Multiply took " + (endTime2 - startTime2) + " milliseconds");
    System.out.println();

    /**
    * Montgomery Multiplication
    */
    System.out.println("\tRunning modular exponentiation with Montgomry Multiplication...");
    long startTime = System.nanoTime();
    answer = montgomeryMult(b, e, m);
    long endTime = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tMontgomery Multiplication took " + (endTime - startTime) + " milliseconds");
    System.out.println();


    System.out.println("\tRunning modular exponentiation with Java Math.pow...");
    long startTime3 = System.nanoTime();
    answer = javaAlgo(b, e, m);
    long endTime3 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava Math.pow took " + (endTime3 - startTime3) + " milliseconds");
    System.out.println();

  }
}
