////////////////////////////////////////////////////////////////
//
//                          CDP Base Class
//
////////////////////////////////////////////////////////////////

CDPBase {
	
	// Color constants
	classvar <bgColor, <buttonColor, <menuColor;
	
	// CDP installation path (can be set manually)
	classvar <>cdpPath;
	
	*initClass {
		bgColor = Color.fromHexString("#1e1e1e");
		buttonColor = Color.fromHexString("#00bfff");
		menuColor = Color.fromHexString("#6e6e6e");
	}
	
	// Check if CDP is installed and available
	*checkCDPInstalled {
		var shellConfigs = ["~/.zshrc", "~/.bash_profile", "~/.bashrc", "~/.profile"];
		var testCmd, result, cdpPath;
		
		// Try each shell config file
		shellConfigs.do {|config|
			if(result.isNil or: {result.size == 0}, {
				testCmd = "/bin/zsh -c \"test -f" + config + "&& source" + config + "&& which distort 2>/dev/null || which distort 2>/dev/null\"";
				result = testCmd.unixCmdGetStdOut.trim;
			});
		};
		
		// If still not found, try common installation locations
		if(result.size == 0, {
			var commonPaths = [
				"/usr/local/bin/distort",
				"/opt/local/bin/distort",
				"/opt/homebrew/bin/distort",
				"~/bin/distort",
				"/Applications/CDP/distort"
			];
			
			commonPaths.do {|path|
				if(result.size == 0, {
					testCmd = "/bin/zsh -c \"test -f" + path + "&& echo" + path ++ "\"";
					result = testCmd.unixCmdGetStdOut.trim;
				});
			};
		});
		
		if(result.size == 0, {
			"".postln;
			"ERROR: CDP (Composers Desktop Project) is not installed or not in PATH.".postln;
			"".postln;
			"The 'distort' command was not found in:".postln;
			"  - Your shell PATH".postln;
			"  - Common installation locations (/usr/local/bin, /opt/local/bin, etc.)".postln;
			"".postln;
			"To fix this:".postln;
			"1. Install CDP from: http://www.composersdesktop.com/".postln;
			"2. Add CDP to your PATH by adding this to ~/.zshrc or ~/.bash_profile:".postln;
			"   export PATH=\"/path/to/CDP/bin:$PATH\"".postln;
			"3. Or use CDPBase.setCDPPath(\"/full/path/to/CDP/bin\") to set it manually".postln;
			"".postln;
			^false;
		});
		
		// Store the CDP path for later use
		cdpPath = PathName(result).pathOnly;
		if(cdpPath.size > 0, {
			this.setCDPPath(cdpPath);
		});
		
		("CDP found at:" + result).postln;
		^true;
	}
	
	// Expand environment variables in a path string
	*expandPath {|path|
		var expanded = path;
		var homeDir = Platform.userHomeDir;
		
		// Expand $HOME
		expanded = expanded.replace("$HOME", homeDir);
		// Expand ~
		if(expanded.beginsWith("~/"), {
			expanded = homeDir ++ expanded[1..];
		});
		
		^expanded;
	}
	
	// Set CDP path manually
	*setCDPPath {|path|
		cdpPath = this.expandPath(path);
		if(cdpPath.endsWith("/").not, {
			cdpPath = cdpPath ++ "/";
		});
		("CDP path set to:" + cdpPath).postln;
		("Expanded from:" + path).postln;
	}
	
	// Get the full command with proper PATH handling
	*prBuildCommand {|program, input, output, params|
		var command, shellCommand;
		
		command = program + "\\\"" ++ input ++ "\\\"" + "\\\"" ++ output ++ "\\\"" + params;
		
		// If we have a specific CDP path, use it directly
		if(cdpPath.notNil, {
			var cmdName = program.split($ )[0]; // Get first word (command name)
			var fullPath = cdpPath ++ cmdName;
			command = command.replace(cmdName, fullPath);
			shellCommand = "/bin/zsh -c \"" ++ command ++ "\"";
		}, {
			// Otherwise try to source shell configs with proper variable expansion
			// Use -l (login shell) to ensure proper initialization and variable expansion
			shellCommand = "/bin/zsh -l -c \"" ++ command ++ "\"";
		});
		
		^shellCommand;
	}
	
	*cdpRunner {|program, input, output, params|
		var shellCommand, outputDir;
		
		// Ensure output directory exists
		outputDir = PathName(output).pathOnly;
		if(outputDir.size > 0, {
			File.mkdir(outputDir);
		});
		
		shellCommand = this.prBuildCommand(program, input, output, params);
		shellCommand.postln;
		shellCommand.unixCmd({|exitCode, pid|
			if(exitCode == 0, {
				("Process completed successfully!").postln;
			}, {
				("Process failed with exit code:" + exitCode).postln;
				if(exitCode == 127, {
					"".postln;
					"ERROR: Command not found. CDP may not be installed or not in PATH.".postln;
					"".postln;
					"If your PATH shows '$HOME' literally (not expanded):".postln;
					"  This is a shell quoting issue. Use the manual path setting below.".postln;
					"".postln;
					"Try these steps:".postln;
					"1. Run: CDPBase.checkCDPInstalled".postln;
					"2. If CDP is installed but not found, set the path manually:".postln;
					"   CDPBase.setCDPPath(\"~/cdpr8/_cdp/_cdprogs\")  // Example, use your path".postln;
					"   CDPBase.setCDPPath(\"/usr/local/bin\")  // Or absolute path".postln;
					"3. Find your CDP path by running 'which distort' in terminal".postln;
					"".postln;
				});
			});
		});
	}
	
	// Special version for cross-synthesis with two input files
	*cdpRunnerCross {|program, inputA, inputB, output, params|
		var command, shellCommand, outputDir;
		
		// Ensure output directory exists
		outputDir = PathName(output).pathOnly;
		if(outputDir.size > 0, {
			File.mkdir(outputDir);
		});
		
		// Build command with two inputs properly quoted
		command = program + "\\\"" ++ inputA ++ "\\\"" + "\\\"" ++ inputB ++ "\\\"" + "\\\"" ++ output ++ "\\\"" + params;
		
		// If we have a specific CDP path, use it directly
		if(cdpPath.notNil, {
			var cmdName = program.split($ )[0]; // Get first word (command name)
			var fullPath = cdpPath ++ cmdName;
			command = command.replace(cmdName, fullPath);
			shellCommand = "/bin/zsh -c \"" ++ command ++ "\"";
		}, {
			// Otherwise use login shell
			shellCommand = "/bin/zsh -l -c \"" ++ command ++ "\"";
		});
		
		shellCommand.postln;
		shellCommand.unixCmd({|exitCode, pid|
			if(exitCode == 0, {
				("Process completed successfully!").postln;
			}, {
				("Process failed with exit code:" + exitCode).postln;
			});
		});
	}
	
	// Synchronous version for batch processing
	*cdpRunnerSync {|program, input, output, params|
		var shellCommand, exitCode, outputDir;
		
		// Ensure output directory exists
		outputDir = PathName(output).pathOnly;
		if(outputDir.size > 0, {
			File.mkdir(outputDir);
		});
		
		shellCommand = this.prBuildCommand(program, input, output, params);
		shellCommand.postln;
		exitCode = shellCommand.systemCmd;
			if(exitCode == 0, {
				("Process completed successfully!").postln;
			}, {
				("Process failed with exit code:" + exitCode).postln;
				if(exitCode == 127, {
					"".postln;
					"ERROR: Command not found. CDP may not be installed or not in PATH.".postln;
					"".postln;
					"If your PATH shows '$HOME' literally (not expanded):".postln;
					"  This is a shell quoting issue. Use the manual path setting below.".postln;
					"".postln;
					"Try these steps:".postln;
					"1. Run: CDPBase.checkCDPInstalled".postln;
					"2. If CDP is installed but not found, set the path manually:".postln;
					"   CDPBase.setCDPPath(\"~/cdpr8/_cdp/_cdprogs\")  // Example, use your path".postln;
					"   CDPBase.setCDPPath(\"/usr/local/bin\")  // Or absolute path".postln;
					"3. Find your CDP path by running 'which distort' in terminal".postln;
					"".postln;
				});
			});
		^exitCode;
	}
	
	*collectInputSounds {|directory|
		^SoundFile.collect(directory ++ "*");
	}
	
	*createHeader {|win, text|
		var headerLabel = StaticText.new(win, Rect(230, 30, 250, 35));
		headerLabel.string = text;
		headerLabel.font = Font("Monaco", 28);
		headerLabel.stringColor = Color.white;
		^headerLabel;
	}
	
	*createLabel {|win, x, y, text|
		var label = StaticText.new(win, Rect(x, y, 250, 25));
		label.string = text;
		label.font = Font("Monaco", 14);
		label.stringColor = Color.white;
		^label;
	}
	
	*createSoundMenu {|win, x, y, inputSounds|
		var soundMenu = PopUpMenu(win, Rect(x, y, 520, 45));
		soundMenu.items = (inputSounds.collect({ arg item; 
			var list = item.path.split($/); 
			list[list.size-1]
		}));
		soundMenu.background_(menuColor);
		soundMenu.stringColor_(Color.white);
		soundMenu.font = Font("Monaco", 13);
		^soundMenu;
	}
	
	*createParameterControls {|win, startY, processParams|
		var currentY = startY;
		var currentSliders = List();
		var currentNumberBoxes = List();
		
		processParams.do{|item, index|
			var nb, lbl, slider;
			nb = NumberBox(win, Rect(420, currentY, 100, 40));
			lbl = StaticText.new(win, Rect(20, currentY, 250, 25));
			lbl.string = item.name;
			lbl.font = Font("Monaco", 14);
			lbl.stringColor = Color.white;
			slider = Slider(win, Rect(120, currentY, 250, 40))
			.action_({|sl|
				var v = item.spec.map(sl.value);
				nb.value = v;
			});
			
			currentSliders.add(slider);
			currentNumberBoxes.add(nb);
			currentY = currentY + 50;
		};
		
		^[currentSliders, currentNumberBoxes, currentY];
	}
	
	*createButton {|win, x, y, width, text|
		var button = Button(win, Rect(x, y, width, 40));
		button.font = Font("Monaco", 14);
		button.states = [[text, Color.black, buttonColor]];
		^button;
	}
	
	*createPlayButton {|win, x, y, lastOutputFunc|
		var playButton = Button(win, Rect(x, y, 250, 40));
		var playing;
		
		playButton.font = Font("Monaco", 14);
		playButton.states = [
			["Play", Color.black, buttonColor], 
			["Stop", Color.black, buttonColor]
		];
		playButton.action = {|state|
			var buffer, lastOutput;
			
			if(state.value == 1, {
				lastOutput = lastOutputFunc.value;
				Routine({
					buffer = Buffer.read(Server.default, lastOutput);
					
					SynthDef(\cdplay, {|out=0, pan=0, rate=1, amp=1.0, buf=0|
						var signal = PlayBuf.ar(1, buf, rate, 1, 0, 1);
						Out.ar(out, Pan2.ar(signal, pan, amp));
					}).add;
					
					Server.default.sync;
					playing = Synth(\cdplay, [\buf, buffer]);
				}).play;
			}, {
				playing.free;
			});
		};
		^playButton;
	}
}
