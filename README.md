# 3DmicrogliaAnalysis_code

## Overview

This repository contains scripts for image analysis using Groovy and Python. The scripts are designed to process and analyze confocal microscopy images, specifically focusing on cell segmentation and marker quantification.

## Scripts

### Groovy Scripts

#### 1. lifToTiffSeg.groovy

**Description**: This script converts `.lif` files to `.tiff` format and prepares images for segmentation.

**Usage**:
1. **Input Parameters**:
    - `inputFiles`: Directory containing the `.lif` files.
    - `outputDir`: Directory to save the converted `.tiff` files.
    - `greenModel`: Path to the green model file.
    - `redModel`: Path to the red model file.
    - `refIndex`: Reference channel index.
    - `targetIndex`: Target channel index.

2. **Execution**:
    - Run the script in ImageJ/Fiji with the specified input parameters.

3. **Output**:
    - The script generates `.tiff` files in the specified output directory.

#### 2. 3DdoubleQuantification_perImage.groovy

**Description**: This script quantifies the presence of two markers within segmented cells in 3D images.

**Usage**:
1. **Input Parameters**:
    - `inputFilesDir`: Directory containing the raw image files.
    - `outputDir`: Directory to save the results.
    - `dapiChannel`: Channel number for DAPI.
    - `marker1Channel`: Channel number for Marker1.
    - `microgliaChannel`: Channel number for Microglia.
    - `mdkChannel`: Channel number for MDK.
    - `lucChannel`: Channel number for LUC.
    - `applyDAPI`: Boolean to apply DAPI.

2. **Execution**:
    - Run the script in ImageJ/Fiji with the specified input parameters.

3. **Output**:
    - The script generates a CSV file with the quantification results and saves merged images in the specified output directory.

#### 3. 3DtripleQuantification_perImage.groovy

**Description**: This script quantifies the presence of three markers within segmented cells in 3D images.

**Usage**:
1. **Input Parameters**:
    - `inputFilesDir`: Directory containing the raw image files.
    - `outputDir`: Directory to save the results.
    - `dapiChannel`: Channel number for DAPI.
    - `marker1Channel`: Channel number for Marker1.
    - `microgliaChannel`: Channel number for Microglia.
    - `mdkChannel`: Channel number for MDK.
    - `lucChannel`: Channel number for LUC.
    - `applyDAPI`: Boolean to apply DAPI.

2. **Execution**:
    - Run the script in ImageJ/Fiji with the specified input parameters.

3. **Output**:
    - The script generates a CSV file with the quantification results and saves merged images in the specified output directory.

### Python Script

#### Description

The Python script uses the Cellpose library to run cell segmentation with specified parameters. It loads images, runs the Cellpose model, and saves the segmentation results.

**Usage**:
1. **Input Parameters**:
    - `directory`: Directory containing the raw image files.
    - `pretrained_model`: Pretrained model to use (e.g., `cyto3`).
    - `stitch_threshold`: Threshold for stitching.
    - `flow_threshold`: Threshold for flow.
    - `cellprob_threshold`: Threshold for cell probability.
    - `chan`: Channel number to use.
    - `save_tif`: Boolean to save results as TIFF.
    - `use_gpu`: Boolean to use GPU.
    - `diameter`: Diameter of cells.

2. **Execution**:
    - Run the script with the specified input parameters.

3. **Output**:
    - The script saves the segmentation results in the specified directory.

## Requirements

### Groovy Scripts

- ImageJ/Fiji
- Required plugins:
    - ChannelSplitter
    - Duplicator
    - RGBStackMerge
    - ZProjector
    - RoiManager
    - BinaryImages
    - CommonColors
    - ColorImages
    - Strel
    - BF
    - ImporterOptions
    - Objects3DPopulation
    - ImageInt

### Python Script

- Python 3.x
- Cellpose library
- Required libraries:
    - models
    - io

## Installation

### Groovy Scripts

1. Install ImageJ/Fiji.
2. Install the required plugins.

### Python Script

1. Install Python 3.x.
2. Install the Cellpose library using pip:
    ```bash
    pip install cellpose
    ```

## License

This repository is licensed under the MIT License. See the `LICENSE` file for more information.




