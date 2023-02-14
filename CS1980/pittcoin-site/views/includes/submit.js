window.addEventListener('load', function() {
  if (typeof web3 == 'undefined') {
    var button = document.getElementById('submit')
    button.disabled = true;
    button.value = "No MetaMask Found!"
  }
})

async function submitQ() {

  var button = document.getElementById("submit")
  button.disabled = true
  button.value = "Confirm on Metamask"

  const eth = new Eth(web3.currentProvider)
  const pittcoin = eth.contract(abi).at(address)

  document.getElementById("question").disabled = true
  question = document.getElementById("question").value

  let txhash = await pittcoin.ask(question, {from: web3.eth.accounts[0]})

  button.value = "Confirming..."

  postQuestion(question, txhash, undefined)

}

function postQuestion(question, txhash, r) {
  if (r == undefined) {
    web3.eth.getTransactionReceipt(txhash, (err, result) => {
        postQuestion(question, txhash, result)
    })
    return
  }

  qid = Number(r.logs[1].data)

  //send data to server
  var request = new XMLHttpRequest()

  request.onload = function () {
    location.reload()
  }

  var data = {
    "question":question,
    "qid":qid
  }

  request.open('POST', '/ask', true)
  request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
  request.send(JSON.stringify(data))
}

async function submitA() {

  var button = document.getElementById("submit")
  button.disabled = true
  button.value = "Confirm on Metamask"

  const eth = new Eth(web3.currentProvider)
  const pittcoin = eth.contract(abi).at(address)

  document.getElementById("answer").disabled = true
  var url = location.href.split('/');
  answer = document.getElementById("answer").value

  let txhash = await pittcoin.answer(Number(url[url.length-1]), {from: web3.eth.accounts[0]})

  button.value = "Confirming..."

  postAnswer(answer, txhash, undefined)
}

function postAnswer(answer, txhash, r) {
  if (r == undefined) {
    web3.eth.getTransactionReceipt(txhash, (err, result) => {
        postAnswer(answer, txhash, result)
    })
    return
  }

  qid = Number(r.logs[1].data)

  //send data to server
  var request = new XMLHttpRequest()

  request.onload = function () {
    location.reload()
  }

  var data = {
    "answer":answer,
    "qid":qid
  }

  request.open('POST', '/submitanswer', true)
  request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
  request.send(JSON.stringify(data))
}

const address = '0x33f975be1d7f660ce128ba13d173a94d61f97336'
const abi = [
	{
		"constant": false,
		"inputs": [
			{
				"name": "questionID",
				"type": "uint256"
			}
		],
		"name": "answer",
		"outputs": [
			{
				"name": "qid",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "name",
		"outputs": [
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "spender",
				"type": "address"
			},
			{
				"name": "tokens",
				"type": "uint256"
			}
		],
		"name": "approve",
		"outputs": [
			{
				"name": "success",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "totalSupply",
		"outputs": [
			{
				"name": "totalSupply",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "questionText",
				"type": "string"
			}
		],
		"name": "ask",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "from",
				"type": "address"
			},
			{
				"name": "to",
				"type": "address"
			},
			{
				"name": "tokens",
				"type": "uint256"
			}
		],
		"name": "transferFrom",
		"outputs": [
			{
				"name": "success",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "decimals",
		"outputs": [
			{
				"name": "",
				"type": "uint8"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"name": "questions",
		"outputs": [
			{
				"name": "questionHash",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "_totalSupply",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "tokenOwner",
				"type": "address"
			}
		],
		"name": "balanceOf",
		"outputs": [
			{
				"name": "balance",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "symbol",
		"outputs": [
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "to",
				"type": "address"
			},
			{
				"name": "tokens",
				"type": "uint256"
			}
		],
		"name": "transfer",
		"outputs": [
			{
				"name": "success",
				"type": "bool"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "tokenOwner",
				"type": "address"
			},
			{
				"name": "spender",
				"type": "address"
			}
		],
		"name": "allowance",
		"outputs": [
			{
				"name": "remaining",
				"type": "uint256"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"name": "from",
				"type": "address"
			},
			{
				"indexed": true,
				"name": "to",
				"type": "address"
			},
			{
				"indexed": false,
				"name": "tokens",
				"type": "uint256"
			}
		],
		"name": "Transfer",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"name": "tokenOwner",
				"type": "address"
			},
			{
				"indexed": true,
				"name": "spender",
				"type": "address"
			},
			{
				"indexed": false,
				"name": "tokens",
				"type": "uint256"
			}
		],
		"name": "Approval",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"name": "qid",
				"type": "uint256"
			}
		],
		"name": "asked",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"name": "qid",
				"type": "uint256"
			}
		],
		"name": "answered",
		"type": "event"
	}
]