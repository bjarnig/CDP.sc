# CDP Batch Processing System

## Overview

The CDP Batch Processing system extends the CDP SuperCollider integration with automated batch processing capabilities, inspired by the JavaScript CDP.js framework. This system operates separately from the GUI system, allowing both interactive and automated workflows.

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
    └── CDPBatchMulti
```

## Quick Start

### Basic Batch Processing

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

## Custom Batch Processes

Create your own batch processes inline:

```supercollider
(
var batch = CDPBatch("~/audio/", 1);

batch.processAll {|file, b|
    var params;
    
    b.channels.do {|i|
        var ch = i + 1;
        var input = b.directory ++ file ++ "_c" ++ ch ++ ".wav";
        
        // Your custom processing
        params = b.rrange(10, 50).asString; // Random 10-50
        CDPBase.cdpRunner("distort average", input,
            b.outputFolder ++ file ++ "_myprocess_c" ++ ch ++ ".wav",
            params);
    };
    
    // Combine channels
    b.combineChannels(file, "_myprocess");
};
)
```

## Key Features

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

Chain multiple batch processes together:

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

### Class Methods

```supercollider
CDPBatch.new(directory, channels)
```

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

3. **Process Chains**: Each pass through the batch adds to total processing time.

4. **Disk Space**: Variations multiply quickly! A 5-file batch with 8 variations = 40 output files.

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

## Credits

Inspired by the CDP.js batch processing framework by Bjarni Gunnarsson.
