import java.util.Date;

/**
 * Created by 橘 on 2016/7/22.
 */
public class Test {
    public static void main(String [] args){
        Date d=new Date();
        d.setTime(1546099200000L+24*60*60*1000);
        System.out.println(d.toLocaleString());
    }
}
