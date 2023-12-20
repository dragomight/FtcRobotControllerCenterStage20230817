package org.firstinspires.ftc.teamcode.BillsTensorTunes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TensorSurfer {
    private final double LEFT_BOUNDARY = 25.0; //TODO: Enter real numbers
    private final double RIGHT_BOUNDARY = 75.0;

    public SpikeMark mark;

    Telemetry telemetry = null;
    ElapsedTime runtime = null;
    private long previousCheck;

    private ArrayList<Double> collectedX = new ArrayList<>();

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

    private TfodProcessor tfod;

    private VisionPortal visionPortal;

    public TensorSurfer (Telemetry telemetry, ElapsedTime runtime) {
        this.telemetry = telemetry;
        this.runtime = runtime;
    }

    public void init(HardwareMap hardwareMap) {
        initTfod(hardwareMap);
    }

    private void initTfod(HardwareMap hardwareMap) { //TODO: COMPLETE INIT

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

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

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }

    public void disable() {
        visionPortal.stopStreaming();
    }
    public void enable() {
        visionPortal.resumeStreaming();
    }

    /**
     * Operates every half second.
     */
    public void scanMarks() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getFreshRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Objects Detected", updatedRecognitions.size());

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                for (Recognition recognition : updatedRecognitions) {
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
                        collectedX.add(col); // KILOSECOND
                    }
                }
            }
        }

        updateMark();
    }

    private void updateMark() {
        if (!collectedX.isEmpty()) {
            //remove oldest recognitions
            if (collectedX.size() > 6) { // stores recognitions from the last three seconds
                collectedX.remove(0);
            } // Only samples from the last three seconds

            //set mark
            double average = averageX(collectedX);
            if (average < LEFT_BOUNDARY)
                mark = SpikeMark.LEFT;
            else if (average > RIGHT_BOUNDARY)
                mark = SpikeMark.RIGHT;
            else mark = SpikeMark.MIDDLE;
        } else
            // Defaults to MIDDLE;
            mark = SpikeMark.MIDDLE;
    }

    private double averageX(ArrayList<Double> list) {
        double denominator = 0.0;
        for (int i = 0; i < list.size(); i++) {
            denominator+=list.get(i);
        }
        return denominator/list.size();
    }
}
