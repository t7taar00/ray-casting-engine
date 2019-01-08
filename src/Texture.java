import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Texture
{
    static final int TEX_WIDTH = 64;
    static final int TEX_HEIGHT = 64;
    
    Image texture1;
    Image texture2;
    Image texture3;
    Image texture4;
    Image texture5;
    Image texture6;
    
    Image textureFloor;
    Image textureCeiling;

    PixelReader textureReader;
    
    public Texture()
    {
        texture1 = new Image("texture/greystone.png");    
        texture2 = new Image("texture/mossy.png");
        texture3 = new Image("texture/wood.png");
        texture4 = new Image("texture/eagle.png");
        texture5 = new Image("texture/redbrick.png");
        texture6 = new Image("texture/purplestone.png");
        
        textureFloor = new Image("texture/dungeon_floor.png");
        textureCeiling = new Image("texture/darkwood.png");
    }
    
    public Color getTexturePixel(int texNum, int x, int y)
    {
        switch(texNum)
        {
            case 1:
                textureReader = texture1.getPixelReader();
                break;
            case 2:
                textureReader = texture2.getPixelReader();
                break;
            case 3:
                textureReader = texture3.getPixelReader();
                break;
            case 4:
                textureReader = texture4.getPixelReader();
                break;
            case 5:
                textureReader = texture5.getPixelReader();
                break;
            case 6:
                textureReader = texture6.getPixelReader();
                break;
            case 10:
                textureReader = textureFloor.getPixelReader();
                break;
            case 11:
                textureReader = textureCeiling.getPixelReader();
                break;
            default:
                break;
        }                  
        return textureReader.getColor(x, y);
    }
}
