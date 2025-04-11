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

def inputFilesRawDir = new File("")
def outputDir = new File("")
def inputFilesMicrogliaDir = new File("")
def listOfFiles = inputFilesRawDir.listFiles()
def tableConditions = new ResultsTable()
def tableConditions_1 = new ResultsTable()
def counter = 0.intValue()
def marker3Mean = 0.doubleValue()
def marker3Std = 0.doubleValue()
def marker2Mean = 0.doubleValue()
def marker2Std = 0.doubleValue()
def marker1Mean = 0.doubleValue()
def marker1Std = 0.doubleValue()
def positiveMarker2Marker1 = null
def positiveMarker2Marker3 = null
def positiveMarker2 = null
def imp = null

/** Begin of calculating distribution */
for (def i = 0; i < listOfFiles.length; i++) {
    if (listOfFiles[i].getName().contains("Marker1")) {
        def sub = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf("_"), listOfFiles[i].getName().lastIndexOf(".")).replaceAll("_", "")
        IJ.log("-Analyzing image: " + listOfFiles[i].getName())

        /** Get raw image */
        imp = new ImagePlus(inputFilesRawDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())
        def labelSeg = new ImagePlus(inputFilesMicrogliaDir.getAbsolutePath() + File.separator + listOfFiles[i].getName().replaceAll(".tif", sub + ".tif").replaceAll(".tif", "_cp_masks.tif"))

        def channels = ChannelSplitter.split(imp)

        /** Get channels image */
        def chSeg = channels[1]
        def chMarker1 = channels[3]
        def chMarker2 = channels[4]

        // Get Seg signal
        def signalSeg = ImageInt.wrap(extractCurrentStack(chSeg))
        // Get marker1 signal
        def signalMarker1 = ImageInt.wrap(extractCurrentStack(chMarker1))
        // Get marker2 signal
        def signalMarker2 = ImageInt.wrap(extractCurrentStack(chMarker2))

        def segMeanPerNuc = new ArrayList<Double>()
        def marker1MeanPerNuc = new ArrayList<Double>()
        def marker2MeanPerNuc = new ArrayList<Double>()

        // Get Seg objects population
        def imgSeg = ImageInt.wrap(extractCurrentStack(labelSeg))
        def populationSeg = new Objects3DPopulation(imgSeg)

        for (int j = 0.intValue(); j < populationSeg.getNbObjects(); j++)
            marker1MeanPerNuc.add(populationSeg.getObject(j).getPixMeanValue(signalMarker1))

        for (int j = 0.intValue(); j < populationSeg.getNbObjects(); j++)
            marker2MeanPerNuc.add(populationSeg.getObject(j).getPixMeanValue(signalMarker2))

        marker1Mean = marker1MeanPerNuc.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0)
        marker2Mean = marker2MeanPerNuc.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0)
        marker1Std = std(marker1MeanPerNuc, marker1Mean)
        marker2Std = std(marker2MeanPerNuc, marker2Mean)

        def merge = RGBStackMerge.mergeChannels(new ImagePlus[]{labelSeg, chMarker2, chMarker1}, false)
        IJ.saveAs(merge, "Tiff", outputDir.getAbsolutePath().replace("csv", "merge") + File.separator + listOfFiles[i].getName())

        positiveMarker2 = new ArrayList<Object3D>()
        for (def j = 0.intValue(); j < populationSeg.getNbObjects(); j++) {
            if (populationSeg.getObject(j).getPixMeanValue(signalMarker2) > (marker2Mean))
                positiveMarker2.add(populationSeg.getObject(j))
        }

        positiveMarker2Marker1 = new ArrayList<Object3D>()
        for (def j = 0.intValue(); j < positiveMarker2.size(); j++) {
            if (positiveMarker2.get(j).getPixMeanValue(signalMarker1) > (marker1Mean))
                positiveMarker2Marker1.add(positiveMarker2.get(j))
        }
    }
    if (listOfFiles[i].getName().contains("Marker3")) {
        def sub = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf("_"), listOfFiles[i].getName().lastIndexOf(".")).replaceAll("_", "")
        IJ.log("-Analyzing image: " + listOfFiles[i].getName())

        /** Get raw image */
        imp = new ImagePlus(inputFilesRawDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())

        /** Get nuclei image */
        def labelSeg = new ImagePlus(inputFilesMicrogliaDir.getAbsolutePath() + File.separator + listOfFiles[i].getName().replaceAll(".tif", sub + ".tif").replaceAll(".tif", "_cp_masks.tif"))

        def channels = ChannelSplitter.split(imp)

        /** Get channels image */
        def chSeg = channels[1]
        def chMarker3 = channels[3]
        def chMarker2 = channels[4]

        // Get Seg signal
        def signalSeg = ImageInt.wrap(extractCurrentStack(chSeg))
        // Get marker3 signal
        def signalMarker3 = ImageInt.wrap(extractCurrentStack(chMarker3))
        def signalMarker2 = ImageInt.wrap(extractCurrentStack(chMarker2))

        def segMeanPerNuc = new ArrayList<Double>()
        def marker3MeanPerNuc = new ArrayList<Double>()
        def marker2MeanPerNuc = new ArrayList<Double>()

        // Get Seg objects population
        def imgSeg = ImageInt.wrap(extractCurrentStack(labelSeg))
        def populationSeg = new Objects3DPopulation(imgSeg)

        for (int j = 0.intValue(); j < populationSeg.getNbObjects(); j++)
            marker3MeanPerNuc.add(populationSeg.getObject(j).getPixMeanValue(signalMarker3))

        for (int j = 0.intValue(); j < populationSeg.getNbObjects(); j++)
            marker2MeanPerNuc.add(populationSeg.getObject(j).getPixMeanValue(signalMarker2))

        marker3Mean = marker3MeanPerNuc.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0)
        marker2Mean = marker2MeanPerNuc.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0)
        marker3Std = std(marker3MeanPerNuc, marker3Mean)
        marker2Std = std(marker2MeanPerNuc, marker2Mean)

        def merge = RGBStackMerge.mergeChannels(new ImagePlus[]{labelSeg, chMarker2, chMarker3}, false)
        IJ.saveAs(merge, "Tiff", outputDir.getAbsolutePath().replace("csv", "merge") + File.separator + listOfFiles[i].getName())

        positiveMarker2 = new ArrayList<Object3D>()
        for (def j = 0.intValue(); j < populationSeg.getNbObjects(); j++) {
            if (populationSeg.getObject(j).getPixMeanValue(signalMarker2) > (marker2Mean))
                positiveMarker2.add(populationSeg.getObject(j))
        }

        positiveMarker2Marker3 = new ArrayList<Object3D>()
        for (def j = 0.intValue(); j < positiveMarker2
