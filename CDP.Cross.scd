 ///////////////////////////////////////////////////////////////
//
//                        CDP Cross
//
////////////////////////////////////////////////////////////////


( // Load CDP GUIs (if needed)

~guiPath = thisProcess.nowExecutingPath.dirname ++ "/GUI/";
this.compileFile(~guiPath ++ "CDP.CROSS.scd").value;
this.compileFile(~guiPath ++ "CDP.FFT.scd").value;
this.compileFile(~guiPath ++ "CDP.GUI.scd").value;

)


///////////////////////// GUI - Cross ///////////////////////////


( // Vocode two sounds

~cdpCross.value(
 	(name:"formants vocode", text:"Vocode",
 	params:[
			(name:"leafsize", prepend:"-f", spec: ControlSpec(10, 40, \linear, 2, 20)),
			(name:"lopass", prepend:"-l", spec: ControlSpec(8, 20, \linear, 2, 4))
	]));
)


( // Morph two sounds

~cdpCross.value(
 	(name:"morph morph 1", text:"Morph",
 	params:[
			(name:"as", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
			(name:"ae", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
			(name:"fs", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
			(name:"fe", prepend:"", spec: ControlSpec(0, 8, \linear, 0.1, 4)),
			(name:"expa", prepend:"", spec: ControlSpec(1, 8, \linear, 0.1, 4)),
			(name:"expf", prepend:"", spec: ControlSpec(1, 8, \linear, 0.1, 4)),
	]));
)


( // Interleave two sounds

~cdpCross.value(
 	(name:"combine interleave", text:"Interleave",
 	params:[
			(name:"leafsize", prepend:"", spec: ControlSpec(4, 32, \linear, 2, 8))
	]));
)


( // Diff two sounds

~cdpCross.value(
 	(name:"combine diff", text:"Diff",
 	params:[
			(name:"crossover", prepend:"-c", spec: ControlSpec(0.0, 1.0, \linear, 0.01, 0.5))
	]));
)

