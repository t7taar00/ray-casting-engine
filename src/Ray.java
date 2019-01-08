public class Ray
{
    private double rayDirX;
    private double rayDirY;
    
    //length of ray from current position to next x or y-side
    private double sideDistX;
    private double sideDistY;
    
    private double deltaDistX;
    private double deltaDistY;
    
    private int playerMapX;
    private int playerMapY;
    
    public Ray()
    {
        
    }
    
    public void calcRayDirX(Player gamePlayer, Plane viewPlane, double cameraX)
    {
        this.rayDirX = gamePlayer.getDirX() + viewPlane.getPlaneX() * cameraX;
    }
    
    public void calcRayDirY(Player gamePlayer, Plane viewPlane, double cameraX)
    {
        this.rayDirY = gamePlayer.getDirY() + viewPlane.getPlaneY() * cameraX;
    }
    
    public void setPlayerMapX(int playerMapX)
    {
        this.playerMapX = playerMapX;
    }
    
    public void setPlayerMapY(int playerMapY)
    {
        this.playerMapY = playerMapY;
    }
    
    public int getPlayerMapX()
    {
        return this.playerMapX;
    }
    
    public int getPlayerMapY()
    {
        return this.playerMapY;
    }
    
    public void calcDeltaDistX()
    {
        this.deltaDistX = Math.abs(1.0 / this.rayDirX);
    }
    
    public void calcDeltaDistY()
    {
        this.deltaDistY = Math.abs(1.0 / this.rayDirY);
    }
    
    public int calcSideDistX(Player gamePlayer)
    {
        if(this.rayDirX < 0)
        {
            this.sideDistX = (gamePlayer.getPosX() - this.playerMapX) * this.deltaDistX;
            return -1;
        }
        else
        {
            this.sideDistX = (this.playerMapX + 1.0 - gamePlayer.getPosX()) * this.deltaDistX;
            return 1;
        }
    }
    
    public int calcSideDistY(Player gamePlayer)
    {
        if(this.rayDirY < 0)
        {
            this.sideDistY = (gamePlayer.getPosY() - this.playerMapY) * this.deltaDistY;
            return -1;
        }
        else
        {
            this.sideDistY = (this.playerMapY + 1.0 - gamePlayer.getPosY()) * this.deltaDistY;
            return 1;
        }
    }
    
    public int calcMapJump(int stepX, int stepY)
    {
        if(this.sideDistX < this.sideDistY)
        {
            this.sideDistX += this.deltaDistX;
            this.playerMapX += stepX;
            return 0;
        }
        else
        {
            this.sideDistY += this.deltaDistY;
            this.playerMapY += stepY;
            return 1;
        }
    }
    
    public double getRayDirX()
    {
        return this.rayDirX;
    }
    
    public double getRayDirY()
    {
        return this.rayDirY;
    }
}
