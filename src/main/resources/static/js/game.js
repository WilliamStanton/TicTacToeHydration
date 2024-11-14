const buttons = document.getElementById("board").getElementsByTagName('button');
const title = document.getElementById('title');
const restartBtn = document.getElementById('restart');
const settingsBtn = document.getElementById('settings');
let gameStatus = 'INCOMPLETE';
let lastMove = '';

// init title after 1s
setTimeout(() => {
    updateGameStatus();
}, 1000);

// settings btn
settingsBtn.addEventListener('click', () => {
    restart(true)
});

// restart btn
restartBtn.addEventListener('click', () => {
    restart(false)
});

// restart game
function restart(toggleMode) {
    fetch(toggleMode ? 'http://localhost:8080/settings' : 'http://localhost:8080/restart', {
        method: "PATCH"
    })
        .then(() => {
            // clear board spots
            for (let button of buttons) {
                button.innerText = '';
                button.disabled = false;
                button.classList.remove('winningSpot')
            }

            // get & set next player
            fetch("http://localhost:8080/next")
                .then((response) => {
                    if (response.ok)
                        return response.json();
                })
                .then((data) => {
                    // set title
                    title.innerText = data.name + " is up!";

                    // reset gameStatus && next move
                    gameStatus = 'INCOMPLETE';
                    lastMove = '';
                })
        })

    // update game mode name
    if (toggleMode)
        settingsBtn.innerText = settingsBtn.innerText === '⚙️ Play AI' ? '⚙️ Play local' : '⚙️ Play AI';
}

// update game status
function updateGameStatus() {
    fetch("http://localhost:8080/next")
        .then((response) => {
            if (response.ok)
                return response.json();
            // else, game over
            else {
                title.innerText = "Game over!";
                fetch("http://localhost:8080/winner")
                    .then((response) => {
                        // winner
                        if (response.ok)
                            return response.json();
                        // tie
                        else {
                            title.innerText = "Game tied!";
                        }
                    })
                    .then(data => {
                        // update all winning spots
                        for (let i = 0; i < data.length; i++) {
                            document.getElementById(data[i].id).classList.add('winningSpot');
                        }
                        // update title
                        title.innerText = data[0].player.name + " won!";
                    })
            }
        })
        .then(data => {
            title.innerText = data.name + " is up!";
        });
}

// update spots
for (let button of buttons) {
    button.addEventListener('click', e => {
        if (e.target.innerText === '' && gameStatus === 'INCOMPLETE') {
            let id = e.target.id;
            fetch("http://localhost:8080/next?id=" + id, {
                method: "POST"
            }).then((response) => {
                if (response.ok)
                    return response.json();
                else
                    throw new Error("Unknown Error");
            }).then(data => {
                // disable specific spot
                document.getElementById(e.target.id).disabled = true;

                // update entire board and next player
                let boardSpots = data.board.boardSpots;
                for (let i = 0; i < boardSpots.length; i++) {
                    for (let j = 0; j < boardSpots[i].length; j++) {
                        let boardSpot = boardSpots[i][j];
                        document.getElementById(boardSpot.id).innerText = boardSpot.player.symbol;
                    }
                }

                // update status
                switch (data.gameStatus) {
                    case 'INCOMPLETE':
                        title.innerText = data.nextPlayer.name + " is up!"; // update next player
                        lastMove = data.nextPlayer.name;
                        break;
                    case 'WON':
                        // display won
                        title.innerText = lastMove + ' won!';
                        gameStatus = 'WON';

                        // get winning spots
                        fetch("http://localhost:8080/winner")
                            .then((response) => {
                                if (response.ok)
                                    return response.json();
                                else
                                    throw new Error("Unknown Error");
                            })
                            .then(data => {
                                // update all winning spots
                                for (let i = 0; i < data.length; i++) {
                                    document.getElementById(data[i].id).classList.add('winningSpot');
                                }
                                // update title
                                title.innerText = data[0].player.name + " won!";
                            });
                        disableAllSpots();
                        break;
                    case 'TIED':
                        // display tie
                        title.innerText = 'Game tied!'
                        gameStatus = 'TIED';
                        disableAllSpots();
                        break;
                }
            });
        } else {
            // modify title for 5s to display error
            let text = title.innerText;
            title.innerText = (gameStatus === 'INCOMPLETE') ? 'Incorrect move..' : 'Game over..';
            setTimeout(() => {
                title.innerText = text;
            }, 5000);
        }
    });
}

// disables all spots
function disableAllSpots() {
    for (let button of buttons) {
        button.disabled = true;
    }
}