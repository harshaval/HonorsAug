package co.harshaval.skeletontest;

import android.graphics.PointF;
import android.os.Bundle;
import ai.fritz.core.Fritz;
import android.graphics.Canvas;
import android.media.Image;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.List;

import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionModels;
import ai.fritz.vision.FritzVisionOrientation;
import ai.fritz.vision.ImageRotation;
import ai.fritz.vision.ModelVariant;
import ai.fritz.vision.poseestimation.FritzVisionPosePredictor;
import ai.fritz.vision.poseestimation.FritzVisionPoseResult;
import ai.fritz.vision.poseestimation.Keypoint;
import ai.fritz.vision.poseestimation.Pose;
import ai.fritz.vision.poseestimation.PoseOnDeviceModel;

/** The main activity uses Fritz AI which can be considered one of the API and is used in a lot of Snapchat applications
 * as well to provide some models, keypoints and
 * image and light estimation*/
public class MainActivity extends LiveCameraActivity implements AdapterView.OnItemSelectedListener {

    private Integer[] imImageIds = {R.drawable.transparent, R.drawable.sportstshirt, R.drawable.ladiestshirt, R.drawable.menstshirt, R.drawable.ladiestop,R.drawable.tshirt, R.drawable.ladies_shirt, R.drawable.suit};
    private static final int REQUEST_CODE=100;
    private TextView textOutput;

    ImageView viewImage;
    private static final String API_KEY = "3ba0db460d37449a844ad5da3308e63d";

    FritzVisionPosePredictor predictor;
    FritzVisionImage visionImage;
    FritzVisionPoseResult poseResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewImage = findViewById(R.id.image);
        viewImage.setImageResource(R.drawable.tshirt);

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this, imImageIds));
        gallery.setOnItemSelectedListener(this);

        setContentView(R.layout.activity_main);
        textOutput=(TextView) findViewById(R.id.textOutput);

    }

    //code for the speech to text instruction button on the top of the view
    public void onClick(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        try {

            //Start the Activity and wait for the response//
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textOutput.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void initializeFritz() {
        Fritz.configure(this, API_KEY);
    }

    @Override
    protected void setupPredictor() {
        // STEP 1: Get the predictor and set the options.
        // ----------------------------------------------
        // A FritzOnDeviceModel object is available when a model has been
        // successfully downloaded and included with the app.
        PoseOnDeviceModel onDeviceModel = FritzVisionModels.getPoseEstimationOnDeviceModel(ModelVariant.FAST);
        predictor = FritzVision.PoseEstimation.getPredictor(onDeviceModel);
        // ----------------------------------------------
        // END STEP 1
    }

    @Override
    protected void setupImageForPrediction(Image image) {
        // Get the system service for the camera manager
       // final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Gets the first camera id
 //       String cameraId = manager.getCameraIdList().get(0);

        // Determine the rotation on the FritzVisionImage from the camera orientaion and the device rotation.
        // "this" refers to the calling Context (Application, Activity, etc)
        ImageRotation imageRotationFromCamera = FritzVisionOrientation.getImageRotationFromCamera(this, cameraId);// STEP 2: Create the FritzVisionImage object from media.Image
        // ------------------------------------------------------------------------
        visionImage = FritzVisionImage.fromMediaImage(image, imageRotationFromCamera);// ------------------------------------------------------------------------
        // END STEP 2
    }

    @Override
    protected void runInference() {
        // STEP 3: Run predict on the image
        // -------------------------------------------------------------
        poseResult = predictor.predict(visionImage);
        // ----------------------------------------------------
        // END STEP 3
    }

    @Override
    protected void showResult(Canvas canvas, Size cameraSize) {
        // STEP 4: Draw the prediction result
        // ----------------------------------
        if(poseResult != null) {
//            poseResult.drawPoses(canvas, cameraSize);
            viewImage.setVisibility(View.VISIBLE);

            try {

                //Get the first pose
                Pose pose = poseResult.getPoses().get(0);

                List<Pose> poses = poseResult.getPoses();

                //Get the body keypoints
                Keypoint[] keypoint = pose.getKeypoints();

                //Get the name of the keypoint
//            String partName1 = keypoint[5].getPartName();
                PointF keypointPosition1 = keypoint[5].getPosition();
                //          String partName2 = keypoint[6].getPartName();
                PointF keypointPosition2 = keypoint[6].getPosition();
                //           String partName3 = keypoint[11].getPartName();
                PointF keypointPosition3 = keypoint[11].getPosition();
                //         String partName4 = keypoint[12].getPartName();
                PointF keypointPosition4 = keypoint[12].getPosition();

                int x1cord = Math.round(keypointPosition1.x);
                int y1cord = Math.round(keypointPosition1.y);
                int x2cord = Math.round(keypointPosition2.x);
                int y2cord = Math.round(keypointPosition2.y);
                int x3cord = Math.round(keypointPosition3.x);
                int y3cord = Math.round(keypointPosition3.y);
                int x4cord = Math.round(keypointPosition4.x);
                int y4cord = Math.round(keypointPosition4.y);

                //Height
                int hcenterx1 = (x1cord+x2cord)/2;
                int hcentery1 = (y1cord+y2cord)/2;
                int hcenterx2 = (x3cord+x4cord)/2;
                int hcentery2 = (y3cord+y4cord)/2;
                int xheight = (hcenterx1 - hcenterx2) * (hcenterx1 - hcenterx2);
                int yheight = (hcentery1- hcentery2) * (hcentery1 - hcentery2);
                int height = (int) Math.round(Math.sqrt(xheight + yheight));

                //Width
                int wcenterx1 = (x1cord+x3cord)/2;
                int wcentery1 = (y1cord+y3cord)/2;
                int wcenterx2 = (x2cord+x4cord)/2;
                int wcentery2 = (y2cord+y4cord)/2;
                int xwidth = (wcenterx1 - wcenterx2) * (wcenterx1 - wcenterx2);
                int ywidth = (wcentery1- wcentery2) * (wcentery1 - wcentery2);
                int width = (int) Math.round(Math.sqrt(xwidth + ywidth));

                int xcord = (x1cord + x2cord + x3cord + x4cord);
                int ycord = (y1cord + y2cord + y3cord + y4cord);

                int viewWidth = width * 6;
                int viewHeight = height * 6;
//                imageView.layout(xcord - viewWidth + 150, ycord - viewHeight + 300, xcord + viewWidth + 650, ycord + viewHeight + 950);
                viewImage.layout(xcord - viewWidth, ycord - viewHeight, xcord + viewWidth, ycord + viewHeight);

            }catch (Exception e){

            }

        } else {
            viewImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        viewImage.setImageResource(imImageIds[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


