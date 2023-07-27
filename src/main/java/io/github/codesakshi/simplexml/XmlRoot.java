package io.github.codesakshi.simplexml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * @author anilalps
 *
 */
public class XmlRoot extends XmlNode {

	/**
	 * Name for the XmlRoot
	 * 
	 * It has nothing to do with XML
	 * 
	 * Used only for identifying this XmlRoot
	 */
	protected String name;

	/**
	 * Get Name
	 * @return Name of the XmlRoot
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Name
	 * @param name - Name of the XmlRoot
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Read a w3c Node to XmlRoot 
	 * @param w3cNode - The w3c Node
	 */
	public void read( Node w3cNode) {

		readNode( w3cNode, this);
	}

	/**
	 * Read XML Document from String 
	 * @param str - Argument string
	 * @throws Exception If read operation fails
	 */
	public void readXmlFromString( String str ) throws Exception {

		InputStream in = new ByteArrayInputStream( str.getBytes( StandardCharsets.UTF_8 ) );
		read( in );
	}

	/**
	 * Read XML Document from Input Stream 
	 * @param in - Input Stream
	 * @throws ParserConfigurationException If any issue with XML Parser Configuration
	 * @throws IOException If any IO errors occur.
	 * @throws SAXException If any parse errors occur.
	 */
	public void read( InputStream in) throws ParserConfigurationException,
		SAXException, IOException  {

		read( in, null);
	}

	/**
	 * Read XML document from an input stream
	 * @param in - An input stream from which the XML document is read
	 * @param name - Name of the XmlRoot ( For identifying the Xml )
	 * @throws ParserConfigurationException If any issue with XML Parser Configuration
	 * @throws IOException If any IO errors occur.
	 * @throws SAXException If any parse errors occur.
	 */
	public void read( InputStream in, String name) throws ParserConfigurationException,
			SAXException, IOException{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments( true);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document document = db.parse(in);

		Node w3cDoc = document.getDocumentElement();

		setTag( w3cDoc.getNodeName() );
		setName( name );
		readNode( w3cDoc, this);

	}
	
	/**
	 * Read w3c Node to XmlNode 
	 * @param w3cNode - w3c Node to be parsed
	 * @param xmlNode - XmlNode created for the given w3cNode
	 */
	protected static void readNode( Node w3cNode, XmlNode xmlNode ) {

		NamedNodeMap w3cAttribMap = w3cNode.getAttributes();

		for( int i = 0; i < w3cAttribMap.getLength(); i++ ) {

			Node w3cAttrib = w3cAttribMap.item(i);

			xmlNode.setAttribute( w3cAttrib.getNodeName(), w3cAttrib.getNodeValue() );
		}

		for( Node w3cChildNode = w3cNode.getFirstChild(); w3cChildNode != null; w3cChildNode = w3cChildNode.getNextSibling() ) {

			switch( w3cChildNode.getNodeType() ) {

			case Node.ATTRIBUTE_NODE:
			default:
				break;

			case Node.ELEMENT_NODE:

				XmlNode childNode = xmlNode.addChild(w3cChildNode.getNodeName() );
				readNode( w3cChildNode, childNode);
				break;

			case Node.TEXT_NODE:

				String nodeValue = w3cChildNode.getNodeValue();

				if( null != nodeValue) {

					nodeValue = nodeValue.trim();

					if( ! nodeValue.isEmpty() ) {

						xmlNode.setValue(nodeValue);
					}
				}

				break;

			case Node.CDATA_SECTION_NODE:

				if( w3cChildNode instanceof CharacterData) {

					CharacterData cd = (CharacterData) w3cChildNode;

					xmlNode.setValue( cd.getData() );
				}

				break;
			}
		}
	}

	/**
	 * Convert the XmlRoot to w3c XML Document
	 * @return w3c XML Document
	 * @throws Exception If XML Tag is not set for any XmlNode.
	 */
	public Document toXmlDocument() throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.newDocument();

		return toXmlDocument( document );
	}

	/**
	 * Add XmlNode to w3c XML Document
	 * @param document - w3c XML Document
	 * @return w3c XML Document
	 * @throws Exception If XML Tag is not set for any XmlNode.
	 */
	public Document toXmlDocument( Document document) throws Exception {

		if( null == getTag() ) {

			throw new Exception( "Xml tag not set for root");
		}

		Element w3cRoot = document.createElement( tag ) ;

		document.appendChild(w3cRoot);

		writeXmlElements( document, w3cRoot, this);

		return document;

	}

	/**
	 * Write XmlNode to w3c XML Document
	 * @param document - w3c XML Document
	 * @param w3cElm - w3c Element
	 * @param xmlNode - XmlNode
	 * @throws Exception If XML Tag is not set for any XmlNode.
	 */
	protected void writeXmlElements(Document document, Element w3cElm, XmlNode xmlNode ) throws Exception {

		for( Map.Entry<String,Object> attrib : xmlNode.getAttributes().entrySet() ) {

			w3cElm.setAttribute( attrib.getKey(), String.valueOf( attrib.getValue() ));
		}

		String value = xmlNode.getValue();

		if( null != value && ! value.isEmpty() ) {

			if( value.contains( "\n" )) {

				w3cElm.appendChild( document.createCDATASection( "\n" + value + "\n"));

			} else {

				w3cElm.appendChild( document.createTextNode(value) );
			}

		} else {

			for( XmlNode child : xmlNode.getChildren() ) {

				if( null == child.getTag() ) {

					throw new Exception( "Xml tag not set for root");
				}

				Element w3cChildElm = document.createElement( child.getTag() ) ;

				w3cElm.appendChild(w3cChildElm);

				writeXmlElements( document, w3cChildElm, child);

			}
		}
	}

	/**
	 * Write the XmlRoot to output stream
	 * @param out - OutputStream for writing
	 * @param omitDeclaration - specify whether to omit the XML declaration at the beginning
	 * @throws Exception if write process fails
	 */
	public void write( OutputStream out, boolean omitDeclaration ) throws Exception {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();

		if( omitDeclaration ) {

			transformer.setOutputProperty("omit-xml-declaration", "yes" );

		}else {

			transformer.setOutputProperty("omit-xml-declaration", "no" );
			transformer.setOutputProperty("version", "1.0" );
			transformer.setOutputProperty("encoding", "UTF-8" );

		}

		transformer.setOutputProperty( "indent", "yes");
		transformer.setOutputProperty( "{http://xml.apache.org/xalan}indent-amount", "4");

		Document document = toXmlDocument();
		DOMSource source = new DOMSource( document );

		StreamResult result = new StreamResult(out) ;
		transformer.transform(source, result);		
	}

	/**
	 * Write the XmlRoot to output stream
	 * @param out - OutputStream for writing
	 * @throws Exception if write process fails.
	 */
	public void write( OutputStream out ) throws Exception {

		write( out, true);
	}

	/**
	 * Convert to String
	 * @return Converted String
	 * @throws Exception If conversion fails
	 */
	public String toXmlString() throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write( out );

		return new String( out.toByteArray(), StandardCharsets.UTF_8 );
	}

}


