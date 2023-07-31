
# Simple XML

This small Java library allows you to create and parse xml in an easy way. This library will convert xml to easily accessible Map and List Objects. 

Feature requests are always welcome. Just open an issue. Also, if my documentation is missing something or unclear, please let me know.

## Features
* Represents XML elements in tree structure for easy traversal.
* Uses java.util.List and java.util.Map to store XML Tags and Attributes.
* imports, modifies or creates XML from scratch.
* Can read and write XML Using Stream, String or org.w3c.dom.Document.
* Perform simple or complex searches with simple XPATH like queries.

## Setup

Include the Maven artifact:

```xml
	
	<!-- https://mvnrepository.com/artifact/io.github.codesakshi/simple-xml -->
	<dependency>
	    <groupId>io.github.codesakshi</groupId>
	    <artifactId>simple-xml</artifactId>
	    <version>1.0.3</version>
	</dependency>

```

Or include the [JAR](https://mvnrepository.com/artifact/io.github.codesakshi/simple-xml/latest) in your project

## Usage Examples

### 1. Creating XML

```java

	String create() throws Exception {
		
		XmlRoot root = new XmlRoot("RootTag");
		root.setAttribute("created", "Today");
		
		XmlNode livingOnEarth = root.addChild("Living");
		livingOnEarth.setAttribute("living-on", "Earth");
		livingOnEarth.setAttribute("distance", 0);
		
		XmlNode humanNode = livingOnEarth.addChild("Human");
		
		XmlNode aliceNode = humanNode.addChild("Name");
		aliceNode.setValue( "Alice");
		aliceNode.setAttribute("Gender", "Female");
		
		XmlNode bobNode = humanNode.addChild("Name");
		bobNode.setValue( "Bob");
		bobNode.setAttribute("Gender", "Male");
		
		humanNode.addChild("Food", "Salad");
		humanNode.addChild("Food", "Pizza");
		
		XmlNode catNode = livingOnEarth.addChild("Cats");
		
		XmlNode billyNode = catNode.addChild("Name", "Billy");
		billyNode.setAttribute("Gender", "Female");
		
		XmlNode miloNode = catNode.addChild("Name", "Milo");
		miloNode.setAttribute("Gender", "Male");

		catNode.addChild("Food", "Chicken");
		
		XmlNode livingMars = root.addChild("Living");
		livingMars.setAttribute("living-on", "Mars");
		livingMars.setAttribute("distance", 100000);
		
		XmlNode alienNode = livingMars.addChild("Alien");
		
		XmlNode borisAlien = alienNode.addChild("Name", "Boris Alien");
		borisAlien.setAttribute("Gender", "Alien Man");
		
		return root.toXmlString();
	}
```

Above code will produce following XML as output.

```xml
	<RootTag created="Today">
	    <Living distance="0" living-on="Earth">
	        <Human>
	            <Name Gender="Female">Alice</Name>
	            <Name Gender="Male">Bob</Name>
	            <Food>Salad</Food>
	            <Food>Pizza</Food>
	        </Human>
	        <Cats>
	            <Name Gender="Female">Billy</Name>
	            <Name Gender="Male">Milo</Name>
	            <Food>Chicken</Food>
	        </Cats>
	    </Living>
	    <Living distance="100000" living-on="Mars">
	        <Alien>
	            <Name Gender="Alien Man">Boris Alien</Name>
	        </Alien>
	    </Living>
	</RootTag>

```

### 2. Parsing XML

Suppose you have above mentioned xml content in a String 'xmlString'


#### 1. Get attribute

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	String created = root.getAttributeString("created");
	
	System.out.println( created );

```

Above code will print the value 'Today'.

#### 2. Get First Child using tag name.

``` java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	XmlNode livingNode = root.getFirstChildByTagName("Living") ;
	
	String livingOn = livingNode.getAttributeString("living-on");
	
	System.out.println( livingOn );
		
```

Above code will print the value 'Earth'.

#### 3. Get a child XmlNode using tag, attribute key and attribute value

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	XmlNode livingNode = root.getFirstChildByTagAndAttribute("Living", "living-on", "Mars") ;
	
	String distance = livingNode.getAttributeString("distance");
	
	System.out.println(  distance );

```

Above code will print the value '100000'.


#### 4. Create new XML with children of given XmlNode.

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	XmlNode livingNode = root.getFirstChildByTagName("Living") ;
			
	List<XmlNode> livingThings = livingNode.getChildren();
	
	XmlRoot newRoot = new XmlRoot("LivingThings");
	newRoot.getChildren().addAll(livingThings);
	
	System.out.println( newRoot.toXmlString() );
		
```

Above code will produce new XML with children of "Living" tag.

```xml

	<LivingThings>
	    <Human>
	        <Name Gender="Female">Alice</Name>
	        <Name Gender="Male">Bob</Name>
	        <Food>Salad</Food>
	        <Food>Pizza</Food>
	    </Human>
	    <Cats>
	        <Name Gender="Female">Billy</Name>
	        <Name Gender="Male">Milo</Name>
	        <Food>Chicken</Food>
	    </Cats>
	</LivingThings>

```

#### 5. Get children of given XmlNode using tag name.

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	XmlNode humanNode = root.getFirstChildByTagName("Living").getFirstChildByTagName("Human");
	
	List<XmlNode> nameList = humanNode.getChildrenByTagName("Name");
	
	XmlRoot newRoot = new XmlRoot("HumanNames");
	newRoot.getChildren().addAll(nameList);
	
	System.out.println( newRoot.toXmlString() );
		
```

Above code will produce new XML with all "Name" tags under "Human" tag.

```xml

	<HumanNames>
	    <Name Gender="Female">Alice</Name>
	    <Name Gender="Male">Bob</Name>
	</HumanNames>

```

#### 6. Get children of given XmlNode recursively using tag name.

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	List<XmlNode> nameList = root.getChildrenRecursiveByTagName("Name");
	
	XmlRoot newRoot = new XmlRoot("AllNames");
	newRoot.getChildren().addAll(nameList);
	
	System.out.println( newRoot.toXmlString() );
		
```

Above code will produce new XML with all "Name" tags under root tag.

```xml

	<AllNames>
	    <Name Gender="Female">Alice</Name>
	    <Name Gender="Male">Bob</Name>
	    <Name Gender="Female">Billy</Name>
	    <Name Gender="Male">Milo</Name>
	    <Name Gender="Alien Man">Boris Alien</Name>
	</AllNames>

```

#### 7. Get children of given XmlNode using path. 

If the path starts with '/' it will start searching from current XmlNode. else will search from children.


```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	List<XmlNode> nodeList = root.getChildrenByPath("/RootTag/Living/Human/Food");
	
	XmlRoot newRoot = new XmlRoot("HumanFoods");
	newRoot.getChildren().addAll(nodeList);
	
	System.out.println( newRoot.toXmlString() );
		
```

Above code will produce new XML with all "Food" tags under "Human" tag.

```xml

	<HumanFoods>
	    <Food>Salad</Food>
	    <Food>Pizza</Food>
	</HumanFoods>

```

#### 8. Get children of given XmlNode using path. ( start search from children ).

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);

	List<XmlNode> nodeList = root.getChildrenByPath("Living/Human/Food");
	
	XmlRoot newRoot = new XmlRoot("HumanFoods");
	newRoot.getChildren().addAll(nodeList);
	
	System.out.println( newRoot.toXmlString() );
		
```
Above code will produce new XML with all "Food" tags under "Human" tag.

```xml

	<HumanFoods>
	    <Food>Salad</Food>
	    <Food>Pizza</Food>
	</HumanFoods>

```
#### 9. Get children of given XmlNode using multiple path. ( Use '|' symbol to separate multiple paths ).

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	List<XmlNode> nodeList = root.getChildrenByPath("Living/Human/Food | Living/Cats/Food");
	
	XmlRoot newRoot = new XmlRoot("HumanAndCatFoods");
	newRoot.getChildren().addAll(nodeList);
	
	System.out.println( newRoot.toXmlString() );
		
```
Above code will produce new XML with all "Food" tags under "Human" tag and "Cat" tag.

```xml
	<HumanAndCatFoods>
	    <Food>Salad</Food>
	    <Food>Pizza</Food>
	    <Food>Chicken</Food>
	</HumanAndCatFoods>

```
#### 10. Get children of given XmlNode using wild card path. ( Use '*' symbol to match all nodes ).

```java

	XmlRoot root = new XmlRoot();
	root.readXmlFromString(xmlString);
	
	List<XmlNode> nodeList = root.getChildrenByPath("Living/*/Food");
	
	XmlRoot newRoot = new XmlRoot("AllFoods");
	newRoot.getChildren().addAll(nodeList);
	
	System.out.println( newRoot.toXmlString() );
		
```

Above code will produce new XML with all "Food" tags under "Human" tag and "Cat" tag.

```xml
	<HumanOrCatFoods>
	    <Food>Salad</Food>
	    <Food>Pizza</Food>
	    <Food>Chicken</Food>
	</HumanOrCatFoods>

```

