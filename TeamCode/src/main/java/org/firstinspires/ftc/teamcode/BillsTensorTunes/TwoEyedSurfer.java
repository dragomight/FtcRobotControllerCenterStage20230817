package org.firstinspires.ftc.teamcode.BillsTensorTunes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TwoEyedSurfer {
    private final double LEFT_BOUNDARY = 25.0; //TODO: Enter real numbers
    private final double RIGHT_BOUNDARY = 75.0;

    public SpikeMark mark;

    Telemetry telemetry = null;
    ElapsedTime runtime = null;
    private long previousCheck;

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "MyModelStoredAsAsset.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/myCustomModel.tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = { //TODO: Fix labels
            "Pixel",
    };

    private TfodProcessor tfodLeft;
    private TfodProcessor tfodRight;

    private VisionPortal visionPortalLeft;
    private VisionPortal visionPortalRight;

    public TwoEyedSurfer (Telemetry telemetry, ElapsedTime runtime) {
        this.telemetry = telemetry;
        this.runtime = runtime;
    }

    private void initTfod(HardwareMap hardwareMap) { //TODO: COMPLETE INIT

        // Create the TensorFlow processor by using a builder.
        tfodLeft = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                //.setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                //.setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        tfodRight = new TfodProcessor.Builder().build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builderLeft = new VisionPortal.Builder();
        VisionPortal.Builder builderRight = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builderLeft.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
            builderRight.setCamera(hardwareMap.get(WebcamName.class, "Webcam 2"));
        } else {
            builderLeft.setCamera(BuiltinCameraDirection.BACK);
            builderRight.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builderLeft.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builderRight.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builderLeft.addProcessor(tfodLeft);
        builderRight.addProcessor(tfodRight);

        // Build the Vision Portal, using the above settings.
        visionPortalLeft = builderLeft.build();
        visionPortalRight = builderRight.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfodLeft.setMinResultConfidence(0.75f);
        tfodRight.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }

    public void disable() {
        visionPortalLeft.stopStreaming();
        visionPortalRight.stopStreaming();
    }
    public void enable() {
        visionPortalLeft.resumeStreaming();
        visionPortalRight.resumeStreaming();
    }

    /**
     * Operates every half second.
     */
    public void scanMarks() {
        List<Recognition> currentRecognitionsLeft = tfodLeft.getRecognitions();
        List<Recognition> currentRecognitionsRight = tfodRight.getRecognitions();

        telemetry.addData("# Objects Detected", currentRecognitionsLeft.size());
        telemetry.addData("# Objects Detected", currentRecognitionsRight.size());

        if (tfodLeft != null && tfodRight != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitionsLeft = tfodLeft.getFreshRecognitions();
            List<Recognition> updatedRecognitionsRight = tfodRight.getFreshRecognitions();
            if (updatedRecognitionsLeft != null && updatedRecognitionsRight != null) {
                telemetry.addData("# Objects Detected", updatedRecognitionsLeft.size());

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                for (Recognition recognition : updatedRecognitionsLeft) {
                    double col = (recognition.getLeft() + recognition.getRight()) / 2;
                    double row = (recognition.getTop() + recognition.getBottom()) / 2;
                    double width = Math.abs(recognition.getRight() - recognition.getLeft());
                    double height = Math.abs(recognition.getTop() - recognition.getBottom());

                    telemetry.addData("", " ");
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                    telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", row, col);
                    telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
                    telemetry.addData("Mark: ", mark);

                    if (runtime.time(TimeUnit.MILLISECONDS) > previousCheck+500) { // Every half second
                        previousCheck = runtime.time(TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
    }
}
