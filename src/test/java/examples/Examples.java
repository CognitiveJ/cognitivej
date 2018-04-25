package examples;

import cognitivej.vision.computervision.ComputerVisionScenario;
import cognitivej.vision.computervision.ImageDescription;
import cognitivej.vision.computervision.OCRResult;
import cognitivej.vision.face.scenario.FaceScenarios;
import cognitivej.vision.face.scenario.ImageHolder;
import cognitivej.vision.face.scenario.ImageNamingStrategy;
import cognitivej.vision.face.scenario.People;
import cognitivej.vision.face.scenario.ScenarioHelper;
import cognitivej.vision.face.task.Face;
import cognitivej.vision.overlay.CognitiveJColourPalette;
import cognitivej.vision.overlay.RectangleType;
import cognitivej.vision.overlay.builder.ImageOverlayBuilder;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * A collection of useful examples.
 */
public final class Examples {
    
    /**
     * A dummy image URL.
     */
    private static final String IMAGE_URL = "DUMMY";
    /**
     * Dummy candidates image URL.
     */
    private static final String CANDIDATE_1 = "C1", CANDIDATE_2 = "C2";
    
    /**
     * Face detection.
     */
    private static void faceDetect() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE_URL);
        imageOverlayBuilder.outlineFacesOnImage(
                faceScenarios.findFaces(IMAGE_URL),
                RectangleType.FULL, CognitiveJColourPalette.STRAWBERRY).launchViewer();
    }
    
    /**
     * Face landmarks detection.
     */
    private static void faceLandmarks() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        Face faces = faceScenarios.findSingleFace(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .outFaceLandmarksOnImage(faces).launchViewer();
    }
    
    /**
     * Face attribute detection.
     */
    private static void faceAttribute() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        List<Face> faces = faceScenarios.findFaces(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .outlineFacesOnImage(faces, RectangleType.CORNERED, CognitiveJColourPalette.MEADOW)
                .writeFaceAttributes(faces, CognitiveJColourPalette.MEADOW).launchViewer();
    }
    
    /**
     * Verify faces.
     */
    private static void faceVerify() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(CANDIDATE_1);
        imageOverlayBuilder.verify(CANDIDATE_2, faceScenarios.verifyFaces(CANDIDATE_1, CANDIDATE_2))
                .launchViewer();
    }
    
    /**
     * Identify faces.
     */
    private static void faceIdentify() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE_URL);
        List<ImageHolder> candidates = new ArrayList<>();
        People people = ScenarioHelper.createPeopleFromHoldingImages(
                candidates, ImageNamingStrategy.DEFAULT);
        String groupId = faceScenarios.createGroupWithPeople(
                randomAlphabetic(6).toLowerCase(), people);
    }
    
    /**
     * Pixelate images.
     */
    private static void facePixelate() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        ImageOverlayBuilder imageOverlayBuilder =
                ImageOverlayBuilder.builder(IMAGE_URL);
        faceScenarios.findFaces(IMAGE_URL).forEach(imageOverlayBuilder::pixelateFaceOnImage);
        imageOverlayBuilder.launchViewer();
    }
    
    /**
     * Detect emotions.
     */
    private static void detectEmotions() {
        FaceScenarios faceScenarios = new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
        ImageOverlayBuilder.builder(IMAGE_URL).outlineEmotionsOnImage(
                faceScenarios.findEmotionFaces(IMAGE_URL)).launchViewer();
    }
    
    /**
     * Find a description of the image.
     */
    private static void describeImage() {
        ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario(
                getProperty("azure.cognitive.vision.subscriptionKey"));
        ImageDescription imageDescription =
                computerVisionScenario.describeImage(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .describeImage(imageDescription).launchViewer();
    }
    
    /**
     * Perform vision OCR.
     */
    private static void visionOCR() {
        ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario(
                getProperty("azure.cognitive.vision.subscriptionKey"));
        OCRResult ocrResult = computerVisionScenario.ocrImage(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .ocrImage(ocrResult).launchViewer();
    }
    
}
