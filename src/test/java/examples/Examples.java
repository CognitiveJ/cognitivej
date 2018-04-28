package examples;

import cognitivej.vision.computervision.ComputerVisionScenario;
import cognitivej.vision.computervision.ImageDescription;
import cognitivej.vision.computervision.OCRResult;
import cognitivej.vision.emotion.Emotion;
import cognitivej.vision.face.scenario.FaceGroupingSet;
import cognitivej.vision.face.scenario.FaceScenarios;
import cognitivej.vision.face.scenario.ImageAndFace;
import cognitivej.vision.face.scenario.ImageHolder;
import cognitivej.vision.face.scenario.ImageNamingStrategy;
import cognitivej.vision.face.scenario.People;
import cognitivej.vision.face.scenario.ScenarioHelper;
import cognitivej.vision.face.task.Face;
import cognitivej.vision.face.task.Face.FaceAttributesResp;
import cognitivej.vision.face.task.FaceAttributes;
import cognitivej.vision.face.task.FaceGrouping;
import cognitivej.vision.overlay.CognitiveJColourPalette;
import cognitivej.vision.overlay.RectangleType;
import cognitivej.vision.overlay.builder.ImageOverlayBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     * Some images of famous people to test grouping.
     */
    private static final String OBAMA_1 = "http://thehill.com/sites/default/files/styles/" +
            "thumb_small_article/public/blogs/obama2_0.jpg",
            OBAMA_2 = "https://upload.wikimedia.org/wikipedia/commons/8/8d/" +
                    "President_Barack_Obama.jpg",
            GABEN_1 = "https://ih1.redbubble.net/image.464960440.1473/" +
                    "raf,750x1000,075,t,f89f2b:1617e563f0.u5.jpg",
            GABEN_2 = "https://i.gadgets360cdn.com/large/Gabe_Newell_1484763796244.jpg",
            GABEN_3 = "https://files.gamebanana.com/img/ss/srends/530-90_5129d4a3be73b.jpg",
            JEFF_DEAN_1 = "https://thumbor.forbes.com/thumbor/960x0/" +
                    "https%3A%2F%2Fblogs-images.forbes.com%2Fpeterhigh%2Ffiles%2F2016%2F08%2F" +
                    "Jeff-Dean-3-1200x566.gif",
            JEFF_DEAN_2 = "https://media.wired.com/photos/5ad69141e7f4053764abb7fc/" +
                    "master/pass/Jeff-Dean-FINAL.jpg";
    
    /**
     * Obtain the face scenarios.
     *
     * @return the constructed face scenarios.
     */
    private static FaceScenarios faceScenarios() {
        return new FaceScenarios(
                getProperty("azure.cognitive.face.subscriptionKey"),
                getProperty("azure.cognitive.emotion.subscriptionKey"));
    }
    
    /**
     * Face detection.
     */
    public static void faceDetect() {
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE_URL);
        imageOverlayBuilder.outlineFacesOnImage(
                faceScenarios().findFaces(IMAGE_URL),
                RectangleType.FULL, CognitiveJColourPalette.STRAWBERRY).launchViewer();
    }
    
    /**
     * Find similar faces and group them.
     */
    public static void faceGroupSimilar() {
        FaceGroupingSet faceGroupingSet = faceScenarios().groupFaceListOnSingleFace(Arrays.asList(
                OBAMA_1, OBAMA_2, GABEN_1, GABEN_2, GABEN_3, JEFF_DEAN_1, JEFF_DEAN_2));
        FaceGrouping faceGrouping = faceGroupingSet.getGroupings();
        List<String> info = faceGroupingSet.getImageAndFaces().parallelStream()
                .map(o -> o.getImage() +  ", " + o.getFace().getFaceId() + "; ")
                .collect(Collectors.toList());
        System.out.println(info);
        System.out.println("Grouped: " + faceGrouping.getGroups());
        System.out.println("UnGrouped: " + faceGrouping.getMessyGroup());
    }
    
    /**
     * Face landmarks detection.
     */
    public static void faceLandmarks() {
        Face faces = faceScenarios().findSingleFace(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .outFaceLandmarksOnImage(faces).launchViewer();
    }
    
    /**
     * Face attribute detection.
     */
    public static void faceAttribute() {
        List<Face> faces = faceScenarios().findFaces(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .outlineFacesOnImage(faces, RectangleType.CORNERED, CognitiveJColourPalette.MEADOW)
                .writeFaceAttributes(faces, CognitiveJColourPalette.MEADOW).launchViewer();
    }
    
    /**
     * Verify faces.
     */
    public static void faceVerify() {
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(OBAMA_1);
        imageOverlayBuilder.verify(OBAMA_2,
                faceScenarios().verifyFaces(OBAMA_1, OBAMA_2))
                .launchViewer();
    }
    
    /**
     * Identify faces.
     */
    public static void faceIdentify() {
        ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE_URL);
        List<ImageHolder> candidates = new ArrayList<>();
        People people = ScenarioHelper.createPeopleFromHoldingImages(
                candidates, ImageNamingStrategy.DEFAULT);
        String groupId = faceScenarios().createGroupWithPeople(
                randomAlphabetic(6).toLowerCase(), people);
    }
    
    /**
     * Pixelate images.
     */
    public static void facePixelate() {
        ImageOverlayBuilder imageOverlayBuilder =
                ImageOverlayBuilder.builder(IMAGE_URL);
        faceScenarios().findFaces(IMAGE_URL).forEach(imageOverlayBuilder::pixelateFaceOnImage);
        imageOverlayBuilder.launchViewer();
    }
    
    /**
     * Detect emotions.
     */
    public static void detectEmotions() {
        ImageOverlayBuilder.builder(IMAGE_URL).outlineEmotionsOnImage(
                faceScenarios().findEmotionFaces(IMAGE_URL)).launchViewer();
    }
    
    /**
     * Find a description of the image.
     */
    public static void describeImage() {
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
    public static void visionOCR() {
        ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario(
                getProperty("azure.cognitive.vision.subscriptionKey"));
        OCRResult ocrResult = computerVisionScenario.ocrImage(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL)
                .ocrImage(ocrResult).launchViewer();
    }
    
}
