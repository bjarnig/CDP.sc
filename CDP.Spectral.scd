 ///////////////////////////////////////////////////////////////
//
//                        CDP Spectral
//
////////////////////////////////////////////////////////////////


(

// Set the directory to your working directory or specify it per process
~directory = "/Users/bjarni/Courses/Classes/CWA/18 - Transformation (CDP)/code/source/"; // thisProcess.nowExecutingPath.dirname;

s.boot; // Boot the server for playback

)


/////////////////////// GUI - Spectral /////////////////////////


( // Stretch time

CDPFft(
 	"stretch time 1", "Stretch", ~directory,
 	[
		(name:"factor", prepend:"", spec: ControlSpec(4, 32, \linear, 1))
    ]);
)


( // Blur

CDPFft(
 	"blur blur", "Blur",  ~directory,
 	[
		(name:"factor", prepend:"", spec: ControlSpec(10, 400, \linear, 1))
    ]);
)


( // Hilite frequencies

CDPFft(
 	"hilite trace 4", "Trace", ~directory,
 	[
		(name:"retain", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),
		(name:"lofrq", prepend:"", spec: ControlSpec(10, 20000, \linear, 1)),
		(name:"hifrq", prepend:"", spec: ControlSpec(10, 20000, \linear, 1))
    ]);
)

( // Shift frequencies

CDPFft(
 	"strange shift 1", "Shift", ~directory,
 	[
		(name:"shift", prepend:"", spec: ControlSpec(-1000, 1000, \linear, 1))
    ]);
)


( // Invert

CDPFft(
 	"strange invert 2", "Shift", ~directory,
 	params:[]);
)


( // Accumulate

CDPFft(
 	"focus accu", "Accumulate", ~directory,
	[
		(name:"decay", prepend:"-d", spec: ControlSpec(0.001, 1.0, \linear, 0.002)),
		(name:"glis", prepend:"-g", spec: ControlSpec(-5, 5, \linear, 1))
    ]);
)


( // Exaggerate

CDPFft(
 	"focus exag", "Exaggerate",
	[
		(name:"exagger", prepend:"", spec: ControlSpec(0.1, 1000, \linear, 0.002))
    ]);
)
