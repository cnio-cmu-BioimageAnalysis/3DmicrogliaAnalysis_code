import ij.IJ
import ij.ImagePlus
import ij.WindowManager
import ij.gui.Roi
import ij.gui.ShapeRoi
import ij.measure.ResultsTable
import ij.plugin.ChannelSplitter
import ij.plugin.Duplicator
import ij.plugin.RGBStackMerge
import ij.plugin.ZProjector
import ij.plugin.frame.RoiManager
import inra.ijpb.binary.BinaryImages
import inra.ijpb.color.CommonColors
import inra.ijpb.data.image.ColorImages
import inra.ijpb.morphology.Strel
import loci.plugins.BF
import loci.plugins.in.ImporterOptions
import mcib3d.geom.Object3D
import net.imglib2.converter.ChannelARGBConverter
import org.apache.commons.compress.utils.FileNameUtils
import mcib3d.geom.Objects3DPopulation
import mcib3d.image3d.ImageInt

import java.io.File

// INPUT UI
//
//#@File(label = "Input File Directory", style = "directory") inputFilesDir
//#@File(label = "Output directory", style = "directory") outputDir
//#@Integer(label = "Dapi Channel", value = 0) dapiChannel
//#@Integer(label = "Marker1 Channel", value = 1) greenChannel
//#@Integer(label = "Microglia Channel", value = 2) cyanChannel
//#@Integer(label = "MDK Channel", value = 3) redChannel
//#@Integer(label = "LUC Channel", value = 4) grayChannel
//#@Boolean(label = "Apply DAPI?") applyDAPI

def inputFilesCPDir = new File("/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_07_24_lalvaroe/output/cellpose")
def outputDir = new File("/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_07_24_lalvaroe/output/csv")
def inputFilesMicrogliaDir = new File("/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_07_24_lalvaroe/output/cd74")

// IDE
//def headless = true;
//new ImageJ().setVisible(true);

IJ.log("-Parameters selected: ")
IJ.log("    -inputFileDir: " + inputFilesCPDir)
IJ.log("    -outputDir: " + outputDir)
IJ.log("                                                           ")

/** Get files (images) from input directory */
def listOfFiles = inputFilesCPDir.listFiles()
def tableConditions = new ResultsTable()
def tableConditions_1 = new ResultsTable()
def counter = 0.intValue()
def marker2Mean = 0.doubleValue()
def marker2Std = 0.doubleValue()
def marker1Mean = 0.doubleValue()
def marker1Std = 0.doubleValue()

/** Begin of calculating distribution */
for (def i = 0; i < listOfFiles.length; i++) {
    IJ.log("-Analyzing image: " + listOfFiles[i].getName())
    
    /** Get raw image */
    def imp = new ImagePlus(inputFilesCPDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())
    
    /** Get nuclei image */
    def labelMarker1 = new ImagePlus(inputFilesMicrogliaDir.getAbsolutePath() + File.separator + listOfFiles[i].getName().replaceAll(".tif","_cp_masks.tif"))

    def channels = ChannelSplitter.split(imp)
    
    /** Get channels image */
    def chMarker1 = channels[2]
    def chMarker2 = channels[1]

    // Get marker1 signal
    def signalMarker1 = ImageInt.wrap(extractCurrentStack(chMarker1))
    // Get marker2 signal
    def signalMarker2 = ImageInt.wrap(extractCurrentStack(chMarker2))

    def marker1MeanPerNuc = new ArrayList<Double>()
    def marker2MeanPerNuc = new ArrayList<Double>()

    // Get marker1 objects population
    def imgMarker1 = ImageInt.wrap(extractCurrentStack(labelMarker1))
    def populationMarker1 = new Objects3DPopulation(imgMarker1)

    for (int j = 0.intValue(); j < populationMarker1.getNbObjects(); j++)
        marker2MeanPerNuc.add(populationMarker1.getObject(j).getPixMeanValue(signalMarker2))

    for (int j = 0.intValue(); j < populationMarker1.getNbObjects(); j++)
        marker1MeanPerNuc.add(populationMarker1.getObject(j).getPixMeanValue(signalMarker1))

    marker2Mean = marker2MeanPerNuc.stream()
            .mapToDouble(d -> d)
            .average()
            .orElse(0.0)
    marker1Mean = marker1MeanPerNuc.stream()
            .mapToDouble(d -> d)
            .average()
            .orElse(0.0)
    marker2Std = std(marker2MeanPerNuc, marker2Mean)
    marker1Std = std(marker1MeanPerNuc, marker1Mean)
    
    def merge = RGBStackMerge.mergeChannels(new ImagePlus[]{labelMarker1, chMarker1, chMarker2}, false)
    IJ.saveAs(merge, "Tiff", outputDir.getAbsolutePath().replace("csv","merge") + File.separator + listOfFiles[i].getName())

    def positiveMarker1 = new ArrayList<Object3D>()
    for (def j = 0.intValue(); j < populationMarker1.getNbObjects(); j++) {
        if (populationMarker1.getObject(j).getPixMeanValue(signalMarker1) > (marker1Mean))
            positiveMarker1.add(populationMarker1.getObject(j))
    }

    def positiveMarker2 = new ArrayList<Object3D>()
    for (def j = 0.intValue(); j < positiveMarker1.size(); j++) {
        if (positiveMarker1.get(j).getPixMeanValue(signalMarker2) > (marker2Mean))
            positiveMarker2.add(populationMarker1.getObject(j))
    }

    tableConditions.incrementCounter()
    tableConditions.setValue("Image Serie Title", i, imp.getTitle())
    tableConditions.setValue("N of Marker1 Cells", i, positiveMarker1.size())
    tableConditions.setValue("N of Marker1+Marker2+ Cells", i, positiveMarker2.size())
    tableConditions.setValue("% Marker1+Marker2+ Cells (Marker1+ Population)", i, (100 * (positiveMarker2.size().doubleValue())) / positiveMarker1.size().doubleValue())
}

tableConditions.saveAs(outputDir.getAbsolutePath() + File.separator + "doublePositiveQuantif_results_table_3D_perImage" + ".csv")
IJ.log("Done!!!")

/** Calculate standard deviation */
static double std(ArrayList<Double> table, double mean) {
    double meanDef = mean
    double temp = 0

    for (int i = 0; i < table.size(); i++) {
        double val = table.get(i)
        double squrDiffToMean = Math.pow(val - meanDef, 2)
        temp += squrDiffToMean
    }

    double meanOfDiffs = temp / table.size()
    return Math.sqrt(meanOfDiffs)
}

/** Extract current stack from ImagePlus */
ImagePlus extractCurrentStack(ImagePlus plus) {
    int[] dims = plus.getDimensions() // XY, C, Z, T
    int channel = plus.getChannel()
    int frame = plus.getFrame()
    ImagePlus stack

    if ((dims[2] > 1) || (dims[4] > 1)) {
        IJ.log("Hyperstack found, extracting current channel " + channel + " and frame " + frame)
        def duplicator = new Duplicator()
        stack = duplicator.run(plus, channel, channel, 1, dims[3], frame, frame)
    } else {
        stack = plus.duplicate()
    }

    return stack
}

