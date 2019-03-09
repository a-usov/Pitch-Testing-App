
# SportsLabs Pitch Testing App

This app is used for the testing of sports pitches by recording the sound of a ball falling from 2 meters high and bouncing twice. The main functionality of the app is to conduct a test of how a certain pitch performs. The pitch is tested at 6 different locations, with 5 drops being conducted at each location. Additional functionality includes calibrating the ball used for the test, exporting the results of the test to a CSV or PDF file and using Bluetooth to operate the drop equipment. 

This software uses code of [FFmpeg](http://ffmpeg.org) licensed under the [LGPLv2.1](http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html) and its source can be downloaded [here](https://github.com/FFmpeg/FFmpeg)
 
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

To build the project, the Android SDK and an installation of the Java JDK is required. Only instructions for setting up the SDK are included. The recommended way is to install Android Studio which will install the SDK for you and allows you to open and build the project

### Prerequisites

Java JDK installation is required.

For building on the command line, the [Android SDK](https://developer.android.com/studio) is required. Scroll down the page to find command line only tools and download the appropriate file for your system. Extract the file to a destination of your choice. Next follow the instructions for your system 

#### Windows

1.  Right-click on ‘My Computer’ and select Properties. Go to Advanced system settings and select ‘Environmental Variables’ option.
2.  Under the User Variable table, click New to open New User Variable dialog.
3.  Put  **ANDROID_HOME**  as Variable name and provide the  **path of the SDK folder**  next to Variable value.
4.  Click OK to close the dialog box
5.  Go to the folder where SDK has been installed.
6.  Inside the SDK folder look for ‘tools’ and ‘platform-tools’ folder.
7.  Copy the path for both tools and platform-tools.
8.  Open ‘Environmental Variables’ dialog box.
9.  Go to System Variables table and locate the Path variable.
10.  Edit the path variable from ‘Edit system Variables’ dialog box.
11.  Add the ‘tools’ and platform-tools’ folder’s full path

#### Mac

Open the file .bash_profile in your home directory and add the following lines, replacing the path with the path where you extracted the folder
```
export PATH="path/to/Android/tools:$PATH"
export PATH="$path/to/Android/platform-tools:$PATH"
export ANDROID_HOME="$path/to/Android"
```
Save the file and reboot your computer

#### Linux
Open the file .bashrc in your home directory and add the following lines, replacing the path with the path where you extracted the folder
```
export PATH="path/to/Android/tools:$PATH"
export PATH="path/to/Android/platform-tools:$PATH"
export ANDROID_HOME="path/to/Android"
```
### Building

To build the project, run the commands below, but [git](http://git-scm.com/) is required. Otherwise you can download the source code manually from the repository and extract to your home directory and follow from the second command. The built APK files will be within the `app/build/outputs/apk` directory.

#### Linux/Mac
```
git clone http://stgit.dcs.gla.ac.uk/tp3-2018-cs23/codebase.git
cd codebase/SoundRecord
./gradlew build
```

#### Windows

```
git clone http://stgit.dcs.gla.ac.uk/tp3-2018-cs23/codebase.git
cd codebase/SoundRecord
gradlew.bat build
```

If the last command does not work, check  that you have permissions to execute the correct file.


### Installing

The built APK can then be copied to an Android device and installed by navigating to it using a file manager and running it. 

## Authors

* **Artem Usov**
* **Olga Jodelka**
* **Conall Clements** 
* **Kara Newlands**

See also the list of [contributors](http://stgit.dcs.gla.ac.uk/tp3-2018-cs23/codebase/blob/master/CONTRIBUTING.md) for contact details 

## License

This project is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International license - see the [LICENSE.md](http://stgit.dcs.gla.ac.uk/tp3-2018-cs23/codebase/blob/master/LICENCE) file for details.

This project uses FFmpeg which we do not own but can be found at [FFmpeg](http://ffmpeg.org) and its source can be downloaded [here](https://github.com/FFmpeg/FFmpeg)
 

## Acknowledgments
* TarsosDSP library
	* Joren Six and Olmo Cornelis and Marc Leman
	*  TarsosDSP, a Real-Time Audio Processing Framework in Java
	* Proceedings of the 53rd AES Conference (AES 53rd), 2014
* Nicola Corso
	* [Wav File Recorder](http://selvaline.blogspot.com/2016/04/record-audio-wav-format-android-how-to.html)	
* [SportLabs](https://www.sportslabs.co.uk)


