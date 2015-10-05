// white
var whiteKing   = "&#9812;";
var whiteQueen  = "&#9813;";
var whiteRock   = "&#9814;";
var whiteBishop = "&#9815;";
var whiteKnight = "&#9816;";
var whitePawn   = "&#9817;";

var blackKing   = "&#9818;";
var blackQueen  = "&#9819;";
var blackRock   = "&#9820;";
var blackBishop = "&#9821;";
var blackKnight = "&#9822;";
var blackPawn   = "&#9823;";

// black

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

    },

    mouseOut: function () {
        //this.props.doSelect("");
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

    onClick: function(currentField){
        this.setState({selectedClass: "selected"});
        this.props.doSelect(this);
    },

    render: function(){
        return(<span onMouseOver={this.mouseOver} onClick={this.onClick} className={this.getClassNames()} onMouseOut={this.mouseOut}  dangerouslySetInnerHTML={{__html: getPiece(this.props.field) }}></span>)
    }
});

var WhiteField = React.createClass({

    render: function(){
        //return(<span className="box white" dangerouslySetInnerHTML={{__html: getPiece(this.props.field) }}></span>)
        return(<Field doSelect={this.props.doSelect} className="box white" field={this.props.field}  />)
    }
});

var BlackField = React.createClass({
    render: function(){
        //return(<span className="box black" dangerouslySetInnerHTML={{__html: getPiece(this.props.field) }}></span>)
        return(<Field doSelect={this.props.doSelect} className="box black" field={this.props.field} />)
    }
});

var ChessBoardField = React.createClass({
    render: function(){
        var field = ""+this.props.rowChar+""+this.props.colNumber;
        if(this.props.fieldId % 2 == 0){
            return(<WhiteField doSelect={this.props.doSelect} field={field}/>);
        }
        else{
          return(<BlackField doSelect={this.props.doSelect} field={field} />);
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
                  return <ChessBoardField doSelect={parent.props.doSelect} key={i} fieldId={i+row.charCodeAt(0)} rowChar={row} colNumber={i} />;
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
                     return <ChessBoardRow  key={i} row={i} doSelect={parent.props.doSelect} columns={columns} />;
           })}
        </div>
                );


    }
});

var Info = React.createClass({

    updateInfo: function(){
        alert('new info');
    },

    render: function(){
        return <div>infooozz: {this.props.hoverField} - {this.props.infoText}</div>;
    }
});

var Root = React.createClass({

	getInitialState: function() {
		return {
			currentSelected: null
		};
	},

    doSelect: function(newSelected){
        if(this.state.currentSelected != null){
            console.log(this.state.currentSelected);
            console.log('****');
            this.state.currentSelected.unSelect();
        }
        this.setState({currentSelected: newSelected});
    },

    render: function(){
        return (
            <div>
                <ChessBoard doSelect={this.doSelect} rows={rows()} columns={columns()}/>
                <Info hoverField="fffield" infoText="JEA" />
            </div>
        );
    }
});


React.render(
    <div>
        <Root />
    </div>,
    document.getElementById('content')
);

