# ⛔️ DEPRECATED: This project is no longer maintained


[![Build Status](https://travis-ci.org/prashant-ramcharan/webdriver-binary-downloader.svg?branch=master)](https://travis-ci.org/prashant-ramcharan/webdriver-binary-downloader)
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

#### Maven
````xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>

<dependency>
  <groupId>io.github.prashant-ramcharan</groupId>
  <artifactId>webdriver-binary-downloader</artifactId>
  <version>1.3.0</version>
</dependency>
````

#### Gradle
````gradle
repositories {
    jcenter()
}

compile 'io.github.prashant-ramcharan:webdriver-binary-downloader:1.3.0'
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
WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(Browser.CHROME);

WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(Browser.FIREFOX);

WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(Browser.IEXPLORER);
````

Download an older binary
````java
WebDriverBinaryDownloader.create().downloadBinaryAndConfigure(Browser.CHROME, "2.35");
````

Download a binary for a specific architecture
````java
WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(Browser.IEXPLORER, TargetArch.X86);
````

Once a binary is downloaded for a specific release, it will remain cached unless its manually deleted. 

Use the strictDownload() method to ensure a new copy is downloaded.
````java
WebDriverBinaryDownloader.create().strictDownload().downloadLatestBinaryAndConfigure(Browser.CHROME);
````


## Submitting Issues
For any issues or requests, please submit [here](https://github.com/pramcharan/webdriver-binary-downloader/issues/new)
