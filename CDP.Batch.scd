///////////////////////////////////////////////////////////////
//
//                CDP Batch Processing Examples
//
////////////////////////////////////////////////////////////////

/*
This file demonstrates how to use the CDP batch processing system.
The batch system processes all .wav files in a directory with
creative transformations using randomized parameters.

THREE WAYS TO CREATE BATCH PROCESSES:

1. RANGES METHOD (Easiest!) - Just specify parameter ranges
   Example: ["distort average", [12, 80], "_average"]
   
2. CUSTOM METHOD (More control) - Use custom parameter functions
   Example: ["distort average", {|b| b.rrange(12, 80).asString}, "_average"]
   
3. PRESET CLASSES (Built-in) - Use CDPBatch* classes
   Example: CDPBatchDistort.process(~dir, 1);

PREREQUISITES:
- CDP (Composers Desktop Project) must be installed
- Run CDPBase.checkCDPInstalled to verify installation

USAGE:
1. Set your directory containing .wav files
2. Set the number of channels (1 for mono, 2 for stereo)
3. Run one of the processes below
*/

// CHECK CDP INSTALLATION FIRST
(
if(CDPBase.checkCDPInstalled, {
	"CDP is installed and ready to use!".postln;
}, {
	"Please install CDP before running batch processes.".postln;
	"Visit: http://www.composersdesktop.com/".postln;
	"".postln;
	"If CDP works in your terminal but not here, set the path manually:".postln;
	"1. Run 'which distort' in terminal to find CDP location".postln;
	"2. Then run: CDPBase.setCDPPath(\"/usr/local/bin\")".postln;
	"   (use your actual CDP path)".postln;
});
)

// OR: Set CDP path manually if it's not being found automatically
// (Uncomment and adjust the path if needed)
// (
// CDPBase.setCDPPath("/usr/local/bin");  // Adjust to your CDP location
// CDPBase.checkCDPInstalled;  // Verify it works
// )

(
// Configuration
~batchDirectory = "/path/to/your/audio/files/"; // Change this!
~channels = 1; // 1 for mono, 2 for stereo

s.boot; // Boot server if needed
)


////////////////////// RANGES METHOD - EASIEST WAY! //////////////////////

// Specify parameter ranges - system automatically generates random values
// Each process is: [command, paramRanges, suffix]

(
"Running ranges batch process...".postln;
CDPBatch.ranges(~batchDirectory, ~channels, [
	// Single parameter: [min, max]
	["distort average", [12, 80], "_average"],
	
	// Single parameter with decimal precision: [min, max, decimals]
	["distort repeat", [2, 6, 0], "_repeat"],  // 0 decimals = integers
	
	// Multiple parameters: [[min1, max1], [min2, max2], ...]
	["distort overload 2", [[0.001, 0.025], [0.8, 0.99], [100, 4000]], "_overload"]
]);
"Ranges batch complete! Check the batch_output folder.".postln;
)

// More examples with different parameter types:
(
CDPBatch.ranges(~batchDirectory, ~channels, [
	// Single param
	["distort reverse", [11, 80], "_reverse"],
	
	// Multiple params with different decimal precision
	["blur chorus 1", [[50, 200, 0], [0.001, 0.01, 3], [10, 50, 0]], "_chorus"],
	
	// Mix random ranges and fixed strings
	["extend zigzag 1", [[1.2, 2.5, 2], [2.5, 4.5, 2], "25.0 0.062 -s15.0"], "_zigzag"]
]);
)

// MULTIPLE PASSES - Reprocess the output!
// Each pass processes ONLY the files created in the previous pass
// Files grow exponentially: N files × M processes = N*M new files per pass
// Example: 3 files with 3 processes and 2 passes = 3 → 9 → 27 variations
(
CDPBatch.ranges(~batchDirectory, ~channels, [
	["distort average", [12, 80], "_average"],
	["distort reverse", [11, 80], "_reverse"],
	["distort repeat", [2, 6, 0], "_repeat"]
], passes: 2);  // <- Add passes parameter
)

// Single process example - simpler growth
// 3 files with 1 process and 3 passes = 3 → 3 → 3 (applying same effect repeatedly)
(
CDPBatch.ranges(~batchDirectory, ~channels, [
	["distort average", [12, 80], "_average"]
], passes: 3);
)


////////////////////// CUSTOM METHOD - MORE CONTROL //////////////////////

// If you need custom logic, use custom parameter functions
// Each process is: [command, paramsFunc, suffix]

(
"Running custom batch process...".postln;
CDPBatch.custom(~batchDirectory, ~channels, [
	// Each line is: [CDP command, function to generate params, output suffix]
	["distort average", {|b| b.rrange(12, 80).asString}, "_average"],
	["distort repeat", {|b| b.rrange(2, 6).asString + " -c40"}, "_repeat"],
	["distort reverse", {|b| b.rrange(11, 80).asString}, "_reverse"]
]);
"Custom batch complete! Check the batch_output folder.".postln;
)


// More custom examples:

// Spectral variations
(
CDPBatch.custom(~batchDirectory, ~channels, [
	["blur blur", {|b| b.rrange(1, 5).asString}, "_blur"],
	["blur chorus 1", {|b| b.rrange(50, 200).asString + " " ++ b.rrange(0.001, 0.01).asString}, "_chorus"],
	["blur scatter", {|b| b.rrange(3, 12).asString + " 4"}, "_scatter"]
]);
)

// Extension variations
(
CDPBatch.custom(~batchDirectory, ~channels, [
	["extend baktobak", {|b| b.rrange(0.01, 1.0, 2).asString + " " ++ b.rrange(2, 50).asInteger.asString}, "_baktobak"],
	["extend zigzag 1", {|b| b.rrange(1.2, 2.5, 2).asString + " " ++ b.rrange(2.5, 4.5, 2).asString + " 25.0 0.062"}, "_zigzag"]
]);
)

// Processes with many parameters - just build the string:
(
CDPBatch.custom(~batchDirectory, ~channels, [
	// Multiple random parameters
	["distort overload 2", {|b|
		b.rrange(0.001, 0.025).asString + " " ++    // param 1
		b.rrange(0.8, 0.99).asString + " " ++        // param 2
		b.rrange(100, 4000).asString                 // param 3
	}, "_overload"],
	
	// Mix of random and fixed parameters
	["extend zigzag 1", {|b|
		b.rrange(1.2, 2.5, 2).asString + " " ++      // random
		b.rrange(2.5, 4.5, 2).asString + " " ++      // random
		"25.0 0.062 -s15.0 -m0.43 -r0"               // fixed flags
	}, "_zigzag"],
	
	// Complex parameter string
	["blur chorus 1", {|b|
		var freq = b.rrange(50, 200);
		var depth = b.rrange(0.001, 0.01, 3);
		var delay = b.rrange(10, 50, 0);
		freq.asString + " " ++ depth.asString + " " ++ delay.asString
	}, "_chorus"]
]);
)


////////////////////// PRESET BATCH CLASSES //////////////////////

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


// 5. ENVEL BATCH
// Creates 8 envelope distortion variations using breakpoint files
(
"Running ENVEL batch process...".postln;
CDPBatchEnvel.process(~batchDirectory, ~channels);
"ENVEL complete! Check the batch_output folder.".postln;
)


// 6. TREMOLO BATCH
// Creates 8 tremolo variations with different breakpoint envelopes
(
"Running TREMOLO batch process...".postln;
CDPBatchTremolo.process(~batchDirectory, ~channels);
"TREMOLO complete! Check the batch_output folder.".postln;
)


// 7. GRANULATE BATCH
// Creates 5 granulation variations using brassage
(
"Running GRANULATE batch process...".postln;
CDPBatchGranulate.process(~batchDirectory, ~channels);
"GRANULATE complete! Check the batch_output folder.".postln;
)


// 8. TRANSPOSE BATCH
// Creates 7 transposition variations at musical intervals
(
"Running TRANSPOSE batch process...".postln;
CDPBatchTranspose.process(~batchDirectory, ~channels);
"TRANSPOSE complete! Check the batch_output folder.".postln;
)


// 9. FILTER BATCH
// Creates 8 comprehensive filtering variations
(
"Running FILTER batch process...".postln;
CDPBatchFilter.process(~batchDirectory, ~channels);
"FILTER complete! Check the batch_output folder.".postln;
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
