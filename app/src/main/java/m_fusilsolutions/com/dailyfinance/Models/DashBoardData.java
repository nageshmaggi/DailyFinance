package m_fusilsolutions.com.dailyfinance.Models;

/**
 * Created by Android on 02-10-2019.
 */

public class DashBoardData
{
    private String Name;
    private String Amount;
    private int Image;

    public DashBoardData(String name, String amount, int image) {
        Name = name;
        Amount = amount;
        Image = image;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getAmount() {
        return Amount;
    }
    public void setAmount(String amount) {
        Amount = amount;
    }

    public int getImage() {
        return Image;
    }
    public void setImage(int image) {
        Image = image;
    }
}
