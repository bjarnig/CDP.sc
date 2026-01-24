# CDP Batch Processing System

## Overview

The CDP Batch Processing system extends the CDP SuperCollider integration with automated batch processing capabilities, inspired by the JavaScript CDP.js framework. This system operates separately from the GUI system, allowing both interactive and automated workflows.

## Recent Fixes (Jan 2026)

**Multi-Pass Processing Fix**: Fixed a critical issue where only the first pass would process files when using multiple passes (`passes: 2` or more). 

**Root Causes**: 
1. The `CDPBase.cdpRunner` method used `unixCmd`, which is **asynchronous**. Pass 1 would launch all CDP commands but immediately move to Pass 2 without waiting for the files to be written, resulting in Pass 2 finding 0 files.
2. All passes wrote to the same output directory, causing subsequent passes to find and reprocess files from previous passes, leading to exponential file growth and file conflicts.

**Solutions**:
1. Created `CDPBase.cdpRunnerSync` that uses `systemCmd` (synchronous) for batch processing. Each CDP command now completes before moving to the next.
2. **File Tracking**: Each pass now tracks which files it creates and only processes those files in the next pass, preventing accidental reprocessing of earlier outputs.

**Additional Improvements**:
1. **Path Normalization**: Directory paths with trailing slashes are now handled consistently
2. **Improved File Filtering**: Changed from `.contains("_c")` to regex `.findRegexp("_c[0-9]+$")` to specifically target channel files (avoiding false positives like "vocal_clip.wav")
3. **Consistent Path Joining**: All path concatenations use `+/+` operator for proper cross-platform handling

**Expected Behavior with Multiple Processes**:
When using multiple processes with multiple passes, files grow exponentially (by design):
- 3 files + 2 processes + 2 passes = 3 → 6 → 12 files
- 3 files + 3 processes + 3 passes = 3 → 9 → 27 → 81 files

Multi-pass processing now works correctly, allowing you to chain multiple transformations across passes!

## Two Modes of Operation

### 1. **Interactive GUI Mode** (Existing)
- Use `CDPGui`, `CDPFft`, and `CDPCross` for visual, interactive processing
- Process single files with manual parameter adjustment
- Visual feedback and immediate playback
- Great for experimentation and precise control

### 2. **Batch Processing Mode** (New!)
- Use `CDPBatch` and `CDPBatch*` classes for automated processing
- Process entire directories of files
- Randomized parameters for creative variations
- Process chains with multiple transformations
- Great for generating libraries of variations

## Architecture

```
CDPBase (Base class)
├── CDPGui (Interactive GUI)
├── CDPFft (Interactive FFT GUI)
├── CDPCross (Interactive Cross-synthesis GUI)
└── CDPBatch (Batch processing engine)
    ├── CDPBatchDistort
    ├── CDPBatchCycles
    ├── CDPBatchExtend
    ├── CDPBatchMulti
    ├── CDPBatchEnvel
    ├── CDPBatchTremolo
    ├── CDPBatchGranulate
    ├── CDPBatchTranspose
    └── CDPBatchFilter
```

## Quick Start

### Method 1: Ranges - Just Specify Ranges (EASIEST!)

The absolute easiest way - just specify parameter ranges:

```supercollider
// Set your audio directory
~dir = "/path/to/your/audio/files/";

// Define processes with parameter ranges: [command, paramRanges, suffix]
CDPBatch.ranges(~dir, 1, [
    // Single parameter: [min, max]
    ["distort average", [12, 80], "_average"],
    
    // Multiple parameters: [[min1, max1], [min2, max2], ...]
    ["distort overload 2", [[0.001, 0.025], [0.8, 0.99], [100, 4000]], "_overload"],
    
    // With decimal precision: [min, max, decimals]
    ["distort repeat", [2, 6, 0], "_repeat"]  // 0 decimals = integers
]);

// Output appears in: /path/to/your/audio/files/batch_output/
```

### Method 2: Custom - Custom Parameter Functions

For more control, use functions to generate parameters:

```supercollider
~dir = "/path/to/your/audio/files/";

// Define processes inline: [command, paramsFunc, suffix]
CDPBatch.custom(~dir, 1, [
    ["distort average", {|b| b.rrange(12, 80).asString}, "_average"],
    ["distort repeat", {|b| b.rrange(2, 6).asString + " -c40"}, "_repeat"],
    ["distort reverse", {|b| b.rrange(11, 80).asString}, "_reverse"]
]);
```

### Method 3: Preset Batch Classes

Use built-in batch process classes:

```supercollider
// Set your audio directory
~dir = "/path/to/your/audio/files/";

// Process all .wav files with distortion variations
CDPBatchDistort.process(~dir, 1); // 1 = mono, 2 = stereo

// Output appears in: /path/to/your/audio/files/batch_output/
```

### Available Batch Processes

1. **CDPBatchDistort** - Creates 8 distortion variations
   - average, repeat, fractal, overload, interpolate, omit, replim, replace

2. **CDPBatchCycles** - Creates 3 cycle-based variations
   - filter, reverse, delete

3. **CDPBatchExtend** - Creates 3 extension variations
   - baktobak, zigzag, iterate

4. **CDPBatchMulti** - Complex multi-layered process
   - Combines 4 main variation types with sub-variations

5. **CDPBatchEnvel** - Creates 8 envelope distortion variations
   - Uses different breakpoint files for tremolo-like envelopes

6. **CDPBatchTremolo** - Creates 8 tremolo variations
   - Different breakpoint envelope patterns

7. **CDPBatchGranulate** - Creates 5 granulation variations
   - Uses modify brassage for granular processing

8. **CDPBatchTranspose** - Creates 7 transposition variations
   - Musical intervals: octaves, fifths, sevenths (up and down)

9. **CDPBatchFilter** - Creates 8 comprehensive filter variations
   - Lowpass, highpass, bandpass, notch, and cycle filters

## Custom Batch Processes

### Option A: Ranges Method (Easiest!)

Just specify parameter ranges - no functions needed:

```supercollider
CDPBatch.ranges("~/audio/", 1, [
    // Single parameter: [min, max]
    ["distort average", [12, 80], "_average"],
    
    // Single parameter with precision: [min, max, decimals]
    ["distort repeat", [2, 6, 0], "_repeat"],  // 0 = integers
    
    // Multiple parameters: [[min1, max1], [min2, max2], ...]
    ["distort overload 2", [[0.001, 0.025], [0.8, 0.99], [100, 4000]], "_overload"],
    
    // Multiple with precision: [[min, max, dec], [min, max, dec], ...]
    ["blur chorus 1", [[50, 200, 0], [0.001, 0.01, 3], [10, 50, 0]], "_chorus"],
    
    // Mix ranges and fixed strings
    ["extend zigzag 1", [[1.2, 2.5, 2], [2.5, 4.5, 2], "25.0 0.062 -s15.0"], "_zigzag"]
]);
```

**Parameter Format:**
- `[min, max]` - Single random parameter (2 decimal places by default)
- `[min, max, decimals]` - Single random parameter with specified precision
- `[[min1, max1], [min2, max2], ...]` - Multiple random parameters
- `"fixed string"` - Fixed parameter value (no randomization)

### Option B: Custom Method (More Control)

Use functions when you need custom parameter logic:

```supercollider
// Each process: [command, paramsFunc, suffix]
CDPBatch.custom("~/audio/", 1, [
    ["distort average", {|b| b.rrange(10, 50).asString}, "_myprocess1"],
    ["distort reverse", {|b| b.rrange(20, 60).asString}, "_myprocess2"],
    ["blur scatter", {|b| b.rrange(3, 12).asString + " 4"}, "_myprocess3"]
]);
```

#### Processes with Many Parameters

For processes requiring multiple parameters, build the string in the function:

```supercollider
CDPBatch.custom("~/audio/", 1, [
    // Multiple random parameters
    ["distort overload 2", {|b|
        b.rrange(0.001, 0.025).asString + " " ++    // param 1
        b.rrange(0.8, 0.99).asString + " " ++        // param 2
        b.rrange(100, 4000).asString                 // param 3
    }, "_overload"],
    
    // Mix random and fixed parameters
    ["extend zigzag 1", {|b|
        b.rrange(1.2, 2.5, 2).asString + " " ++      // random
        b.rrange(2.5, 4.5, 2).asString + " " ++      // random
        "25.0 0.062 -s15.0 -m0.43 -r0"               // fixed flags
    }, "_zigzag"],
    
    // Use variables for clarity
    ["blur chorus 1", {|b|
        var freq = b.rrange(50, 200);
        var depth = b.rrange(0.001, 0.01, 3);
        var delay = b.rrange(10, 50, 0);
        freq.asString + " " ++ depth.asString + " " ++ delay.asString
    }, "_chorus"]
]);
```

### Option C: Advanced Custom Logic

For complex processing with custom logic, use `processAll`:

```supercollider
(
var batch = CDPBatch("~/audio/", 1);

batch.processAll {|file, b|
    var params;
    
    if(b.channels == 1, {
        // Mono: process directly
        var input = b.directory ++ file ++ ".wav";
        
        // Your custom processing logic
        params = b.rrange(10, 50).asString;
        CDPBase.cdpRunner("distort average", input,
            b.outputFolder ++ file ++ "_myprocess.wav",
            params);
    }, {
        // Multi-channel: process each channel
        b.channels.do {|i|
            var ch = i + 1;
            var input = b.directory ++ file ++ "_c" ++ ch ++ ".wav";
            
            params = b.rrange(10, 50).asString;
            CDPBase.cdpRunner("distort average", input,
                b.outputFolder ++ file ++ "_myprocess_c" ++ ch ++ ".wav",
                params);
        };
        
        // Combine channels
        b.combineChannels(file, "_myprocess");
        b.cleanChannelFiles(file);
    });
};
)
```

## Key Features

### Multiple Passes (Recursive Processing)

Both `.ranges()` and `.custom()` support a `passes` parameter that reprocesses outputs:

```supercollider
// Single pass (default) - process original files
CDPBatch.ranges(~dir, 1, [
    ["distort average", [12, 80], "_average"],
    ["distort reverse", [11, 80], "_reverse"]
]);
// Result: 3 files -> 6 files

// Multiple passes - reprocess the outputs!
CDPBatch.ranges(~dir, 1, [
    ["distort average", [12, 80], "_average"],
    ["distort reverse", [11, 80], "_reverse"]
], passes: 2);
// Result: 3 files -> 6 files (pass 1) -> 12 files (pass 2)

// Even more passes for exponential variations
CDPBatch.ranges(~dir, 1, [
    ["distort average", [12, 80], "_average"]
], passes: 3);
// Result: 3 files -> 3 files (pass 1) -> 3 files (pass 2) -> 3 files (pass 3)
```

**How it works:**
- Pass 1: Processes original files from your directory
- Pass 2: Takes outputs from pass 1 as inputs, processes them again
- Pass 3+: Continues chain, each pass processing the previous pass's outputs

**Warning:** Files multiply quickly! With 3 processes and 3 passes on 3 files: 3 → 9 → 27 → 81 files!

### Random Parameter Generation

The `rrange()` method generates random values for creative variation:

```supercollider
b.rrange(0.1, 1.0)      // Random float 0.1-1.0
b.rrange(10, 50)        // Random float 10-50
b.rrange(1, 10, 0)      // Random integer 1-10
```

### Multi-Channel Support

The system automatically handles:
- Splitting multi-channel files into separate channels
- Processing each channel independently
- Recombining channels into stereo/multi-channel output
- Cleaning up intermediate channel files

### Process Chaining

Two ways to chain processes:

**Option 1: Multiple Passes (Automatic)**
```supercollider
// Automatically reprocess outputs multiple times
CDPBatch.ranges("~/audio/", 1, [
    ["distort average", [12, 80], "_average"],
    ["distort reverse", [11, 80], "_reverse"]
], passes: 3);
// Each pass processes the previous pass's outputs
```

**Option 2: Manual Chaining (Different Process Types)**
```supercollider
(
var dir = "~/audio/";

// First pass: Distort
CDPBatchDistort.process(dir, 2);

// Second pass: Extend the distorted files
dir = dir ++ "batch_output/";
CDPBatchExtend.process(dir, 2);
)
```

### Combining Variations

The `combineSources()` method merges multiple processed versions:

```supercollider
// Create multiple variations
4.do {|i|
    // Process variation i
    b.combineChannels(file, "_var" ++ i);
};

// Merge all variations into one file
b.combineSources(file, "_merged", ["_var0", "_var1", "_var2", "_var3"]);
```

## CDPBatch API Reference

### Class Methods

| Method | Description |
|--------|-------------|
| `CDPBatch.new(directory, channels)` | Create batch processor instance |
| `CDPBatch.ranges(directory, channels, processes)` | **Easiest - specify parameter ranges** |
| `CDPBatch.custom(directory, channels, processes)` | Inline batch with custom parameter functions |

#### CDPBatch.ranges (EASIEST)

The simplest way to create batch processes - just specify parameter ranges.

**Syntax:**
```supercollider
CDPBatch.ranges(directory, channels, processes, passes: 1)
```

**Parameters:**
- `directory` - Path to folder containing .wav files
- `channels` - Number of channels (1 = mono, 2 = stereo)
- `processes` - Array of process specs: `[command, paramRanges, suffix]`
- `passes` - Number of processing passes (default: 1). Each pass reprocesses the previous pass's outputs
  - `command` (String) - CDP command (e.g., "distort average")
  - `paramRanges` - Parameter specification:
    - `[min, max]` - Single random parameter (2 decimals default)
    - `[min, max, decimals]` - Single random parameter with precision
    - `[[min1, max1], [min2, max2], ...]` - Multiple random parameters
    - `"fixed string"` - Fixed parameter (no randomization)
  - `suffix` (String) - Output filename suffix (e.g., "_average")

**Examples:**
```supercollider
// Single parameter
CDPBatch.ranges("~/audio/", 1, [
    ["distort average", [12, 80], "_avg"]
]);

// Multiple parameters
CDPBatch.ranges("~/audio/", 2, [
    ["distort overload 2", [[0.001, 0.025], [0.8, 0.99], [100, 4000]], "_overload"]
]);

// With precision control
CDPBatch.ranges("~/audio/", 1, [
    ["distort repeat", [2, 6, 0], "_repeat"],  // 0 decimals = integers
    ["blur chorus 1", [[50, 200, 0], [0.001, 0.01, 3]], "_chorus"]
]);

// Mix random and fixed
CDPBatch.ranges("~/audio/", 1, [
    ["extend zigzag 1", [[1.2, 2.5, 2], "25.0 0.062 -s15.0"], "_zigzag"]
]);
```

#### CDPBatch.custom

Create batch processes with custom parameter functions.

**Syntax:**
```supercollider
CDPBatch.custom(directory, channels, processes, passes: 1)
```

**Parameters:**
- `directory` - Path to folder containing .wav files
- `channels` - Number of channels (1 = mono, 2 = stereo)
- `processes` - Array of process specs: `[command, paramsFunc, suffix]`
- `passes` - Number of processing passes (default: 1). Each pass reprocesses the previous pass's outputs
  - `command` (String) - CDP command (e.g., "distort average")
  - `paramsFunc` (Function) - Function that returns parameter string
    - Receives batch object `b` as argument
    - Can use `b.rrange(lo, hi, decimals)` for random values
    - Should return a String with all parameters space-separated
  - `suffix` (String) - Output filename suffix (e.g., "_average")

**Examples:**
```supercollider
// Simple - single parameter
CDPBatch.custom("~/audio/", 1, [
    ["distort average", {|b| b.rrange(12, 80).asString}, "_avg"]
]);

// Multiple processes
CDPBatch.custom("~/audio/", 2, [
    ["distort average", {|b| b.rrange(12, 80).asString}, "_average"],
    ["distort repeat", {|b| b.rrange(2, 6).asString + " -c40"}, "_repeat"],
    ["blur scatter", {|b| b.rrange(3, 12).asString + " 4"}, "_scatter"]
]);

// Process with many parameters
CDPBatch.custom("~/audio/", 1, [
    ["distort overload 2", {|b|
        b.rrange(0.001, 0.025).asString + " " ++
        b.rrange(0.8, 0.99).asString + " " ++
        b.rrange(100, 4000).asString
    }, "_overload"]
]);
```

### Instance Methods

| Method | Description |
|--------|-------------|
| `readFiles()` | Scans directory for .wav files |
| `rrange(lo, hi, decimals)` | Generate random value |
| `splitChannels(file)` | Split multi-channel file |
| `combineChannels(file, suffix)` | Recombine channels |
| `combineSources(file, suffix, sources)` | Merge multiple files |
| `processAll(func)` | Execute function for all files |
| `cleanChannelFiles(file)` | Remove temporary files |

## Creating New Batch Process Classes

Template for new batch processes:

```supercollider
CDPBatchMyProcess {
    
    *process {|directory, channels=1|
        var batch = CDPBatch(directory, channels);
        
        batch.processAll {|file, b|
            var suffixes = ["_variation1", "_variation2"];
            
            channels.do {|i|
                var ch = i + 1;
                var input = directory ++ file ++ "_c" ++ ch ++ ".wav";
                
                // Variation 1
                CDPBase.cdpRunner("distort average", input,
                    b.outputFolder ++ file ++ "_variation1_c" ++ ch ++ ".wav",
                    b.rrange(10, 50).asString);
                
                // Variation 2
                CDPBase.cdpRunner("distort repeat", input,
                    b.outputFolder ++ file ++ "_variation2_c" ++ ch ++ ".wav",
                    b.rrange(2, 6).asString);
            };
            
            // Combine channels for each variation
            suffixes.do {|suf|
                b.combineChannels(file, suf);
            };
            
            // Clean up
            if(channels > 1, { b.cleanChannelFiles(file) });
        };
    }
}
```

## Comparison with CDP.js

| Feature | CDP.js | SuperCollider |
|---------|--------|---------------|
| Base Class | `Transform` | `CDPBatch` |
| Command Execution | `execSync()` | `CDPBase.cdpRunner()` |
| Random Generation | `rrange()` | `rrange()` |
| Channel Splitting | `housekeep chans` | `splitChannels()` |
| Channel Combining | `submix interleave` | `combineChannels()` |
| File Merging | `submix mergemany` | `combineSources()` |

## Performance Tips

1. **Batch Size**: Process large batches can take time. Start with a few files to test.

2. **Multi-Channel**: Processing stereo files takes ~2x longer than mono.

3. **Multiple Passes**: Files grow exponentially! Formula: `original_files × processes^passes`
   - Example: 3 files, 3 processes, 2 passes = 3 × 3² = 27 files
   - Example: 5 files, 4 processes, 3 passes = 5 × 4³ = 320 files!

4. **Disk Space**: Variations multiply quickly! Monitor your disk space with multiple passes.

5. **Memory**: The system runs commands sequentially to avoid memory issues.

## Examples

See `CDP.Batch.scd` for complete examples including:
- Simple batch processing
- Custom processes
- Process chains
- Multi-variation combinations

## Workflow Recommendations

### Exploration Phase
Use **GUI Mode** to:
- Experiment with parameters
- Find interesting parameter ranges
- Test individual processes
- Listen and evaluate immediately

### Production Phase
Use **Batch Mode** to:
- Apply discovered parameters to libraries
- Generate variations for selection
- Create process chains
- Build sound design palettes