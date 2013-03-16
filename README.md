README
=======
Table of content
---------------------------------
1. Program description

2. Specifications

3. Problems

4. How to build

5. How to run

6. Author and contact info



---------------------------------------
1.Program description  

Created for Android 4.2.2. Jelly Bean.  

The applications gives you a read only view of your Facebook wall and your Twitter news feed. 
You can navigate between Twitter and Facebook by clicking the settings button and then chose Facebook or Twitter.   

Facebook:  
The displayed posts are limited to status update and single picture posts.   

Twitter:   
All tweets from your news feed will be displayed. 

---------------------------------------
2.Specifications  

2.1. To use the application, internet connection is required.   
If no internet, you will get an error displayed on your screen telling you to connect.   

2.2. Login  
On your first screen you can chose to log in to either Facebook or Twitter.   
If you chose Facebook, you go through the login process and your Facebook wall will then be displayed. If you press the menu button you can then chose to go back to the settings page and log out of Facebook, or you can direct to your Twitter feed. Here a Twitter Login button will be displayed.   
If you chose Twitter in the fist screen, you will be directed to the same Twitter feed as in the approach above. By clicking the Twitter Login button you will be directed through Twitter login and then be directed back to the Twitter feed. You can navigate back to Facebook by clicking menu -> Facebook    

---------------------------------------
3.Problems  

3.1. Connection between Facebook and Twitter   
We used fragments on the Facebook part. We found this approach difficult to implement on the Twitter API, instead we used activities.   

3.2. Twitter Login  
Struggled with this for a couple of days.   
You need to press the login button two times to load the timeline.

3.3. Exceeded Twitter API limit    

---------------------------------------
4.How to build  

Before you can build the application, you would have to email us your Facebook Key Hash. You will reseive an email when the key is successfuly added and you can then build the application, using Eclipse.   


---------------------------------------
5. How to run

To run the application you have to install it on an android device, preferably a device running Android Jelly Bean 4.2. The application can be installed from the attached .apk file by running
	
	adb install aggregator.apk

---------------------------------------
6.Created by:

Vegar Engen 		vegar.engen@gmail.com  
Anlaug Underdal 	a.underdal@gmail.com  

--------------------------------------
