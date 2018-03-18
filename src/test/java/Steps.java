import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.asserts.SoftAssert;

public class Steps {

    private POFactory methodsClass;
    private String[][] _actualResult;

    @Given("^method to search books$")
    public void initClass(){
        methodsClass = new POFactory();
    }

    @When("^author name is (.*)$")
    public void setAuthorName(String authorName) throws Exception{
//        methodsClass = new POFactory();
        _actualResult = methodsClass.booksList(authorName);
    }

//    When all books are saved to array
    @Then("^check if (.*) is an author for all$")
    public void checkAuthors(String authorName) {
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < _actualResult.length; i++){
            softAssert.assertTrue(_actualResult[i][2].contains(authorName), "Failed with: " +
                    _actualResult[i][0] + " : " + _actualResult[i][1] + " : " + _actualResult[i][2] + " : " + _actualResult[i][3]);
        }
        softAssert.assertAll();
    }

}
