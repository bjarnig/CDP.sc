 ////////////////////////////////////////////////////////////////
//
//                          CDP GUI
//
////////////////////////////////////////////////////////////////


CDPGui {

*view {|processName, infoText, directory, processParams|

var win, menu, soundMenu, playing;
var headerLabel, processLabel, processButton, playButton, soundLabel, paramsLabel;
var cdpRunner, inputSounds, currentY, lastOutput;
var currentSliders, currentNumberBoxes;

/////////////////////// FILES //////////////////////////

cdpRunner = {|program, input, output, params|
	var q = "\"", command = program+q++input++q+q++output++q+params;
	command.postln;
	(command).runInTerminal;
};

inputSounds = SoundFile.collect(directory ++ "*");

/////////////////////// GUI //////////////////////////

// Create the window
win = Window.new("CDP", Rect(20, 200, 560, 350 + (processParams.size * 50)), scroll: false);
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
	    Pen.moveTo(20@200);
		Pen.lineTo(540@200);
		Pen.stroke;
	};


// Set the header label
headerLabel = StaticText.new(win, Rect(230, 30, 250, 35));
headerLabel.string = infoText;
headerLabel.font = Font("Monaco", 28);
headerLabel.stringColor = Color.white;


// Set the sound label
soundLabel = StaticText.new(win, Rect(20, 100, 250, 25));
soundLabel.string = "Input:";
soundLabel.font = Font("Monaco", 14);
soundLabel.stringColor = Color.white;


// Set the params label
paramsLabel = StaticText.new(win, Rect(20, 220, 250, 25));
paramsLabel.string = "Parameters:";
paramsLabel.font = Font("Monaco", 14);
paramsLabel.stringColor = Color.white;


// Parameter controls
currentY = 260;
currentSliders = List();
currentNumberBoxes = List();
processParams.do{|item, index|
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

// Sound menu
soundMenu = PopUpMenu(win,Rect(20,130,520,45));
soundMenu.items = (inputSounds.collect({ arg item; var list; list=item.path.split($/); list[list.size-1]}));
soundMenu.background_(Color.fromHexString("#6e6e6e"));
soundMenu.stringColor_(Color.white);
soundMenu.font = Font("Monaco", 13);
currentY = currentY + 20;

// Execute the process
processButton = Button(win, Rect(20, currentY, 250, 40));
processButton.font = Font("Monaco", 14);
processButton.states = [["Process", Color.black, Color.fromHexString("#00bfff")]];
processButton.action = {arg state;
	var input, output, params, current, outputDirectory;
	current = inputSounds[soundMenu.value];
	input = current.path;
	outputDirectory = directory +/+ "output/";
	File.mkdir(outputDirectory);
	output = outputDirectory ++ (soundMenu.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".wav";
	params = "";
	currentNumberBoxes.do{|nb,i| params = params + processParams[i].prepend ++ nb.value};

	if(File.exists(output), { File.delete(output) });
	cdpRunner.value(processName, input, output, params);
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
