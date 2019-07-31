 ///////////////////////////////////////////////////////////////
//
//                        CDP Spectral
//
////////////////////////////////////////////////////////////////


( // Load CDP GUIs (if needed)

~guiPath = thisProcess.nowExecutingPath.dirname ++ "/GUI/";
this.compileFile(~guiPath ++ "CDP.CROSS.scd").value;
this.compileFile(~guiPath ++ "CDP.FFT.scd").value;
this.compileFile(~guiPath ++ "CDP.GUI.scd").value;

)


/////////////////////// GUI - Spectral /////////////////////////


( // Stretch time

~cdpSpectral.value(
 	(name:"stretch time 1", text:"Stretch",
 	params:[
			(name:"factor", prepend:"", spec: ControlSpec(4, 32, \linear, 1))
    ]));
)


( // Blur

~cdpSpectral.value(
 	(name:"blur blur", text:"Blur",
 	params:[
			(name:"factor", prepend:"", spec: ControlSpec(10, 400, \linear, 1))
    ]));
)


( // Hilite frequencies

~cdpSpectral.value(
 	(name:"hilite trace 4", text:"Trace",
 	params:[
			(name:"retain", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),
			(name:"lofrq", prepend:"", spec: ControlSpec(10, 20000, \linear, 1)),
			(name:"hifrq", prepend:"", spec: ControlSpec(10, 20000, \linear, 1))
    ]));

)

( // Shift frequencies

~cdpSpectral.value(
 	(name:"strange shift 1", text:"Shift",
 	params:[
			(name:"shift", prepend:"", spec: ControlSpec(-1000, 1000, \linear, 1))
    ]));

)


( // Invert

~cdpSpectral.value(
 	(name:"strange invert 2", text:"Shift",
 	params:[]));

)


( // Accumulate

~cdpSpectral.value(
 	(name:"focus accu", text:"Accumulate",
	params:[
			(name:"decay", prepend:"-d", spec: ControlSpec(0.001, 1.0, \linear, 0.002)),
			(name:"glis", prepend:"-g", spec: ControlSpec(-5, 5, \linear, 1))
    ]));
)


( // Exaggerate

~cdpSpectral.value(
 	(name:"focus exag", text:"Exaggerate",
	params:[
		(name:"exagger", prepend:"", spec: ControlSpec(0.1, 1000, \linear, 0.002))
    ]));
)
