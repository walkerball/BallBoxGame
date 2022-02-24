
public class Score {
    private int Level, Points;

    public Score(int level, int points)
    {
        this.Level = level;
        this.Points = points;
    }

    public Score()
    {
        this.Level = 1;
        this.Points = 0;
    }

    public int getPoints()
    {
        return Points;
    }

    public int getLevel()
    {
        return Level;
    }

    public void setPoints(int points)
    {
        this.Points = points;
    }

    public void setLevel(int level)
    {
        this.Level = level;
    }

    public void addPoints(int points)
    {
        this.Points += points;
    }

    public void addLevel(int level)
    {
        this.Level += level;
    }

    public void resetScore()
    {
        this.Level = 1;
        this.Points = 0;
    }
}
