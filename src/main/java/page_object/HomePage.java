package page_object;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;
import static utils.Constants.*;

public class HomePage {

    private static final Logger LOGGER = Logger.getLogger(HomePage.class);
    private static final int TIMEOUT = 10;
    protected WebDriver driver;

    @FindBy(xpath = "//div[1]/div/a[contains(@class, 'collection')]")
    private List<WebElement> regionsMenuList;

    @FindBy(xpath = "//div[@class='card-content']/h3")
    private WebElement wineDetailsTitle;

    @FindBy(xpath = "//div[@class='card-content']/p")
    private List<WebElement> wineDetailsList;

    @FindBy(xpath = "//div[@class='card-action']/a[2]")
    private WebElement commentButton;

    private String xPathLikeButton = "//div[@class='card-action']/a[1]";
    private String xPathWineMenu = "//div[2]/div/a[contains(@class, 'collection')]";
    private String xPathLikeText = "//div[@class='card-action']/a[1]/span";
    private String xPathWineDetailsTitle = "//div[@class='card-content']/h3";
    private String xPathTempLikeEffect = "//div[@class='waves-ripple ']";


    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, TIMEOUT), this);
    }

    public void navigateToRegion(String region) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='" + region + "']"))).click();
            LOGGER.info("Region clicked " + region);
            Thread.sleep(2000); // waiting for new data in HTML page to be loaded

        } catch (Exception exception) {
            driver.close();
            Assert.fail(exception.getMessage());
        }
    }

    public void verifyNumberOfWines(String wineCatalogSize) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        try {
            List<WebElement> winesMenu = driver.findElements(By.xpath(xPathWineMenu));
            LOGGER.info("Size of wine list: " + winesMenu.size());
            Assert.assertEquals(((Integer) winesMenu.size()).toString(), wineCatalogSize);

        } catch (Exception exception) {
            driver.close();
            Assert.fail(exception.getMessage());
        }
    }

    public void checkWineAndRegionDetails(String region) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        String wineDetailsTitleName = "";

        try {
            List<WebElement> winesMenu = driver.findElements(By.xpath(xPathWineMenu));
            for (WebElement wine : winesMenu) {
                wait.until(ExpectedConditions.elementToBeClickable(wine)).click();
                LOGGER.info("   Wine clicked " + wine.getText());

                //Avoiding Cache issues, waiting the element to be refreshed with the right text
                wait.until(textToBePresentInElement(wineDetailsTitle, wine.getText()));
                wineDetailsTitleName = driver.findElement(By.xpath(xPathWineDetailsTitle)).getText();

                // Wine name is correct in Wine Details
                Assert.assertEquals(wine.getText(), wineDetailsTitleName);
                // Region name is correct in Wine Details
                Assert.assertTrue(wineDetailsList.get(WINE_DETAILS_REGION_INDEX).getText().contains(region));
            }

        } catch (Exception exception) {
            driver.close();
            Assert.fail(exception.getMessage());
        }
    }

    public void likeUnlikeAllWines() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        boolean isLikedBefore;
        boolean isLikedAfter;

        try {
            List<WebElement> winesMenu = driver.findElements(By.xpath(xPathWineMenu));

            for (WebElement wine : winesMenu) {
                new Actions(driver).moveToElement(wine).perform();
                wait.until(ExpectedConditions.elementToBeClickable(wine)).click();
                LOGGER.info("   Wine clicked " + wine.getText());

                // Getting "Like" status before like action
                new Actions(driver).moveToElement(driver.findElement(By.xpath(xPathLikeButton))).perform();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathLikeText)));
                isLikedBefore = driver.findElement(By.xpath(xPathLikeText)).getText().contains("UNLIKE");

                // Like/Unlike the wine
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathLikeButton))).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPathTempLikeEffect)));
                LOGGER.info("   Wine liked/unliked ");

                // Getting "Like" status after like action
                new Actions(driver).moveToElement(driver.findElement(By.xpath(xPathLikeButton))).perform();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathLikeText)));
                isLikedAfter = driver.findElement(By.xpath(xPathLikeText)).getText().contains("UNLIKE");

                // Validation of "Like" status transition
                Assert.assertTrue(isLikedAfter!=isLikedBefore);
            }

        } catch (Exception exception) {
            driver.close();
            Assert.fail(exception.getMessage());
        }
    }

    public void closeBrowser() {
        LOGGER.info("Closing browser");
        driver.close();
    }
}
