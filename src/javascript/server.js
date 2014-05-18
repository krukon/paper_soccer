var net = require('net');
var PORT = 1444;
var listOfAvailableGames = [];

function handlePacket(data){
    var packet = JSON.parse(data);
    if(packet.request=="newRoom"){
        console.log("Move from user: " + packet.username);
        console.log("X coordinate: " + packet.moveX);
        console.log("Y coordinate: " + packet.moveY);
    }
}
net.createServer(function(sock) {
    
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);

    sock.on('data', function(data) {
        handlePacket(data);
    });

    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
    });

}).listen(PORT);

console.log('Server listening on ' + PORT);
