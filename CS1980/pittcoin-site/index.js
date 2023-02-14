//express setup
const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const port = 80

//connect to websocket for contract
const Web3 = require('web3')
const wsProvider = new Web3.providers.WebsocketProvider("wss://ropsten.infura.io/ws/v3/80839889b7174fff915ab3909e97ce97")
const web3 = new Web3(wsProvider);
const pittcoin = new web3.eth.Contract(require('./abi'), "0x33f975be1d7f660ce128ba13d173a94d61f97336")

//hold question data
questions = []

//get the balance info from contract for leaderboard
function displayLearderboard(res) {
    sortedBalances = []
    pittcoin.getPastEvents('Transfer', {filter: {}, fromBlock:5065833}, function (error, events) {
        if(!error) {
            balances = {}
            for(i = 0; i < events.length; i++) {
                if(events[i].event === 'Transfer') {
                    from = '0x' + events[i].raw.topics[1].substring(26,66)
                    if (from !== '0x0000000000000000000000000000000000000000') {
                        balances['0x' + events[i].raw.topics[1].substring(26,66)] -= parseInt(events[i].returnValues.tokens)
                    }
                    
                    to = '0x' + events[i].raw.topics[2].substring(26,66)
                    if (to !== '0x0000000000000000000000000000000000000000') {
                        if (balances[to] === undefined) balances[to] = 0;
                        balances['0x' + events[i].raw.topics[2].substring(26,66)] += parseInt(events[i].returnValues.tokens)
                    }
                }
            }
            for (var key in balances) {
                sortedBalances.push([key, balances[key]])
            }
            sortedBalances.sort(function(a,b){
                return b[1] - a[1]
            })
            res.render('leaderboard', sortedBalances)
        } else {
            console.log(error)
            res.render('leaderboard', [])
        }
    })
}

app.use(bodyParser.json())
app.set('view engine', 'pug')
//express routes
app.get('/', (req, res) => res.render('index', questions))
app.get('/leaderboard', (req, res) => displayLearderboard(res))
app.get('/answer/:qid', (req, res) => {
    if(questions[Number(req.params.qid)] == undefined) {
        res.send("404: Page not found")
        return
    }
    question = questions[Number(req.params.qid)]
    res.render('answer', question)
})

app.post('/ask', (req, res) => {
    questions[req.body.qid] = {"text":req.body.question, "answer":undefined}
    res.sendStatus(200)
})

app.post('/submitanswer', (req, res) => {
    questions[req.body.qid].answer = req.body.answer
    res.sendStatus(200)
})

app.listen(port, () => console.log(`Example app listening on port ${port}!`))