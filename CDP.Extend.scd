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


( // Baktobak, Join reversed copy to normal copy

CDPGui(
	"extend baktobak", "Baktobak", ~directory,
	[
		(name:"join_time", prepend:"", spec: ControlSpec(0.01, 10.0, \linear, 0.01)),
		(name:"splice-length", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Zigzag Mode 1, Read soundfile backwards and forwards randomly

CDPGui(
	"extend zigzag 1", "Zigzag", ~directory,
	[
		(name:"start", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"end", prepend:"", spec: ControlSpec(0.1, 10.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"minzig", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"ssplicelen", prepend:"-s", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"mmaxzig", prepend:"-m", spec: ControlSpec(0.1, 5.0, \linear, 0.01))
	]);
)


( // Iterate Mode 1, Repeat sound with subtle variations (specify duration)

CDPGui(
	"extend iterate 1", "Iterate Dur", ~directory,
	[
		(name:"outduration", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"ddelay", prepend:"-d", spec: ControlSpec(0.01, 5.0, \linear, 0.01)),
		(name:"rrand", prepend:"-r", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"ppshift", prepend:"-p", spec: ControlSpec(0.0, 12.0, \linear, 0.1)),
		(name:"aampcut", prepend:"-a", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Iterate Mode 2, Repeat sound with subtle variations (specify repetitions)

CDPGui(
	"extend iterate 2", "Iterate Reps", ~directory,
	[
		(name:"repetitions", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"ddelay", prepend:"-d", spec: ControlSpec(0.01, 5.0, \linear, 0.01)),
		(name:"rrand", prepend:"-r", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"ppshift", prepend:"-p", spec: ControlSpec(0.0, 12.0, \linear, 0.1)),
		(name:"aampcut", prepend:"-a", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Freeze Mode 1, Freeze segment by iteration (specify duration)

CDPGui(
	"extend freeze 1", "Freeze Dur", ~directory,
	[
		(name:"outduration", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"delay", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"rand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"pshift", prepend:"", spec: ControlSpec(0.0, 12.0, \linear, 0.1)),
		(name:"ampcut", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"starttime_of_freeze", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"endtime", prepend:"", spec: ControlSpec(0.01, 10.0, \linear, 0.01)),
		(name:"gain", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Freeze Mode 2, Freeze segment by iteration (specify repetitions)

CDPGui(
	"extend freeze 2", "Freeze Reps", ~directory,
	[
		(name:"repetitions", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"delay", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01)),
		(name:"rand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"pshift", prepend:"", spec: ControlSpec(0.0, 12.0, \linear, 0.1)),
		(name:"ampcut", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"starttime_of_freeze", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"endtime", prepend:"", spec: ControlSpec(0.01, 10.0, \linear, 0.01)),
		(name:"gain", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Hover, Move through file zig-zag reading at given frequency

CDPGui(
	"hover hover", "Hover", ~directory,
	[
		(name:"frq", prepend:"", spec: ControlSpec(1, 100, \exponential)),
		(name:"loc", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"frqrand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"locrand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"splice", prepend:"", spec: ControlSpec(0.0, 100.0, \linear, 0.1)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1))
	]);
)


( // Hover2, Hover with inverted copies

CDPGui(
	"hover2 hover2", "Hover2", ~directory,
	[
		(name:"frq", prepend:"", spec: ControlSpec(1, 100, \exponential)),
		(name:"loc", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"frqrand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"locrand", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1))
	]);
)


( // Echo, Repeat sound with timing and level adjustments

CDPGui(
	"sfecho echo", "Echo", ~directory,
	[
		(name:"delay", prepend:"", spec: ControlSpec(0.1, 10.0, \linear, 0.01)),
		(name:"attenuation", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01)),
		(name:"totaldur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"rrand", prepend:"-r", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Shrink Mode 1, Repeat sound shortening from end

CDPGui(
	"shrink shrink 1", "Shrink End", ~directory,
	[
		(name:"shrinkage", prepend:"", spec: ControlSpec(0.1, 0.9, \linear, 0.01)),
		(name:"gap", prepend:"", spec: ControlSpec(0.05, 10.0, \linear, 0.01)),
		(name:"contract", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"spl", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Shrink Mode 2, Repeat sound shortening around midpoint

CDPGui(
	"shrink shrink 2", "Shrink Mid", ~directory,
	[
		(name:"shrinkage", prepend:"", spec: ControlSpec(0.1, 0.9, \linear, 0.01)),
		(name:"gap", prepend:"", spec: ControlSpec(0.05, 10.0, \linear, 0.01)),
		(name:"contract", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"spl", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Shrink Mode 3, Repeat sound shortening from start

CDPGui(
	"shrink shrink 3", "Shrink Start", ~directory,
	[
		(name:"shrinkage", prepend:"", spec: ControlSpec(0.1, 0.9, \linear, 0.01)),
		(name:"gap", prepend:"", spec: ControlSpec(0.05, 10.0, \linear, 0.01)),
		(name:"contract", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.01)),
		(name:"dur", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1)),
		(name:"spl", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Dvdwind, Shorten sound by read-skip-read-skip

CDPGui(
	"dvdwind dvdwind", "DVD Wind", ~directory,
	[
		(name:"contraction", prepend:"", spec: ControlSpec(1.1, 10.0, \exponential)),
		(name:"clipsize", prepend:"", spec: ControlSpec(10, 500, \linear, 1))
	]);
)
