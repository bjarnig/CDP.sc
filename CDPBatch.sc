////////////////////////////////////////////////////////////////
//
//                     CDP Batch Processing
//
////////////////////////////////////////////////////////////////

CDPBatch : CDPBase {
	
	var <directory;
	var <files;
	var <channels;
	var <outputFolder;
	
	*new {|directory, channels=1|
		^super.new.init(directory, channels);
	}
	
	init {|dir, ch|
		directory = dir;
		channels = ch;
		outputFolder = directory +/+ "batch_output/";
		files = List();
		this.readFiles();
	}
	
	readFiles {
		var pathMatch = directory ++ "*.wav";
		PathName(directory).files.do {|file|
			if(file.extension == "wav", {
				// Filter out system files and channel files
				if((file.fileNameWithoutExtension.beginsWith("._").not) 
					and: (file.fileNameWithoutExtension.contains("_c").not), {
					files.add(file.fileNameWithoutExtension);
				});
			});
		};
		("Found" + files.size + "files to process:").postln;
		files.postln;
	}
	
	// Random range generator for creative variations
	rrange {|lo, hi, decimals=2|
		^((hi - lo).rand + lo).round(10.pow(decimals.neg));
	}
	
	// Run CDP command for a specific file and channel
	runForChannel {|file, channel, command, params|
		var input, output, fullCommand;
		input = directory ++ file ++ "_c" ++ channel ++ ".wav";
		output = outputFolder ++ file ++ params["suffix"] ++ "_c" ++ channel ++ ".wav";
		fullCommand = command + "\"" ++ input ++ "\"" + "\"" ++ output ++ "\"";
		
		// Add parameters
		params.keysValuesDo {|key, value|
			if(key != "suffix", {
				fullCommand = fullCommand + value.asString;
			});
		};
		
		this.class.cdpRunner(command, input, output, params["params"] ? "");
	}
	
	// Split multi-channel file into separate channel files
	splitChannels {|file|
		var inputFile = directory ++ file ++ ".wav";
		var outputBase = directory ++ file;
		
		("Splitting channels for" + file).postln;
		
		if(channels > 1, {
			// Use housekeep chans 2 to extract channels
			this.class.cdpRunner("housekeep chans 2", inputFile, "", "");
		});
	}
	
	// Recombine channel files into stereo/multi-channel
	combineChannels {|file, suffix|
		var outputFile = outputFolder ++ file ++ suffix ++ ".wav";
		var inputFiles = "";
		
		File.mkdir(outputFolder);
		
		channels.do {|i|
			var ch = i + 1;
			inputFiles = inputFiles + "\"" ++ outputFolder ++ file ++ suffix ++ "_c" ++ ch ++ ".wav\" ";
		};
		
		("Combining channels for" + file ++ suffix).postln;
		this.class.cdpRunner("submix interleave", inputFiles, outputFile, "");
		
		// Clean up channel files
		channels.do {|i|
			var ch = i + 1;
			var channelFile = outputFolder ++ file ++ suffix ++ "_c" ++ ch ++ ".wav";
			File.delete(channelFile);
		};
	}
	
	// Combine multiple processed versions into one file
	combineSources {|file, suffix, sources|
		var outputFile = outputFolder ++ file ++ suffix ++ ".wav";
		var inputFiles = "";
		
		sources.do {|src|
			inputFiles = inputFiles + "\"" ++ outputFolder ++ file ++ src ++ ".wav\" ";
		};
		
		("Combining sources for" + file ++ suffix).postln;
		this.class.cdpRunner("submix mergemany", inputFiles, outputFile, "");
		
		// Clean up source files
		sources.do {|src|
			var srcFile = outputFolder ++ file ++ src ++ ".wav";
			File.delete(srcFile);
		};
	}
	
	// Process all files with a function
	processAll {|processFunc|
		File.mkdir(outputFolder);
		
		files.do {|file|
			("Processing:" + file).postln;
			
			// Split channels if needed
			if(channels > 1, {
				this.splitChannels(file);
			});
			
			// Run the process function for this file
			processFunc.value(file, this);
		};
		
		"Batch processing complete!".postln;
	}
	
	// Clean up temporary channel files
	cleanChannelFiles {|file|
		channels.do {|i|
			var ch = i + 1;
			var channelFile = directory ++ file ++ "_c" ++ ch ++ ".wav";
			File.delete(channelFile);
		};
	}
}
