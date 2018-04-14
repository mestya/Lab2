package seleniumJavaWebDriver;

//import java.util.regex.Pattern;
//import java.util.concurrent.TimeUnit;
//import org.junit.*;
//import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;
//import org.openqa.selenium.*;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.Select;import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//
//public class SeleniumWebDriver {
//	
//       WebElement usernameEle = driver.findElement(By.id("username"));
//       WebElement passwordEle = driver.findElement(By.id("password"));
//       usernameEle.sendKeys(user);
//       passwordEle.sendKeys(pwd);
//       driver.findElement(By.id("submitButton")).click();
//
//       WebElement emailEle = driver.findElement(By.xpath("//p"));
//       
//       String address = emailEle.attr("href");
////       driver.get("https://psych.liebes.top/st");
////       driver.findElement(By.id("username")).click();
////       driver.findElement(By.id("username")).clear();
////       driver.findElement(By.id("username")).sendKeys("3015230112");
////       driver.findElement(By.id("password")).click();
////       driver.findElement(By.id("password")).clear();
////       driver.findElement(By.id("password")).sendKeys("230112");
////       driver.findElement(By.id("submitButton")).click();
////       driver.findElement(By.xpath("//p")).click();
//       
//       try {  
//           BufferedReader reader = new BufferedReader(new FileReader("input.xlsx"));//��������ļ��� 
//           
//           String line = null;  
//           while((line=reader.readLine())!=null){  
//               String item[] = line.split(",");//CSV��ʽ�ļ�Ϊ���ŷָ����ļ���������ݶ����з�
////               System.out.println(line);
//               String user = item[0]; 
//               String address = item[1];//�������Ҫ�������� 
//
//           }  
//
//           return Arrays.asList(o);
//       } catch (Exception e) {  
//           e.printStackTrace(); 
//           return null;
//       }
//}
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.*;  
import org.openqa.selenium.support.ui.Select;

public class SeleniumWebDriver {     
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "webDriver\\chromedriver.exe");
    driver = new ChromeDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.get("https://psych.liebes.top/st");
  }
  public Map<String,String> readExcel() throws Exception
  {
	  //����������
      FileInputStream fis = new FileInputStream(new File("input.xlsx"));
      //���������õ�������
      XSSFWorkbook workbook = new XSSFWorkbook(fis);
      //�õ�������
      XSSFSheet sheet = workbook.getSheet("Sheet1");
      
      Map<String,String> info = new HashMap<String,String>();
      for(int i=sheet.getFirstRowNum();i<=sheet.getLastRowNum();i++){//��ѭ��
    	  XSSFRow row = sheet.getRow(i);
    	  String cellValue[] = new String[row.getLastCellNum()];
    	  for(int j=row.getFirstCellNum();j<row.getLastCellNum();j++){//��ѭ��
    		  XSSFCell cell = row.getCell(j);//��ȡ��Ԫ������
    		  switch (cell.getCellType()) 
    		  {  
              case Cell.CELL_TYPE_STRING:  
            	   cellValue[j] = cell.getStringCellValue().trim();
                   break;  
              case Cell.CELL_TYPE_NUMERIC:  //�����Ԫ���е���������Ϊ����
                   if(DateUtil.isCellDateFormatted(cell)) {  
                	   cellValue[j] = cell.getDateCellValue().toString().trim();  //תΪ�ַ���
                   }else
                   {  
                	  BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                	  cellValue[j] = bd.toPlainString().trim();  
                   }  
                   break;  
    	      }
    	  }
    		   
    		  info.put(cellValue[0],cellValue[1]);//����Ӧ��ѧ�źͲ��͵�ַ����Map��
      }
      workbook.close();
      fis.close();
      return info;
  }
  @Test
  public void testSelenium() throws Exception {
	  Map<String,String> info = readExcel();
	  Iterator<Entry<String, String>> iterator = info.entrySet().iterator();  
      while (iterator.hasNext()) {  //ѭ����ʼ
          Entry<String, String> entry = iterator.next();  
          String username = entry.getKey();  
          String password = username.substring(4);
          String url = entry.getValue();
          
          if(url==null) {continue;}//�ų�urlΪ�յ����
          driver.get("https://psych.liebes.top/st");
          driver.findElement(By.id("username")).click();
  	      driver.findElement(By.id("username")).clear();
  	      driver.findElement(By.id("username")).sendKeys(username);//�����û���
  	      driver.findElement(By.id("password")).click();
  	      driver.findElement(By.id("password")).clear();
  	      driver.findElement(By.id("password")).sendKeys(password);//��������
  	      driver.findElement(By.id("submitButton")).click();
  	      String geturl = driver.findElement(By.xpath("//p")).getText();
  	      
  	      if(url.charAt(url.length()-1)=='/') url = url.substring(0, url.length()-1);//ȥ����ַĩβ�ġ�/��
  	      if(geturl.charAt(geturl.length()-1)=='/') geturl = geturl.substring(0, geturl.length()-1);
  	      assertEquals(url, geturl);//����
      }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
