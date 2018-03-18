import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class testRun {

    POFactory poFactory = new POFactory();

    @Test
    public void tryIt() throws Exception{
        SoftAssert softAssert = new SoftAssert();
        String authorName = "Jack London";
        String[][] end = poFactory.booksList(authorName);
        for (int i = 0; i < end.length; i++){
//            for (int j = 0; j < end[i].length; j++){
                softAssert.assertTrue(end[i][2].contains(authorName), "Error with: " +
                end[i][0] + " : " + end[i][1] + " : " + end[i][2] + " : " + end[i][3]);
                //if (!end[i][2].equals(authorName))
//                    System.out.print("Record #: " + i + " : " + end[i][j] + " : ");

            System.out.println();
        }

        poFactory.outAll("Jack London");
    }
}
