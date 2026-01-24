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

This repository provides GUI wrappers that:

- Automatically construct terminal commands from user input
- Provide appropriate parameter ranges and scaling (linear/exponential)
- Handle file I/O with configurable working directories
- Execute CDP processes directly from SuperCollider

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

## Requirements

- SuperCollider 3.x
- CDP system installed and accessible from command line
- Audio files in the specified working directory

## Documentation

Detailed documentation for CDP functions can be found in `doc/html/cdistort.htm` and related HTML files.

## Credits

- **CDP System**: Trevor Wishart, Richard Dobson, and the Composers Desktop Project
- **SuperCollider Interface**: GUI wrappers and parameter specifications
