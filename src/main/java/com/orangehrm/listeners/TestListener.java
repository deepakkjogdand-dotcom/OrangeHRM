//package com.orangehrm.listeners;
//
//import com.aventstack.extentreports.MediaEntityBuilder;
//import com.aventstack.extentreports.ExtentTest;
//import com.orangehrm.base.BaseTest;
//import com.orangehrm.utils.ExtentManager;
//import com.orangehrm.utils.ScreenshotUtil;
//
//import org.testng.ITestContext;
//import org.testng.ITestListener;
//import org.testng.ITestResult;
//
//public class TestListener implements ITestListener {
//
//    private static final ThreadLocal<ExtentTest> test =
//            new ThreadLocal<>();
//
//    @Override
//    public void onStart(ITestContext context) {
//
//        BaseTest.extent =
//                ExtentManager.getInstance();
//    }
//
//    @Override
//    public void onTestStart(ITestResult result) {
//
//        ExtentTest extentTest =
//                BaseTest.extent.createTest(
//                        result.getMethod().getMethodName());
//
//        test.set(extentTest);
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//
//        test.get().pass("Test Passed");
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//
//        test.get().fail(result.getThrowable());
//
//        Object currentClass =
//                result.getInstance();
//
//        BaseTest baseTest =
//                (BaseTest) currentClass;
//
//        String screenshotPath =
//                ScreenshotUtil.captureScreenshot(
//                        baseTest.driver,
//                        result.getMethod().getMethodName());
//
//        try {
//
//            test.get().fail(
//                    "Failure Screenshot",
//                    MediaEntityBuilder
//                            .createScreenCaptureFromPath(
//                                    screenshotPath)
//                            .build());
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//
//        test.get().skip("Test Skipped");
//    }
//
//    @Override
//    public void onFinish(ITestContext context) {
//
//        BaseTest.extent.flush();
//    }
//}
