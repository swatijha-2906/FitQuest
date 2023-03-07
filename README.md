# FitQuest

## About-

This app helps you track your workout. 
* Start time
* End time
* Quality
* Time spent

The 'Start' button will start tracking your workout for the day. When you click on 'Stop' button, a screen will appear where you can select the quality
of your workout and the end time is recorded.

All your workout data is saved in database so that you don't lose it when the app is closed.
Finally the list of workout records are displayed on the main screen in a RecyclerView instead of a static ScrollView.

## Screenshots

![Screenshot 1](/Screenshots/pic_1.jpg) ![Screenshot 2](/Screenshots/pic_2.jpg) ![Screenshot 3](/Screenshots/pic_3.jpg) ![Screenshot 4](/Screenshots/pic_4.jpg) ![Screenshot 5](/Screenshots/pic_5.jpg)![Screenshot 6](/Screenshots/pic_6.jpg)

## How to run this project-

1. Clone this repository in your local environment.
2. Open the application in Android Studio
3. Run the app using emulator or connecting to physical device.


## Insights-

This app demonstrates the following techniques:
* MVVM architecture
* Fragments and Navigation component within Jetpack
* Room database
* DAO
* Coroutines
* RecyclerView
* Refactoring code to make RecyclerView more efficient to maintain and test.
* Transformation map
* Data Binding in XML files
* ViewModel Factory
* Using Backing Properties to protect MutableLiveData
* Observable state LiveData variables to trigger navigation
