# Java Utilities

## Features

- Cryptography utilities  
- JDBC utilities (Named parameters, Array parameters, LIKE_ANY operator, etc.)  
- JPA utilities (Array parameters, LIKE_ANY operator, etc.)  
- Record framework for SQL databases  
- JSON Web Signature / JSON Web Token (HS/RS/ES/PS)  
- Geographic utilities (distance, moving, DMS, etc.)  
- CSV Utilities  
- ToStringBuilder  
- 450+ unit tests included  

## Installation

### Java Version

- Java 21+

### Maven

```xml
<dependency>
  <groupId>com.appslandia</groupId>
  <artifactId>appslandia-common</artifactId>
  <version>19.42.0</version>
</dependency>
```

### Gradle

```groovy
dependencies {
  compile 'com.appslandia:appslandia-common:19.42.0'
}
```

## Sample Usage

### ToString Builder

```java
new ToStringBuilder().toString(anyObject);
new ToStringBuilder().setTsDepthLevel(3).toString(anyObject);
new ToStringBuilder().setToOneLine(true).toString(anyObject);
```

### JDBC Named Parameters

```java
// Can be reused/threadsafe
var query = new SqlQuery("SELECT * FROM User WHERE status=:status");

try (var conn = new ConnectionImpl(dataSource)) {
  try (var stat = conn.prepareStatement(query)) {
    stat.setInt("status", 1); // Named parameter
    stat.executeQuery();
  }
}
```

### JDBC Named Array Parameters

```java
var query = new SqlQuery("SELECT * FROM User WHERE type IN :types");
try (var conn = new ConnectionImpl(dataSource)) {
  try (var stat = conn.prepareStatement(query)) {
    stat.setIntArray("types", 1, 2); // type = 1 OR type = 2
    stat.executeQuery();
  }
}
```

### JDBC LIKE_ANY

```java
var query = new SqlQuery("SELECT * FROM User WHERE name LIKE_ANY :names");
try (var conn = new ConnectionImpl(dataSource)) {
  try (var stat = conn.prepareStatement(query)) {
    stat.setLikeAny("names", "a", "b"); // name LIKE '%a%' OR name LIKE '%b%'
    stat.executeQuery();
  }
}
```

### The Best JDBC Utilities

```java
try (var conn = new ConnectionImpl(dataSource)) {
  // conn.executeUpdate, executeScalar, executeSingle, 
  // conn.executeQuery, executeList, executeSet, executeMap,
  // conn.executeStream, etc.
}
```

### Record Framework

```java
try (var connScoped = new ConnectionImpl(dataSource)) {
  try (var db = new RecordContext()) {
    db.insert("UserTable", new DataRecord()
      .set("FirstName", fName)
      .set("LastName", lName)
      .set("Email", email));

    // db.update, delete, getRecord, exists, etc.
  }
}
```

### System.getProperty & getenv

```java
var password = SYS.resolve("{db.password, env.DB_PASSWORD}");
// Resolving order: System.getProperty("db.password"), System.getenv("DB_PASSWORD")
```

### Geography

```java
var loc = new GeoLocation(longitudeX, latitudeY);
var locEast = loc.move(Direction.EAST, 10, DistanceUnit.MILE);

var distanceInMiles = loc.distanceTo(locEast, DistanceUnit.MILE); // ~10 miles
```

### JWT

```java
// Can be reused/threadsafe
var jsonProcessor = JoseGson.newJsonProcessor();

// JwtSigner - HS256/HS384/HS512
var jwtSigner = HsJwtSigner.HS256()
  .setJsonProcessor(jsonProcessor)
  .setSecret("secret".getBytes())
  .setIssuer("Issuer1")
  .build();

// OR JwtSigner - ES256/ES384/ES512 - RS256/RS384/RS512 - PS256/PS384/PS512
jwtSigner = DsaJwtSigner.ES256()
  .setJsonProcessor(jsonProcessor)
  .setPrivateKey(privateKey)
  .setPublicKey(publicKey)
  .setIssuer("Issuer1")
  .build();

var header = jwtSigner.newHeader();
var payload = jwtSigner.newPayload().setExp(1, TimeUnit.DAYS);

var jwt = jwtSigner.sign(new JwtToken(header, payload));
var token = jwtSigner.parse(jwt);
jwtSigner.verify(token);

token.getHeader();
token.getPayload();
```

### CSV Reader

```java
// Can be reused/threadsafe
var csv = new CsvProcessor();

try (var br = IOUtils.readerBOM("input.csv", "UTF-8")) {
  var records = csv.parseRecords(br, record0AsHeader);
  records.get(0).getString(0);

  // getInt(), getBool(), getLocalDate(), ...
}
```

### CSV Export

```java
try (var conn = new ConnectionImpl(dataSource)) {
  var exporter = new CsvExporter();

  exporter.setPQuery("SELECT * FROM User WHERE type=:type");
  exporter.setPQueryParams(new Params().set("type", 1));

  exporter.execute("output.csv");
}
```

## License

This code is distributed under the terms and conditions of the [MIT license](LICENSE).
