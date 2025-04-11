import ij.IJ
import ij.ImagePlus
import ij.ImageStack
import ij.WindowManager
import ij.measure.Calibration
import ij.measure.ResultsTable
import ij.plugin.ChannelSplitter
import ij.plugin.Duplicator
import ij.plugin.RGBStackMerge
import ij.plugin.ZProjector
import inra.ijpb.label.LabelImages
import inra.ijpb.measure.region3d.RegionAnalyzer3D
import inra.ijpb.morphology.Strel
import loci.plugins.BF
import loci.plugins.in.ImporterOptions
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest
import java.util.stream.Stream;

// INPUT UI
//
//#@File(label = "Input LIF File Directory", style = "directory") inputFiles
//#@File(label = "Output directory", style = "directory") outputDir
//#@File(label = "Green model", style = "file") greenModel
//#@File(label = "Red model", style = "file") redModel
//#@Integer(label = "Reference Channel", value = 1) refIndex
//#@Integer(label = "Target Channel", value = 2) targetIndex
def inputFiles = new File("/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_07_24_lalvaroe/images")
def outputDir = new File("/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_07_24_lalvaroe/output")

// IDE
//
//
//def headless = true;
//new ImageJ().setVisible(true);

IJ.log("-Parameters selected: ")
IJ.log("    -inputFileDir Ref: " + inputFiles)
IJ.log("    -outputDir: " + outputDir)
//IJ.log("    -Green Model: "+greenModel)
//IJ.log("    -Red Model: "+redModel)


IJ.log("                                                           ");
/** Get files (images) from input directory */
def listOfFiles = inputFiles.listFiles(); ;

for (def i = 0; i < listOfFiles.length; i++) {
/** Importer options for .lif file */
    def options = new ImporterOptions();
    options.setId(inputFiles.getAbsolutePath() + File.separator + listOfFiles[i].getName());
    options.setSplitChannels(false);
    options.setSplitTimepoints(false);
    options.setSplitFocalPlanes(false);
    options.setAutoscale(true);
    options.setStackFormat(ImporterOptions.VIEW_HYPERSTACK);
    options.setStackOrder(ImporterOptions.ORDER_XYCZT);
    options.setColorMode(ImporterOptions.COLOR_MODE_COMPOSITE);
    options.setCrop(false);
    options.setOpenAllSeries(true);
    def imps = BF.openImagePlus(options);

    for (def j = 0; j < imps.length; j++) {

        /** Get image serie per lif */
        def imp = imps[j]
        /** Get channels separately */
        if (imp.getNChannels() == 4) {
            def channels = ChannelSplitter.split(imp)
            IJ.saveAs(imp, "Tiff", outputDir.getAbsolutePath() + File.separator + "images" + File.separator + imp.getTitle().replaceAll("/", "") + "_" + j)
            def impCP = RGBStackMerge.mergeChannels(new ImagePlus[]{channels[0], channels[2], channels[3]}, false)
            IJ.saveAs(impCP, "Tiff", outputDir.getAbsolutePath() + File.separator + "cellpose" + File.separator + imp.getTitle().replaceAll("/", "") + "_" + j)

        }
        //IJ.saveAs(imp, "Tiff", outputDir.getAbsolutePath() + File.separator + imps[j].getTitle().replaceAll("/", ""))


        //IJ.saveAs(channels[dapiChannel], "Tiff", outputDir.getAbsolutePath() + File.separator + imp.getTitle().replaceAll("/", "") +"s_"+j+  "_dapi")

//            }

        //            IJ.saveAs(blueCh, "Tiff", outputDir.getAbsolutePath() + File.separator + imps[j].getTitle().replaceAll("/", ""))
//            IJ.saveAs(refCh, "Tiff", outputDir.getAbsolutePath() + File.separator + imps[j].getTitle().replaceAll("/", "")+"_ch_Ref")
//            IJ.saveAs(targetCh, "Tiff", outputDir.getAbsolutePath() + File.separator + imps[j].getTitle().replaceAll("/", "")+"_ch_Target")


    }
}