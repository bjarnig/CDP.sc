# SC.CDP

![alt text](https://bjarnig.s3.eu-central-1.amazonaws.com/images/cdp.png)

## Overview

SuperCollider frontend for the **Composers Desktop Project (CDP)** sound transformation system. This quark provides intuitive GUI interfaces for CDP's powerful command-line tools, making waveset distortion, spectral processing, and cross-synthesis techniques easily accessible within SuperCollider.

## What is CDP?

The Composers Desktop Project is a comprehensive suite of sound transformation tools developed by Trevor Wishart, Richard Dobson, and others. It contains, for example:

- **Waveset (Pseudo-wavecycle) Distortion** - Time-domain transformations based on zero-crossings
- **Spectral Processing** - Frequency-domain manipulations
- **Cross-Synthesis** - Hybrid techniques combining multiple sound sources

## Requirements

**Important:** This quark requires CDP (Composers Desktop Project) to be installed on your system.

### Installing CDP

1. **Download CDP** from: http://www.composersdesktop.com/
2. **Install** following the instructions for your OS
3. **Verify installation:**
   ```bash
   which distort  # Should show the path to CDP binaries
   ```

### Troubleshooting Installation

If you get "command not found" errors (exit code 127):

1. **Check if CDP is installed:**
   ```supercollider
   CDPBase.checkCDPInstalled;  // Run this in SuperCollider
   ```

2. **If CDP works in terminal but not in SuperCollider:**
   
   This is a common issue where your interactive shell has the PATH but SuperCollider doesn't. 
   
   **Solution: Set the CDP path manually in SuperCollider:**
   ```supercollider
   // Find where CDP is installed (run in terminal):
   // which distort
   
   // Then in SuperCollider, set the path to the directory containing CDP:
   CDPBase.setCDPPath("/usr/local/bin");  // Adjust this to your CDP location
   
   // Or if CDP is in a custom location:
   CDPBase.setCDPPath("~/cdpr8/_cdp/_cdprogs");  // ~ and $HOME are supported
   CDPBase.setCDPPath("$HOME/cdpr8/_cdp/_cdprogs");  // Both work!
   ```
   
   **Common Issue: $HOME not expanding**
   
   If you see `PATH='$HOME/...'` in error output (with literal `$HOME`), your shell config uses single quotes which prevent variable expansion. The manual path setting above will fix this!
   
   **To make this permanent**, add it to your SuperCollider startup file:
   - Open: `Platform.userAppSupportDir +/+ "startup.scd"`
   - Add: `CDPBase.setCDPPath("/usr/local/bin");`

3. **Alternative: Add CDP to system PATH:**
   - Add CDP bin directory to your shell configuration
   - For macOS/Linux, add to `~/.zshrc` or `~/.bash_profile`:
     ```bash
     export PATH="/path/to/CDP/bin:$PATH"
     ```
   - Restart SuperCollider after changing PATH

4. **Test in terminal:**
   ```bash
   distort   # Should show CDP distort help, not "command not found"
   which distort  # Shows the full path to CDP
   ```

## Features

This repository provides two modes of operation:

### Interactive GUI Mode
- Intuitive visual interfaces for single-file processing
- Real-time parameter adjustment with sliders and number boxes
- Integrated audio playback for immediate feedback
- Automatically construct terminal commands from user input
- Provide appropriate parameter ranges and scaling (linear/exponential)

### Batch Processing Mode
- **Ranges method** - Just specify parameter ranges (easiest!)
- **Custom method** - Custom parameter functions for more control
- **Preset classes** - Built-in batch processes for common workflows
- Automated processing of entire directories
- Creative parameter randomization for variations
- Multi-channel support (mono/stereo/multi-channel)
- **Multi-pass processing** - Chain transformations across multiple passes (now working correctly!)
- Process chaining for complex transformations
- Inspired by the CDP.js framework

## Included GUI Collections

### CDP.Cycles.scd - Waveset Distortion (26+ functions)
Time-domain transformations using pseudo-wavecycles:
- **Distort**: delete, repeat, omit, divide, multiply, fractal, telescope, envel, replace, average, filter, interpolate, overload, pitch, reform, reverse
- **Other**: clip, quirk, distrep, distshift, distwarp, distmore

### CDP.Spectral.scd - Spectral Processing
Frequency-domain transformations (FFT-based)

### CDP.Cross.scd - Cross-Synthesis
Techniques combining multiple sound sources

## Usage

### Interactive GUI Usage

```supercollider
// 1. Set your working directory
~directory = "/path/to/your/audio/files/";

// 2. Boot the server
s.boot;

// 3. Evaluate any GUI block to open the interface
// Example: Wavecycle distortion
(
CDPGui(
    "distort repeat", "Repeat", ~directory,
    [
        (name:"repeat", prepend:"", spec: ControlSpec(4, 32, \linear, 1)),
        (name:"group", prepend:"-c", spec: ControlSpec(10, 250, \linear, 1))
    ]
);
)
```

### Batch Processing Usage

```supercollider
// Process all .wav files in a directory with random parameters
~dir = "/path/to/audio/files/";

// Easiest method - just specify parameter ranges:
CDPBatch.ranges(~dir, 1, [
    ["distort average", [12, 80], "_average"],
    ["distort overload 2", [[0.001, 0.025], [0.8, 0.99], [100, 4000]], "_overload"],
    ["blur scatter", [[3, 12], "4"], "_scatter"]
]);

// Multiple passes - reprocess outputs recursively:
CDPBatch.ranges(~dir, 1, [
    ["distort average", [12, 80], "_average"]
], passes: 3);  // Processes 3 times: original -> pass1 -> pass2 -> pass3

// Or use custom parameter functions:
CDPBatch.custom(~dir, 1, [
    ["distort average", {|b| b.rrange(12, 80).asString}, "_average"],
    ["distort repeat", {|b| b.rrange(2, 6).asString + " -c40"}, "_repeat"]
]);

// Or use preset batch classes:
CDPBatchDistort.process(~dir, 1); // Creates 8 variations per file
CDPBatchCycles.process(~dir, 2);   // Cycle-based transformations
CDPBatchExtend.process(~dir, 1);   // Extension processes
CDPBatchMulti.process(~dir, 1);    // Complex multi-layered process

// Output appears in: /path/to/audio/files/batch_output/
```

See `BATCH_README.md` and `CDP.Batch.scd` for complete batch processing documentation and examples.

## Requirements

- SuperCollider 3.x
- CDP system installed and accessible from command line
- Audio files in the specified working directory

## Documentation

- **README.md** - This file (main overview)
- **BATCH_README.md** - Complete batch processing system documentation
- **CDP.Batch.scd** - Batch processing examples and templates
- **CDP.Cycles.scd** - Waveset distortion GUI examples (26+ functions)
- **CDP.Spectral.scd** - Spectral processing GUI examples (27+ functions)
- **CDP.Extend.scd** - Extension processing GUI examples (13+ functions)
- **CDP.Cross.scd** - Cross-synthesis GUI examples
- **doc/html/** - Detailed CDP function reference (HTML format)

## Architecture

```
CDPBase (Base class with shared utilities)
├── CDPGui (Interactive single-file GUI)
├── CDPFft (Interactive FFT/spectral GUI)
├── CDPCross (Interactive cross-synthesis GUI)
└── CDPBatch (Batch processing engine)
    ├── CDPBatchDistort (8 distortion variations)
    ├── CDPBatchCycles (3 cycle-based variations)
    ├── CDPBatchExtend (3 extension variations)
    └── CDPBatchMulti (Complex multi-layered process)
