'use strict';

/**
 * https://github.com/maxnachlinger/reactjs-websocket-example/blob/master/client/src/socketService.js
 */
function SocketService(onReadyCallback) {
	var service = {};
    var connected = false;
	var ws;

	function init() {
		service = {};

		ws = new WebSocket("ws://localhost:9000/ws");
		ws.onopen = function () {
            console.log("got onopen");
            connected = true;
            console.log(onReadyCallback);
            onReadyCallback();
		};
		ws.onclose = function() {
            console.log("got onclose");
            connected = false;
		};
		ws.onmessage = function (message) {
		    console.log(message);
            var arrayLength = onMsgInListener.length;
            for (var i = 0; i < arrayLength; i++) {
                onMsgInListener[i](JSON.parse(message.data));
            }
		};
	}

	init();

    function wrapMessage(msgType, msg){
        return {
            msgType: msgType,
            payload: msg
        }
    }

    function ping(){
        var msg = wrapMessage("pingPong", {msg: "ping"});
        call(msg);
    }

	function setState(msg){
	    console.log('call setState()');
	    console.log(msg);
	    if(undefined == msg.type){
	        msg.type = "";
	    }
        var msg = wrapMessage("selectField", {pieceType: msg.type, fieldName: msg.field});

        console.log(msg);

        call(msg);
	}

    function setPieceToField(pieceType, fieldName){
        var msg = wrapMessage("setPieceToField", {pieceType: pieceType, fieldName: fieldName});
        call(msg);
    }

    var onMsgOutListener = [];
    function addOnMsgOut(callback){
        console.log("added onMsgOutListener callback listener: " + callback);
        onMsgOutListener.push(callback);
    }

    var onMsgInListener = [];
    function addOnMsgIn(callback){
        console.log("added onMsgInListener callback listener: " + callback);
        onMsgInListener.push(callback);
    }

    //private
    function call(msg){
        //fist: send via WS
        ws.send(JSON.stringify(msg));

        //second: inform listener
        var arrayLength = onMsgOutListener.length;
        for (var i = 0; i < arrayLength; i++) {
            onMsgOutListener[i](msg);
        }
    }

    service.setState = setState;
    service.addOnMsgOut = addOnMsgOut;
    service.addOnMsgIn = addOnMsgIn;
    service.setPieceToField = setPieceToField;
    service.ping = ping;

    return service;
}
