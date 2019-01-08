import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Engine extends Application
{   
    static final double SCREEN_WIDTH = 800;
    static final double SCREEN_HEIGHT = 600;
    
    static final double VIEW_WIDTH = 640;
    static final double VIEW_HEIGHT = 480;
    
    static final double BRIGHTNESS_LEVEL = 2.0;
    static final double MAX_BRIGHTNESS = 1.2;
    
    static final int RAY_STEP = 2;
    
    Scene gameScene;
    Group gameObjects;
    
    ImageView bufferView;
    
    ImageView uiBottom;
    ImageView uiRight;
    
    Audio gameSound;
    Audio gameMusic;
    
    AnimationTimer gameTimer;
    Input gameInput;
    
    Plane viewPlane;
    Ray castRay;
    Map gameMap;
    Player gamePlayer;
    Texture surfaceTexture;
    
    Rectangle gameCeiling;
    
    Label engineInfo;
    
    int frameCount = 0;
    
    int prevMapX = 0;
    int prevMapY = 0;
    
    double stepRate = 1;

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void start(Stage gameStage)
    {   
        gameObjects = new Group();
        
        gameScene = new Scene(gameObjects,
                              SCREEN_WIDTH,
                              SCREEN_HEIGHT,
                              Color.BLACK);
        
        bufferView = new ImageView();
        
        uiBottom = new ImageView("texture/ui_bottom.png");
        uiBottom.relocate(0, 480);
        
        uiRight = new ImageView("texture/ui_right.png");
        uiRight.relocate(640, 0);
        
        gameObjects.getChildren().addAll(uiBottom, uiRight);
        
        viewPlane = new Plane();
        castRay = new Ray();
        gameMap = new Map();
        gamePlayer = new Player();
        
        gameCeiling = new Rectangle(0,
                                    0,
                                    VIEW_WIDTH,
                                    VIEW_HEIGHT / 2);
        
        Stop[] ceilingStops = new Stop[] {new Stop(0, Color.FIREBRICK.darker()), new Stop(1, Color.MIDNIGHTBLUE)};
        LinearGradient ceilingGradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, ceilingStops);
        
        gameCeiling.setFill(ceilingGradient);
        //gameObjects.getChildren().add(gameCeiling);       
        
        engineInfo = new Label("Frames:\n" + "X:\n" + "Y:\n");
        engineInfo.relocate(10, 10);
        
        try
        { 
            final Font gameFont = Font.loadFont(new FileInputStream(new File("font/OwreKynge.ttf")), 24);
            engineInfo.setFont(gameFont);
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        engineInfo.setTextFill(Color.GOLD);
        
        gameObjects.getChildren().add(engineInfo);
        
        gameSound = new Audio();
        gameMusic = new Audio();
        gameMusic.playMusic(gameScene, "tune");
        
        surfaceTexture = new Texture();
        
        gameScene.setCursor(Cursor.CROSSHAIR);
        gameStage.setTitle("Ray Casting Engine");
        gameStage.setScene(gameScene);
        gameStage.setFullScreen(false);
        gameStage.setResizable(true);
        //gameStage.initStyle(StageStyle.UNDECORATED);
        gameStage.show();
        
        gameInput = new Input();
        gameInput.setKeyEvents(gameScene, gamePlayer, viewPlane);
        
        gameTimer = new AnimationTimer()
        {
            @Override
            public void handle(long timeStamp)
            {   
                frameCount++;
                               
                WritableImage screenBuffer = new WritableImage((int)VIEW_WIDTH, (int)VIEW_HEIGHT);
                PixelWriter bufferWriter = screenBuffer.getPixelWriter();
                   
                for(int x = 0; x < VIEW_WIDTH; x = x + RAY_STEP)
                {   
                    // calculate ray position and direction
                    double cameraX = 2 * x / VIEW_WIDTH - 1; // x-coordinate in camera space

                    castRay.calcRayDirX(gamePlayer, viewPlane, cameraX);
                    castRay.calcRayDirY(gamePlayer, viewPlane, cameraX);

                    //which box of the map we're in
                    int mapX = (int)gamePlayer.getPosX();
                    int mapY = (int)gamePlayer.getPosY();

                    engineInfo.setText("Frames\t:\t" + frameCount + "\t\t\t\t\tDungeon Demo" + "\n" + 
                                       "V.Rays\t:\t" + (int)VIEW_WIDTH / RAY_STEP + "\n" +
                                       "X Pos.\t:\t" + mapX + "\n" + 
                                       "Y Pos.\t:\t" + mapY);

                    castRay.setPlayerMapX(mapX);
                    castRay.setPlayerMapY(mapY);
                    
                    // play step sound every ten frames if player moved
                    if(x == 0)
                    {
                        if(frameCount % 10 == 0)
                        {
                            if(prevMapX != mapX || prevMapY != mapY)
                            {
                                gameSound.playSound("step", stepRate);

                                prevMapX = mapX;
                                prevMapY = mapY;
                                
                                if(stepRate == 1) stepRate = 1.05;
                                else stepRate = 1;
                            }
                        }
                    }

                    //length of ray from one x or y-side to next x or y-side
                    castRay.calcDeltaDistX();
                    castRay.calcDeltaDistY();

                    double perpWallDist;

                    //what direction to step in x or y-direction (either +1 or -1)
                    int stepX;
                    int stepY;

                    int hit = 0; //was there a wall hit?
                    int side = 0; //was a NS or a EW wall hit?

                    //calculate step and initial sideDist
                    stepX = castRay.calcSideDistX(gamePlayer);
                    stepY = castRay.calcSideDistY(gamePlayer);

                    //perform DDA
                    while(hit == 0)
                    {
                        //jump to next map square, OR in x-direction, OR in y-direction
                        side = castRay.calcMapJump(stepX, stepY);

                        mapX = castRay.getPlayerMapX();
                        mapY = castRay.getPlayerMapY();

                        //Check if ray has hit a wall
                        if(Map.MAP_ARRAY[mapX][mapY] > 0) hit = 1;
                    }

                    //Calculate distance projected on camera direction
                    if(side == 0) perpWallDist = (mapX - gamePlayer.getPosX() + (1 - stepX) / 2) / castRay.getRayDirX();
                    else          perpWallDist = (mapY - gamePlayer.getPosY() + (1 - stepY) / 2) / castRay.getRayDirY();

                    //Calculate height of line to draw on screen
                    int lineHeight = (int)(VIEW_HEIGHT / perpWallDist);

                    //calculate lowest and highest pixel to fill in current stripe
                    int drawStart = -lineHeight / 2 + (int)VIEW_HEIGHT / 2;
                    if(drawStart < 0)drawStart = 0;
                    int drawEnd = lineHeight / 2 + (int)VIEW_HEIGHT / 2;
                    if(drawEnd >= (int)VIEW_HEIGHT)drawEnd = (int)VIEW_HEIGHT - 1;

                    //texturing calculations
                    int texNum = Map.MAP_ARRAY[mapX][mapY];

                    //calculate value of wallX
                    double wallX; //where exactly the wall was hit
                    if(side == 0) wallX = gamePlayer.getPosY() + perpWallDist * castRay.getRayDirY();
                    else          wallX = gamePlayer.getPosX() + perpWallDist * castRay.getRayDirX();
                    wallX -= Math.floor((wallX));

                    //x coordinate on the texture
                    int texX = (int)(wallX * (double)Texture.TEX_WIDTH);
                    if(side == 0 && castRay.getRayDirX() > 0) texX = Texture.TEX_WIDTH - texX - 1;
                    if(side == 1 && castRay.getRayDirY() < 0) texX = Texture.TEX_WIDTH - texX - 1;

                    for(int y = drawStart; y < drawEnd; y++)
                    {
                        int d = y * 256 - (int)VIEW_HEIGHT * 128 + lineHeight * 128;  //256 and 128 factors to avoid floats
                        // TODO: avoid the division to speed this up
                        int texY = ((d * Texture.TEX_HEIGHT) / lineHeight) / 256;

                        Color texColor = surfaceTexture.getTexturePixel(texNum, texX, texY);

                        double lightLevel = lineHeight / VIEW_HEIGHT * BRIGHTNESS_LEVEL;
                        if(lightLevel > MAX_BRIGHTNESS) lightLevel = MAX_BRIGHTNESS;

                        if(side == 1) bufferWriter.setColor(x, y, texColor.deriveColor(0.0, 1.0, lightLevel, 1.0).darker());
                        else          bufferWriter.setColor(x, y, texColor.deriveColor(0.0, 1.0, lightLevel, 1.0));
                    }    

                    //FLOOR CASTING
                    double floorXWall, floorYWall; //x, y position of the floor texel at the bottom of the wall

                    //4 different wall directions possible
                    if(side == 0 && castRay.getRayDirX() > 0)
                    {
                        floorXWall = mapX;
                        floorYWall = mapY + wallX;
                    }
                    else if(side == 0 && castRay.getRayDirX() < 0)
                    {
                        floorXWall = mapX + 1.0;
                        floorYWall = mapY + wallX;
                    }
                    else if(side == 1 && castRay.getRayDirY() > 0)
                    {
                        floorXWall = mapX + wallX;
                        floorYWall = mapY;
                    }
                    else
                    {
                        floorXWall = mapX + wallX;
                        floorYWall = mapY + 1.0;
                    }

                    double distWall, distPlayer, currentDist;

                    distWall = perpWallDist;
                    distPlayer = 0.0;

                    if (drawEnd < 0) drawEnd = (int)VIEW_HEIGHT; //becomes < 0 when the integer overflows

                    //draw the floor from drawEnd to the bottom of the screen
                    for(int y = drawEnd + 1; y < VIEW_HEIGHT; y++)
                    {
                        currentDist = VIEW_HEIGHT / (2.0 * y - VIEW_HEIGHT); //you could make a small lookup table for this instead

                        double weight = (currentDist - distPlayer) / (distWall - distPlayer);

                        double currentFloorX = weight * floorXWall + (1.0 - weight) * gamePlayer.getPosX();
                        double currentFloorY = weight * floorYWall + (1.0 - weight) * gamePlayer.getPosY();

                        int floorTexX, floorTexY;
                        floorTexX = (int)(currentFloorX * Texture.TEX_WIDTH) % Texture.TEX_WIDTH;
                        floorTexY = (int)(currentFloorY * Texture.TEX_HEIGHT) % Texture.TEX_HEIGHT;

                        double lightLevel = 1 / currentDist * BRIGHTNESS_LEVEL;
                        if(lightLevel > MAX_BRIGHTNESS) lightLevel = MAX_BRIGHTNESS;

                        //floor
                        Color texFloorColor = surfaceTexture.getTexturePixel(10, floorTexX, floorTexY);
                        bufferWriter.setColor(x, y, texFloorColor.deriveColor(0.0, 1.0, lightLevel, 1.0));

                        //ceiling 
                        Color texCeilingColor = surfaceTexture.getTexturePixel(11, floorTexX, floorTexY);
                        bufferWriter.setColor(x, (int)VIEW_HEIGHT - y, texCeilingColor.deriveColor(0.0, 1.0, lightLevel, 1.0));
                    }
                }
                bufferView.setImage(screenBuffer);
                //gameObjects.getChildren().clear();
                gameObjects.getChildren().remove(bufferView);
                gameObjects.getChildren().remove(engineInfo);
                //gameObjects.getChildren().add(gameCeiling);              
                gameObjects.getChildren().add(bufferView);
                gameObjects.getChildren().add(engineInfo);
            }
        };      
        gameTimer.start();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
