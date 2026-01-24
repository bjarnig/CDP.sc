 ////////////////////////////////////////////////////////////////
//
//                        CDP Cycles
//
////////////////////////////////////////////////////////////////

(

// Set the directory to your working directory or specify it per process
~directory = "/Users/bjarni/Desktop/cdp/samples/";

// thisProcess.nowExecutingPath.dirname;

s.boot; // Boot the server for playback

)



////////////////////// GUI - Wavecycles ////////////////////////


( // Delete, Time-contract by telescoping N wavecycles into 1

CDPGui(
    "distort delete 2", "Delete", ~directory,
    [
    (name:"delete", prepend:"", spec: ControlSpec(4, 32, \linear, 1))
    ]
);

)


( // Repeat, Time-expand by repeating N wavecycles

CDPGui(
    "distort repeat", "Repeat", ~directory,
    [
    (name:"repeat", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),
    (name:"group", prepend:"-c", spec: ControlSpec(10, 250, \linear, 1))
    ]
 );

)


( // Omit A out of every B 'wavecycles', replacing with silence

CDPGui(
	"distort omit", "Omit", ~directory,
	[
		(name:"omit", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
		(name:"group", prepend:"", spec: ControlSpec(4, 16, \linear, 1)),
    ]
);

)


( // Divide, Distortion by dividing 'wavecycle' frequency

CDPGui(
	"distort divide", "Divide", ~directory,
	[
		(name:"divider", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]);
)


( // Multiply, Distortion by multiplying 'wavecycle' frequency

CDPGui(
	"distort multiply", "Multiply", ~directory,
	[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 16, \linear, 1))
    ]);
)


( // Fractal, Superimpose 'wavecycles' onto themselves

CDPGui(
	"distort fractal", "Fractal", ~directory,
	[
		(name:"scaling", prepend:"", spec: ControlSpec(200, 1000, \linear, 1)),
		(name:"gain", prepend:"", spec: ControlSpec(0.1, 1.0, \linear, 0.1))
    ]);
)


( // Telescope, Time-contract by telescoping N wavecycles into 1

CDPGui(
	"distort telescope", "Telescope", ~directory,
	[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
    ]);
)


( // Envel, Impose envelope on groups

CDPGui(
	"distort envel 2", "Envel", ~directory,
	[
		(name:"group", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"troughing", prepend:"-t", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
    ]);
)


( // Replace Cycles

CDPGui(
	"distort replace", "Replace", ~directory,
	[(name:"cyclecnt", prepend:"", spec: ControlSpec(4, 16, \linear, 1))]);
)


( // Average, Average the waveshape over N 'wavecycles'

CDPGui(
	"distort average", "Average", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Filter Mode 1, Omit cycles below frequency

CDPGui(
	"distort filter 1", "Filter Low", ~directory,
	[
		(name:"freq", prepend:"", spec: ControlSpec(10.0, 22050.0, \exponential))
	]);
)


( // Filter Mode 2, Omit cycles above frequency

CDPGui(
	"distort filter 2", "Filter High", ~directory,
	[
		(name:"freq", prepend:"", spec: ControlSpec(10.0, 22050.0, \exponential))
	]);
)


( // Interpolate, Time-stretch by repeating and interpolating

CDPGui(
	"distort interpolate", "Interpolate", ~directory,
	[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 32, \linear, 1))
	]);
)


( // Overload Mode 1, Clip the signal

CDPGui(
	"distort overload 1", "Overload", ~directory,
	[
		(name:"clip-level", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01)),
		(name:"depth", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Pitch, Pitchwarp 'wavecycles' of sound

CDPGui(
	"distort pitch", "Pitch", ~directory,
	[
		(name:"octvary", prepend:"", spec: ControlSpec(0.1, 8.0, \linear, 0.1))
	]);
)


( // Reform Mode 1, Convert to fixed level square wave

CDPGui(
	"distort reform 1", "Reform Square", ~directory,
	[]
);
)


( // Reform Mode 2, Convert to square wave

CDPGui(
	"distort reform 2", "Reform Square 2", ~directory,
	[]
);
)


( // Reform Mode 7, Convert to sinusoid

CDPGui(
	"distort reform 7", "Reform Sine", ~directory,
	[]
);
)


( // Reform Mode 8, Exaggerate waveform contour

CDPGui(
	"distort reform 8", "Reform Exaggerate", ~directory,
	[
		(name:"exaggeration", prepend:"", spec: ControlSpec(0.01, 10.0, \exponential))
	]);
)


( // Repeat2, Repeat without time-stretching

CDPGui(
	"distort repeat2", "Repeat2", ~directory,
	[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 32, \linear, 1)),
		(name:"cyclecnt", prepend:"-c", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Reverse, Cycle-reversal distortion

CDPGui(
	"distort reverse", "Reverse", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 500, \linear, 1))
	]);
)


////////////////////// Other Related Functions ////////////////////////


( // Clip Mode 1, Clip signal at specified level

CDPGui(
	"clip clip 1", "Clip Level", ~directory,
	[
		(name:"level", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
	]);
)


( // Clip Mode 2, Clip half-waveforms at fraction

CDPGui(
	"clip clip 2", "Clip Fraction", ~directory,
	[
		(name:"fraction", prepend:"", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
	]);
)


( // Quirk Mode 1, Distort by raising sample values to power (half-wavesets)

CDPGui(
	"quirk quirk 1", "Quirk HalfWave", ~directory,
	[
		(name:"powfac", prepend:"", spec: ControlSpec(0.1, 10.0, \exponential, 0.01))
	]);
)


( // Quirk Mode 2, Distort by raising sample values to power (entire signal)

CDPGui(
	"quirk quirk 2", "Quirk Full", ~directory,
	[
		(name:"powfac", prepend:"", spec: ControlSpec(0.1, 10.0, \exponential, 0.01))
	]);
)


( // Distrep Mode 1, Timestretch by repeating wavecycles

CDPGui(
	"distrep distrep 1", "Distrep", ~directory,
	[
		(name:"multiplier", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"cyclecnt", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Distshift Mode 2, Swap alternate half-wavecycle groups

CDPGui(
	"distshift distshift 2", "Distshift Swap", ~directory,
	[
		(name:"grpcnt", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Distwarp Mode 1, Warp wavecycles samplewise

CDPGui(
	"distwarp distwarp 1", "Distwarp Sample", ~directory,
	[
		(name:"warp", prepend:"", spec: ControlSpec(0.0001, 0.1, \exponential, 0.0001))
	]);
)


( // Distwarp Mode 2, Warp wavecycles by waveset

CDPGui(
	"distwarp distwarp 2", "Distwarp Waveset", ~directory,
	[
		(name:"warp", prepend:"", spec: ControlSpec(0.0001, 0.1, \exponential, 0.0001)),
		(name:"wavesetcnt", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Envel Mode 1, Rising envelope

CDPGui(
	"distort envel 1", "Envel Rise", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"troughing", prepend:"-t", spec: ControlSpec(0.01, 1.0, \linear, 0.01))
	]);
)


( // Envel Mode 3, Troughed envelope

CDPGui(
	"distort envel 3", "Envel Trough", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 100, \linear, 1)),
		(name:"troughing", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Delete Mode 1, One cycle in every N retained

CDPGui(
	"distort delete 1", "Delete Mode1", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Delete Mode 3, Weakest cycle deleted

CDPGui(
	"distort delete 3", "Delete Weak", ~directory,
	[
		(name:"cyclecnt", prepend:"", spec: ControlSpec(2, 100, \linear, 1))
	]);
)


( // Distmore Double, Double frequency of each waveset

CDPGui(
	"distmore double", "Double Freq", ~directory,
	[
		(name:"mult", prepend:"", spec: ControlSpec(1.0, 4.0, \linear, 0.1))
	]);
)
