var net, PORT, controller;

net = require('net')

PORT = 1444

controller = require('./game_controller')

net.createServer(function(sock) {
    
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);

    sock.on('data', function(raw) {
        var message, data, handleRequest, blocks;
        console.log('REQUEST: ' + raw)
        console.log('FROM: ', sock.roles, '\n')

        handleRequest = function(request) {
            try {
                message = JSON.parse(request)
                data = message.data
                switch (message.type) {
                    case 'list_games':
                        controller.listGames(sock); break;
                    
                    case 'create_game':
                        controller.createGame(sock, data['host_name'], data['width'], data['height']); break;
                    
                    case 'join_game':
                        controller.joinGame(sock, data['id'], data['guest_name']); break;

                    case 'watch_game':
                        controller.watchGame(sock, data['id'], data['spectator_name']); break;
                    
                    case 'start_game':
                        controller.startGame(sock, data['id']); break;
                    
                    case 'finish_game':
                        controller.finishGame(sock, data['id'], data); break;
                    
                    case 'register_move':
                        controller.registerMove(sock, data['id'], data); break;
                    
                    case 'request_next_move':
                        controller.requestNextMove(sock, data['id']); break;
                    
                    case 'get_next_move':
                        controller.getNextMove(sock, data['id'], data); break;
                    
                    case 'close_game':
                        controller.closeGame(sock, data['id']); break;
                    
                    case 'chat':
                        controller.sendChatMessage(sock, data['id'], data); break;
                }
            } catch (e) {
                console.log("Incorrect request: " + e)
            }
        }

        blocks = raw.toString().split('\n')

        for (var i = 0; i < blocks.length; i++)
            if (blocks[i].length > 0)
                try {
                    console.log('BLOCK: ', blocks[i])
                    handleRequest(blocks[i])
                } catch (e) { console.log("ERRRRROR:", e)}

    });

    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.roles);
        try {
            controller.removeClient(sock)
        } catch (e) { console.log('closing error:',e) }
    });

}).listen(PORT);

console.log('Server listening on ' + PORT);
