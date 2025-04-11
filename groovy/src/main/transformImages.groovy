import ij.IJ
import ij.ImagePlus
import ij.gui.ShapeRoi
import ij.measure.ResultsTable
import ij.plugin.ChannelSplitter
import ij.plugin.RGBStackMerge
import ij.plugin.frame.RoiManager
import inra.ijpb.binary.BinaryImages
import loci.plugins.BF
import loci.plugins.in.ImporterOptions
import java.io.File;

// INPUT UI
//
#@File(label = "Input File Directory", style = "directory") inputFilesDir
#@File(label = "Output directory", style = "directory") outputDir
//#@Boolean(label = "Apply DAPI?") applyDAPI

//def inputFiles = "C://Users//acayuela//Desktop//users//lalvaroe//image//Ibudilast H2030-BrM#1_GFP_488_MDK_555_CD74_633_Iba1_680//HNU40997"
//def outputDir = "C://Users//acayuela//Desktop//users//lalvaroe//image//Ibudilast H2030-BrM#1_GFP_488_MDK_555_CD74_633_Iba1_680//HNU40997_1"
// IDE
//
//
//def headless = true;
//new ImageJ().setVisible(true);

IJ.log("-Parameters selected: ")
IJ.log("    -inputFileDir: " + inputFilesDir)
IJ.log("    -outputDir: " + outputDir)
IJ.log("                                                           ");
/** Get files (images) from input directory */
def listOfFiles = inputFilesDir.listFiles();

def subTitleList = new ArrayList<String>();
for (def j = 0.intValue(); j < listOfFiles.length; j++)
    if (listOfFiles[j].getName().contains("_ch"))
        subTitleList.add(listOfFiles[j].getName())


def imp1_0 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(0))
IJ.run(imp1_0, "8-bit", "");
def imp1_1 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(1))
IJ.run(imp1_1, "8-bit", "");
def imp1_2 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(2))
IJ.run(imp1_2, "8-bit", "");
def imp1_3 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(3))
IJ.run(imp1_3, "8-bit", "");
def imp1_4 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(4))
IJ.run(imp1_4, "8-bit", "");
def imp1 = RGBStackMerge.mergeChannels(new ImagePlus[]{imp1_0, imp1_1, imp1_2, imp1_3, imp1_4}, false)
IJ.saveAs(imp1, "Tiff", outputDir.getAbsolutePath() + File.separator + subTitleList.get(0).replaceAll("_ch00", "").replaceAll(".tif", ""))

def imp2_0 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(5))
IJ.run(imp2_0, "8-bit", "");
def imp2_1 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(6))
IJ.run(imp2_1, "8-bit", "");
def imp2_2 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(7))
IJ.run(imp2_2, "8-bit", "");
def imp2_3 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(8))
IJ.run(imp2_3, "8-bit", "");
def imp2_4 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(9))
IJ.run(imp2_4, "8-bit", "");
def imp2 = RGBStackMerge.mergeChannels(new ImagePlus[]{imp2_0, imp2_1, imp2_2, imp2_3, imp2_4}, false)
IJ.saveAs(imp2, "Tiff", outputDir.getAbsolutePath() + File.separator + subTitleList.get(5).replaceAll("_ch00", "").replaceAll(".tif", ""))

def imp3_0 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(10))
IJ.run(imp3_0, "8-bit", "");
def imp3_1 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(11))
IJ.run(imp3_1, "8-bit", "");
def imp3_2 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(12))
IJ.run(imp3_2, "8-bit", "");
def imp3_3 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(13))
IJ.run(imp3_3, "8-bit", "");
def imp3_4 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(14))
IJ.run(imp3_4, "8-bit", "");
def imp3 = RGBStackMerge.mergeChannels(new ImagePlus[]{imp3_0, imp3_1, imp3_2, imp3_3, imp3_4}, false)
IJ.saveAs(imp3, "Tiff", outputDir.getAbsolutePath() + File.separator + subTitleList.get(10).replaceAll("_ch00", "").replaceAll(".tif", ""))

//def imp4_0 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(15))
//IJ.run(imp4_0, "8-bit", "");
//def imp4_1 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(16))
//IJ.run(imp4_1, "8-bit", "");
//def imp4_2 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(17))
//IJ.run(imp4_2, "8-bit", "");
//def imp4_3 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(18))
//IJ.run(imp4_3, "8-bit", "");
//def imp4_4 = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + subTitleList.get(19))
//IJ.run(imp4_4, "8-bit", "");
//def imp4 = RGBStackMerge.mergeChannels(new ImagePlus[]{imp4_0, imp4_1, imp4_2, imp4_3, imp4_4}, false)
//IJ.saveAs(imp4, "Tiff", outputDir.getAbsolutePath() + File.separator + subTitleList.get(15).replaceAll("_ch00", "").replaceAll(".tif", ""))









