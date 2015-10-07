// white
var whiteKing   = {code: "&#9812;", name: "whiteKing"};
var whiteQueen  = {code: "&#9813;", name: "whiteQueen"};
var whiteRock   = {code: "&#9814;", name: "whiteRock"};
var whiteBishop = {code: "&#9815;", name: "whiteBishop"};
var whiteKnight = {code: "&#9816;", name: "whiteKnight"};
var whitePawn   = {code: "&#9817;", name: "whitePawn"};

// black
var blackKing   = {code: "&#9818;", name: "blackKing"};
var blackQueen  = {code: "&#9819;", name: "blackQueen"};
var blackRock   = {code: "&#9820;", name: "blackRock"};
var blackBishop = {code: "&#9821;", name: "blackBishop"};
var blackKnight = {code: "&#9822;", name: "blackKnight"};
var blackPawn   = {code: "&#9823;", name: "blackPawn"};



var troops = new Array ();
troops["A1"] = whiteRock;
troops["A2"] = whiteBishop;
troops["A3"] = whiteKnight;
troops["A4"] = whiteQueen;
troops["A5"] = whiteKing;
troops["A6"] = whiteKnight;
troops["A7"] = whiteBishop;
troops["A8"] = whiteRock;
troops["B1"] = whitePawn;
troops["B2"] = whitePawn;
troops["B3"] = whitePawn;
troops["B4"] = whitePawn;
troops["B5"] = whitePawn;
troops["B6"] = whitePawn;
troops["B7"] = whitePawn;
troops["B8"] = whitePawn;

troops["H1"] = blackRock;
troops["H2"] = blackBishop;
troops["H3"] = blackKnight;
troops["H4"] = blackQueen;
troops["H5"] = blackKing;
troops["H6"] = blackKnight;
troops["H7"] = blackBishop;
troops["H8"] = blackRock;
troops["G1"] = blackPawn;
troops["G2"] = blackPawn;
troops["G3"] = blackPawn;
troops["G4"] = blackPawn;
troops["G5"] = blackPawn;
troops["G6"] = blackPawn;
troops["G7"] = blackPawn;
troops["G8"] = blackPawn;

function getPiece(field){
    return troops[field];
}

function rows(){
    var foo = ['A','B','C','D','E','F','G','H']
    return foo;
}

function columns() {
    var start = 1;
    var end = 8;
    var foo = [];
    for (var i = start; i <= end; i++) {
        foo.push(i);
    }
    return foo;
}

var Field = React.createClass({

    mouseOver: function () {
        this.props.root.currentHover(this);
    },

    mouseOut: function () {
        this.props.root.currentHover(null);
    },

	getInitialState: function() {
		return {
			selectedClass: ""
		};
	},

	unSelect: function(){
	   this.setState({selectedClass: ""});
	},

	getClassNames: function(){
	    return this.props.className + " " + this.state.selectedClass;
	},

	getInitialState: function(){
	    var piece = getPiece(this.props.field);
	    if(piece != undefined){
	        //send this piece on this field to akka
	        this.props.root.setPieceToField(piece.name, this.props.field);
	        
            return{
                fieldIcon: piece.code,
                pieceName: piece.name
            }
	    }
	    else{
	        return {};
	    }
	},

    onClick: function(currentField){
        this.setState({selectedClass: "selected"});
        this.props.root.doSelect(this);
    },

    render: function(){
        return(<span onMouseOver={this.mouseOver} onClick={this.onClick} className={this.getClassNames()} onMouseOut={this.mouseOut}  dangerouslySetInnerHTML={{__html: this.state.fieldIcon }}></span>)
    }
});

var WhiteField = React.createClass({

    render: function(){
        return(<Field root={this.props.root} className="box white" field={this.props.field}  />)
    }
});

var BlackField = React.createClass({
    render: function(){
        return(<Field root={this.props.root} className="box black" field={this.props.field} />)
    }
});

var ChessBoardField = React.createClass({
    render: function(){
        var field = ""+this.props.rowChar+""+this.props.colNumber;
        if(this.props.fieldId % 2 == 0){
            return(<WhiteField root={this.props.root} field={field}/>);
        }
        else{
          return(<BlackField root={this.props.root} field={field} />);
        }
    }
});

var ChessBoardRow = React.createClass({

    render: function(){
        var row = this.props.row;
        parent = this;
        return(
        <div className="row">
        {this.props.columns.map(function(i) {
                  return <ChessBoardField  root={parent.props.root} key={i} fieldId={i+row.charCodeAt(0)} rowChar={row} colNumber={i} />;
        })}
        </div>
    );
    }
});

var ChessBoard = React.createClass({

    render: function() {
        var rows = this.props.rows;
        var columns = this.props.columns;
        parent = this;
        return(
        <div>
           {rows.map(function(i) {
                     return <ChessBoardRow  key={i} row={i} root={parent.props.root} columns={columns} />;
           })}
        </div>
                );
    }
});

var Info = React.createClass({

    onMsgOut: function(msg) {
        newLog = "--> " +JSON.stringify(msg);
        this.prependLog(newLog);
    },

    onMsgIn: function(msg) {
        inSign = "<-- "
        msgText = inSign + JSON.stringify(msg);

        if(msg.msgType == "error"){
            newLog = <span className="error">{inSign}{msg.message}</span>
        }

        this.prependLog(<div>{newLog}</div>);
    },

    prependLog: function(msg){
        state = this.state.eventLog;
        state.unshift(msg); //prepend
        this.setState({eventLog: state});
    },

	getInitialState: function() {

	    this.props.socketService.addOnMsgOut(this.onMsgOut);
	    this.props.socketService.addOnMsgIn(this.onMsgIn);

		return {
			content: "---- web-socket logs ----",
			eventLog: []
		};
	},

    render: function(){
        var items = this.state.eventLog.map((function(item, i) {
            return <li id={i}>{item}</li>;
         }).bind(this));

        return (<div>
            <h2>{this.props.currentHoverLabel}</h2>
            <ul>{items}</ul>
            </div>);
    }
});

var Root = React.createClass({

	getInitialState: function() {
		return {
			currentSelected: null,
			currentHover: ""
		};
	},

    currentHover: function(field){

        if(null == field){
            this.setState({currentHover: "Field:"});
            return;
        }

        var label = "Field:" +field.props.field;
        if(field.state != undefined && field.state.pieceName != undefined){
            label += ", " + field.state.pieceName;
        }
        this.setState({currentHover: label});
    },

    doSelect: function(newSelected){
        if(this.state.currentSelected != null){
            this.state.currentSelected.unSelect();
        }
        this.setState({currentSelected: newSelected});

        msg = {field: newSelected.props.field, type: newSelected.state.pieceName}
        socketService.setState(msg);
    },

    setPieceToField: function(pieceName, field){
        console.log(field);
        socketService.setPieceToField(pieceName, field);
    },

    render: function(){
        return (
            <table><tr>
                <td id="infoLogContainer"><div id="infoLog"><Info currentHoverLabel={this.state.currentHover} socketService={socketService} /></div></td>
                <td><ChessBoard root={this} rows={rows()} columns={columns()}/></td>
            </tr></table>
        );
    }
});

var onReady = function(){
    React.render(
        <div>
            <Root socketService={socketService} />
        </div>,
        document.getElementById('content')
    );
}

var socketService = new SocketService(onReady);


