package StepDefinitionUI;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import page_object.HomePage;
import utils.Constants;

import java.util.concurrent.TimeUnit;

public class HomeSteps {

    WebDriver driver;
    HomePage homePage;
    String regionName;

    private static final Logger LOGGER = Logger.getLogger(HomeSteps.class);

    @Before
    public void beforeScenario() {
        System.setProperty("webdriver.chrome.driver", Constants.RESOURCES_PATH + "chromedriver.exe");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        LOGGER.info("Chrome browser is used for this test");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Given("user opens the browser and navigate into WineStore")
    public void openTheBrowserAndNavigateIntoWineStore() {
        driver.get(Constants.URL);
        homePage = new HomePage(driver);
        LOGGER.info("Wine Store page is loaded");
    }

    @When("user navigates to a {string}")
    public void userNavigatesToA(String region) {
        regionName = region;
        homePage.navigateToRegion(regionName);
    }

    @Then("user validates the amount of {string} wines available in current Region")
    public void userValidatesTheAmountOfWinesAvailableInCurrentRegion(String winesNumber) {
        homePage.verifyNumberOfWines(winesNumber);
    }

    @Then("user validates the wine names in current Region")
    public void userValidatesTheWineNamesInCurrentRegion() {
        homePage.checkWineAndRegionDetails(regionName);
    }

    @After
    public void afterScenario() {
        homePage.closeBrowser();
    }

    @Then("user clicks on Like button for all the wines in the current Region")
    public void userClicksOnLikeButtonForAllTheWinesInTheCurrentRegion() {
        homePage.likeUnlikeAllWines();
    }
}
