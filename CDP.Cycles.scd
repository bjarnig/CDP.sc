 ////////////////////////////////////////////////////////////////
//
//                        CDP Cycles
//
////////////////////////////////////////////////////////////////


( // Load CDP GUIs (if needed)

~guiPath = thisProcess.nowExecutingPath.dirname ++ "/GUI/";
this.compileFile(~guiPath ++ "CDP.CROSS.scd").value;
this.compileFile(~guiPath ++ "CDP.FFT.scd").value;
this.compileFile(~guiPath ++ "CDP.GUI.scd").value;

)


////////////////////// GUI - Wavecycles ////////////////////////


( // Delete, Time-contract by telescoping N wavecycles into 1

~cdp.value(
 	(name:"distort delete 2", text:"Delete",
 	params:[(name:"delete", prepend:"", spec: ControlSpec(4, 32, \linear, 1))]));
)


( // Repeat, Time-expand by repeating N wavecycles

~cdp.value(
 	(name:"distort repeat", text:"Repeat",
 	params:[(name:"repeat", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),(name:"group", prepend:"-c", spec: ControlSpec(10, 250, \linear, 1))]),
 );

)


( // Omit A out of every B 'wavecycles', replacing with silence

~cdp.value(
	(name:"distort omit", text:"Omit",
	params:[
		(name:"omit", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
		(name:"group", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
    ]));
)


( // Divide, Distortion by dividing 'wavecycle' frequency

~cdp.value(
	(name:"distort divide", text:"Divide",
	params:[
		(name:"divider", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]));
)


( // Multiply, Distortion by multiplying 'wavecycle' frequency

~cdp.value(
	(name:"distort multiply", text:"Multiply",
	params:[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]));
)


( // Fractal, Superimpose 'wavecycles' onto themselves

~cdp.value(
	(name:"distort fractal", text:"Fractal",
	params:[
		(name:"scaling", prepend:"", spec: ControlSpec(200, 1000, \linear, 1)),
		(name:"gain", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.1))
    ]));
)


( // Telescope, Time-contract by telescoping N wavecycles into 1

~cdp.value(
	(name:"distort telescope", text:"Telescope",
	params:[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
    ]));
)


( // Envel, Impose envelope on groups

~cdp.value(
	(name:"distort envel 2", text:"Envel",
	params:[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"troughing", prepend:"-t", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
    ]));
)


( // Replace Cycles

~cdp.value(
	(name:"distort replace", text:"Replace",
	params:[(name:"cyclecnt", prepend:"", spec: ControlSpec(4, 16, \linear, 1))]));
)
