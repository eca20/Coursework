/**
*   Ed Anderson
*   CS 1699
*   Due Oct 4th
*   Deliverable 1
*   A second representation of a block as a transaction, for use in processing transactions
*   This may seem redundant, and thats because it is. I started the project
*   in the wrong direction, and this was a course adjustment.
*   
*/


public class Transaction {
    public String prevHash;
    public String coin;
    public String type;
    public String destination;
    public String source;
    public String signature;

    public Transaction(String c, String t, String s, String d, String sig1, String p){
      coin = c;
      type = t;
      destination = d;
      source = s;
      signature = sig1;
      prevHash = p;
    }

    public String toString(){
      return this.type + " " + this.coin + " from " + this.source.substring(this.source.length()-5, this.source.length()) + " to " + this.destination.substring(this.destination.length()-5, this.destination.length()) + " with signature " + this.signature.substring(this.signature.length()-5, this.signature.length());
    }
    public String getVerifyString(){
      return this.prevHash + "," + this.type + "," + this.coin + "," + this.destination;

    }
}
