# Java Utilities 

## Features
- Cryptography utilities
- JDBC utilities (Named parameters, Array parameters, LIKE_ANY operator, etc.)
- Record framework
- JSON Web Signature/Json Web Token (HS/RS/ES/PS)
- Geography utilities
- ToStringBuilder
- 360+ Unit tests

## Installation

### VERSIONS
- [appslandia-common](https://search.maven.org/search?q=a:appslandia-common)

### Maven
```XML
<dependency>
    <groupId>com.appslandia</groupId>
    <artifactId>appslandia-common</artifactId>
    <version>16.8.0</version>
</dependency>
```

### Gradle
```
dependencies {
   compile 'com.appslandia:appslandia-common:16.8.0'
}
```

## Sample Usage
### ToString Builder
```java
// Print all fields
System.out.println( new ToStringBuilder().toString(any_object));
```
### JDBC Named Parameters
```java
// JdbcSql can be reused
JdbcSql sql = new JdbcSql("SELECT * FROM User WHERE status=:status");
ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource);

try (StatementImpl stat = conn.prepareStatement(sql)) {
  stat.setInt("status", 1); // Named parameter
  //
  stat.executeQuery();
}
```
### JDBC Named Array Parameters
```java
JdbcSql sql = new JdbcSql("SELECT * FROM User WHERE userType IN :types");
ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource);

try (StatementImpl stat = conn.prepareStatement(sql)) {
  stat.setIntArray("types", new int[] {1,2,3});
  //
  stat.executeQuery();
}
```
### JDBC LIKE_ANY
```java
JdbcSql sql = new JdbcSql("SELECT * FROM User WHERE name LIKE_ANY :names");
ConnectionImpl conn = new ConnectionImpl(javax.sql.DataSource);

try (StatementImpl stat = conn.prepareStatement(sql)) {
  stat.setLikeAny("names", new String[] {"a, "b"}); // name LIKE '%a%' OR name LIKE '%b%'
  //
  stat.executeQuery();
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
  }
}
```
### System.getProperty & getenv
```java
 String password = SYS.resolve("${db.password, env.DB_PASSWORD:default_password}")
 // resolving order:  System.getProperty("db.password"),  System.getenv("DB_PASSWORD"), 'default_password'
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
  // JoseJsonb or JoseGson.newJsonProcessor() or your implementation
  JsonProcessor jsonProcessor = JoseGson.newJsonProcessor();
  
  // JwtSigner - HS256/HS384/HS512
  JwtSigner jwtSigner = HsJwtSigner.HS256().setJsonProcessor(jsonProcessor)
  							  .setSecret("secret".getBytes()).setIssuer("Issuer1").build();

  // OR JwtSigner - ES256/ES384/ES512 - RS256/RS384/RS512 - PS256/PS384/PS512
  JwtSigner jwtSigner = DsaJwtSigner.ES256().setJsonProcessor(jsonProcessor)
  							  .setPrivateKey(privateKey).setPublicKey(publicKey).setIssuer("Issuer1").build();

  JoseHeader header = processor.newHeader();
  JwtPayload payload = processor.newPayload().setExpiresIn(1, TimeUnit.DAYS);

  // Sign
  String jwt = jwtSigner.sign(new JwtToken(header, payload));
  
  // Parse
  JwtToken token = jwtSigner.parse(jwt);
  
  // Verify
  jwtSigner.verify(token);
  
  token.getHeader();
  token.getPayload();
```

## License
This code is distributed under the terms and conditions of the [MIT license](LICENSE).
