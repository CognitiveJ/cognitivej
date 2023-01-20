***Looking for new collaborators for this repo - open an issue if you would like to be considered*** 

# CognitiveJ - Image Analysis in Java
[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![gradle](https://github.com/CognitiveJ/cognitivej/actions/workflows/gradle.yml/badge.svg)](https://github.com/CognitiveJ/cognitivej/actions/workflows/gradle.yml)
[![Download](https://api.bintray.com/packages/cognitivej/CognitiveJ/CognitiveJ/images/download.svg)](https://bintray.com/cognitivej/CognitiveJ/CognitiveJ/_latestVersion)
[![Circle CI](https://circleci.com/gh/CognitiveJ/cognitivej.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/CognitiveJ/cognitivej)

CognitiveJ is an open-source fluent Java (8) API that manages and orchestrates the interaction between Java applications and 
Microsofts’ Cognitive (Project Oxford) Machine Learning & Image Processing libraries and allows you to query and analyze images.

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-11-54-02.png) 

> Face service access is limited based on eligibility and usage criteria. You will need a valid subscription id to Azure Face service

**Faces**

 * Facial Detection – Capture faces, gender, age and associated facial features and landmarks from an image
 * Emotion Detection – Derive emotional state from faces within an image
 * Verification – Verify, with a confidence scale on whether 2 different faces are of the same person
 * Identification – Identify a person from a set of known people.
 * Find Similar – detect, group and rank similar faces
 * Grouping – group people based on facial characteristics
 * Person Group/Person/Face Lists; Create, manage and train groups, face lists and persons to interact with the identification/grouping/find similar face features.

**Vision**

 * Image Describe - Describe visual content of an image and return real world caption to what the Image is of.
 * Image Analysis – extract key details from an image and if the image is of an adult/racy nature.
 * OCR – detect and extract text from an image.
 * Thumbnail – Create thumbnail images based on key points of interest from the image.

**Overlay _(Experimental)_**

 * Apply image layers onto images to visually represent found features.
 * Apply captions onto faces and images
 * Graphically illustrate the Faces/Vision feature sets.
 * Pixelate faces in an image.

**Other Features**

 * Works with local or remote images
 * validation of parameters

**Getting Started**

 * Java 8 or above
 * Subscription keys for the MS Cognitive libraries ([free registration here](https://www.microsoft.com/cognitive-services/))
 * Add the dependency from ~JCenter~ (Since JCenter is no longer available, build from source until artifacts are published elsewhere)

###### Gradle

```groovy
dependencies {
    implementation 'cognitivej:cognitivej:0.6.2'
    ...
}
```
###### Maven
```xml
    <dependency>
      <groupId>cognitivej</groupId>
      <artifactId>cognitivej</artifactId>
      <version>0.6.2</version>
      <type>pom</type>
    </dependency>
```

**Chained Builders** - The builders are simple lightweight wrappers over the MS Cognitive REST calls that manages the marshalling of parameters/responses, the HTTP communications and retry strategies. The builders are chained to allow for follow up manipulation on resources that have been created or retrieved & where applicable.

**Scenarios** - Scenarios are real world use case classes that greatly simplifies the interaction between the builders and the wrapper classes. _While there is no reason you can’t interact directly with the builders, scenarios have much of the boilerplate logic in place to reduce burden._ 

**Overlay** - Allows for creating and writing new images based on the results from the queries. Note: work is ongoing around collision detection and observing boundaries

**Wrappers** Simple domain wrappers around request/response/parameter objects (e.g. Face, FaceAttributes,Person etc)
 
**Face – Detect** can detect faces from within an image and return the results as a collection of ‘face’ results. 

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-12-22-30.png)

 
###### Example

```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE_URL);
    imageOverlayBuilder.outlineFacesOnImage(faceScenarios.findFaces(IMAGE_URL), RectangleType.FULL,
            CognitiveJColourPalette.STRAWBERRY).launchViewer();
}
```


**Face – Landmarks** can detect faces from within an image and apply facial landmarks

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-12-at-15-18-59.png)

###### Example
```java
public static void main(String[] args) throws IOException {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    Face faces = faceScenarios.findSingleFace(IMAGE_URL);
    ImageOverlayBuilder.builder(IMAGE_URL).outFaceLandmarksOnImage(faces).launchViewer();
}
```

**Face – Detect with Attributes** displays associated attributes for detected faces 

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-12-56-15.png)

###### Example
```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    List<Face> faces = faceScenarios.findFaces(IMAGE_URL);
    ImageOverlayBuilder.builder(IMAGE_URL).outlineFacesOnImage(faces, RectangleType.CORNERED,
            CognitiveJColourPalette.MEADOW).writeFaceAttributesToTheSide(faces, CognitiveJColourPalette.MEADOW).launchViewer();
}
```

**Face – Verify** will validate (with a confidence ratio) if 2 different faces are of the same persons.
 
 ![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-13-04-38.png)
 ![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-13-06-31.png)

###### Example
```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(CANDIDATE_1);
    imageOverlayBuilder.verify(CANDIDATE_2, faceScenarios.verifyFaces(CANDIDATE_1, CANDIDATE_2)).launchViewer();
}
```

**Face – Identify** will identify a person (or people) within an image. Before the library can identify, we need to provide the the Cognitive libraries with the samples set of candidates. Currently supports 1000 candidates. 

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-14-00-55.png)

###### Example
```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE);
    List<ImageHolder> candidates = candidates();
    People people = ScenarioHelper.createPeopleFromHoldingImages(candidates, ImageNamingStrategy.DEFAULT);
    String groupId = faceScenarios.createGroupWithPeople(randomAlphabetic(6).toLowerCase(), people);
}
```

**Face – Pixelate** will identify all faces within an image and pixelate them. 

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-19-23-23.png)

```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    ImageOverlayBuilder imageOverlayBuilder = ImageOverlayBuilder.builder(IMAGE);
    faceScenarios.findFaces(IMAGE).stream().forEach(imageOverlayBuilder:: pixelateFaceOnImage);
    imageOverlayBuilder.launchViewer();
}
```

**Emotion – Detect** will detect what emotion a face(s) is showing within an image.
![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-14-36-14.png)



```java
public static void main(String[] args) {
    FaceScenarios faceScenarios = new FaceScenarios(getProperty("azure.cognitive.face.subscriptionKey"),
            getProperty("azure.cognitive.emotion.subscriptionKey"));
    ImageOverlayBuilder.builder(IMAGE_URL).outlineEmotionsOnImage(faceScenarios.findEmotionFaces(IMAGE_URL)).launchViewer();
}
```
**Vision – Describe** will analyse and describe the contents of an image in a human readable caption.

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-11-at-17-12-49.png)

```java
public static void main(String[] args) {
    ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario(getProperty("azure.cognitive.vision.subscriptionKey"));
    ImageDescription imageDescription = computerVisionScenario.describeImage(IMAGE_URL);
    ImageOverlayBuilder.builder(IMAGE_URL).describeImage(imageDescription).launchViewer();

}
```

**Vision – OCR** will analyse and extract text from within an image into a computer understandable stream.

![](https://iwkelly.files.wordpress.com/2016/05/screen-shot-2016-05-12-at-11-41-25.png)

```java
public static void main(String[] args) {
    ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario(getProperty("azure.cognitive.vision.subscriptionKey"));
    OCRResult ocrResult = computerVisionScenario.ocrImage(IMAGE_URL);
    ImageOverlayBuilder.builder(IMAGE_URL).ocrImage(ocrResult).launchViewer();
}
```
