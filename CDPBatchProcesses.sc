////////////////////////////////////////////////////////////////
//
//              CDP Batch Process Definitions
//
////////////////////////////////////////////////////////////////

/*
These classes define batch processing chains that combine
multiple CDP operations with randomized parameters.
Usage: CDPBatchDistort.process(directory, channels)
*/

CDPBatchDistort {
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_average", "_repeat", "_fractal", "_overload", 
			                   "_interpol", "_omit", "_replim", "_replace"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Average
				params = b.rrange(12, 80);
				CDPBase.cdpRunnerSync("distort average", input, 
					b.outputFolder +/+ (file ++ "_average.wav"), 
					params.asString);
				
				// Repeat
				params = b.rrange(2, 6).asString + " -c40";
				CDPBase.cdpRunnerSync("distort repeat", input,
					b.outputFolder +/+ (file ++ "_repeat.wav"),
					params);
				
				// Fractal
				params = b.rrange(4, 60).asString + " " ++ b.rrange(0.1, 0.6).asString;
				CDPBase.cdpRunnerSync("distort fractal", input,
					b.outputFolder +/+ (file ++ "_fractal.wav"),
					params);
				
				// Overload
				params = b.rrange(0.001, 0.025).asString + " " ++ b.rrange(0.8, 0.99).asString 
					+ " " ++ b.rrange(100, 4000).asString;
				CDPBase.cdpRunnerSync("distort overload 2", input,
					b.outputFolder +/+ (file ++ "_overload.wav"),
					params);
				
				// Interpolate
				params = b.rrange(4, 8);
				CDPBase.cdpRunnerSync("distort interpolate", input,
					b.outputFolder +/+ (file ++ "_interpol.wav"),
					params.asString);
				
				// Omit
				params = b.rrange(2, 5).asString + " " ++ b.rrange(6, 8).asString;
				CDPBase.cdpRunnerSync("distort omit", input,
					b.outputFolder +/+ (file ++ "_omit.wav"),
					params);
				
				// Replim
				params = b.rrange(2, 10).asString + " -c30 -s0 -f2000";
				CDPBase.cdpRunnerSync("distort replim", input,
					b.outputFolder +/+ (file ++ "_replim.wav"),
					params);
				
				// Replace
				params = b.rrange(2, 12);
				CDPBase.cdpRunnerSync("distort replace", input,
					b.outputFolder +/+ (file ++ "_replace.wav"),
					params.asString);
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					// Average
					params = b.rrange(12, 80);
					CDPBase.cdpRunnerSync("distort average", input, 
						b.outputFolder +/+ (file ++ "_average_c" ++ ch ++ ".wav"), 
						params.asString);
					
					// Repeat
					params = b.rrange(2, 6).asString + " -c40";
					CDPBase.cdpRunnerSync("distort repeat", input,
						b.outputFolder +/+ (file ++ "_repeat_c" ++ ch ++ ".wav"),
						params);
					
					// Fractal
					params = b.rrange(4, 60).asString + " " ++ b.rrange(0.1, 0.6).asString;
					CDPBase.cdpRunnerSync("distort fractal", input,
						b.outputFolder +/+ (file ++ "_fractal_c" ++ ch ++ ".wav"),
						params);
					
					// Overload
					params = b.rrange(0.001, 0.025).asString + " " ++ b.rrange(0.8, 0.99).asString 
						+ " " ++ b.rrange(100, 4000).asString;
					CDPBase.cdpRunnerSync("distort overload 2", input,
						b.outputFolder +/+ (file ++ "_overload_c" ++ ch ++ ".wav"),
						params);
					
					// Interpolate
					params = b.rrange(4, 8);
					CDPBase.cdpRunnerSync("distort interpolate", input,
						b.outputFolder +/+ (file ++ "_interpol_c" ++ ch ++ ".wav"),
						params.asString);
					
					// Omit
					params = b.rrange(2, 5).asString + " " ++ b.rrange(6, 8).asString;
					CDPBase.cdpRunnerSync("distort omit", input,
						b.outputFolder +/+ (file ++ "_omit_c" ++ ch ++ ".wav"),
						params);
					
					// Replim
					params = b.rrange(2, 10).asString + " -c30 -s0 -f2000";
					CDPBase.cdpRunnerSync("distort replim", input,
						b.outputFolder +/+ (file ++ "_replim_c" ++ ch ++ ".wav"),
						params);
					
					// Replace
					params = b.rrange(2, 12);
					CDPBase.cdpRunnerSync("distort replace", input,
						b.outputFolder +/+ (file ++ "_replace_c" ++ ch ++ ".wav"),
						params.asString);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up channel files
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchCycles {
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_filter", "_reverse", "_delete"];
			
			if(channels == 1, {
				// Mono: process directly without channel splitting
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Filter
				params = b.rrange(100, 4000);
				CDPBase.cdpRunnerSync("distort filter 1", input,
					b.outputFolder +/+ (file ++ "_filter.wav"),
					params.asString + " -s0");
				
				// Reverse
				params = b.rrange(11, 80);
				CDPBase.cdpRunnerSync("distort reverse", input,
					b.outputFolder +/+ (file ++ "_reverse.wav"),
					params.asString);
				
				// Delete
				params = b.rrange(2, 8);
				CDPBase.cdpRunnerSync("distort delete 2", input,
					b.outputFolder +/+ (file ++ "_delete.wav"),
					params.asString);
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					// Filter
					params = b.rrange(100, 4000);
					CDPBase.cdpRunnerSync("distort filter 1", input,
						b.outputFolder +/+ (file ++ "_filter_c" ++ ch ++ ".wav"),
						params.asString + " -s0");
					
					// Reverse
					params = b.rrange(11, 80);
					CDPBase.cdpRunnerSync("distort reverse", input,
						b.outputFolder +/+ (file ++ "_reverse_c" ++ ch ++ ".wav"),
						params.asString);
					
					// Delete
					params = b.rrange(2, 8);
					CDPBase.cdpRunnerSync("distort delete 2", input,
						b.outputFolder +/+ (file ++ "_delete_c" ++ ch ++ ".wav"),
						params.asString);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchExtend {
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_baktobak", "_zigzag", "_iterate"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Baktobak
				params = b.rrange(0.01, 1.0, 2).asString + " " ++ b.rrange(2, 50).asInteger.asString;
				CDPBase.cdpRunnerSync("extend baktobak", input,
					b.outputFolder +/+ (file ++ "_baktobak.wav"),
					params);
				
				// Zigzag
				params = b.rrange(1.2, 2.5, 2).asString + " " ++ b.rrange(2.5, 4.5, 2).asString 
					+ " 25.0 0.062 -s15.0 -m0.43 -r0";
				CDPBase.cdpRunnerSync("extend zigzag 1", input,
					b.outputFolder +/+ (file ++ "_zigzag.wav"),
					params);
				
				// Iterate
				params = b.rrange(2, 8).asInteger.asString + " " ++ b.rrange(0.5, 2.0, 2).asString;
				CDPBase.cdpRunnerSync("extend iterate 1", input,
					b.outputFolder +/+ (file ++ "_iterate.wav"),
					params);
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					// Baktobak
					params = b.rrange(0.01, 1.0, 2).asString + " " ++ b.rrange(2, 50).asInteger.asString;
					CDPBase.cdpRunnerSync("extend baktobak", input,
						b.outputFolder +/+ (file ++ "_baktobak_c" ++ ch ++ ".wav"),
						params);
					
					// Zigzag
					params = b.rrange(1.2, 2.5, 2).asString + " " ++ b.rrange(2.5, 4.5, 2).asString 
						+ " 25.0 0.062 -s15.0 -m0.43 -r0";
					CDPBase.cdpRunnerSync("extend zigzag 1", input,
						b.outputFolder +/+ (file ++ "_zigzag_c" ++ ch ++ ".wav"),
						params);
					
					// Iterate
					params = b.rrange(2, 8).asInteger.asString + " " ++ b.rrange(0.5, 2.0, 2).asString;
					CDPBase.cdpRunnerSync("extend iterate 1", input,
						b.outputFolder +/+ (file ++ "_iterate_c" ++ ch ++ ".wav"),
						params);
				};
				
				// Combine channels
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchMulti {
	/*
	Multi combines several variations and merges them together
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var variations = ["_var1", "_var2", "_var3", "_var4"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Variation 1: Interpolate variations
				4.do {|v|
					params = b.rrange(2, 2 + (v+1));
					CDPBase.cdpRunnerSync("distort interpolate", input,
						b.outputFolder +/+ (file ++ "_var1_" ++ v ++ ".wav"),
						params.asString);
				};
				
				// Variation 2: Pitch variations
				2.do {|v|
					params = b.rrange(0.5, 4, 2).asString + " -c" ++ b.rrange(11, 89).asInteger;
					CDPBase.cdpRunnerSync("distort pitch", input,
						b.outputFolder +/+ (file ++ "_var2_" ++ v ++ ".wav"),
						params);
				};
				
				// Variation 3: Omit variations
				4.do {|v|
					params = b.rrange(2, 4).asInteger.asString + " " ++ b.rrange(5, 8).asInteger.asString;
					CDPBase.cdpRunnerSync("distort omit", input,
						b.outputFolder +/+ (file ++ "_var3_" ++ v ++ ".wav"),
						params);
				};
				
				// Variation 4: Reverse variations
				4.do {|v|
					params = b.rrange(11, 80).asInteger;
					CDPBase.cdpRunnerSync("distort reverse", input,
						b.outputFolder +/+ (file ++ "_var4_" ++ v ++ ".wav"),
						params.asString);
				};
				
				// Merge sub-variations for each main variation
				4.do {|varNum|
					var varSuffix = "_var" ++ (varNum + 1);
					var subFiles = [];
					var count = [4, 2, 4, 4][varNum]; // Number of sub-variations
					
					count.do {|v|
						subFiles = subFiles.add(varSuffix ++ "_" ++ v);
					};
					
					// Merge all sub-variations into one
					b.combineSources(file, varSuffix, subFiles);
				};
				
				// Finally, merge all main variations
				b.combineSources(file, "_multi", variations);
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					// Variation 1: Interpolate variations
					4.do {|v|
						params = b.rrange(2, 2 + (v+1));
						CDPBase.cdpRunnerSync("distort interpolate", input,
							b.outputFolder +/+ (file ++ "_var1_" ++ v ++ "_c" ++ ch ++ ".wav"),
							params.asString);
					};
					
					// Variation 2: Pitch variations
					2.do {|v|
						params = b.rrange(0.5, 4, 2).asString + " -c" ++ b.rrange(11, 89).asInteger;
						CDPBase.cdpRunnerSync("distort pitch", input,
							b.outputFolder +/+ (file ++ "_var2_" ++ v ++ "_c" ++ ch ++ ".wav"),
							params);
					};
					
					// Variation 3: Omit variations
					4.do {|v|
						params = b.rrange(2, 4).asInteger.asString + " " ++ b.rrange(5, 8).asInteger.asString;
						CDPBase.cdpRunnerSync("distort omit", input,
							b.outputFolder +/+ (file ++ "_var3_" ++ v ++ "_c" ++ ch ++ ".wav"),
							params);
					};
					
					// Variation 4: Reverse variations
					4.do {|v|
						params = b.rrange(11, 80).asInteger;
						CDPBase.cdpRunnerSync("distort reverse", input,
							b.outputFolder +/+ (file ++ "_var4_" ++ v ++ "_c" ++ ch ++ ".wav"),
							params.asString);
					};
				};
				
				// Combine sub-variations for each main variation
				4.do {|varNum|
					var varSuffix = "_var" ++ (varNum + 1);
					var subFiles = [];
					var count = [4, 2, 4, 4][varNum]; // Number of sub-variations
					
					count.do {|v|
						// First combine channels for each sub-variation
						b.combineChannels(file, varSuffix ++ "_" ++ v);
						subFiles = subFiles.add(varSuffix ++ "_" ++ v);
					};
					
					// Then merge all sub-variations into one
					b.combineSources(file, varSuffix, subFiles);
				};
				
				// Finally, merge all main variations
				b.combineSources(file, "_multi", variations);
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchEnvel {
	/*
	Envelope distortion variations using different breakpoint files
	Creates 8 variations per file with tremolo-like envelopes
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		var brkPath = Platform.userExtensionDir +/+ "CDP/CDP.js/processes/envel/brk/";
		
		batch.processAll {|file, b|
			var subSuffixes = ["_envela", "_envelb", "_envelc", "_enveld", 
			                   "_envele", "_envelf", "_envelg", "_envelh"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Each envelope uses a different breakpoint file
				params = brkPath ++ "trema.txt " ++ b.rrange(0.005, 0.02, 3).asString + " -e" ++ b.rrange(0.3, 0.7, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envela.wav"), params);
				
				params = brkPath ++ "tremb.txt " ++ b.rrange(0.08, 0.16, 3).asString + " -e" ++ b.rrange(0.8, 1.2, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envelb.wav"), params);
				
				params = brkPath ++ "tremc.txt " ++ b.rrange(0.18, 0.28, 3).asString + " -e" ++ b.rrange(1.5, 2.5, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envelc.wav"), params);
				
				params = brkPath ++ "tremd.txt " ++ b.rrange(0.16, 0.26, 3).asString + " -e" ++ b.rrange(2.5, 3.5, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_enveld.wav"), params);
				
				params = brkPath ++ "treme.txt " ++ b.rrange(0.13, 0.23, 3).asString + " -e" ++ b.rrange(3.5, 4.5, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envele.wav"), params);
				
				params = brkPath ++ "tremf.txt " ++ b.rrange(0.25, 0.35, 3).asString + " -e" ++ b.rrange(3.2, 4.0, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envelf.wav"), params);
				
				params = brkPath ++ "tremg.txt " ++ b.rrange(0.38, 0.48, 3).asString + " -e" ++ b.rrange(28, 36, 0).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envelg.wav"), params);
				
				params = brkPath ++ "tremh.txt " ++ b.rrange(0.25, 0.35, 3).asString + " -e" ++ b.rrange(3.2, 4.0, 1).asString;
				CDPBase.cdpRunnerSync("distort envel 3", input,
					b.outputFolder +/+ (file ++ "_envelh.wav"), params);
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					params = brkPath ++ "trema.txt " ++ b.rrange(0.005, 0.02, 3).asString + " -e" ++ b.rrange(0.3, 0.7, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envela_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremb.txt " ++ b.rrange(0.08, 0.16, 3).asString + " -e" ++ b.rrange(0.8, 1.2, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envelb_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremc.txt " ++ b.rrange(0.18, 0.28, 3).asString + " -e" ++ b.rrange(1.5, 2.5, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envelc_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremd.txt " ++ b.rrange(0.16, 0.26, 3).asString + " -e" ++ b.rrange(2.5, 3.5, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_enveld_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "treme.txt " ++ b.rrange(0.13, 0.23, 3).asString + " -e" ++ b.rrange(3.5, 4.5, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envele_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremf.txt " ++ b.rrange(0.25, 0.35, 3).asString + " -e" ++ b.rrange(3.2, 4.0, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envelf_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremg.txt " ++ b.rrange(0.38, 0.48, 3).asString + " -e" ++ b.rrange(28, 36, 0).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envelg_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremh.txt " ++ b.rrange(0.25, 0.35, 3).asString + " -e" ++ b.rrange(3.2, 4.0, 1).asString;
					CDPBase.cdpRunnerSync("distort envel 3", input,
						b.outputFolder +/+ (file ++ "_envelh_c" ++ ch ++ ".wav"), params);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchTremolo {
	/*
	Tremolo effects with different breakpoint file envelopes
	Creates 8 variations per file
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		var brkPath = Platform.userExtensionDir +/+ "CDP/CDP.js/processes/tremolo/brk/";
		
		batch.processAll {|file, b|
			var subSuffixes = ["_tremoloa", "_tremolob", "_tremoloc", "_tremolod",
			                   "_tremoloe", "_tremolof", "_tremolog", "_tremoloh"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				params = brkPath ++ "trema.txt " ++ b.rrange(0.4, 0.8, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremoloa.wav"), params);
				
				params = brkPath ++ "tremb.txt " ++ b.rrange(0.8, 0.99, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremolob.wav"), params);
				
				params = brkPath ++ "tremc.txt " ++ b.rrange(0.5, 0.95, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremoloc.wav"), params);
				
				params = brkPath ++ "tremd.txt " ++ b.rrange(0.85, 0.95, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremolod.wav"), params);
				
				params = brkPath ++ "treme.txt " ++ b.rrange(0.4, 0.8, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremoloe.wav"), params);
				
				params = brkPath ++ "tremf.txt " ++ b.rrange(0.5, 0.7, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremolof.wav"), params);
				
				params = brkPath ++ "tremg.txt " ++ b.rrange(0.6, 0.9, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremolog.wav"), params);
				
				params = brkPath ++ "tremh.txt " ++ b.rrange(0.3, 0.5, 2).asString + " 1.0";
				CDPBase.cdpRunnerSync("envel tremolo 1", input,
					b.outputFolder +/+ (file ++ "_tremoloh.wav"), params);
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					params = brkPath ++ "trema.txt " ++ b.rrange(0.4, 0.8, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremoloa_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremb.txt " ++ b.rrange(0.8, 0.99, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremolob_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremc.txt " ++ b.rrange(0.5, 0.95, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremoloc_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremd.txt " ++ b.rrange(0.85, 0.95, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremolod_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "treme.txt " ++ b.rrange(0.4, 0.8, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremoloe_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremf.txt " ++ b.rrange(0.5, 0.7, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremolof_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremg.txt " ++ b.rrange(0.6, 0.9, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremolog_c" ++ ch ++ ".wav"), params);
					
					params = brkPath ++ "tremh.txt " ++ b.rrange(0.3, 0.5, 2).asString + " 1.0";
					CDPBase.cdpRunnerSync("envel tremolo 1", input,
						b.outputFolder +/+ (file ++ "_tremoloh_c" ++ ch ++ ".wav"), params);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchGranulate {
	/*
	Granulation effects using modify brassage
	Creates 5 variations per file with different granulation settings
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_grana", "_granb", "_granc", "_grand", "_grane"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Scramble grainsize & scramble range
				params = b.rrange(60, 100, 0).asString + " -r" ++ b.rrange(30, 50, 0).asString;
				CDPBase.cdpRunnerSync("modify brassage 4", input,
					b.outputFolder +/+ (file ++ "_grana.wav"), params);
				
				// Granulate density
				params = b.rrange(1.5, 2.0, 2).asString;
				CDPBase.cdpRunnerSync("modify brassage 5", input,
					b.outputFolder +/+ (file ++ "_granb.wav"), params);
				
				// Timeshrink slow
				params = b.rrange(0.2, 0.35, 2).asString;
				CDPBase.cdpRunnerSync("modify brassage 2", input,
					b.outputFolder +/+ (file ++ "_granc.wav"), params);
				
				// Timeshrink fast
				params = b.rrange(1.3, 1.7, 2).asString;
				CDPBase.cdpRunnerSync("modify brassage 2", input,
					b.outputFolder +/+ (file ++ "_grand.wav"), params);
				
				// Another timeshrink variation
				params = b.rrange(1.4, 1.6, 2).asString;
				CDPBase.cdpRunnerSync("modify brassage 2", input,
					b.outputFolder +/+ (file ++ "_grane.wav"), params);
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					params = b.rrange(60, 100, 0).asString + " -r" ++ b.rrange(30, 50, 0).asString;
					CDPBase.cdpRunnerSync("modify brassage 4", input,
						b.outputFolder +/+ (file ++ "_grana_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(1.5, 2.0, 2).asString;
					CDPBase.cdpRunnerSync("modify brassage 5", input,
						b.outputFolder +/+ (file ++ "_granb_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(0.2, 0.35, 2).asString;
					CDPBase.cdpRunnerSync("modify brassage 2", input,
						b.outputFolder +/+ (file ++ "_granc_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(1.3, 1.7, 2).asString;
					CDPBase.cdpRunnerSync("modify brassage 2", input,
						b.outputFolder +/+ (file ++ "_grand_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(1.4, 1.6, 2).asString;
					CDPBase.cdpRunnerSync("modify brassage 2", input,
						b.outputFolder +/+ (file ++ "_grane_c" ++ ch ++ ".wav"), params);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchTranspose {
	/*
	Transposition/pitch shifting variations
	Creates 7 variations per file at different musical intervals
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_octdown", "_2octdown", "_octup", "_fifth", "_fifthdown", "_seventh", "_seventhdown"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				
				// Octave down
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_octdown.wav"), "-12");
				
				// Two octaves down
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_2octdown.wav"), "-24");
				
				// Octave up
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_octup.wav"), "12");
				
				// Perfect fifth up
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_fifth.wav"), "7");
				
				// Perfect fifth down
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_fifthdown.wav"), "-7");
				
				// Major seventh up
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_seventh.wav"), "11");
				
				// Major seventh down
				CDPBase.cdpRunnerSync("modify speed 2", input,
					b.outputFolder +/+ (file ++ "_seventhdown.wav"), "-11");
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_octdown_c" ++ ch ++ ".wav"), "-12");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_2octdown_c" ++ ch ++ ".wav"), "-24");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_octup_c" ++ ch ++ ".wav"), "12");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_fifth_c" ++ ch ++ ".wav"), "7");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_fifthdown_c" ++ ch ++ ".wav"), "-7");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_seventh_c" ++ ch ++ ".wav"), "11");
					
					CDPBase.cdpRunnerSync("modify speed 2", input,
						b.outputFolder +/+ (file ++ "_seventhdown_c" ++ ch ++ ".wav"), "-11");
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}


CDPBatchFilter {
	/*
	Comprehensive filtering variations
	Creates 8 variations per file with different filter types
	*/
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_lopa", "_hipa", "_lopb", "_hipb", "_cylop", "_cyhip", "_band", "_notch"];
			
			if(channels == 1, {
				// Mono: process directly
				var input = directory +/+ (file ++ ".wav");
				var params;
				
				// Lowpass A
				params = "-6 " ++ b.rrange(70, 120, 0).asString + " " ++ b.rrange(122, 160, 0).asString;
				CDPBase.cdpRunnerSync("filter lohi 1", input,
					b.outputFolder +/+ (file ++ "_lopa.wav"), params);
				
				// Highpass A
				params = "-6 " ++ b.rrange(6000, 8000, 0).asString + " " ++ b.rrange(4000, 6000, 0).asString;
				CDPBase.cdpRunnerSync("filter lohi 1", input,
					b.outputFolder +/+ (file ++ "_hipa.wav"), params);
				
				// Lowpass B
				params = "-6 " ++ b.rrange(60, 100, 0).asString + " " ++ b.rrange(120, 180, 0).asString;
				CDPBase.cdpRunnerSync("filter lohi 1", input,
					b.outputFolder +/+ (file ++ "_lopb.wav"), params);
				
				// Highpass B
				params = "-6 " ++ b.rrange(8000, 10000, 0).asString + " " ++ b.rrange(5000, 8000, 0).asString;
				CDPBase.cdpRunnerSync("filter lohi 1", input,
					b.outputFolder +/+ (file ++ "_hipb.wav"), params);
				
				// Cycle lowpass (distort filter)
				params = b.rrange(1000, 4000, 0).asString + " -s0";
				CDPBase.cdpRunnerSync("distort filter 1", input,
					b.outputFolder +/+ (file ++ "_cylop.wav"), params);
				
				// Cycle highpass (distort filter)
				params = b.rrange(500, 2000, 0).asString + " -s0";
				CDPBase.cdpRunnerSync("distort filter 2", input,
					b.outputFolder +/+ (file ++ "_cyhip.wav"), params);
				
				// Bandpass
				params = b.rrange(0.2, 0.8, 2).asString + " 2 " ++ b.rrange(200, 2000, 0).asString;
				CDPBase.cdpRunnerSync("filter variable 3", input,
					b.outputFolder +/+ (file ++ "_band.wav"), params);
				
				// Notch
				params = b.rrange(0.1, 0.8, 2).asString + " 2 " ++ b.rrange(400, 3000, 0).asString;
				CDPBase.cdpRunnerSync("filter variable 4", input,
					b.outputFolder +/+ (file ++ "_notch.wav"), params);
				
			}, {
				// Multi-channel: split, process, recombine
				channels.do {|i|
					var ch = i + 1;
					var input = directory +/+ (file ++ "_c" ++ ch ++ ".wav");
					var params;
					
					params = "-6 " ++ b.rrange(70, 120, 0).asString + " " ++ b.rrange(122, 160, 0).asString;
					CDPBase.cdpRunnerSync("filter lohi 1", input,
						b.outputFolder +/+ (file ++ "_lopa_c" ++ ch ++ ".wav"), params);
					
					params = "-6 " ++ b.rrange(6000, 8000, 0).asString + " " ++ b.rrange(4000, 6000, 0).asString;
					CDPBase.cdpRunnerSync("filter lohi 1", input,
						b.outputFolder +/+ (file ++ "_hipa_c" ++ ch ++ ".wav"), params);
					
					params = "-6 " ++ b.rrange(60, 100, 0).asString + " " ++ b.rrange(120, 180, 0).asString;
					CDPBase.cdpRunnerSync("filter lohi 1", input,
						b.outputFolder +/+ (file ++ "_lopb_c" ++ ch ++ ".wav"), params);
					
					params = "-6 " ++ b.rrange(8000, 10000, 0).asString + " " ++ b.rrange(5000, 8000, 0).asString;
					CDPBase.cdpRunnerSync("filter lohi 1", input,
						b.outputFolder +/+ (file ++ "_hipb_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(1000, 4000, 0).asString + " -s0";
					CDPBase.cdpRunnerSync("distort filter 1", input,
						b.outputFolder +/+ (file ++ "_cylop_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(500, 2000, 0).asString + " -s0";
					CDPBase.cdpRunnerSync("distort filter 2", input,
						b.outputFolder +/+ (file ++ "_cyhip_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(0.2, 0.8, 2).asString + " 2 " ++ b.rrange(200, 2000, 0).asString;
					CDPBase.cdpRunnerSync("filter variable 3", input,
						b.outputFolder +/+ (file ++ "_band_c" ++ ch ++ ".wav"), params);
					
					params = b.rrange(0.1, 0.8, 2).asString + " 2 " ++ b.rrange(400, 3000, 0).asString;
					CDPBase.cdpRunnerSync("filter variable 4", input,
						b.outputFolder +/+ (file ++ "_notch_c" ++ ch ++ ".wav"), params);
				};
				
				// Combine channels for each variation
				subSuffixes.do {|sub|
					b.combineChannels(file, sub);
				};
				
				// Clean up
				b.cleanChannelFiles(file);
			});
		};
	}
}
