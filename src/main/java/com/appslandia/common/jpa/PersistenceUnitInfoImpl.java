// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jpa;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

/**
 *
 * @author Loc Ha
 *
 */
public class PersistenceUnitInfoImpl extends InitializeObject implements PersistenceUnitInfo {

  private String unitName;
  private String providerClassName;
  private PersistenceUnitTransactionType transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;

  private DataSource jtaDataSource;
  private DataSource nonJtaDataSource;

  private List<String> managedClassNames = new ArrayList<>();
  private List<String> mappingFileNames = new ArrayList<>();
  private List<URL> jarFileUrls = new ArrayList<>();

  private URL unitRootUrl;
  private boolean excludeUnlistedClasses = true;

  private SharedCacheMode sharedCacheMode;
  private ValidationMode validationMode;
  private Properties properties = new Properties();

  private String xmlSchemaVersion;
  private ClassLoader classLoader;
  private ClassLoader newTempClassLoader;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.unitName);
    Arguments.notNull(this.providerClassName);
    Arguments.notNull(this.transactionType);
  }

  @Override
  public String getPersistenceUnitName() {
    this.initialize();
    return this.unitName;
  }

  public PersistenceUnitInfoImpl setPersistenceUnitName(String unitName) {
    this.assertNotInitialized();
    this.unitName = unitName;
    return this;
  }

  @Override
  public String getPersistenceProviderClassName() {
    this.initialize();
    return this.providerClassName;
  }

  public PersistenceUnitInfoImpl setPersistenceProviderClassName(String providerClassName) {
    this.assertNotInitialized();
    this.providerClassName = providerClassName;
    return this;
  }

  @Override
  public PersistenceUnitTransactionType getTransactionType() {
    this.initialize();
    return this.transactionType;
  }

  public PersistenceUnitInfoImpl setTransactionType(PersistenceUnitTransactionType transactionType) {
    this.assertNotInitialized();
    this.transactionType = transactionType;
    return this;
  }

  @Override
  public DataSource getJtaDataSource() {
    this.initialize();
    return this.jtaDataSource;
  }

  public PersistenceUnitInfoImpl setJtaDataSource(DataSource jtaDataSource) {
    this.assertNotInitialized();
    this.jtaDataSource = jtaDataSource;
    return this;
  }

  @Override
  public DataSource getNonJtaDataSource() {
    this.initialize();
    return this.nonJtaDataSource;
  }

  public PersistenceUnitInfoImpl setNonJtaDataSource(DataSource nonJtaDataSource) {
    this.assertNotInitialized();
    this.nonJtaDataSource = nonJtaDataSource;
    return this;
  }

  @Override
  public List<String> getMappingFileNames() {
    this.initialize();
    return this.mappingFileNames;
  }

  public PersistenceUnitInfoImpl addMappingFileName(String mappingFileName) {
    this.assertNotInitialized();
    this.mappingFileNames.add(mappingFileName);
    return this;
  }

  @Override
  public List<URL> getJarFileUrls() {
    this.initialize();
    return this.jarFileUrls;
  }

  public PersistenceUnitInfoImpl addJarFileUrl(URL jarFileUrl) {
    this.assertNotInitialized();
    this.jarFileUrls.add(jarFileUrl);
    return this;
  }

  @Override
  public URL getPersistenceUnitRootUrl() {
    this.initialize();
    return this.unitRootUrl;
  }

  public PersistenceUnitInfoImpl setPersistenceUnitRootUrl(URL unitRootUrl) {
    this.assertNotInitialized();
    this.unitRootUrl = unitRootUrl;
    return this;
  }

  @Override
  public List<String> getManagedClassNames() {
    this.initialize();
    return this.managedClassNames;
  }

  public PersistenceUnitInfoImpl addManagedClassName(String managedClassName) {
    this.assertNotInitialized();
    this.managedClassNames.add(managedClassName);
    return this;
  }

  @Override
  public boolean excludeUnlistedClasses() {
    this.initialize();
    return this.excludeUnlistedClasses;
  }

  public PersistenceUnitInfoImpl setExcludeUnlistedClasses(boolean excludeUnlistedClasses) {
    this.assertNotInitialized();
    this.excludeUnlistedClasses = excludeUnlistedClasses;
    return this;
  }

  @Override
  public SharedCacheMode getSharedCacheMode() {
    this.initialize();
    return this.sharedCacheMode;
  }

  public PersistenceUnitInfoImpl setSharedCacheMode(SharedCacheMode sharedCacheMode) {
    this.assertNotInitialized();
    this.sharedCacheMode = sharedCacheMode;
    return this;
  }

  @Override
  public ValidationMode getValidationMode() {
    this.initialize();
    return this.validationMode;
  }

  public PersistenceUnitInfoImpl setValidationMode(ValidationMode validationMode) {
    this.assertNotInitialized();
    this.validationMode = validationMode;
    return this;
  }

  @Override
  public Properties getProperties() {
    this.initialize();
    return this.properties;
  }

  public PersistenceUnitInfoImpl setProperties(Properties properties) {
    this.assertNotInitialized();
    this.properties = properties;
    return this;
  }

  @Override
  public String getPersistenceXMLSchemaVersion() {
    this.initialize();
    return this.xmlSchemaVersion;
  }

  public PersistenceUnitInfoImpl setPersistenceXMLSchemaVersion(String xmlSchemaVersion) {
    this.assertNotInitialized();
    this.xmlSchemaVersion = xmlSchemaVersion;
    return this;
  }

  @Override
  public ClassLoader getClassLoader() {
    this.initialize();
    return this.classLoader;
  }

  public PersistenceUnitInfoImpl setClassLoader(ClassLoader classLoader) {
    this.assertNotInitialized();
    this.classLoader = classLoader;
    return this;
  }

  @Override
  public ClassLoader getNewTempClassLoader() {
    this.initialize();
    return this.newTempClassLoader;
  }

  public PersistenceUnitInfoImpl setNewTempClassLoader(ClassLoader newTempClassLoader) {
    this.assertNotInitialized();
    this.newTempClassLoader = newTempClassLoader;
    return this;
  }

  @Override
  public void addTransformer(ClassTransformer transformer) {
    this.assertNotInitialized();
  }
}
