'use strict';

/**
 * https://github.com/maxnachlinger/reactjs-websocket-example/blob/master/client/src/socketService.js
 */
function SocketService() {
	var service = {};
    var connected = false;
	var ws;

	function init() {
		service = {};

		ws = new WebSocket("ws://localhost:9000/ws");
		ws.onopen = function () {
            console.log("got onopen");
            connected = true;
		};
		ws.onclose = function() {
            console.log("got onclose");
            connected = false;
		};
		ws.onmessage = function (message) {
            console.log("got message");
		};
	}

	init();

    function wrapMessage(msgType, msg){
        return {
            type: msgType,
            payload: msg
        }
    }

	function setState(msg){
	    console.log('call setState()');
	    msg = wrapMessage("select", msg);
	    call(msg);
	}

    var onMsgOutListener = [];
    function addOnMsgOut(callback){
        console.log("added onMsgOutListener callback listener: " . callback);
        onMsgOutListener.push(callback);
    }

    //private
    function call(msg){
        //fist: send via WS

        //second: inform listener
        var arrayLength = onMsgOutListener.length;
        for (var i = 0; i < arrayLength; i++) {
            onMsgOutListener[i](msg);
        }
    }

    service.setState = setState;
    service.addOnMsgOut = addOnMsgOut;

    return service;
}
