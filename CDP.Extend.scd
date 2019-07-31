////////////////////////////////////////////////////////////////
//
//                        CDP Extend
//
////////////////////////////////////////////////////////////////


( // Load CDP GUIs (if needed)

~guiPath = thisProcess.nowExecutingPath.dirname ++ "/GUI/";
this.compileFile(~guiPath ++ "CDP.CROSS.scd").value;
this.compileFile(~guiPath ++ "CDP.FFT.scd").value;
this.compileFile(~guiPath ++ "CDP.GUI.scd").value;

)


/////////////////////// GUI - Extend //////////////////////////


( // Doublets, divide a sound into segments that repeat

~cdp.value(
	(name:"extend doublets", text:"Doublets",
	params:[
		(name:"segdur", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"repeats", prepend:"", spec: ControlSpec(2, 12.0, \linear, 1))
    ]));
)


( // Scramble soundfile and write to any given length

~cdp.value(
	(name:"extend scramble 1", text:"Scramble",
	params:[
		(name:"minSeg", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
		(name:"maxSeg", prepend:"", spec: ControlSpec(0.01, 4.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 20, \linear, 0.1))
    ]));
)


( // Shred, Sound is shredded, within its existing duration

~cdp.value(
	(name:"modify radical 2", text:"Shred",
	params:[
	    (name:"repeats",  prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
		(name:"chunklen",  prepend:"", spec: ControlSpec(0.1, 0.8, \linear, 0.1))])
);

)


( // Loop, repeat advancing segments inside soundfile

~cdp.value(
	(name:"extend loop 2", text:"Loop",
	params:[
		(name:"dur", prepend:"", spec: ControlSpec(1, 50, \linear, 1)),
		(name:"start", prepend:"", spec: ControlSpec(0.0, 8.0, \linear, 0.01)),
		(name:"len", prepend:"", spec: ControlSpec(10, 300, \linear, 1)),
		(name:"lstep", prepend:"-l", spec: ControlSpec(1, 30, \linear, 1)),
		(name:"wsplen", prepend:"-w", spec: ControlSpec(2, 40, \linear, 1)),
		(name:"sscat", prepend:"-s", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
    ]));
)


( // Drunk, drunken walk through source file

~cdp.value(
	(name:"extend drunk 1", text:"Drunk",
	params:[
		(name:"dur", prepend:"", spec: ControlSpec(1, 20, \linear, 0.1)),
		(name:"locus", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
		(name:"amb", prepend:"", spec: ControlSpec(0.01, 4.0, \linear, 0.01)),
		(name:"step", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"clock", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"ssplicelen", prepend:"-s", spec: ControlSpec(10, 100, \linear, 0.1)),
		(name:"cclokrand", prepend:"-c", spec: ControlSpec(0.0, 1.0, \linear, 0.001)),
		(name:"overlap", prepend:"-o", spec: ControlSpec(0.0, 1.0, \linear, 0.001)),
    ]));
)
