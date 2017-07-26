# LinkedinProfileAnalysis
## 1.Target<br>
My target is using a web automatic test framework Selenium to crawling the users' homepage in LinkedIn, I use the regular expression and some tricks to match the information the most recruiters are interested in such as working experience and educational experience without being detected as a web crawler, I also make a rule to calculate the score of the profile of the user, the more score the profile means the more competitive the user is in IT market.


## 2.How to use it
### 2.1 Something to prepare<br>
Before running it, you will have a firefox.exe as the kenel of the webdriver to run the program, you should also notice the version of the firefox.exe and the version of the Selenium framework, I strongly recommond you to download the firefox from the project to make sure the version of firefox is compatible with the Selenium, we use selenium-server-standalone-2.44.0.jar and firefox33.1.
#### step1: set to your firefox.exe path 
        FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe"));
#### step2: set to your linkedin website login name and password
        id.sendKeys("********");
        pass.sendKeys("********");
        
        
### 2.2 Why we use Selenium
#### If you use the traditional way such as using some web tools to construct the http request and get the http response will have the following question:<br>
2.2.1: The Linkedin website has done a lot of things to anti the web crawler, first you have to login before you enter their website which means you have to get all the cookies from the website and construct a request using these cookies, they also generate some random number in the form, you also have the take the random number to their server. I have done this in another project by using go language.<br>
<br>
2.2.2: Lots of websites are not using the static html, they use ajax to dynamic load the page, in order to get the entire page, you have to form all the ajax request, it can be done, but it will take a lot of time and have bugs.<br>
<br>
2.2.3 Selenium can do all things for you, what you need to do is just get the page and analyse it which confront with our needs.<br>
<br>

### 2.3 Some Examples
#### you can use xpath syntax or get the node by the node name and id...<br>
        WebElement position = driver.findElement(By.xpath("//h2[@class='pv-top-card-section__headline Sans-19px-black-85%']"));
        
 
 For more information about the Selenium, please look at here:    [Selenium doc](http://www.seleniumhq.org/docs/) <br>
 <br>
 
 
 
 ## 3.The rules to calculate the profile
 We want to evaluate a user from several prospectives:
 <br>
 * The educational experience: The full socre is 100 scores <br>

 * The working experience: The full score is 100 scores <br>

 * The skills match the recruiters' requirment <br>

 #### if the candidate is a graduate student from the university we will use that formula:
        scores = (float) (0.4 * skillScore + 0.3 * eduScore + 0.3 * workScore);
 
 #### if the candidate is not a graduate student from the university the weights of the educational experience will be lower:
        scores = (float) (0.5 * skillScore + 0.4 * workScore + 0.1 * eduScore);
        
        
 <br> 
        
 # PS: I think the rule to calculate the score is not perfect and flexible, you can join me and make interesting rules to analyse the profile, welcome to discuss with me and give me some suggestions.
 
 
