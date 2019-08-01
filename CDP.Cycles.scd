 ////////////////////////////////////////////////////////////////
//
//                        CDP Cycles
//
////////////////////////////////////////////////////////////////


// Set the directory to your working directory or specify it per process
~directory = "/Users/bjarni/Desktop/material/code/source/"; // thisProcess.nowExecutingPath.dirname;

s.boot; // Boot the server for playback

)

////////////////////// GUI - Wavecycles ////////////////////////


( // Delete, Time-contract by telescoping N wavecycles into 1

CDPGui.view(
 	"distort delete 2", "Delete", ~directory,
 	[
    (name:"delete", prepend:"", spec: ControlSpec(4, 32, \linear, 1))
    ]
);

)


( // Repeat, Time-expand by repeating N wavecycles

CDPGui.view(
 	"distort repeat", "Repeat", , ~directory,
 	[
    (name:"repeat", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),
    (name:"group", prepend:"-c", spec: ControlSpec(10, 250, \linear, 1))
    ],
 );

)


( // Omit A out of every B 'wavecycles', replacing with silence

CDPGui.view(
	"distort omit", "Omit", ~directory,
	[
		(name:"omit", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
		(name:"group", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
    ]
);

)


( // Divide, Distortion by dividing 'wavecycle' frequency

CDPGui.view(
	"distort divide", "Divide", ~directory,
	[
		(name:"divider", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]);
)


( // Multiply, Distortion by multiplying 'wavecycle' frequency

CDPGui.view(
	"distort multiply", "Multiply", ~directory,
	[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]);
)


( // Fractal, Superimpose 'wavecycles' onto themselves

CDPGui.view(
	"distort fractal", "Fractal", ~directory,
	[
		(name:"scaling", prepend:"", spec: ControlSpec(200, 1000, \linear, 1)),
		(name:"gain", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.1))
    ]);
)


( // Telescope, Time-contract by telescoping N wavecycles into 1

CDPGui.view(
	"distort telescope", "Telescope", ~directory,
	[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
    ]);
)


( // Envel, Impose envelope on groups

CDPGui.view(
	"distort envel 2", "Envel", ~directory,
	[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"troughing", prepend:"-t", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
    ]);
)


( // Replace Cycles

CDPGui.view(
	"distort replace", "Replace", ~directory,
	[(name:"cyclecnt", prepend:"", spec: ControlSpec(4, 16, \linear, 1))]);
)
