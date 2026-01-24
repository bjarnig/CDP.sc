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
			var suffix = "_distorted";
			var subSuffixes = ["_average", "_repeat", "_fractal", "_overload", 
			                   "_interpol", "_omit", "_replim", "_replace"];
			
			channels.do {|i|
				var ch = i + 1;
				var input = directory ++ file ++ "_c" ++ ch ++ ".wav";
				var params;
				
				// Average
				params = b.rrange(12, 80);
				CDPBase.cdpRunner("distort average", input, 
					b.outputFolder ++ file ++ "_average_c" ++ ch ++ ".wav", 
					params.asString);
				
				// Repeat
				params = b.rrange(2, 6).asString + " -c40";
				CDPBase.cdpRunner("distort repeat", input,
					b.outputFolder ++ file ++ "_repeat_c" ++ ch ++ ".wav",
					params);
				
				// Fractal
				params = b.rrange(4, 60).asString + b.rrange(0.1, 0.6).asString;
				CDPBase.cdpRunner("distort fractal", input,
					b.outputFolder ++ file ++ "_fractal_c" ++ ch ++ ".wav",
					params);
				
				// Overload
				params = b.rrange(0.001, 0.025).asString + b.rrange(0.8, 0.99).asString 
					+ b.rrange(100, 4000).asString;
				CDPBase.cdpRunner("distort overload 2", input,
					b.outputFolder ++ file ++ "_overload_c" ++ ch ++ ".wav",
					params);
				
				// Interpolate
				params = b.rrange(4, 8);
				CDPBase.cdpRunner("distort interpolate", input,
					b.outputFolder ++ file ++ "_interpol_c" ++ ch ++ ".wav",
					params.asString);
				
				// Omit
				params = b.rrange(2, 5).asString + b.rrange(6, 8).asString;
				CDPBase.cdpRunner("distort omit", input,
					b.outputFolder ++ file ++ "_omit_c" ++ ch ++ ".wav",
					params);
				
				// Replim
				params = b.rrange(2, 10).asString + " -c30 -s0 -f2000";
				CDPBase.cdpRunner("distort replim", input,
					b.outputFolder ++ file ++ "_replim_c" ++ ch ++ ".wav",
					params);
				
				// Replace
				params = b.rrange(2, 12);
				CDPBase.cdpRunner("distort replace", input,
					b.outputFolder ++ file ++ "_replace_c" ++ ch ++ ".wav",
					params.asString);
			};
			
			// Combine channels for each variation
			subSuffixes.do {|sub|
				b.combineChannels(file, sub);
			};
			
			// Clean up channel files
			if(channels > 1, { b.cleanChannelFiles(file) });
		};
	}
}


CDPBatchCycles {
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_filter", "_reverse", "_delete"];
			
			channels.do {|i|
				var ch = i + 1;
				var input = directory ++ file ++ "_c" ++ ch ++ ".wav";
				var params;
				
				// Filter
				params = b.rrange(100, 4000);
				CDPBase.cdpRunner("distort filter 1", input,
					b.outputFolder ++ file ++ "_filter_c" ++ ch ++ ".wav",
					params.asString + " -s0");
				
				// Reverse
				params = b.rrange(11, 80);
				CDPBase.cdpRunner("distort reverse", input,
					b.outputFolder ++ file ++ "_reverse_c" ++ ch ++ ".wav",
					params.asString);
				
				// Delete
				params = b.rrange(2, 8);
				CDPBase.cdpRunner("distort delete 2", input,
					b.outputFolder ++ file ++ "_delete_c" ++ ch ++ ".wav",
					params.asString);
			};
			
			// Combine channels for each variation
			subSuffixes.do {|sub|
				b.combineChannels(file, sub);
			};
			
			// Clean up
			if(channels > 1, { b.cleanChannelFiles(file) });
		};
	}
}


CDPBatchExtend {
	
	*process {|directory, channels=1|
		var batch = CDPBatch(directory, channels);
		
		batch.processAll {|file, b|
			var subSuffixes = ["_baktobak", "_zigzag", "_iterate"];
			
			channels.do {|i|
				var ch = i + 1;
				var input = directory ++ file ++ "_c" ++ ch ++ ".wav";
				var params;
				
				// Baktobak
				params = b.rrange(0.01, 1.0, 2).asString + b.rrange(2, 50).asInteger.asString;
				CDPBase.cdpRunner("extend baktobak", input,
					b.outputFolder ++ file ++ "_baktobak_c" ++ ch ++ ".wav",
					params);
				
				// Zigzag
				params = b.rrange(1.2, 2.5, 2).asString + b.rrange(2.5, 4.5, 2).asString 
					+ "25.0 0.062 -s15.0 -m0.43 -r0";
				CDPBase.cdpRunner("extend zigzag 1", input,
					b.outputFolder ++ file ++ "_zigzag_c" ++ ch ++ ".wav",
					params);
				
				// Iterate
				params = b.rrange(2, 8).asInteger.asString + b.rrange(0.5, 2.0, 2).asString;
				CDPBase.cdpRunner("extend iterate 1", input,
					b.outputFolder ++ file ++ "_iterate_c" ++ ch ++ ".wav",
					params);
			};
			
			// Combine channels
			subSuffixes.do {|sub|
				b.combineChannels(file, sub);
			};
			
			// Clean up
			if(channels > 1, { b.cleanChannelFiles(file) });
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
			
			channels.do {|i|
				var ch = i + 1;
				var input = directory ++ file ++ "_c" ++ ch ++ ".wav";
				var params;
				
				// Variation 1: Interpolate variations
				4.do {|v|
					params = b.rrange(2, 2 + (v+1));
					CDPBase.cdpRunner("distort interpolate", input,
						b.outputFolder ++ file ++ "_var1_" ++ v ++ "_c" ++ ch ++ ".wav",
						params.asString);
				};
				
				// Variation 2: Pitch variations
				2.do {|v|
					params = b.rrange(0.5, 4, 2).asString + " -c" ++ b.rrange(11, 89).asInteger;
					CDPBase.cdpRunner("distort pitch", input,
						b.outputFolder ++ file ++ "_var2_" ++ v ++ "_c" ++ ch ++ ".wav",
						params);
				};
				
				// Variation 3: Omit variations
				4.do {|v|
					params = b.rrange(2, 4).asInteger.asString + b.rrange(5, 8).asInteger.asString;
					CDPBase.cdpRunner("distort omit", input,
						b.outputFolder ++ file ++ "_var3_" ++ v ++ "_c" ++ ch ++ ".wav",
						params);
				};
				
				// Variation 4: Reverse variations
				4.do {|v|
					params = b.rrange(11, 80).asInteger;
					CDPBase.cdpRunner("distort reverse", input,
						b.outputFolder ++ file ++ "_var4_" ++ v ++ "_c" ++ ch ++ ".wav",
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
			if(channels > 1, { b.cleanChannelFiles(file) });
		};
	}
}
