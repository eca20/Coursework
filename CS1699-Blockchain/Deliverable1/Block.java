/**
*   Ed Anderson
*   CS 1699
*   Due Oct 4th
*   Deliverable 1
*   Modified from code provided by: https://github.com/laboon/CS1699_Fall2018
*/


/**
 * A single block on the blockchain.
 */

public class Block implements HashableObject {

    // hash of entire previous line
    private String _prevHash;

    // COIN
    public String _coin;

    // Type of block
    public String _type;

    //COINSIG
    public String _coinsig_or_pk;

    // SIG
    public String _sig;

    // Hash pointer to predecessor
    private HashPointer _hp;

    public String getPrevHash(){
      return _prevHash;
    }

    public String getDataAsString() {
        return _prevHash + "," + _type + "," + _coin + "," + _coinsig_or_pk + "," + _sig;

    }

    public boolean validPointer() throws InvalidHashException {
        // If invalid, will throw an InvalidHashException

        return (_hp == null || _hp.referenceValid());
    }

    public Block previousBlock() throws InvalidHashException {
        if (validPointer()) {
            return (Block) _hp.getReference();
        } else {
            // This will actually never happen.
            // If invalid, an InvalidHashException will be thrown by validPointer call
            return null;
        }
    }

    public Block(Block predecessor, String data) {
        if (predecessor == null) {
            _prevHash = "0";
        } else {
            //left over from sample code for reference
          //  _prevHash = HashPointer.calculateHash(predecessor);
        }
        String delims = ",";
        String[] tokens = data.split(delims);
        _prevHash = tokens[0];
        _type = tokens[1];
        _coin = tokens[2];
        _coinsig_or_pk = tokens[3];
        _sig = tokens[4];
        _hp = new HashPointer(predecessor);
    }
}
