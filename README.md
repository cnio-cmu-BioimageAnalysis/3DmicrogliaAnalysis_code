# Image Analysis Scripts

## Overview

This repository contains scripts for image analysis using Groovy and Python. The scripts are designed to process and analyze confocal microscopy images, specifically focusing on cell segmentation and marker quantification.

## Scripts

### Groovy Script

#### Description

The Groovy script processes confocal microscopy images to quantify the presence of specific markers within segmented cells. It uses various ImageJ plugins and libraries to handle image processing tasks.

#### Usage

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

#### Script Details

- **Segmentation**:
    - The script uses the `Objects3DPopulation` class to segment cells based on the provided masks.
- **Marker Quantification**:
    - It calculates the mean and standard deviation of marker intensities within segmented cells.
- **Results**:
    - The script saves the quantification results in a CSV file and merged images in the output directory.

### Python Script

#### Description

The Python script uses the Cellpose library to run cell segmentation with specified parameters. It loads images, runs the Cellpose model, and saves the segmentation results.

#### Usage

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

#### Script Details

- **Model Loading**:
    - The script loads the Cellpose model with the specified parameters.
- **Image Processing**:
    - It processes the images and runs the Cellpose model to segment cells.
- **Results**:
    - The script saves the segmentation results in the specified directory.

## Requirements

### Groovy Script

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

### Groovy Script

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

## Contact

For any questions or issues, please contact [your-email@example.com].

