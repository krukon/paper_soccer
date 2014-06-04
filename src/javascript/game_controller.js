Array.prototype.contains = function(e) {
    return this.indexOf(e) != -1
};

module.exports = (function () {

    var games, gameId, createGame, listGames, joinGame, watchGame,
        registerMove, getNextMove, startGame, finishGame, closeGame, notifyAll, notify,
        removeClient, requestNextMove, sendChatMessage, printAll,
        validate, removeRole;

    games = {}
    gameId = 0

    createGame = function(sock, hostName, width, height) {
        var response, game = {
            'id': gameId,
            'host': sock,
            'guest': null,
            'spectators': [],
            'active': false,
            'width': width,
            'height': height,
            'host_result': 0,
            'guest_result': 0,
            'host_name': hostName,
            'guest_name': null
        };

        response = {
            'type': 'create_game',
            'data': {
                'id': gameId
            }
        }

        sock.roles = sock.roles || {}
        sock.roles[gameId] = 'host'

        games[gameId] = game;
        gameId++

        notify(game['host'], response)
    }

    joinGame = function(sock, id, guestName) {
        var response, game;

        validate(sock, id, ['open'])

        game = games[id];
        game['guest'] = sock;
        game['guest_name'] = guestName;
        game['active'] = true;

        response = {
            'type': 'join_game',
            'data': {
                'id': id,
                'guest_name': guestName
            }
        }

        sock.roles = sock.roles || {}
        sock.roles[id] = 'guest'

        notify(game['host'], response)
        notifyAll(id, response)
    }

    watchGame = function(sock, id, spectatorName) {
        var response, game;

        validate(sock, id, ['nobody'])

        game = games[id];
        game['spectators'].push(sock)

        response = {
            'type': 'watch_game',
            'data': {
                'id': game['id'],
                'host_name': game['host_name'],
                'guest_name': game['guest_name'],
                'width': game['width'],
                'height': game['height']
            }
        }

        sock.roles = sock.roles || {}
        sock.roles[id] = 'host'

        notify(sock, response)
    }

    notify = function(sock, response) {
        try {
            if (sock.writable) {
                sock.write(JSON.stringify(response) + '\n')
                console.log('SENDING (' + sock.address + '): ' + JSON.stringify(response) + '\n')
            }
        } catch (e) { console.log("NOTIFY ERROR", e)}
    }

    notifyAll = function(id, response) {
        var game = games[id], i;
        notify(game['guest'], response)
        for (i in game['spectators'])
            notify(game['spectators'][i], response)
    }

    listGames = function(sock) {
        var response, id, data, game;

        response = {
            'type': 'list_games',
            'data': []
        }

        for (id in games) {
            game = games[id]
            data = {
                'id': game['id'],
                'host_name': game['host_name'],
                'guest_name': game['guest_name'],
                'width': game['width'],
                'height': game['height'],
                'active': game['active']
            }
            response['data'].push(data)
        }

        notify(sock, response)
    }

    registerMove = function(sock, id, data) {
        var response = {
            'type': 'register_move',
            'data': {
                'id': data['id'],
                'move': data['move'],
                'player': data['player']
            }
        };

        validate(sock, id, ['host'])

        notifyAll(id, response);
    }

    getNextMove = function(sock, id, data) {
        var response = {
            'type': 'get_next_move',
            'data': {
                'id': data['id'],
                'move': data['move'],
                'player': 'guest'
            }
        };

        validate(sock, id, ['guest'])

        notify(games[id]['host'], response)
    }

    requestNextMove = function(sock, id) {
        var response = {
            'type': 'request_next_move',
            'data': {
                'id': id
            }
        };

        validate(sock, id, ['host'])

        notify(games[id]['guest'], response)
    }

    startGame = function(sock, id) {
        var response, game;
        
        validate(sock, id, ['host'])

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

        notifyAll(id, response)
    }

    finishGame = function(sock, id, data) {
        var response, game;
        
        validate(sock, id, ['host'])

        game = games[id]

        response = {
            'type': 'finish_game',
            'data': data
        }

        notifyAll(id, response)
    }

    removeRole = function (sock, id) {
        try {
            delete sock.roles[id];
        } catch (e) {}
    }

    removeRoles = function(id) {
        var i, game = games[id];
        removeRole(game['host'], id)
        removeRole(game['guest'], id)
        for (i in game['spectators'])
            removeRole(game['spectators'][i], id)
    }

    closeGame = function(sock, id) {
        var response = {
            'type': 'close_game',
            'data': {
                'id': id
            }
        };

        validate(sock, id, ['player'])

        removeRoles(id)

        notify(games[id]['host'], response)
        notifyAll(id, response)
        delete games[id]
    }

    removeClient = function(sock) {
        var game, id;

        for (id in games)
            try {
                game = games[id]
                if (game['host'] === sock || game['guest'] === sock) {
                    closeGame(sock, id)
                }
                game['spectators'] = game['spectators'].filter(function(e) {
                    return e != sock;
                })
            } catch (e) { console.log("REMOVING CLIENT ERROR:", e)}
    }

    sendChatMessage = function(sock, id, data) {
        var game, response;

        validate(sock, id, ['anyone'])

        game = games[id]
        response = {
            'type': 'chat',
            'data': data
        }

        notify(game['host'], response)
        notifyAll(id, response)
    }

    validate = function(sock, id, arr) {
        var game = games[id], errors = [], result;

        if (arr === undefined)
            arr = []
        
        if (game === undefined)
            errors.push("Such game does not exist.")
        
        if (arr.contains('host') && game['host'] !== sock)
            errors.push("You are not the host of this game.")
        
        if (arr.contains('guest') && game['guest'] !== sock)
            errors.push("You are not the guest of this game.")
        
        if (arr.contains('player') && (game['guest'] !== sock && game['host'] !== sock))
            errors.push("You are not a player in this game.")
        
        if (arr.contains('spectator') && !game['spectators'].contains(sock))
            errors.push("You are not watching this game.")
        
        if (arr.contains('anyone') && (game['guest'] !== sock && game['host'] !== sock &&
                !game['spectators'].contains(sock)))
            errors.push("You are not in this game.")
        
        if (arr.contains('nobody') && (game['guest'] === sock || game['host'] === sock &&
                game['spectators'].contains(sock)))
            errors.push("You are already in this game.")

        if (arr.contains('open') && game['guest'] !== null)
                errors.push("Game is already running.")

        if (errors.length > 0) {
            try {
                sock.write(JSON.stringify({ type: 'error', data: errors }) + "\n")
            } catch (e) {}
            throw 'VALIDATION ERROR: ' + JSON.stringify(errors)
        }
    }

    return new (function GameController() {
        this.createGame = createGame,
        this.joinGame = joinGame,
        this.listGames = listGames,
        this.registerMove = registerMove,
        this.getNextMove = getNextMove,
        this.requestNextMove = requestNextMove,
        this.startGame = startGame,
        this.finishGame = finishGame,
        this.closeGame = closeGame,
        this.removeClient = removeClient,
        this.sendChatMessage = sendChatMessage
    })();
})();