# Java Utilities 

## Features
- Cryptography utilities
- JDBC utilities (Named parameters, Array parameters, LIKE_ANY operator, etc.)
- EasyRecord framework
- DI framework
- Type formatters/parsers with localization
- Java bean validators
- GSON adapters
- JSON Web Token
- Geography utilities
- ToStringBuilder
- 400+ Unit tests

## Installation

### Maven
```XML
<dependency>
    <groupId>com.appslandia</groupId>
    <artifactId>appslandia-common</artifactId>
    <version>{LATEST_VERSION}</version>
</dependency>
```

### Gradle
```
dependencies {
   compile 'com.appslandia:appslandia-common:{LATEST_VERSION}'
}
```

## Sample Usage
### JDBC Array
```java
Sql cmd = new Sql("SELECT * FROM User WHERE status=:status AND userType IN :types");

try (StatementImpl stat = new StatementImpl(connection, cmd)) {
  stat.setInt("status", 1); // Named parameter
  stat.setIntArray("types", new int[] {1,2,3}); // Named array parameter
  //
  stat.executeQuery();
}
```
### JDBC LIKE_ANY
```java
Sql cmd = new Sql("SELECT * FROM User WHERE name LIKE_ANY :names");

try (StatementImpl stat = new StatementImpl(connection, cmd)) {
  stat.setLikeAny("names", new String[] {"a, "b"}); // name LIKE '%a%' OR name LIKE '%b%'
  //
  stat.executeQuery();
}
```
### System.getProperty & getenv
```java
 String password = SYS.resolveExpr("${db.password, env.DB_PASSWORD:default_password}")
 // resolving order:  System.getProperty("db.password"),  System.getenv("DB_PASSWORD"), default_password
```
### Geography
```java
 GeoLocation loc = new GeoLocation(lat, long);
 GeoLocation loc_east = loc.move(Direction.EAST, 10, DistanceUnit.MILE);
 //
 double distanceInMiles = loc.distanceTo(loc_east, DistanceUnit.MILE); // ~10 miles
```
### JWT
```java
  // JwtProcessor
  JwtProcessor processor = new JwtProcessor().setIssuer("Issuer1");

  GsonBuilder gsonBuilder = GsonProcessor.newBuilder()
				.registerTypeAdapter(JwtHeader.class, new GsonMapDeserializer<>((m) -> new JwtHeader(m)))
				.registerTypeAdapter(JwtPayload.class, new GsonMapDeserializer<>((m) -> new JwtPayload(m)));

  // GsonProcessor or your JsonProcessor
  processor.setJsonProcessor(new GsonProcessor().setBuilder(gsonBuilder));
  
  processor.setJwtSigner(new JwtSigner().setAlg("HS256")
  				.setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret")));

  JwtHeader header = processor.newHeader();
  JwtPayload payload = processor.newPayload().setExpiresIn(1, TimeUnit.DAYS);

  // Serialize
  String jwt = processor.toJwt(new JwtToken(header, payload));
  
  // Deserialize
  JwtToken token = processor.parseJwt(jwt);
  token.getHeader(); token.getPayload();
```
## Questions?
Please feel free to contact me if you have any questions or comments.
Email: haducloc13@gmail.com

## License
This code is distributed under the terms and conditions of the [MIT license](LICENSE).
