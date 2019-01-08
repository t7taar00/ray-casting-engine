public class Player
{
    private double posX;
    private double posY;
            
    private double dirX;
    private double dirY;
    
    public Player()
    {
        this.posX = 22;
        this.posY = 12;
        
        this.dirX = -1;
        this.dirY = 0;
    }
    
    public double getPosX()
    {
        return this.posX;
    }
    
    public double getPosY()
    {
        return this.posY;
    }
    
    public double getDirX()
    {
        return this.dirX;
    }
    
    public double getDirY()
    {
        return this.dirY;
    }
    
    public void setPosX(double posX)
    {
        this.posX = posX;
    }
    
    public void setPosY(double posY)
    {
        this.posY = posY;
    }
    
    public void setDirX(double dirX)
    {
        this.dirX = dirX;
    }
    
    public void setDirY(double dirY)
    {
        this.dirY = dirY;
    }
}
