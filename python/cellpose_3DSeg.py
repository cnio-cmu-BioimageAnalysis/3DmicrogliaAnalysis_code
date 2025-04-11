from cellpose import models, io

# Define the directory and parameters
directory = "/run/user/752424298/gvfs/smb-share:server=imgserver,share=images/CONFOCAL/IA/Projects/2024/2024_10_17_lalvaroe_0/output/cellpose"
pretrained_model = "cyto3"
stitch_threshold = 0.25
flow_threshold = 0.8
cellprob_threshold = -1.0
chan = 2
save_tif = True
use_gpu = True
diameter = 65

# Load the model
model = models.Cellpose(gpu=use_gpu, model_type=pretrained_model)

# Run Cellpose
images = io.load_images(directory)
masks, flows, styles, diams = model.eval(
    images,
    diameter=diameter,
    channels=[0, chan],
    flow_threshold=flow_threshold,
    cellprob_threshold=cellprob_threshold,
    stitch_threshold=stitch_threshold,
    do_3D=False,
    save_tif=save_tif,
    verbose=True
)

# Save the results
io.save_masks(directory, masks, flows, diams, save_tif=save_tif)
