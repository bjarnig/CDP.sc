////////////////////////////////////////////////////////////////
//
//                     CDP Batch Processing
//
////////////////////////////////////////////////////////////////

CDPBatch : CDPBase {
	
	var <directory;
	var <>files;  // <> allows both reading and writing for multi-pass processing
	var <channels;
	var <>outputFolder;  // <> allows both reading and writing
	
	*new {|directory, channels=1|
		^super.new.init(directory, channels);
	}
	
	// Create batch from parameter ranges
	// processes is an array of [command, paramRanges, suffix]
	// paramRanges can be:
	//   - [min, max] for single parameter
	//   - [min, max, decimals] for single parameter with decimal precision
	//   - [[min1, max1], [min2, max2], ...] for multiple parameters
	//   - [[min1, max1, dec1], [min2, max2, dec2], ...] for multiple with decimals
	//   - "fixed string" for fixed parameters
	// passes: number of times to reprocess (default 1)
	// Example: [["distort average", [12, 80], "_average"]]
	*ranges {|directory, channels=1, processes, passes=1|
		var currentDir = directory.asString;
		var outputDir;
		var batch;
		var filesToProcess; // Track which files to process in each pass
		
		// Normalize directory path
		if(currentDir.endsWith("/"), {
			currentDir = currentDir[..currentDir.size-2];
		});
		
		outputDir = currentDir +/+ "batch_output";
		
		// Create output directory once
		File.mkdir(outputDir);
		
		passes.do {|passNum|
			var newFiles = List(); // Track files created in this pass
			
			("=== PASS" + (passNum + 1) + "of" + passes + "===").postln;
			
			batch = this.new(currentDir, channels);
			batch.outputFolder = outputDir;  // Override to use single output dir
			
			// Use specific file list if we have one, otherwise use all files from batch
			if(filesToProcess.notNil, {
				batch.files = filesToProcess;
			});
			
			batch.processAll {|file, b|
				if(channels == 1, {
					// Mono: process directly
					var input = currentDir +/+ (file ++ ".wav");
					
					processes.do {|spec|
						var command = spec[0];
						var paramRanges = spec[1];
						var suffix = spec[2];
						var params = this.prBuildParams(paramRanges, b);
						var outputBasename = file ++ suffix;
						
						CDPBase.cdpRunnerSync(command, input,
							outputDir +/+ (outputBasename ++ ".wav"),
							params);
						
						// Track the new file (without extension)
						if(newFiles.includes(outputBasename).not, {
							newFiles.add(outputBasename);
						});
					};
				}, {
					// Multi-channel: split, process, recombine
					channels.do {|i|
						var ch = i + 1;
						var input = currentDir +/+ (file ++ "_c" ++ ch ++ ".wav");
						
						processes.do {|spec|
							var command = spec[0];
							var paramRanges = spec[1];
							var suffix = spec[2];
							var params = this.prBuildParams(paramRanges, b);
							
							CDPBase.cdpRunnerSync(command, input,
								outputDir +/+ (file ++ suffix ++ "_c" ++ ch ++ ".wav"),
								params);
						};
					};
					
					// Combine channels for each variation
					processes.do {|spec|
						var outputBasename = file ++ spec[2];
						b.combineChannels(file, spec[2]);
						
						// Track the new file (without extension)
						if(newFiles.includes(outputBasename).not, {
							newFiles.add(outputBasename);
						});
					};
					
					// Clean up
					b.cleanChannelFiles(file);
				});
			};
			
			// For next pass, only process the files we just created
			if(passNum < (passes - 1), {
				currentDir = outputDir;
				filesToProcess = newFiles;
			});
		};
		
		^batch;
	}
	
	// Private method to build parameter string from range specifications
	*prBuildParams {|paramRanges, batch|
		var params = "";
		
		// Handle different input formats
		case
		{ paramRanges.isString } {
			// Fixed string
			params = paramRanges;
		}
		{ paramRanges.isArray and: { paramRanges[0].isNumber } } {
			// Single parameter: [min, max] or [min, max, decimals]
			var decimals = if(paramRanges.size > 2, { paramRanges[2] }, { 2 });
			params = batch.rrange(paramRanges[0], paramRanges[1], decimals).asString;
		}
		{ paramRanges.isArray and: { paramRanges[0].isArray } } {
			// Multiple parameters: [[min1, max1], [min2, max2], ...]
			paramRanges.do {|range, i|
				if(range.isString, {
					// Fixed string parameter
					params = params ++ range;
				}, {
					// Random range parameter
					var decimals = if(range.size > 2, { range[2] }, { 2 });
					params = params ++ batch.rrange(range[0], range[1], decimals).asString;
				});
				// Add space between parameters (except after last one)
				if(i < (paramRanges.size - 1), {
					params = params + " ";
				});
			};
		};
		
		^params;
	}
	
	// Create batch with custom parameter functions
	// processes is an array of [command, paramsFunc, suffix]
	// passes: number of times to reprocess (default 1)
	// Example: [["distort average", {|b| b.rrange(12, 80).asString}, "_average"]]
	*custom {|directory, channels=1, processes, passes=1|
		var currentDir = directory.asString;
		var outputDir;
		var batch;
		var filesToProcess; // Track which files to process in each pass
		
		// Normalize directory path
		if(currentDir.endsWith("/"), {
			currentDir = currentDir[..currentDir.size-2];
		});
		
		outputDir = currentDir +/+ "batch_output";
		
		// Create output directory once
		File.mkdir(outputDir);
		
		passes.do {|passNum|
			var newFiles = List(); // Track files created in this pass
			
			("=== PASS" + (passNum + 1) + "of" + passes + "===").postln;
			
			batch = this.new(currentDir, channels);
			batch.outputFolder = outputDir;  // Override to use single output dir
			
			// Use specific file list if we have one, otherwise use all files from batch
			if(filesToProcess.notNil, {
				batch.files = filesToProcess;
			});
			
			batch.processAll {|file, b|
				if(channels == 1, {
					// Mono: process directly
					var input = currentDir +/+ (file ++ ".wav");
					
					processes.do {|spec|
						var command = spec[0];
						var paramsFunc = spec[1];
						var suffix = spec[2];
						var params = paramsFunc.value(b);
						var outputBasename = file ++ suffix;
						
						CDPBase.cdpRunnerSync(command, input,
							outputDir +/+ (outputBasename ++ ".wav"),
							params);
						
						// Track the new file (without extension)
						if(newFiles.includes(outputBasename).not, {
							newFiles.add(outputBasename);
						});
					};
				}, {
					// Multi-channel: split, process, recombine
					channels.do {|i|
						var ch = i + 1;
						var input = currentDir +/+ (file ++ "_c" ++ ch ++ ".wav");
						
						processes.do {|spec|
							var command = spec[0];
							var paramsFunc = spec[1];
							var suffix = spec[2];
							var params = paramsFunc.value(b);
							
							CDPBase.cdpRunnerSync(command, input,
								outputDir +/+ (file ++ suffix ++ "_c" ++ ch ++ ".wav"),
								params);
						};
					};
					
					// Combine channels for each variation
					processes.do {|spec|
						var outputBasename = file ++ spec[2];
						b.combineChannels(file, spec[2]);
						
						// Track the new file (without extension)
						if(newFiles.includes(outputBasename).not, {
							newFiles.add(outputBasename);
						});
					};
					
					// Clean up
					b.cleanChannelFiles(file);
				});
			};
			
			// For next pass, only process the files we just created
			if(passNum < (passes - 1), {
				currentDir = outputDir;
				filesToProcess = newFiles;
			});
		};
		
		^batch;
	}
	
	init {|dir, ch|
		// Ensure directory path doesn't end with trailing slash for consistency
		directory = dir.asString;
		if(directory.endsWith("/"), {
			directory = directory[..directory.size-2];
		});
		
		// Validate channels parameter
		if(ch.isNil, {
			Error("channels parameter cannot be nil. Please specify 1 for mono or 2 for stereo.").throw;
		});
		
		channels = ch;
		outputFolder = directory +/+ "batch_output";
		files = List();
		this.readFiles();
	}
	
	readFiles {
		var dirPath, allFiles, filteredCount = 0;
		
		// PathName may need trailing slash to work correctly
		dirPath = directory;
		if(dirPath.endsWith("/").not, {
			dirPath = dirPath ++ "/";
		});
		
		allFiles = PathName(dirPath).files;
		
		allFiles.do {|file|
			if(file.extension == "wav", {
				// Filter out system files and channel files (_c1, _c2, etc.)
				// Use regex-like pattern to only exclude files ending with _c followed by digit
				var fname = file.fileNameWithoutExtension;
				var isChannelFile = fname.findRegexp("_c[0-9]+$").size > 0;
				
				if((fname.beginsWith("._").not) and: (isChannelFile.not), {
					files.add(fname);
				}, {
					filteredCount = filteredCount + 1;
				});
			});
		};
		
		("Found" + files.size + "files to process.").postln;
	}
	
	// Random range generator for creative variations
	rrange {|lo, hi, decimals=2|
		^((hi - lo).rand + lo).round(10.pow(decimals.neg));
	}
	
	// Run CDP command for a specific file and channel
	runForChannel {|file, channel, command, params|
		var input, output, fullCommand;
		input = directory +/+ (file ++ "_c" ++ channel ++ ".wav");
		output = outputFolder +/+ (file ++ params["suffix"] ++ "_c" ++ channel ++ ".wav");
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
		var inputFile = directory +/+ (file ++ ".wav");
		var outputBase = directory +/+ file;
		
		("Splitting channels for" + file).postln;
		
		if(channels > 1, {
			// Use housekeep chans 2 to extract channels
			this.class.cdpRunner("housekeep chans 2", inputFile, "", "");
		});
	}
	
	// Recombine channel files into stereo/multi-channel
	combineChannels {|file, suffix|
		var outputFile = outputFolder +/+ (file ++ suffix ++ ".wav");
		var inputFiles = "";
		
		File.mkdir(outputFolder);
		
		channels.do {|i|
			var ch = i + 1;
			inputFiles = inputFiles + "\"" ++ (outputFolder +/+ (file ++ suffix ++ "_c" ++ ch ++ ".wav")) ++ "\" ";
		};
		
		("Combining channels for" + file ++ suffix).postln;
		this.class.cdpRunner("submix interleave", inputFiles, outputFile, "");
		
		// Clean up channel files
		channels.do {|i|
			var ch = i + 1;
			var channelFile = outputFolder +/+ (file ++ suffix ++ "_c" ++ ch ++ ".wav");
			File.delete(channelFile);
		};
	}
	
	// Combine multiple processed versions into one file
	combineSources {|file, suffix, sources|
		var outputFile = outputFolder +/+ (file ++ suffix ++ ".wav");
		var inputFiles = "";
		
		sources.do {|src|
			inputFiles = inputFiles + "\"" ++ (outputFolder +/+ (file ++ src ++ ".wav")) ++ "\" ";
		};
		
		("Combining sources for" + file ++ suffix).postln;
		this.class.cdpRunner("submix mergemany", inputFiles, outputFile, "");
		
		// Clean up source files
		sources.do {|src|
			var srcFile = outputFolder +/+ (file ++ src ++ ".wav");
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
			var channelFile = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
			File.delete(channelFile);
		};
	}
}
