pragma solidity ^0.4.0;

contract PittCoin {
  string myName = "Ed";

  function getMyName() public view returns(string) {
    return myName;
  }

  function changeMyName(string _newName) public {
    myName = _newName;
  }
}
