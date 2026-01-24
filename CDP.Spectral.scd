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
 	"focus exag", "Exaggerate", ~directory,
	[
		(name:"exagger", prepend:"", spec: ControlSpec(0.1, 1000, \exponential))
    ]);
)


////////////////////// HILITE Functions ////////////////////////


( // Hilite Arpeg Mode 1, Arpeggiate spectrum (play inside band only)

CDPFft(
	"hilite arpeg 1", "Arpeg On", ~directory,
	[
		(name:"wave", prepend:"", spec: ControlSpec(1, 4, \linear, 1)),
		(name:"rate", prepend:"", spec: ControlSpec(0.1, 100, \exponential)),
		(name:"llowest", prepend:"-l", spec: ControlSpec(0, 10000, \linear, 10)),
		(name:"hhighest", prepend:"-h", spec: ControlSpec(100, 22050, \linear, 10)),
		(name:"bbandwidth", prepend:"-b", spec: ControlSpec(10, 5000, \exponential)),
		(name:"aamplif", prepend:"-a", spec: ControlSpec(1.0, 20.0, \linear, 0.1))
	]);
)


( // Hilite Arpeg Mode 2, Arpeggiate spectrum (boost band)

CDPFft(
	"hilite arpeg 2", "Arpeg Boost", ~directory,
	[
		(name:"wave", prepend:"", spec: ControlSpec(1, 4, \linear, 1)),
		(name:"rate", prepend:"", spec: ControlSpec(0.1, 100, \exponential)),
		(name:"llowest", prepend:"-l", spec: ControlSpec(0, 10000, \linear, 10)),
		(name:"hhighest", prepend:"-h", spec: ControlSpec(100, 22050, \linear, 10)),
		(name:"bbandwidth", prepend:"-b", spec: ControlSpec(10, 5000, \exponential)),
		(name:"aamplif", prepend:"-a", spec: ControlSpec(1.0, 20.0, \linear, 0.1))
	]);
)


( // Hilite BLTR, Time-average and TRACE spectrum

CDPFft(
	"hilite bltr", "BLTR", ~directory,
	[
		(name:"blurring", prepend:"", spec: ControlSpec(2, 500, \linear, 1)),
		(name:"tracing", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Hilite Filter, Filter the spectrum

CDPFft(
	"hilite filter", "Hilite Filter", ~directory,
	[
		(name:"lofrq", prepend:"", spec: ControlSpec(0, 10000, \linear, 10)),
		(name:"hifrq", prepend:"", spec: ControlSpec(100, 22050, \linear, 10)),
		(name:"rolloff", prepend:"", spec: ControlSpec(1, 20, \linear, 1))
	]);
)


( // Hilite Pluck, Emphasize newly prominent spectral components

CDPFft(
	"hilite pluck", "Pluck", ~directory,
	[
		(name:"gain", prepend:"", spec: ControlSpec(1, 100, \exponential))
	]);
)


( // Hilite Trace Mode 1, Highlight N loudest partials

CDPFft(
	"hilite trace 1", "Trace All", ~directory,
	[
		(name:"N", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


( // Hilite Trace Mode 2, Highlight N loudest above lofrq

CDPFft(
	"hilite trace 2", "Trace Above", ~directory,
	[
		(name:"N", prepend:"", spec: ControlSpec(1, 100, \linear, 1)),
		(name:"lofrq", prepend:"", spec: ControlSpec(10, 10000, \linear, 10))
	]);
)


( // Hilite Trace Mode 3, Highlight N loudest below hifrq

CDPFft(
	"hilite trace 3", "Trace Below", ~directory,
	[
		(name:"N", prepend:"", spec: ControlSpec(1, 100, \linear, 1)),
		(name:"hifrq", prepend:"", spec: ControlSpec(100, 22050, \linear, 10))
	]);
)


////////////////////// FOCUS Functions ////////////////////////


( // Focus Fold, Octave-transpose into frequency range

CDPFft(
	"focus fold", "Fold", ~directory,
	[
		(name:"lofrq", prepend:"", spec: ControlSpec(20, 5000, \exponential)),
		(name:"hifrq", prepend:"", spec: ControlSpec(100, 10000, \exponential))
	]);
)


( // Focus Step, Step-frame through sound

CDPFft(
	"focus step", "Step", ~directory,
	[
		(name:"timestep", prepend:"", spec: ControlSpec(0.01, 2.0, \linear, 0.01))
	]);
)


////////////////////// BLUR Functions ////////////////////////


( // Blur Avrg, Average spectral energy over N channels

CDPFft(
	"blur avrg", "Average", ~directory,
	[
		(name:"N", prepend:"", spec: ControlSpec(2, 256, \linear, 1))
	]);
)


( // Blur Chorus Mode 1, Randomize partial amplitudes

CDPFft(
	"blur chorus 1", "Chorus Amp", ~directory,
	[
		(name:"aspread", prepend:"", spec: ControlSpec(1.0, 100.0, \exponential))
	]);
)


( // Blur Chorus Mode 2, Randomize partial frequencies

CDPFft(
	"blur chorus 2", "Chorus Freq", ~directory,
	[
		(name:"fspread", prepend:"", spec: ControlSpec(1.0, 4.0, \linear, 0.01))
	]);
)


( // Blur Chorus Mode 5, Randomize both amplitude and frequency

CDPFft(
	"blur chorus 5", "Chorus Both", ~directory,
	[
		(name:"aspread", prepend:"", spec: ControlSpec(1.0, 100.0, \exponential)),
		(name:"fspread", prepend:"", spec: ControlSpec(1.0, 4.0, \linear, 0.01))
	]);
)


( // Blur Drunk, Drunken walk along analysis windows

CDPFft(
	"blur drunk", "Drunk Walk", ~directory,
	[
		(name:"range", prepend:"", spec: ControlSpec(1, 64, \linear, 1)),
		(name:"starttime", prepend:"", spec: ControlSpec(0.0, 10.0, \linear, 0.01)),
		(name:"duration", prepend:"", spec: ControlSpec(1, 60, \linear, 0.1))
	]);
)


( // Blur Noise, Add noise to spectrum

CDPFft(
	"blur noise", "Noise", ~directory,
	[
		(name:"noise", prepend:"", spec: ControlSpec(0.0, 1.0, \linear, 0.01))
	]);
)


( // Blur Scatter, Randomly thin the spectrum

CDPFft(
	"blur scatter", "Scatter", ~directory,
	[
		(name:"keep", prepend:"", spec: ControlSpec(1, 200, \linear, 1)),
		(name:"bblocksize", prepend:"-b", spec: ControlSpec(1, 500, \linear, 1))
	]);
)


( // Blur Suppress, Suppress N loudest partials

CDPFft(
	"blur suppress", "Suppress", ~directory,
	[
		(name:"N", prepend:"", spec: ControlSpec(1, 100, \linear, 1))
	]);
)


////////////////////// STRANGE Functions ////////////////////////


( // Strange Shift Mode 2, Shift spectrum above frequency

CDPFft(
	"strange shift 2", "Shift Above", ~directory,
	[
		(name:"frqshift", prepend:"", spec: ControlSpec(-5000, 5000, \linear, 10)),
		(name:"frqdivide", prepend:"", spec: ControlSpec(20, 10000, \exponential))
	]);
)


( // Strange Shift Mode 3, Shift spectrum below frequency

CDPFft(
	"strange shift 3", "Shift Below", ~directory,
	[
		(name:"frqshift", prepend:"", spec: ControlSpec(-5000, 5000, \linear, 10)),
		(name:"frqdivide", prepend:"", spec: ControlSpec(20, 10000, \exponential))
	]);
)


( // Strange Waver Mode 1, Oscillate harmonic/inharmonic

CDPFft(
	"strange waver 1", "Waver", ~directory,
	[
		(name:"vibfrq", prepend:"", spec: ControlSpec(0.1, 50, \exponential)),
		(name:"stretch", prepend:"", spec: ControlSpec(1, 500, \exponential)),
		(name:"botfrq", prepend:"", spec: ControlSpec(20, 5000, \exponential))
	]);
)
