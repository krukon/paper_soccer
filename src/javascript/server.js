var net, PORT, controller;

net = require('net')

PORT = 1444

controller = require('./game_controller')

net.createServer(function(sock) {
    
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);

    sock.on('data', function(request) {
        var message, data;
        console.log('REQUEST: ' + request)
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
                
                case 'start_new_game':
                    controller.startGame(sock, data['id']); break;
                
                case 'finish_game':
                    controller.finishGame(sock, data['id'], data['result']); break;
                
                case 'register_move':
                    controller.registerMove(sock, data['id'], data['move']); break;
                
                case 'request_next_move':
                    controller.requestNextMove(sock, data['id']); break;
                
                case 'get_next_move':
                    controller.getNextMove(sock, data['id']); break;
                
                case 'close_game':
                    controller.closeGame(sock, data['id']); break;
                
                case 'chat':
                    controller.sendChatMessage(sock, data['id'], data); break;
            }
        } catch (e) {
            console.log("Incorrect request: " + e)
        }
    });

    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
        try {
            controller.removeClient(sock)
        } catch (e) {}
    });

}).listen(PORT);

console.log('Server listening on ' + PORT);
