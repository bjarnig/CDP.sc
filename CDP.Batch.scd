///////////////////////////////////////////////////////////////
//
//                CDP Batch Processing Examples
//
////////////////////////////////////////////////////////////////

/*
This file demonstrates how to use the CDP batch processing system.
The batch system processes all .wav files in a directory with
creative transformations using randomized parameters.

USAGE:
1. Set your directory containing .wav files
2. Set the number of channels (1 for mono, 2 for stereo)
3. Run one of the processes below
*/

(
// Configuration
~batchDirectory = "/path/to/your/audio/files/"; // Change this!
~channels = 1; // 1 for mono, 2 for stereo

s.boot; // Boot server if needed
)


////////////////////// EXAMPLE PROCESSES //////////////////////

// 1. DISTORT BATCH
// Creates 8 variations per file: average, repeat, fractal, overload, 
// interpolate, omit, replim, replace
(
"Running DISTORT batch process...".postln;
CDPBatchDistort.process(~batchDirectory, ~channels);
"DISTORT complete! Check the batch_output folder.".postln;
)


// 2. CYCLES BATCH
// Creates 3 variations per file: filter, reverse, delete
(
"Running CYCLES batch process...".postln;
CDPBatchCycles.process(~batchDirectory, ~channels);
"CYCLES complete! Check the batch_output folder.".postln;
)


// 3. EXTEND BATCH
// Creates 3 variations per file: baktobak, zigzag, iterate
(
"Running EXTEND batch process...".postln;
CDPBatchExtend.process(~batchDirectory, ~channels);
"EXTEND complete! Check the batch_output folder.".postln;
)


// 4. MULTI BATCH (Complex!)
// Creates multiple variations and combines them into complex textures
(
"Running MULTI batch process...".postln;
CDPBatchMulti.process(~batchDirectory, ~channels);
"MULTI complete! Check the batch_output folder.".postln;
)


////////////////////// CUSTOM PROCESS EXAMPLE //////////////////////

// You can also create custom batch processes on the fly:
(
var batch = CDPBatch(~batchDirectory, ~channels);

batch.processAll {|file, b|
	var params;
	
	"Processing:" + file;
	
	~channels.do {|i|
		var ch = i + 1;
		var input = ~batchDirectory ++ file ++ "_c" ++ ch ++ ".wav";
		
		// Example: Apply distort average with random parameters
		params = b.rrange(20, 60).asString;
		CDPBase.cdpRunner("distort average", input,
			b.outputFolder ++ file ++ "_custom_c" ++ ch ++ ".wav",
			params);
	};
	
	// Combine channels
	b.combineChannels(file, "_custom");
	
	// Clean up
	if(~channels > 1, { b.cleanChannelFiles(file) });
};

"Custom batch processing complete!".postln;
)


////////////////////// PROCESS CHAIN EXAMPLE //////////////////////

// Process files through multiple transformations in sequence
(
"Running process chain...".postln;

// First pass: Distort
CDPBatchDistort.process(~batchDirectory, ~channels);

// Second pass: Take the distorted files and extend them
~batchDirectory = ~batchDirectory ++ "batch_output/";
CDPBatchExtend.process(~batchDirectory, ~channels);

"Process chain complete!".postln;
)


////////////////////// NOTES //////////////////////

/*
KEY DIFFERENCES FROM GUI VERSION:
- The GUI version (CDPGui, CDPFft, CDPCross) is for INTERACTIVE processing
  of single files with visual feedback
  
- The BATCH version (CDPBatch, CDPBatchDistort, etc.) is for AUTOMATED 
  processing of entire directories with creative parameter randomization

TIPS:
- All output goes to a "batch_output" folder in your source directory
- Intermediate channel files are automatically cleaned up
- Use rrange() for randomized parameters in custom processes
- Combine multiple batch processes for complex transformations
- The system handles both mono and multi-channel files automatically

CREATING YOUR OWN BATCH PROCESSES:
See CDPBatchProcesses.sc for examples of how to define new batch processes.
You can also create processes inline using the custom process example above.
*/
