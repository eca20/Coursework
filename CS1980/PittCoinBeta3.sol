pragma solidity ^0.4.24;

// ----------------------------------------------------------------------------
// 'PittCoin' token contract
//
// Deployed to      : 0x6DDFc23606B2221Fae7837c4f979aEa25b250341
// Symbol           : H2PBeta3
// Name             : PittCoinBeta3
// Total supply     : 1,000,000,000
// Decimals         : 0
//
// Notes            : Some methods are implmented to satisfy the ERC-20 
//                  : interface, but are not needed for the current
//                  : state purpose of PittCoin, the "stack overflow"
//                  : model.
// ----------------------------------------------------------------------------

// ----------------------------------------------------------------------------
// ERC20 Token, with the addition of symbol, name and decimals and assisted
// token transfers
// ----------------------------------------------------------------------------
contract PittCoinBeta3{
    string public symbol;
    string public  name;
    uint8 public decimals;
    uint public _totalSupply;

    struct Question {
        uint256 questionHash;
    }

    Question[] public questions;
    
    event Transfer(address indexed from, address indexed to, uint tokens);
    event Approval(address indexed tokenOwner, address indexed spender, uint tokens);
    event asked(uint qid);
    event answered(uint256 qid);
    event error(uint256 errorID);
    
    mapping(address => uint) balances;
    mapping(address => mapping(address => uint)) allowed;
    mapping(uint256 => bool) questionToAnswered;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    constructor() public {
        symbol = "H2PBeta3";
        name = "PittCoinBeta3";
        decimals = 0;
        _totalSupply = 1000000000;
        balances[0x94d546081A2C829B4a0d8617922a210b078e593e] = _totalSupply;
        emit Transfer(address(0), 0x94d546081A2C829B4a0d8617922a210b078e593e, _totalSupply);
    }

    // ------------------------------------------------------------------------
    // Total supply
    // ------------------------------------------------------------------------
    function totalSupply() public constant returns (uint256 totalSupply) {
        return _totalSupply  - balances[address(0)];
    }

    // ------------------------------------------------------------------------
    // Get the token balance for account tokenOwner
    // ------------------------------------------------------------------------
    function balanceOf(address tokenOwner) public constant returns (uint256 balance) {
        return balances[tokenOwner];
    }

    // ------------------------------------------------------------------------
    // Transfer the balance from token owner's account to to account
    // - Owner's account must have sufficient balance to transfer
    // - 0 value transfers are allowed
    // ------------------------------------------------------------------------
    function transfer(address to, uint tokens) public returns (bool success) {
        balances[msg.sender] = sub(balances[msg.sender], tokens);
        balances[to] = add(balances[to],tokens);
        emit Transfer(msg.sender, to, tokens);
        return true;
    }

    // ------------------------------------------------------------------------
    // Token owner can approve for spender to transferFrom(...) tokens
    // from the token owner's account
    // ------------------------------------------------------------------------
    function approve(address spender, uint tokens) public returns (bool success) {
        allowed[msg.sender][spender] = tokens;
        emit Approval(msg.sender, spender, tokens);
        return true;
    }

    // ------------------------------------------------------------------------
    // Transfer tokens from the from account to the to account
    // ------------------------------------------------------------------------
    function transferFrom(address from, address to, uint tokens) public returns (bool success) {
        balances[from] = sub(balances[from], tokens);
        balances[to] = add(balances[to], tokens);
        emit Transfer(from, to, tokens);
        return true;
    }

    // ------------------------------------------------------------------------
    // Returns the amount of tokens approved by the owner that can be
    // transferred to the spender's account
    // ------------------------------------------------------------------------
    function allowance(address tokenOwner, address spender) public constant returns (uint remaining) {
        return allowed[tokenOwner][spender];
    }

    // ------------------------------------------------------------------------
    // Ask a question, takes 100 PittCoin from users account to escrow
    // ------------------------------------------------------------------------
    function ask(string questionText) public returns (uint256){
        if(balanceOf(msg.sender) >= uint256(100)){
        uint256 questionHash = generateQuestionHash(questionText);
        uint256 qid = questions.push(Question(questionHash));
        transfer(address(0), 100);
        emit asked(qid);
        return qid;
        }
        else{
            emit error(0);
            return 0;
        }

    }

    function generateQuestionHash(string text) private pure returns(uint256 hash){
        return uint256(keccak256(abi.encodePacked(text)));
    }
    
    // ------------------------------------------------------------------------
    // Answer a question, takes 100 PittCoin from escrow account to user
    // ------------------------------------------------------------------------
    function answer(uint256 questionID) public returns (uint256 qid){
        questionToAnswered[questionID] = true;
        balances[msg.sender] = add(balances[msg.sender], 100);
        emit Transfer(address(0), msg.sender, 100);
        emit answered(questionID);
        return questionID;
    }


    // ------------------------------------------------------------------------
    // Safe Math implementations
    // ------------------------------------------------------------------------
    function add(uint256 a, uint256 b) private pure returns (uint256 c) {
          c = a + b;
          assert(c >= a);
          return c;
    }
    function sub(uint256 a, uint256 b) private pure returns (uint256 c) {
          assert(b <= a);
          c = a - b;
          return c;
    }
    function mul(uint256 a, uint256 b) private pure returns (uint256 c) {
          c = a * b;
          assert(a == 0 || c / a == b);
          return c;
    }
    function div(uint256 a, uint256 b) private pure returns (uint256 c) {
          assert(b > 0);
          c = a / b;
          return c;
    }

}
