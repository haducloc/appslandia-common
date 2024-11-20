# Java Utilities

## Features

- Cryptography utilities
- JDBC utilities (Named parameters, Array parameters, LIKE_ANY operator, etc.)
- JPA utilities (Array parameters, LIKE_ANY operator, etc.)
- Record framework for SQL databases
- JSON Web Signature/Json Web Token (HS/RS/ES/PS)
- Geographic utilities (distance, moving, DMS, etc.)
- CSV Utilities
- ToStringBuilder

## Installation

### VERSIONS

- [appslandia-common](https://search.maven.org/search?q=a:appslandia-common)
- Java 21

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

### ToString Builder

```java
new ToStringBuilder().toString(anyObject);
new ToStringBuilder().setTsDepthLevel(3).toString(anyObject);
new ToStringBuilder().setToOneLine(true).toString(anyObject);
```

### JDBC Named Parameters

```java
// Can be reused / thread safe
final SqlQuery query = new SqlQuery("SELECT * FROM User WHERE status=:status");
try (ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource)) {

  try (StatementImpl stat = conn.prepareStatement(query)) {
    stat.setInt("status", 1); // Named parameter
    //
    stat.executeQuery();
  }
}
```

### JDBC Named Array Parameters

```java
final SqlQuery query = new SqlQuery("SELECT * FROM User WHERE type IN :types");
try (ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource)) {

  try (StatementImpl stat = conn.prepareStatement(query)) {
    // type = 1 OR type = 2
    stat.setIntArray("types", 1, 2);
    //
    stat.executeQuery();
  }
}
```

### JDBC LIKE_ANY

```java
final SqlQuery query = new SqlQuery("SELECT * FROM User WHERE name LIKE_ANY :names");
try (ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource)) {

  try (StatementImpl stat = conn.prepareStatement(query)) {
    // name LIKE '%a%' OR name LIKE '%b%'
    stat.setLikeAny("names", "a", "b");
    //
    stat.executeQuery();
  }
}
```

### THE BEST JDBC Utilities

```java
try (ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource)) {
  // conn.executeUpdate, executeScalar, executeSingle, 
  // conn.executeQuery, executeList, executeSet, executeMap
  // conn.executeSteam, etc. 
}
```

### Record Framework

```java
try (ConnectionImpl connScoped = new ConnectionImpl(javax.sql.DataSource)) {
  try (RecordContext db = new RecordContext()) {

    // Insert an user into the User table.
    db.insert("UserTable", new DataRecord().set("FirstName", fName)
                                       .set("LastName", lName)
                                       .set("Email", email));
    // db.update, delete, getRecord, exists, etc.
  }
}
```

### System.getProperty & getenv

```java
String password = SYS.resolve("{db.password, env.DB_PASSWORD}")
// resolving order:  System.getProperty("db.password"),  System.getenv("DB_PASSWORD")
```

### Geography

```java
GeoLocation loc = new GeoLocation(longitudeX, latitudeY);
GeoLocation loc_east = loc.move(Direction.EAST, 10, DistanceUnit.MILE);
//
double distanceInMiles = loc.distanceTo(loc_east, DistanceUnit.MILE); // ~10 miles
```

### JWT

```java
// JoseJsonb or JoseGson.newJsonProcessor() or your implementation
JsonProcessor jsonProcessor = JoseGson.newJsonProcessor();

// JwtSigner - HS256/HS384/HS512
JwtSigner jwtSigner = 
          HsJwtSigner.HS256().setJsonProcessor(jsonProcessor)
                     .setSecret("secret".getBytes()).setIssuer("Issuer1").build();

// OR JwtSigner - ES256/ES384/ES512 - RS256/RS384/RS512 - PS256/PS384/PS512
JwtSigner jwtSigner = 
          DsaJwtSigner.ES256().setJsonProcessor(jsonProcessor)
                      .setPrivateKey(privateKey).setPublicKey(publicKey).setIssuer("Issuer1").build();

JoseHeader header = jwtSigner.newHeader();
JwtPayload payload = jwtSigner.newPayload().setExp(1, TimeUnit.DAYS);

// Sign
String jwt = jwtSigner.sign(new JwtToken(header, payload));

// Parse
JwtToken token = jwtSigner.parse(jwt);

// Verify
jwtSigner.verify(token);

token.getHeader();
token.getPayload();
```

### CSV Reader

```java
// Can be reused/thread safe
CsvProcessor csv = new CsvProcessor();

// BufferedReader
BufferedReader br = IOUtils.readerBOM("input.csv", "UTF-8");

List<CsvRecord> records = csv.parseRecords(br, record0AsHeader);
records.get(0).getString(0);

// getInt(), getBool(), getLocalDate(), ...
```

### CSV Export

```java
try (ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource)) {

  CsvExporter exporter = new CsvExporter();
  exporter.setConnection(conn);
  exporter.setCsvOutput("output.csv", "UTF-8");

  // Set query and parameters
  exporter.setPQuery("SELECT * FROM User WHERE type=:type");
  exporter.setPQueryParams(new Params().set("type", 1));

  // Export CSV
  exporter.execute();
}
```

## License

This code is distributed under the terms and conditions of the [MIT license](LICENSE).