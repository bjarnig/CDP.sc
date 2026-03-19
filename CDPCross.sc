CDPCross : CDPBase {

	*new {|processName, infoText, directory, processParams|

	var win, soundMenuA, soundMenuB, processButton, playButton;
	var analyseButton, synthesizeButton;
	var inputSounds, currentY, lastOutput;
	var currentSliders, currentNumberBoxes, paramControls;
	var analyseFunc;

	/////////////////////// FILES //////////////////////////

	inputSounds = CDPBase.collectInputSounds(directory);

	/////////////////////// GUI //////////////////////////

	// Create the window
	win = Window.new("CDP", Rect(20, 200, 560, 500 + (processParams.size * 50)), scroll: true);
	win.front;
	win.background = CDPBase.bgColor;
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

	// GUI Elements
	CDPBase.createHeader(win, infoText);
	CDPBase.createLabel(win, 20, 100, "Input A:");
	CDPBase.createLabel(win, 20, 190, "Input B:");

	soundMenuA = CDPBase.createSoundMenu(win, 20, 130, inputSounds);
	soundMenuB = CDPBase.createSoundMenu(win, 20, 220, inputSounds);

	// Set the params label
	if(processParams.size > 0, {
		CDPBase.createLabel(win, 20, 320, "Parameters:");
	});

	// Parameter controls
	paramControls = CDPBase.createParameterControls(win, 360, processParams);
	currentSliders = paramControls[0];
	currentNumberBoxes = paramControls[1];
	currentY = paramControls[2] + 20;

	analyseFunc = {|menu|
		var input, output, params, current, inputDirectory;
		inputDirectory = directory +/+ "input/";
		current = inputSounds[menu.value];
		input = current.path;
		output = inputDirectory ++ menu.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
		if(File.exists(output), { File.delete(output) });
		CDPBase.cdpRunner("pvoc anal 1", input, output, "-c4096");
		lastOutput = output;
	};

	// analyseButton
	analyseButton = CDPBase.createButton(win, 20, currentY, 250, "Analyze");
	analyseButton.action = {
		analyseFunc.value(soundMenuA);
		analyseFunc.value(soundMenuB);
	};

	// Execute the process
	processButton = CDPBase.createButton(win, 290, currentY, 250, "Process");
	processButton.action = {
		var inputA, inputB, output, params, inputDirectory, outputDirectory;
		inputDirectory = directory +/+ "input/";
		outputDirectory = directory +/+ "output/";
		inputA = inputDirectory ++ soundMenuA.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
		inputB = inputDirectory ++ soundMenuB.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
		output = outputDirectory ++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".ana";
		params = "";
		currentNumberBoxes.do{|nb,i| params = params + processParams[i].prepend ++ nb.value};

		if(File.exists(output), { File.delete(output) });
		CDPBase.cdpRunnerCross(processName, inputA, inputB, output, params);
		lastOutput = output;
	};

	currentY = currentY + 50;

	synthesizeButton = CDPBase.createButton(win, 20, currentY, 250, "Synthesize");
	synthesizeButton.action = {
		var input, output, params, current, outputDirectory;
		outputDirectory = directory +/+ "output/";
		current = inputSounds[soundMenuA.value];
		input = outputDirectory ++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".ana";
		output = outputDirectory ++ (soundMenuA.item.replace(".wav", " ") ++ "_" ++ soundMenuB.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".wav";
		params = "";
		currentNumberBoxes.do{|nb,i| params = params + processParams[i].prepend ++ nb.value};

		if(File.exists(output), { File.delete(output) });
		CDPBase.cdpRunner("pvoc synth", input, output, "");
		lastOutput = output;
	};


	// Play the output
	playButton = CDPBase.createPlayButton(win, 290, currentY, { lastOutput });
	}
}
