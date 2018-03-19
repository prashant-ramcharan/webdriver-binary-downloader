[![Build Status](https://travis-ci.org/prashant-ramcharan/webdriver-binary-downloader.svg?branch=master)](https://travis-ci.org/pramcharan/webdriver-binary-downloader)
<a href='https://bintray.com/prashantr/WebDriver-Binary-Downloader/webdriver-binary-downloader/_latestVersion'><img src='https://api.bintray.com/packages/prashantr/WebDriver-Binary-Downloader/webdriver-binary-downloader/images/download.svg'></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


# WebDriver Binary Downloader #

WebDriver Binary Downloader automatically downloads and configures the latest driver binaries needed for Selenium Web Driver tests, without any need for explicit configuration.

## Key Features
- **Always have the latest driver binaries** as soon as its released.
- **No configuration** needed.
- **Windows, MacOS and Linux** supported (32 bit and 64 bit)
- **Auto configured driver binary path** for Selenium Web Driver tests.
- **Latest driver binaries** are automatically downloaded and unpacked.
- **Older driver binaries** can be automatically downloaded and unpacked.
- **All driver binaries are cached** for quicker configuration.

## Requirements
- Java 8

## Installation

#### Repository: [jcenter](https://bintray.com/prashantr/WebDriver-Binary-Downloader/webdriver-binary-downloader)

<a href='https://bintray.com/prashantr/WebDriver-Binary-Downloader/webdriver-binary-downloader?source=watch' alt='Get automatic notifications about new "courgette-jvm" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

#### Maven
````markdown
<dependency>
  <groupId>io.github.prashant-ramcharan</groupId>
  <artifactId>webdriver-binary-downloader</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
````

#### Gradle
````markdown
repositories {
    jcenter()
}

compile 'io.github.prashant-ramcharan:webdriver-binary-downloader:1.2.0'
````

#### Included Dependencies
* commons-io 2.6


#### Currently Supported Driver Binaries
* ChromeDriver
* GeckoDriver
* IEDriver


## Usage

Download the latest binary
````java
WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.CHROME);

WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.FIREFOX);

WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.IEXPLORER);
````

Download an older binary
````java
WebDriverBinaryDownloader.create().downloadBinaryAndConfigure(BrowserType.CHROME, "2.35");
````

Once a binary is downloaded for a specific release, it will remain cached unless its manually deleted. 

Use the strictDownload() method to ensure a new copy is downloaded.
````java
WebDriverBinaryDownloader.create().strictDownload().downloadLatestBinaryAndConfigure(BrowserType.CHROME);
````


## Submitting Issues
For any issues or requests, please submit [here](https://github.com/pramcharan/webdriver-binary-downloader/issues/new)
