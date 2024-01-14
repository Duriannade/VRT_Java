package Web;

import io.visual_regression_tracker.sdk_java.IgnoreAreas;
import io.visual_regression_tracker.sdk_java.TestRunOptions;
import io.visual_regression_tracker.sdk_java.VisualRegressionTracker;
import io.visual_regression_tracker.sdk_java.VisualRegressionTrackerConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Collections;

public class GoogleTest {
    static VisualRegressionTracker vrt;
    static WebDriver driver;
    static VisualRegressionTrackerConfig config = VisualRegressionTrackerConfig.builder()
            .apiUrl("http://localhost:4200")
            .apiKey("DEFAULTUSERAPIKEYTOBECHANGED")
            .project("Default project")
            .branchName("master")
            .enableSoftAssert(true)
            .ciBuildId("some build id")
            //Nếu fail do lỗi time out thì chỉnh Timeout in Seconds lên
            .httpTimeoutInSeconds(300)
            .build();

    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/mac/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();
        vrt = new VisualRegressionTracker(config);
        vrt.start();
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        driver.quit();
        vrt.stop();
    }

    @Test
    public void ExampleTest() throws IOException, InterruptedException {
        driver.navigate().to("http://google.com");
        driver.manage().window().fullscreen();
        Thread.sleep(5000);
        vrt.track(
                "Home page",
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64),TestRunOptions.builder().device("Macbook Air").os("macOS Catalina")
                        .browser("Chrome").viewport("3360 x 2012").diffTollerancePercent(0.0f)
                        .ignoreAreas(Collections.singletonList(IgnoreAreas.builder()
                                .x(10L).y(10L).width(100L).height(200L).build())).build());
        driver.findElement(By.xpath("//a[@aria-label='Tìm kiếm hình ảnh (mở trong thẻ mới)']")).click();
        Thread.sleep(5000);
        vrt.track(
                "Product page",
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64),
                TestRunOptions.builder().device("Macbook Air").os("macOS Catalina")
                        .browser("Chrome").viewport("3360 x 2012").diffTollerancePercent(0.0f)
                        .ignoreAreas(Collections.singletonList(IgnoreAreas.builder()
                        .x(10L).y(10L).width(100L).height(200L).build())).build());
    }
}
