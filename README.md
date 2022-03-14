# SeamlessPay Java client library

The official [SeamlessPay][seamlesspay] Java client library.

## Installation

### Requirements

- Java 1.8 or later
- [Google Gson][gson]

### Install jar-file

Download the SeamlessPay JAR from <https://github.com/seamlesspay/seamlesspay-java/releases/latest>

and install it to `libs` folder at the root of the project.

### Gradle users

Add this dependency to your project's build file:

```groovy
implementation files('libs/seamlesspay-java-0.0.1.jar')
```

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.seamlesspay</groupId>
    <artifactId>seamlesspay-java</artifactId>
    <version>0.0.1</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/seamlesspay-java-0.0.1.jar</systemPath>
</dependency>
```

### [ProGuard][proguard]

If you're planning on using ProGuard, make sure that you exclude the Seamless Payments
client library. You can do this by adding the following to your `proguard.cfg`
file:

```
-keep class com.seamlesspay.** { *; }
```

## Usage

Example.java

```java
import com.seamlesspay.exception.SPException;
import com.seamlesspay.model.Charge;
import com.seamlesspay.param.ChargeCreateParams;

public class SeamlessPayExample {

    public static void main(String[] args) {
        SPAPI.apiKey = "sk_01...";
        SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);

        ChargeCreateParams params = ChargeCreateParams.builder()
                .amount("1.00")
                .token(VALID_TOKEN)
                .build();

        Charge charge = Charge.create(params);
        System.out.println("charge=" + charge);
    }
}
```

See the project's [functional tests][functional-tests] for more examples.

### Per-request Configuration

All of the request methods accept an optional `RequestOptions` object. This is
used if you want to set an [idempotency key][idempotency-keys] or if you want to pass the secret API
key on each method.

```java
RequestOptions requestOptions = RequestOptions.builder()
        .setApiKey("sk_test_...")
        .setIdempotencyKey("a1b2c3...")
        .setSeamlessPayAccount("...")
        .build();

ChargeCollection list = Charge.list();
ChargeCollection list2 = Charge.list(requestOptions);
```

### Configuring automatic retries

The library can be configured to automatically retry requests that fail due to
an intermittent network problem or other knowingly non-deterministic errors.
This can be enabled globally:

```java
SPAPI.setMaxNetworkRetries(2);
```

Or on a finer grain level using `RequestOptions`:

```java
RequestOptions options = RequestOptions.builder()
    .setMaxNetworkRetries(2)
    .build();
Customer.create(params, options);
```

[Idempotency keys][idempotency-keys] are added to requests to guarantee that
retries are safe.

### Configuring Timeouts

Connect and read timeouts can be configured globally:

```java
SPAPI.setConnectTimeout(30 * 1000); // in milliseconds
SPAPI.setReadTimeout(80 * 1000);
```

Or on a finer grain level using `RequestOptions`:

```java
RequestOptions options = RequestOptions.builder()
    .setConnectTimeout(30 * 1000) // in milliseconds
    .setReadTimeout(80 * 1000)
    .build();
Charge.create(params, options);
```

Please take care to set conservative read timeouts. Some API requests can take
some time, and a short timeout increases the likelihood of a problem within our
servers.

## Development

To run all checks (tests and code formatting):

```sh
./gradlew check
```

To run the tests:

```sh
./gradlew test
```

You can run particular tests by passing `--tests Class#method`. Make sure you
use the fully qualified class name. For example:

```sh
./gradlew test --tests com.seamlesspay.functional.charge.ChargeCreateTest
./gradlew test --tests com.seamlesspay.functional.charge.ChargeCreateTest.testCreatesChargeIfSuccess
```

The library uses [Project Lombok][lombok]. While it is not a requirement, you
might want to install a [plugin][lombok-plugins] for your favorite IDE to
facilitate development.


## Logging

SeamlessPay Java uses JUL API. By default it takes configuration from the `resources/logging.properties` file embedded into the jar.
You can replace defulat configuration with your own by adding `logging.properties` to your `resources` folder.

Here are major configuration options
```properties
# specifies list of log handlers
handlers=java.util.logging.ConsoleHandler

# each handler has its own log level separate from logger level
java.util.logging.ConsoleHandler.level=FINER

# specify global log level
.level= FINE

# specify log level for the SeamlessPay java library
com.seamlesspay.level=FINE

# customize log messages format
java.util.logging.SimpleFormatter.format=%1$tF %1$tT.%1$tL %4$s %3$s: %5$s%6$s%n
```
See [logging configuration] for more configuration options. See [log messages formatting] for more details.

[functional-tests]: https://github.com/seamlesspay/seamlesspay-java/tree/main/src/test/java/com/seamlesspay/functional
[gson]: https://github.com/google/gson
[idempotency-keys]: https://docs.seamlesspay.com/2020-08-01/#section/Idempotent-Requests
[lombok]: https://projectlombok.org
[lombok-plugins]: https://projectlombok.org/setup/overview
[proguard]: https://www.guardsquare.com/en/products/proguard
[seamlesspay]: https://seamlesspay.com
[logging configuration]: https://docs.oracle.com/cd/E57471_01/bigData.100/data_processing_bdd/src/rdp_logging_config.html
[log messages formatting]: https://docs.oracle.com/javase/7/docs/api/java/util/logging/SimpleFormatter.html#format(java.util.logging.LogRecord)
