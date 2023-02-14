import java.math.*;

public class Project1 {

  /**
  * Default java method
  */
  public static BigInteger javaDefault(BigInteger base, BigInteger exp, BigInteger mod){
      BigInteger result;
      result = base.modPow(exp, mod);
      return result;
  }

  public static BigInteger javaSlow(BigInteger base, BigInteger exp, BigInteger mod){
    BigInteger result;
    result = BigInteger.ONE;
    for (BigInteger i = BigInteger.ONE; i.compareTo(exp) <= 0; i = i.add(BigInteger.ONE)){
      result = result.multiply(base);
    }
    result = result.mod(mod);
    return result;
  }
  /**
  * Algorithm B
  */
  public static BigInteger javaFast(BigInteger base, BigInteger exp, BigInteger mod){
      BigInteger result;
      result = BigInteger.ONE;
      for (BigInteger i = BigInteger.ONE; i.compareTo(exp) <= 0; i = i.add(BigInteger.ONE)){
        result = (result.multiply(base).mod(mod));
      }
      return result;
  }

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

  public static BigInteger montgomeryMult(BigInteger base, BigInteger exponent, BigInteger mod){
    BigInteger digitBaseMin = new BigInteger("10");
    BigInteger TEN = new BigInteger("10");

    long powerCount = 1;
    BigInteger xPrime, yPrime;
    BigInteger result;
    //Find R
    while(base.compareTo(digitBaseMin) > 0){
      digitBaseMin = digitBaseMin.multiply(TEN);
      powerCount +=1;
    }
    xPrime = (base.multiply(digitBaseMin)).mod(mod);
    yPrime = (exponent.multiply(digitBaseMin)).mod(mod);
    result = xPrime.multiply(yPrime);
    System.out.println("answer thus far = " + result);

    result = result.add(mod.multiply(4));
    System.out.println("answer thus far = " + result);

    result = result.add(mod.multiply(20));
    System.out.println("answer thus far = " + result);

    result = result.divide(digitBaseMin);
    System.out.println("answer thus far = " + result);

    result =   mod.modInverse(1/digitBaseMin).mod(mod);
    System.out.println("answer thus far = " + result);




    return answer;
  }




  public static void main(String[] args){
    BigInteger answer;
    BigInteger b = BigInteger.ZERO, e = BigInteger.ZERO, m = BigInteger.ZERO;

    if(args.length < 2 ){
      System.out.println("\tUsage: java Project1 <base> <exponent> <mod> <algorithm>");
      System.exit(-1);
    }
    try{
      b = new BigInteger(args[0]);
      e = new BigInteger(args[1]);
      m = new BigInteger(args[2]);
    }
    catch(Exception e1){
      System.out.println("\t <base> <exponent> and <mod> must be integer values");
      System.exit(-1);
    }
    System.out.println("\tbase: " + b + " exponent: " + e + " mod: " + m);

    /**
    * Java modPow
    */
    System.out.println("\tRunning modular exponentiation with Java modPow..");
    long startTime1 = System.nanoTime();
    answer = javaDefault(b, e, m);
    long endTime1 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava modPow took " + (endTime1 - startTime1) + " milliseconds");
    System.out.println();

    System.out.println("\tRunning modular exponentiation with java slow...");
    long startTime4 = System.nanoTime();
    answer = javaSlow(b, e, m);
    long endTime4 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava slow " + (endTime4 - startTime4) + " milliseconds");
    System.out.println();

    System.out.println("\tRunning modular exponentiation with java fast...");
    long startTime2 = System.nanoTime();
    answer = javaFast(b, e, m);
    long endTime2 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava fast " + (endTime2 - startTime2) + " milliseconds");
    System.out.println();


    System.out.println("\tRunning modular exponentiation with divide and conquer...");
    long startTime3 = System.nanoTime();
    answer = divideNConquer(b, e, m);
    long endTime3 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava divide n conquer " + (endTime3 - startTime3) + " milliseconds");
    System.out.println();

    System.out.println("\tRunning modular exponentiation with montgomeryMult...");
    long startTime5 = System.nanoTime();
    answer = montgomeryMult(b, e, m);
    long endTime5 = System.nanoTime();
    System.out.println("\tAnswer: " + answer);
    System.out.println("\tJava divide n conquer " + (endTime5 - startTime5) + " milliseconds");
    System.out.println();

  }
}
