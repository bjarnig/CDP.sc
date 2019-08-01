////////////////////////////////////////////////////////////////
//
//                        CDP Extend
//
////////////////////////////////////////////////////////////////


(

// Set the directory to your working directory or specify it per process
~directory = "/Users/bjarni/Courses/Classes/CWA/18 - Transformation (CDP)/code/source/"; // thisProcess.nowExecutingPath.dirname;

s.boot; // Boot the server for playback

)

/////////////////////// GUI - Extend //////////////////////////


( // Doublets, divide a sound into segments that repeat

CDPGui(
	"extend doublets", "Doublets", ~directory,
	[
		(name:"segdur", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"repeats", prepend:"", spec: ControlSpec(2, 12.0, \linear, 1))
    ]);
)


( // Scramble soundfile and write to any given length

CDPGui(
	"extend scramble 1", "Scramble", ~directory,
	[
		(name:"minSeg", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
		(name:"maxSeg", prepend:"", spec: ControlSpec(0.01, 4.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 20, \linear, 0.1))
    ]);
)


( // Shred, Sound is shredded, within its existing duration

CDPGui(
	"modify radical 2", "Shred", ~directory,
	[
	    (name:"repeats",  prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
		(name:"chunklen",  prepend:"", spec: ControlSpec(0.1, 0.8, \linear, 0.1))
	]);
)


( // Loop, repeat advancing segments inside soundfile

CDPGui(
	"extend loop 2", "Loop", ~directory,
	[
		(name:"dur", prepend:"", spec: ControlSpec(1, 50, \linear, 1)),
		(name:"start", prepend:"", spec: ControlSpec(0.0, 8.0, \linear, 0.01)),
		(name:"len", prepend:"", spec: ControlSpec(10, 300, \linear, 1)),
		(name:"lstep", prepend:"-l", spec: ControlSpec(1, 30, \linear, 1)),
		(name:"wsplen", prepend:"-w", spec: ControlSpec(2, 40, \linear, 1)),
		(name:"sscat", prepend:"-s", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
    ]);
)


( // Drunk, drunken walk through source file

CDPGui(
	"extend drunk 1", "Drunk", ~directory,
	[
		(name:"dur", prepend:"", spec: ControlSpec(1, 20, \linear, 0.1)),
		(name:"locus", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
		(name:"amb", prepend:"", spec: ControlSpec(0.01, 4.0, \linear, 0.01)),
		(name:"step", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"clock", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"ssplicelen", prepend:"-s", spec: ControlSpec(10, 100, \linear, 0.1)),
		(name:"cclokrand", prepend:"-c", spec: ControlSpec(0.0, 1.0, \linear, 0.001)),
		(name:"overlap", prepend:"-o", spec: ControlSpec(0.0, 1.0, \linear, 0.001)),
    ]);
)
