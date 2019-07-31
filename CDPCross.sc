CDPCross {

*view {|process|


var win, menu, soundMenuA, soundMenuB, playing;
var headerLabel, processLabel, processButton, playButton, soundLabelA, soundLabelB, paramsLabel;
var analyseButton, synthesizeButton;
var cdpRunner, inputSounds, path, currentY, lastOutput;
var currentSliders, currentNumberBoxes;
var analyseFunc, synthesizeFunc, processFunc, playFunc;

/////////////////////// FILES //////////////////////////

cdpRunner = {|program, input, output, params|
	var q = "\"", command = program+q++input++q+q++output++q+params;
	command.postln;
	(command).runInTerminal;
};

path = thisProcess.nowExecutingPath.dirname;
inputSounds = SoundFile.collect(path +/+ "input/*.wav");


/////////////////////// GUI //////////////////////////

// Create the window
win = Window.new("CDP", Rect(20, 200, 560, 500 + (process.params.size * 50)), scroll: true);
win.front;
win.background = Color.fromHexString("#1e1e1e");
win.alpha = 0.95;
win.front;

// Draw the seperator
win.drawFunc = {
		Pen.strokeColor = Color.white;
		Pen.moveTo(20@75);
		Pen.lineTo(540@75);
		Pen.stroke;
	    Pen.moveTo(20@295);
		Pen.lineTo(540@295);
		Pen.stroke;
	};


// Set the header label
headerLabel = StaticText.new(win, Rect(230, 30, 250, 35));
headerLabel.string = process.text;
headerLabel.font = Font("Monaco", 28);
headerLabel.stringColor = Color.white;

// Set the sound labels
soundLabelA = StaticText.new(win, Rect(20, 100, 250, 25));
soundLabelA.string = "Input A:";
soundLabelA.font = Font("Monaco", 14);
soundLabelA.stringColor = Color.white;

soundLabelB = StaticText.new(win, Rect(20, 190, 250, 25));
soundLabelB.string = "Input B:";
soundLabelB.font = Font("Monaco", 14);
soundLabelB.stringColor = Color.white;

// Sound menu A
soundMenuA = PopUpMenu(win,Rect(20,130,520,45));
soundMenuA.items = (inputSounds.collect({ arg item; var list; list=item.path.split($/); list[list.size-1]}));
soundMenuA.background_(Color.fromHexString("#6e6e6e"));
soundMenuA.stringColor_(Color.white);
soundMenuA.font = Font("Monaco", 13);

// Sound menu B
soundMenuB = PopUpMenu(win,Rect(20,220,520,45));
soundMenuB.items = (inputSounds.collect({ arg item; var list; list=item.path.split($/); list[list.size-1]}));
soundMenuB.background_(Color.fromHexString("#6e6e6e"));
soundMenuB.stringColor_(Color.white);
soundMenuB.font = Font("Monaco", 13);

// Set the params label
if(process.params.size > 0, {
paramsLabel = StaticText.new(win, Rect(20, 320, 250, 25));
paramsLabel.string = "Parameters:";
paramsLabel.font = Font("Monaco", 14);
paramsLabel.stringColor = Color.white;
});

// Parameter controls
currentY = 360;
currentSliders = List();
currentNumberBoxes = List();
process.params.do{|item, index|
var nb, lbl, slider;
	nb = NumberBox(win, Rect(420, currentY, 100, 40));  item.postln;
	lbl = StaticText.new(win, Rect(20, currentY, 250, 25));
	lbl.string = item.name;
	lbl.font = Font("Monaco", 14);
	lbl.stringColor = Color.white;
	slider = Slider(win, Rect(120, currentY, 250, 40))
	.action_({|slider|
		var v = item.spec.map(slider.value);
		nb.value = v;
    });

	currentSliders.add(slider);
	currentNumberBoxes.add(nb);
	currentY = currentY + 50;
};

currentY = currentY + 20;

analyseFunc = {|menu|
	var input, output, params, current;
	current = inputSounds[menu.value];
	input = current.path;
	output = path +/+ "input/"++ menu.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
	if(File.exists(output), { File.delete(output) });
	cdpRunner.value("pvoc anal 1", input, output, "-c4096");
	lastOutput = output;
};

	// processFunc = {|menu|
	//
	// };

synthesizeFunc = {|menu|

};

playFunc = {

};

// analyseButton

analyseButton = Button(win, Rect(20, currentY, 250, 40));
analyseButton.font = Font("Monaco", 14);
analyseButton.states = [["Analyze", Color.black, Color.fromHexString("#00bfff")]];
analyseButton.action = {
	analyseFunc.value(soundMenuA);
	analyseFunc.value(soundMenuB);
};

// Execute the process
processButton = Button(win, Rect(290, currentY, 250, 40));
processButton.font = Font("Monaco", 14);
processButton.states = [["Process", Color.black, Color.fromHexString("#00bfff")]];
processButton.action = {
	var inputA, inputB, output, params;
	inputA =  path +/+ "input/"++ soundMenuA.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
	inputB =  path +/+ "input/"++ soundMenuB.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
	output = path +/+ "output/"++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ process.name).toLower.replace(" ", "") ++".ana";
	params = "";
	currentNumberBoxes.do{|nb,i| params = params + process.params[i].prepend ++ nb.value};

	if(File.exists(output), { File.delete(output) });
	cdpRunner.value(process.name, inputA ++ "\"" + "\"" ++ inputB, output, params);
	lastOutput = output;
};

currentY = currentY + 50;

synthesizeButton = Button(win, Rect(20, currentY, 250, 40));
synthesizeButton.font = Font("Monaco", 14);
synthesizeButton.states = [["Synthesize", Color.black, Color.fromHexString("#00bfff")]];
synthesizeButton.action = {
	var input, output, params, current;
	current = inputSounds[soundMenuA.value];
	input = path +/+ "output/"++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ process.name).toLower.replace(" ", "") ++".ana";
	output = path +/+ "output/"++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ process.name).toLower.replace(" ", "") ++".wav";
	params = "";
	currentNumberBoxes.do{|nb,i| params = params + process.params[i].prepend ++ nb.value};

	if(File.exists(output), { File.delete(output) });
	cdpRunner.value("pvoc synth", input, output, "");
	lastOutput = output;
};


// Play the output
playButton = Button(win, Rect(290, currentY, 250, 40));
playButton.font = Font("Monaco", 14);
playButton.states = [["Play", Color.black, Color.fromHexString("#00bfff")], ["Stop", Color.black, Color.fromHexString("#00bfff")]];
playButton.action = {arg state;
	var buffer;
	state.postln;
	if(state.value == 1, {
			Routine({
				buffer = Buffer.read(Server.local, lastOutput);
				buffer.postln;

				SynthDef(\cdplay, {|out=0,pan=0,rate=1,amp=1.0,buf=0|
					var signal = PlayBuf.ar(1, buf, rate, 1, 0, 1);
					Out.ar(out, Pan2.ar(signal, pan, amp));
				}).add;

				Server.default.sync;
				playing = Synth(\cdplay, [\buf, buffer]);
			}).play;
		}, { playing.free });
	};
}
}