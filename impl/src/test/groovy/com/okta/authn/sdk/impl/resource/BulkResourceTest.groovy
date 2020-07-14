/*
 * Copyright 2017 Okta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.authn.sdk.impl.resource

import com.okta.authn.sdk.impl.util.TestUtil
import com.okta.commons.lang.Assert
import com.okta.sdk.impl.ds.InternalDataStore
import com.okta.sdk.impl.resource.AbstractResource
import com.okta.sdk.impl.resource.BooleanProperty
import com.okta.sdk.impl.resource.CharacterArrayProperty
import com.okta.sdk.impl.resource.DateProperty
import com.okta.sdk.impl.resource.EnumProperty
import com.okta.sdk.impl.resource.IntegerProperty
import com.okta.sdk.impl.resource.MapProperty
import com.okta.sdk.impl.resource.Property
import com.okta.sdk.impl.resource.ResourceReference
import com.okta.sdk.impl.resource.StringProperty
import com.okta.sdk.resource.PropertyRetriever
import com.okta.sdk.resource.Resource
import org.testng.TestException
import org.testng.annotations.Test

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import static org.mockito.Mockito.mock

import static com.okta.authn.sdk.impl.util.TestUtil.toResource

/**
 * Reflection based tests for basic getter and setter methods on generated code.  These tests provide very basic
 * coverage and do NOT test anything specific.
 */
class BulkResourceTest {

    /**
     * Sets values via setter methods then validates the value was set by using the corresponding getter method.
     */
    @Test
    void testGetterAndSetters() {
        resourceStream().forEach { resource ->
            setViaSetter(resource)
            validateViaGetter(resource.getClass(), resource)
        }
    }

    /**
     * Sets values via property methods then validates the value was set by using the corresponding getter method.
     */
    @Test
    void testPropertySetThenGetters() {
        resourceStream().forEach { resource ->
            setViaProperty(resource)
            validateViaGetter(resource.getClass(), resource, false)
        }
    }

    /**
     * Tests calling Resource(InternalDataStore) does NOT throw an exception and the internal properties
     * results in an empty map.
     */
    @Test
    void testConstructorsDoNotThrowException() {
        // just make sure this doesn't throw an exception.
        resourceStream().forEach { resource ->
            assertThat resource.getInternalProperties(), anEmptyMap()
        }
    }

    @Test
    void resourceRegisteredPropertiesTest() {
        resourceStream().forEach { resource ->

            SortedMap<String, Property> propertiesFromFields = new TreeMap<>()
            getProperties(resource.getClass()).each {
                it.setAccessible(true)
                Property property = it.get(null)
                propertiesFromFields.put(property.name, property)
            }

            SortedMap<String, Property> propertiesFromDescriptors = new TreeMap<>((Map) resource.getPropertyDescriptors())
            assertThat("Property mismatch in class: ${resource.getClass()}", propertiesFromDescriptors.keySet(), equalTo(propertiesFromFields.keySet()))
        }
    }

    ///////////////////////
    // Test Util methods //
    ///////////////////////

    static Set<Field> getProperties(Class<?> type, Set<Field> properties = new HashSet<>()) {
        properties.addAll(type.getDeclaredFields().findAll {
            Property.isAssignableFrom(it.type) &&
                Modifier.isStatic(it.modifiers) &&
                !it.name.startsWith("NESTED__")
        })

        if (type.getSuperclass() != null) {
            getProperties(type.getSuperclass(), properties)
        }

        return properties
    }

    static Set<Method> getResourceMethods(Class<AbstractResource> type, Closure closure, Set<Method> methods = new HashSet<>()) {
        methods.addAll(type.getDeclaredMethods().findAll(closure))

        Class superClass = type.getSuperclass()
        if (superClass != null && superClass != AbstractResource) {
            getResourceMethods(superClass, closure, methods)
        }

        return methods
    }

    static Stream<AbstractResource> resourceStream() {
        InternalDataStore dataStore = TestUtil.createMockDataStore()
        return clazzStream().map { clazz ->
            (AbstractResource) toResource(clazz, dataStore)
        }
    }

    static Stream<Class> clazzStream() {
        return Arrays.stream(getClasses("com.okta.authn.sdk.impl.resource"))
                .filter { clazz -> clazz.superclass != null}
                .filter { clazz -> AbstractResource.isAssignableFrom(clazz)}
                .filter { clazz -> !clazz.name.contains("Test")}
                .filter { clazz -> !clazz.name.contains("Abstract")}
                .filter { clazz -> clazz.name.contains("Default")}
    }

    static void validateViaGetter(Class clazz, Resource resource, boolean withSettersOnly = true) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

        def fieldGetters = getProperties(resource.getClass()).stream()
                .map {
                    it.accessible = true
                    Property prop = it.get(null)
                    return "get${camelize(prop.name, false)}".toString()
                }
                .collect(Collectors.toSet())

        getResourceMethods(clazz, {
            it.name.startsWith("get") &&
                (!withSettersOnly || setterFromGetter(clazz, it) != null) &&
                (withSettersOnly || fieldGetters.contains(it.name))
        }).forEach {
            def result = it.invoke(resource)
            if (it.returnType == String) {
                assertThat "Checking resource: ${resource.class}", it.invoke(resource), equalTo("value for: ${it.name}".toString())
            }
            else if (it.returnType == Integer){
                assertThat it.invoke(resource), is(42)
            }
            else if (it.returnType == Date) {
                assertThat it.invoke(resource), equalTo(df.parse("2001-07-04T12:08:56.235-0700"))
            }
            else if (it.returnType == Map) {
                assertThat it.invoke(resource), instanceOf(Map)
            }
            else if (it.returnType == List && !withSettersOnly) { // TODO: Lists are only being set via property right now
                assertThat it.invoke(resource), instanceOf(List)
            }
            else if (it.returnType.interfaces.contains(Resource)) {
                assertThat "method ${it.toString()} returned null value.", result, notNullValue()
            }
            else if (it.returnType.isEnum()) {
                assertThat "method ${it.toString()} returned null value.", result, notNullValue()
            }
        }
    }

    static void setViaProperty(def resource) {
        resource.getPropertyDescriptors()
                .forEach { key, property ->
            if (property instanceof StringProperty) {
                resource.setProperty(property, "value for: get${camelize(key, false)}")
            }
            else if (property instanceof IntegerProperty) {
                resource.setProperty(property, 42)
            }
            else if (property instanceof DateProperty) {
                resource.setProperty(property, "2001-07-04T12:08:56.235-0700")
            }
            else if (property instanceof MapProperty) {
                resource.setProperty(property, [one: "two"])
            }
            else if (property instanceof ResourceReference) {
                resource.setProperty(property, [one: "two"])
            }
            else if (property instanceof BooleanProperty) {
                resource.setProperty(property, true)
            }
            else if (property instanceof EnumProperty) {
                def value = property.type.getEnumConstants()[0]
                resource.setProperty(property, value.name())
            }
            else if (property instanceof CharacterArrayProperty) {
                resource.setProperty(property, "secret".toCharArray())
            }
            else {
                throw new TestException("Unknown property type: ${property.getClass()}")
            }
        }
        resource.materialize()
    }

    static Method setterFromGetter(Class clazz, Method method) {
        try {
            return clazz.getMethod(method.name.replaceFirst("get", "set"), method.returnType)
        }
        catch (NoSuchMethodException e) {
            // ignored
        }
        return null
    }

    static void setViaSetter(AbstractResource resource) {
        getResourceMethods(resource.getClass(), {it.name.startsWith("set") && it.getParameterTypes().length == 1 })
            .forEach { method ->

            def type = method.getParameterTypes()[0]
            def value

            if (String.isAssignableFrom(type)) {
                value = "value for: ${method.getName().replaceAll("^set", "get")}".toString()
            } else if (Integer.isAssignableFrom(type)) {
                value = 42
            } else if (Date.isAssignableFrom(type)) {
                value = "2001-07-04T12:08:56.235-0700"
            } else if (Map.isAssignableFrom(type) && !PropertyRetriever.isAssignableFrom(type)) {
                value = [one: "two"]
            } else if (Boolean.isAssignableFrom(type)) {
                value = true
            } else if (List.isAssignableFrom(type)) {
                value = new ArrayList()
            } else if (type.isEnum()) {
                value = type.getEnumConstants()[0]
            } else if (char[].isAssignableFrom(type)) {
                value = "secret".toCharArray()
            } else {
                value = mock(type)
            }

            Object[] args = [value]
            method.invoke(resource, args)
        }
        resource.materialize()
    }

    /**
     * Camelize name (parameter, property, method, etc)
     *
     * @param word string to be camelize
     * @param lowercaseFirstLetter lower case for first letter if set to true
     * @return camelized string
     */
    static String camelize(String word, boolean lowercaseFirstLetter) {

        // Replace all slashes with dots (package separator)
        Pattern p = Pattern.compile("\\/(.?)")
        Matcher m = p.matcher(word)
        while (m.find()) {
            word = m.replaceFirst("." + m.group(1)/*.toUpperCase()*/) // FIXME: a parameter should not be assigned. Also declare the methods parameters as 'final'.
            m = p.matcher(word)
        }

        // case out dots
        String[] parts = word.split("\\.")
        StringBuilder f = new StringBuilder()
        for (String z : parts) {
            if (z.length() > 0) {
                f.append(Character.toUpperCase(z.charAt(0))).append(z.substring(1))
            }
        }
        word = f.toString()

        m = p.matcher(word)
        while (m.find()) {
            word = m.replaceFirst("" + Character.toUpperCase(m.group(1).charAt(0)) + m.group(1).substring(1)/*.toUpperCase()*/)
            m = p.matcher(word)
        }

        // Uppercase the class name.
        p = Pattern.compile('''(\\.?)(\\w)([^\\.]*)$''')
        m = p.matcher(word)
        if (m.find()) {
            String rep = m.group(1) + m.group(2).toUpperCase() + m.group(3)
            rep = rep.replaceAll('''\\$''', '''\\\\\\$''')
            word = m.replaceAll(rep)
        }

        // Remove all underscores
        p = Pattern.compile("(_)(.)")
        m = p.matcher(word)
        while (m.find()) {
            word = m.replaceFirst(m.group(2).toUpperCase())
            m = p.matcher(word)
        }

        if (lowercaseFirstLetter && word.length() > 0) {
            word = word.substring(0, 1).toLowerCase() + word.substring(1)
        }

        word = word.replace('#', '')
        word = word.replace('$', '')

        return word
    }


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader()
        assert classLoader != null
        String path = packageName.replace('.', '/')
        Enumeration<URL> resources = classLoader.getResources(path)
        List<File> dirs = new ArrayList<File>()
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement()
            dirs.add(new File(resource.getFile()))
        }
        ArrayList<Class> classes = new ArrayList<Class>()
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName))
        }
        return classes.toArray(new Class[classes.size()])
    }

/**
 * Recursive method used to find all classes in a given directory and subdirs.
 *
 * @param directory   The base directory
 * @param packageName The package name for classes found inside the base directory
 * @return The classes
 * @throws ClassNotFoundException
 */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {

//        return Collections.singletonList(DefaultFactor)

        List<Class> classes = new ArrayList<Class>()
        if (!directory.exists()) {
            return classes
        }
        File[] files = directory.listFiles()
        for (File file : files) {
            if (file.isDirectory()) {
                Assert.isTrue(!file.getName().contains("."), "Expected directory containing '.' in path")
                classes.addAll(findClasses(file, packageName + "." + file.getName()))
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)))
            }
        }
        return classes
    }

}
