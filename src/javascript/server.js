var net, PORT, controller;

net = require('net')

PORT = 1444

controller = (function GameController() {

    var games, gameIds, gameId, createGame, listActiveGames, joinGame,
        registerMove, getNextMove, startGame, finishGame, closeGame, notify,
        isHost, isGuest, removeClient, requestNextMove, sendChatMessage, printAll;

    games = {}
    gameIds = []
    gameId = 0

    createGame = function(host, hostName, width, height) {
        var response, game = {
            'id': gameId,
            'host': host,
            'guest': undefined,
            'spectators': [],
            'active': false,
            'width': width,
            'height': height,
            'host_result': 0,
            'guest_result': 0,
            'host_name': hostName,
            'guest_name': undefined
        };

        response = {
            'type': 'create_game',
            'data': {
                'id': gameId
            }
        }

        gameIds.push(gameId);
        games[gameId] = game;
        gameId++

        try {
            game['host'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
    }

    joinGame = function(id, guest, guestName) {
        var response, game;

        game = games[id];

        if (game === undefined)
            return;
        if (game['guest'] !== undefined)
            return;
        game['guest'] = guest;
        game['guest_name'] = guestName;
        game['active'] = true;

        response = {
            'type': 'create_game',
            'data': null
        }

        try {
            game['host'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
    }

    notify = function(id, response) {
        var game, i;
        game = games[id];
        try {
            game['guest'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
        for (i = 0; i < game['spectators'].length; i++)
            try {
                game['spectators'][i].write(JSON.stringify(response) + "\n")
            } catch (e) {}
    }

    listActiveGames = function() {
        var response, i, data, game;

        response = {
            'type': 'list_games',
            'data': []
        }

        for (i = 0; i < gameIds.length; i++) {
            game = games[gameIds[i]]
            data = {
                'id': game['id'],
                'host_name': game['host_name'],
                'width': game['width'],
                'height': game['height'],
                'active': game['active']
            }
            response['data'].push(data)
        }

        return response
    }

    registerMove = function(id, move) {
        var response = {
            'type': 'register_move',
            'data': move
        };

        notify(id, response);
    }

    getNextMove = function(id, move) {
        var response = {
            'type': 'get_next_move',
            'data': move
        };
        try {
            games[id]['host'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
    }

    requestNextMove = function(id) {
        var response = {
            'type': 'request_next_move',
            'data': null
        };
        try {
            games[id]['guest'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
    }

    startGame = function(id) {
        var response, game;

        game = games[id]

        response = {
            'type': 'start_game',
            'data': {
                'width': game['width'],
                'height': game['height'],
                'host_name': game['host_name'],
                'guest_name': game['guest_name']
            }
        }

        notify(id, response)
    }

    finishGame = function(id, result) {
        var response, game;

        game = games[id]

        response = {
            'type': 'finish_game',
            'data': result
        }

        notify(id, response)
    }

    closeGame = function(id) {
        var response = {
            'type': 'close_game',
            'data': {
                'id': id
            }
        };

        try {
            games[id]['host'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
        notify(id, response)

        gameIds = gameIds.filter(function(e) { return e != id })
        delete games[id]
    }

    isHost = function(id, sock) {
        return games[id]['host'] == sock
    }

    isGuest = function(id, sock) {
        return games[id]['guest'] == sock
    }

    removeClient = function(sock) {
        var game, i, id;

        for (id in games)
            try {
                game = games[id]
                if (game['host'] == sock || game['guest'] == sock)
                    closeGame(game['id'])
                i = game['spectators'].indexOf(sock);
                if (i != -1) {
                    game['spectators'] = game['spectators'].filter(function(e, k) {
                        return k != i;
                    })
                }
            } catch (e) {}
    }

    sendChatMessage = function(id, data) {
        var game, response;
        game = games[id]
        response = {
            'type': 'chat',
            'data': data
        }
        try {
            game['host'].write(JSON.stringify(response) + "\n")
        } catch (e) {}
        notify(id, response)
    }

    printAll = function() {
        var game;
        console.log("GAME: " + gameId);
        console.log("GAME[0]: " + games[0]);
        for (id in games) {
            game = games[id]
            console.log(" " + game['id']);
        }
    }

    return {
        'createGame': createGame,
        'joinGame': joinGame,
        'listGames': listActiveGames,
        'registerMove': registerMove,
        'getNextMove': getNextMove,
        'requestNextMove': requestNextMove,
        'startGame': startGame,
        'finishGame': finishGame,
        'closeGame': closeGame,
        'isHost': isHost,
        'isGuest': isGuest,
        'removeClient': removeClient,
        'sendChatMessage': sendChatMessage,
        'printAll': printAll
    };
})();

net.createServer(function(sock) {
    
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);

    sock.on('data', function(data) {
        console.log('DATA: ' + data)
        var message;
        try {
            message = JSON.parse(data)
            data = message.data
            switch (message.type) {
                case 'list_games':
                    sock.end(controller.listGames())
                    break;
                case 'create_game':
                    controller.createGame(sock, data['host_name'], data['width'], data['height'])
                    break;
                case 'join_game':
                    if (!controller.isHost(data['id'], sock))
                    controller.joinGame(data['id'], sock, data['guest_name'])
                    break;
                case 'start_new_game':
                    if (controller.isHost(data['id'], sock))
                        controller.startGame(data['id'])
                    break;
                case 'finish_game':
                    if (controller.isHost(data['id'], sock))
                        controller.finishGame(data['id'], data['result'])
                    break;
                case 'register_move':
                    if (controller.isHost(data['id'], sock))
                        controller.registerMove(data['id'], data['move'])
                    break;
                case 'request_next_move':
                    if (controller.isHost(data['id'], sock))
                        controller.requestNextMove(data['id'])
                    break;
                case 'get_next_move':
                    if (controller.isGuest(data['id'], sock))
                        controller.getNextMove(data['id'])
                    break;
                case 'close_game':
                    if (controller.isHost(data['id'], sock) || controller.isGuest(data['id'], sock))
                        controller.closeGame(data['id'])
                    break;
                case 'chat':
                    controller.sendChatMessage(data['id'], data)
                    break;
            }
        } catch (e) {
            console.log("Incorrect request: " + e + " message=" + JSON.stringify(message))
        }
    });

    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
        try {
            controller.removeClient(sock)
        } catch (e) {
            console.log("Error closing game: " + e + " data=" + data)
        }
    });

}).listen(PORT);

console.log('Server listening on ' + PORT);
