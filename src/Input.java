import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Input
{
    static final double MOVE_SPEED = 0.2;
    static final double ROT_SPEED = 0.05;
    
    public Input()
    {
        
    }
    
    public void setKeyEvents(Scene gameScene, Player gamePlayer, Plane viewPlane)
    {
        gameScene.setOnKeyPressed((KeyEvent event) ->
        {
            if(event.getCode() == KeyCode.W)
            {     
                if(Map.MAP_ARRAY[(int)(gamePlayer.getPosX() + gamePlayer.getDirX() * MOVE_SPEED)][(int)gamePlayer.getPosY()] == 0) 
                {
                    gamePlayer.setPosX(gamePlayer.getPosX() + gamePlayer.getDirX() * MOVE_SPEED);
                }
                if(Map.MAP_ARRAY[(int)gamePlayer.getPosX()][(int)(gamePlayer.getPosY() + gamePlayer.getDirY() * MOVE_SPEED)] == 0)
                {
                    gamePlayer.setPosY(gamePlayer.getPosY() + gamePlayer.getDirY() * MOVE_SPEED);
                }
            }
            
            if(event.getCode() == KeyCode.S)
            {     
                if(Map.MAP_ARRAY[(int)(gamePlayer.getPosX() - gamePlayer.getDirX() * MOVE_SPEED)][(int)gamePlayer.getPosY()] == 0) 
                {
                    gamePlayer.setPosX(gamePlayer.getPosX() - gamePlayer.getDirX() * MOVE_SPEED);
                }
                if(Map.MAP_ARRAY[(int)gamePlayer.getPosX()][(int)(gamePlayer.getPosY() - gamePlayer.getDirY() * MOVE_SPEED)] == 0)
                {
                    gamePlayer.setPosY(gamePlayer.getPosY() - gamePlayer.getDirY() * MOVE_SPEED);
                }
            }  
            
            if(event.getCode() == KeyCode.A)
            {     
                double oldDirX = gamePlayer.getDirX();
                gamePlayer.setDirX(gamePlayer.getDirX() * Math.cos(ROT_SPEED) - gamePlayer.getDirY() * Math.sin(ROT_SPEED));
                gamePlayer.setDirY(oldDirX * Math.sin(ROT_SPEED) + gamePlayer.getDirY() * Math.cos(ROT_SPEED));
                
                double oldPlaneX = viewPlane.getPlaneX();
                viewPlane.setPlaneX(viewPlane.getPlaneX() * Math.cos(ROT_SPEED) - viewPlane.getPlaneY() * Math.sin(ROT_SPEED));
                viewPlane.setPlaneY(oldPlaneX * Math.sin(ROT_SPEED) + viewPlane.getPlaneY() * Math.cos(ROT_SPEED));
            }   
            
            if(event.getCode() == KeyCode.D)
            {     
                double oldDirX = gamePlayer.getDirX();
                gamePlayer.setDirX(gamePlayer.getDirX() * Math.cos(-ROT_SPEED) - gamePlayer.getDirY() * Math.sin(-ROT_SPEED));
                gamePlayer.setDirY(oldDirX * Math.sin(-ROT_SPEED) + gamePlayer.getDirY() * Math.cos(-ROT_SPEED));
                
                double oldPlaneX = viewPlane.getPlaneX();
                viewPlane.setPlaneX(viewPlane.getPlaneX() * Math.cos(-ROT_SPEED) - viewPlane.getPlaneY() * Math.sin(-ROT_SPEED));
                viewPlane.setPlaneY(oldPlaneX * Math.sin(-ROT_SPEED) + viewPlane.getPlaneY() * Math.cos(-ROT_SPEED));
            }
            
            if(event.getCode() == KeyCode.Q)
            {     
                if(Map.MAP_ARRAY[(int)(gamePlayer.getPosX() - viewPlane.getPlaneX() * MOVE_SPEED)][(int)gamePlayer.getPosY()] == 0) 
                {
                    gamePlayer.setPosX(gamePlayer.getPosX() - viewPlane.getPlaneX() * MOVE_SPEED);
                }
                if(Map.MAP_ARRAY[(int)gamePlayer.getPosX()][(int)(gamePlayer.getPosY() - viewPlane.getPlaneY() * MOVE_SPEED)] == 0)
                {
                    gamePlayer.setPosY(gamePlayer.getPosY() - viewPlane.getPlaneY() * MOVE_SPEED);
                }                
            }  
            
            if(event.getCode() == KeyCode.E)
            {     
                if(Map.MAP_ARRAY[(int)(gamePlayer.getPosX() + viewPlane.getPlaneX() * MOVE_SPEED)][(int)gamePlayer.getPosY()] == 0) 
                {
                    gamePlayer.setPosX(gamePlayer.getPosX() + viewPlane.getPlaneX() * MOVE_SPEED);
                }
                if(Map.MAP_ARRAY[(int)gamePlayer.getPosX()][(int)(gamePlayer.getPosY() + viewPlane.getPlaneY() * MOVE_SPEED)] == 0)
                {
                    gamePlayer.setPosY(gamePlayer.getPosY() + viewPlane.getPlaneY() * MOVE_SPEED);
                }
            }
        });
    }
}
