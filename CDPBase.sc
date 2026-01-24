////////////////////////////////////////////////////////////////
//
//                          CDP Base Class
//
////////////////////////////////////////////////////////////////

CDPBase {
	
	// Color constants
	classvar <bgColor, <buttonColor, <menuColor;
	
	*initClass {
		bgColor = Color.fromHexString("#1e1e1e");
		buttonColor = Color.fromHexString("#00bfff");
		menuColor = Color.fromHexString("#6e6e6e");
	}
	
	*cdpRunner {|program, input, output, params|
		var command, shellCommand;
		command = program + "\\\"" ++ input ++ "\\\"" + "\\\"" ++ output ++ "\\\"" + params;
		shellCommand = "/bin/zsh -c \"source ~/.zshrc && " ++ command ++ "\"";
		shellCommand.postln;
		shellCommand.unixCmd({|exitCode, pid|
			if(exitCode == 0, {
				("Process completed successfully!").postln;
			}, {
				("Process failed with exit code:" + exitCode).postln;
			});
		});
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
			state.postln;
			
			if(state.value == 1, {
				lastOutput = lastOutputFunc.value;
				Routine({
					buffer = Buffer.read(Server.default, lastOutput);
					buffer.postln;
					
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
