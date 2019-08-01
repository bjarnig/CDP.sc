 ///////////////////////////////////////////////////////////////
//
//                        CDP Cross
//
////////////////////////////////////////////////////////////////


(

// Set the directory to your working directory or specify it per process
~directory = "/Users/bjarni/Courses/Classes/CWA/18 - Transformation (CDP)/code/source/"; // thisProcess.nowExecutingPath.dirname;

s.boot; // Boot the server for playback

)


///////////////////////// GUI - Cross ///////////////////////////


( // Vocode two sounds

CDPCross(
 	"formants vocode", "Vocode", ~directory,
 	[
		(name:"leafsize", prepend:"-f", spec: ControlSpec(10, 40, \linear, 2, 20)),
		(name:"lopass", prepend:"-l", spec: ControlSpec(8, 20, \linear, 2, 4))
	]));
)


( // Morph two sounds

CDPCross(
 	"morph morph 1", "Morph", ~directory,
 	[
		(name:"as", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
		(name:"ae", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
		(name:"fs", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
		(name:"fe", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
		(name:"expa", prepend:"", spec: ControlSpec(1, 8, \linear, 0.1, 4)),
		(name:"expf", prepend:"", spec: ControlSpec(1, 8, \linear, 0.1, 4))
	]);
)


( // Interleave two sounds

CDPCross(
 	"combine interleave", "Interleave", ~directory,
 	[
		(name:"leafsize", prepend:"", spec: ControlSpec(4, 32, \linear, 2, 8))
	]);
)


( // Diff two sounds

CDPCross(
 	"combine diff", "Diff", ~directory,
 	[
		(name:"crossover", prepend:"-c", spec: ControlSpec(0.0, 1.0, \linear, 0.01, 0.5))
	]);
)
