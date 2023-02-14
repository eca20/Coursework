// index.js

const Web3 = require('web3');
const fs = require('fs');
const solc = require('solc');

const input = fs.readFileSync('PittCoin.sol');
const output = solc.compile(input.toString(), 1);

const provider = new Web3.providers.HttpProvider("http://localhost:8545");
const web3 = new Web3(provider);

const { interface, bytecode } = output.contracts[":PittCoin"];
const abi = JSON.parse(interface);
const contract = new web3.eth.Contract(abi);

const deployAndRunContract = async () => {
  const addresses = await web3.eth.getAccounts();
  const gasPrice = await web3.eth.getGasPrice();

  try {
    const contractInstance = contract.deploy({
      data: bytecode
    }).send({
      from: addresses[0],
      gas: 1000000,
      gasPrice,
    });

    console.log("Deployed at", contractInstance.options.address);

    const myName = await contractInstance.methods.getMyName().call();
    console.log("Result from blockchain:", myName);

  } catch (err) {
    console.log("Failed to deploy", err);
  }
}

deployAndRunContract();
