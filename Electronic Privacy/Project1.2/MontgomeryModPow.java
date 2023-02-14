/* Ed Anderson, CS 1699 - Privacy In Electronic Society
* eca20@pitt.edu
*/

import java.math.BigInteger;
import java.util.Objects;
public final class MontgomeryModPow {

	private BigInteger mod;  							//odd number at least 3
	private BigInteger montReducer;       // 1 shifted left by a multiple of 8
	private int reducerBits;          		// a multiple of 8 calculated by bit length of the input mod
	private BigInteger reciprocal;    		//reciprocal of the reducer % mod
	private BigInteger bitMask;          	//to get last bit of reducer through an AND for multiplication
	private BigInteger factor;        		//((reducer * reducer^-1 - 1) / n)

	// The mod must be odd and >= 3
	public MontgomeryModPow(BigInteger mod) {

		// test mod
		if (!mod.testBit(0) || mod.compareTo(BigInteger.ONE) <= 0){
			System.out.println("Mod must be greater than 3 and odd... Exiting.");
			System.exit(-1);
		}
		else{
			this.mod = mod;

			// reduce
			reducerBits = (mod.bitLength() / 8 + 1) * 8;
			montReducer = BigInteger.ONE.shiftLeft(reducerBits);  //shift left by log2 of the number of reducer bits
			bitMask = montReducer.subtract(BigInteger.ONE);
			reciprocal = montReducer.modInverse(mod);
			factor = montReducer.multiply(reciprocal).subtract(BigInteger.ONE).divide(mod);
		}
	}

	/* multiplication of montgomery numbers, result also in montgomery form
	*/
	public BigInteger multiply(BigInteger result, BigInteger base) {
		BigInteger product = result.multiply(base);
		BigInteger temp = product.and(bitMask).multiply(factor).and(bitMask);
		BigInteger reduced = product.add(temp.multiply(mod)).shiftRight(reducerBits);
		BigInteger output;

		if(reduced.compareTo(mod) < 0){
			output = reduced;
		}
		else{
			output = reduced.subtract(mod);
		}
		return output;
	}

	/*
	* X and output are in montgomery form. Y is normal form.
	*/
	public BigInteger power(BigInteger base, BigInteger exponent) {
		BigInteger result = montReducer.mod(mod); //start at reducer % mod

		for (int i = 0, len = exponent.bitLength(); i < len; i++) {
			if (exponent.testBit(i)){
				result = multiply(result, base);
			}
			base = multiply(base, base);
		}
		return result;
	}

	public BigInteger convertInput(BigInteger x) {
		return x.shiftLeft(reducerBits).mod(mod);
	}

	public BigInteger convertOutput(BigInteger x) {
		return x.multiply(reciprocal).mod(mod);
	}


}
