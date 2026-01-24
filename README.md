# SC.CDP

![alt text](https://bjarnig.s3.eu-central-1.amazonaws.com/images/cdp.png)

## Overview

SuperCollider frontend for the **Composers Desktop Project (CDP)** sound transformation system. This quark provides intuitive GUI interfaces for CDP's powerful command-line tools, making waveset distortion, spectral processing, and cross-synthesis techniques easily accessible within SuperCollider.

## What is CDP?

The Composers Desktop Project is a comprehensive suite of sound transformation tools developed by Trevor Wishart, Richard Dobson, and others. It contains, for example:

- **Waveset (Pseudo-wavecycle) Distortion** - Time-domain transformations based on zero-crossings
- **Spectral Processing** - Frequency-domain manipulations
- **Cross-Synthesis** - Hybrid techniques combining multiple sound sources

## Features

This repository provides two modes of operation:

### Interactive GUI Mode
- Intuitive visual interfaces for single-file processing
- Real-time parameter adjustment with sliders and number boxes
- Integrated audio playback for immediate feedback
- Automatically construct terminal commands from user input
- Provide appropriate parameter ranges and scaling (linear/exponential)

### Batch Processing Mode
- Automated processing of entire directories
- Creative parameter randomization for variations
- Multi-channel support (mono/stereo/multi-channel)
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

// Run a batch process (creates 8 variations per file)
CDPBatchDistort.process(~dir, 1); // 1 = mono, 2 = stereo

// Other batch processes:
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
```

## Credits

- **CDP System**: Trevor Wishart, Richard Dobson, and the Composers Desktop Project
- **SuperCollider GUI Interface**: Bjarni Gunnarsson
- **Batch Processing System**: Inspired by CDP.js (Bjarni Gunnarsson)
