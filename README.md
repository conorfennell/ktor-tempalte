# Ktor template repository


## Setup quickly on developer machine
Requires the following to run locally:
* docker  
* gradle

```sh
docker-compose up # Starts up Postgres locally and binds to port 5432
gradle hydrate-database # Creates tables in local postgres database
gradle run # Starts service and binds to port 5432
```

### Local developer Setup

Java
```sh
brew tap AdoptOpenJDK/openjdk
brew cask install adoptopenjdk11
```

Gradle
```sh
brew install gradle
gradle build
```

Run locally
```sh
docker-compose up
gradle hydrate-database
gradle run
```

Docker
```sh
docker build -t ktor-template . 
docker run -p 8000:8000 ktor-template
```

### Run
```sh
gradle run
```

### Linting the code
```sh
gradle ktlintCheck
gradle ktlintFormat
```

### Running tests
```sh
gradle test
```
