# Created for Applitools hackathon
https://go.applitools.com/hackathon-apply

# How to run
### Prerequisites
* JDK >= 11 installed
* Set your APPLITOOLS_API_KEY environment variable
    * Mac:
        ```
        export APPLITOOLS_API_KEY='YOUR_API_KEY'
        ```
    * Windows:
        ```
        set APPLITOOLS_API_KEY='YOUR_API_KEY'
        ```
  
### Run from command line
Traditional tests
```shell script
./gradlew test --tests TraditionalTests
```
VisualAI tests
```shell script
./gradlew test --tests VisualAITests
```
