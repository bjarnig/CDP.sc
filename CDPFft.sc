////////////////////////////////////////////////////////////////
//
//                          CDP FFT GUI
//
////////////////////////////////////////////////////////////////

CDPFft : CDPBase {

	*new {|processName, infoText, directory, processParams|

	var win, soundMenu, processButton, playButton;
	var analyseButton, synthesizeButton;
	var inputSounds, currentY, lastOutput;
	var currentSliders, currentNumberBoxes, paramControls;

	/////////////////////// FILES //////////////////////////

	inputSounds = CDPBase.collectInputSounds(directory);

	/////////////////////// GUI //////////////////////////

	// Create the window
	win = Window.new("CDP", Rect(20, 200, 560, 450 + (processParams.size * 50)), scroll: true);
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
		Pen.moveTo(20@200);
		Pen.lineTo(540@200);
		Pen.stroke;
	};

	// GUI Elements
	CDPBase.createHeader(win, infoText);
	CDPBase.createLabel(win, 20, 100, "Input:");
	CDPBase.createLabel(win, 20, 220, "Parameters:");

	soundMenu = CDPBase.createSoundMenu(win, 20, 130, inputSounds);

	// Parameter controls
	paramControls = CDPBase.createParameterControls(win, 260, processParams);
	currentSliders = paramControls[0];
	currentNumberBoxes = paramControls[1];
	currentY = paramControls[2] + 20;

	// AnalyseButton
	analyseButton = CDPBase.createButton(win, 20, currentY, 250, "Analyze");
	analyseButton.action = {arg state;
	   var input, output, params, current, inputDirectory;
	   inputDirectory = directory +/+ "input/";
	   File.mkdir(inputDirectory);
	   current = inputSounds[soundMenu.value];
	   input = current.path;
	   output = inputDirectory ++ soundMenu.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
	   if(File.exists(output), { File.delete(output) });
	   CDPBase.cdpRunner("pvoc anal 1", input, output, "-c4096");
	   lastOutput = output;
	};

	// Execute the process
	processButton = CDPBase.createButton(win, 290, currentY, 250, "Process");
	processButton.action = {arg state;
	   var input, output, params, current, outputDirectory, inputDirectory;
	   outputDirectory = directory +/+ "output/";
	   inputDirectory = directory +/+ "input/";
	   File.mkdir(outputDirectory);
	   current = inputSounds[soundMenu.value];
	   input =  inputDirectory ++ soundMenu.item.replace(".wav", " ").toLower.replace(" ", "") ++".ana";
	   output = outputDirectory ++ (soundMenu.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".ana";
	   params = "";
	   currentNumberBoxes.do{|nb,i| params = params + processParams[i].prepend ++ nb.value};

	   if(File.exists(output), { File.delete(output) });
	   CDPBase.cdpRunner(processName, input, output, params);
	   lastOutput = output;
	};

	currentY = currentY + 50;

	synthesizeButton = CDPBase.createButton(win, 20, currentY, 250, "Synthesize");
	synthesizeButton.action = {arg state;
	   var input, output, params, current, outputDirectory;
	   outputDirectory = directory +/+ "output/";
	   current = inputSounds[soundMenu.value];
	   input = outputDirectory ++ (soundMenu.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".ana";
	   output = outputDirectory ++ (soundMenu.item.replace(".wav", " ") ++ "_" ++ processName).toLower.replace(" ", "") ++".wav";
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
