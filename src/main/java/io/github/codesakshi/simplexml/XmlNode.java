package io.github.codesakshi.simplexml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author anilalps
 *
 */
public class XmlNode {

	/**
	 *  Store the XML Tag of the xmlNode
	 */
	protected String tag;

	/**
	 * Store the XML TEXT Value of the xmlNode
	 */
	protected String value;

	/**
	 * Store Attributes of the xml xmlNode
	 */
	protected Map<String,Object> attributes = new HashMap<String,Object>();

	/**
	 * Store XML Child Nodes 
	 */
	protected List<XmlNode> children = new ArrayList<XmlNode>();

	/**
	 * Get tag of the XmlNode
	 * @return Tag of the XmlNode
	 */
	public String getTag() {
		return tag;
	}

	/**
	 *  Set tag of the XmlNode
	 * @param tag - tag of the XmlNode
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 *  Return value of the XmlNode
	 *  
	 * @return Value of the XmlNode
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set value of the XmlNode
	 * @param value - Value of the XmlNode
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Return attribute value for a give key
	 * 
	 * @param key - Key for the attribute
	 * 
	 * @return Value of the attribute if present. null otherwise. 
	 */
	public Object getAttribute( String key ) {

		return attributes.get( key );
	}

	/**
	 * Return attribute value in String format for a given key
	 * 
	 * @param key - Key for the attribute
	 * @return Attribute for the given key. null if attribute is not present
	 */
	public String getAttributeString( String key ) {

		return String.valueOf( attributes.get( key ) );
	}	

	/**
	 * Get all Attributes 
	 * @return Attribute map
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Set attribute value for a given key
	 * @param key - Attribute key 
	 * @param value - Attribute value
	 */
	public void setAttribute( String key, Object value) {
		this.attributes.put(key, value);
	}

	/**
	 * Get children for an Xml xmlNode
	 * @return List of children
	 */
	public List<XmlNode> getChildren() {
		return children;
	}

	/**
	 * Add given XmlNode as child of this xmlNode
	 * @param xmlNode - xmlNode to be added as child
	 */
	public void addChild( XmlNode xmlNode ) {
		this.children.add(xmlNode);
	}

	/**
	 * Add child XmlNode using the given tag
	 * @param tag - Tag of the new XmlNode to be added
	 * @return Added child XmlNode
	 */
	public XmlNode addChild( String tag ) {

		XmlNode xmlNode = new XmlNode();
		xmlNode.setTag( tag );
		addChild( xmlNode );
		return xmlNode;
	}

	/**
	 * Add Child XmlNode using the given tag and value.
	 * @param tag - Tag of the XmlNode  
	 * @param value - Value of the XmlNode 
	 * @return Added child XmlNode
	 */
	public XmlNode addChild( String tag, String value) {

		XmlNode xmlNode = addChild( tag );
		xmlNode.setValue(value);
		return xmlNode;
	}

	/**
	 * Return Immediate children XmlNodes for which the tag is matching
	 * 
	 * @param tag - XML tag for searching
	 * @return List of XmlNode for which the given tag is matching
	 */
	public List<XmlNode> getChildrenByTagName( String tag){

		List<XmlNode> list = new LinkedList<XmlNode>();

		for( XmlNode xmlNode : this.children ) {

			if( tag.equals( xmlNode.getTag() )) {

				list.add(xmlNode);
			}
		}

		return list;
	}

	/**
	 * Return Recursive children XmlNodes for which the tag is matching
	 * 
	 * @param tag - XML tag for searching
	 * @return List of XmlNode for which the given tag is matching recursively
	 */
	public List<XmlNode> getChildrenRecursiveByTagName( String tag ){

		List<XmlNode> list = new LinkedList<XmlNode>();

		getChildrenRecurisveByTagName( list, this, tag );

		return list;
	}

	/**
	 * Return Recursive children XmlNode for which the tag is matching
	 * 
	 * @param outList - List in which the output is added.
	 * @param parent - Parent XmlNode
	 * @param tag XML - Tag for searching
	 */
	public void getChildrenRecurisveByTagName( List<XmlNode> outList, XmlNode parent, String tag ) {

		for(XmlNode xmlNode: parent.getChildren() ) {

			if( tag.equals( xmlNode.getTag() )) {

				outList.add( xmlNode );
			}

			getChildrenRecurisveByTagName( outList, xmlNode, tag);
		}
	}

	/**
	 * Return first XmlNode matches the given tag
	 * @param tag - XML tag for searching
	 * @return First XmlNode matches the given tag. null if no match
	 */
	public XmlNode getFirstChildByTagName( String tag ) {

		for( XmlNode xmlNode : this.children ) {

			if( tag.equals( xmlNode.getTag() )) {

				return xmlNode;
			}
		}

		return null;
	}

	/**
	 * Return first XmlNode matches the given tag, attribute key and attribute value.
	 * 
	 * @param tag - XML tag for searching 
	 * @param attribKey - XML attribute key for searching 
	 * @param attribValue - XML attribute value for searching  
	 * @return First XmlNode matches the given attribute key and attribute value. null if no match
	 */
	public XmlNode getFirstChildByTagAndAttribute( String tag, String attribKey, Object attribValue ) {

		for( XmlNode xmlNode : this.children ) {

			if( tag.equals( xmlNode.getTag() )

					&&  attribValue.equals(xmlNode.getAttribute(attribKey) )

					) {

				return xmlNode;
			}
		}

		return null;
	}

	/**
	 * Remove xmlNode from the children if present.
	 * @param xmlNode - XmlNode to remove from the children 
	 * @return true if this XmlNode contained the specified child element
	 */
	public boolean remove( XmlNode xmlNode ) {

		return this.children.remove( xmlNode );
	}

	/**
	 * Copy given xmlNode as a Child
	 * @param xmlNode - XmlNode to be copied as child
	 */
	public void copy( XmlNode xmlNode ) {

		XmlNode newNode = addChild( xmlNode.getTag() );

		newNode.setValue( xmlNode.getValue() );

		for( Map.Entry<String,Object> attrib : xmlNode.getAttributes().entrySet() ) {

			newNode.setAttribute(attrib.getKey(), attrib.getValue() );
		}

		for( XmlNode child : xmlNode.getChildren() ) {

			newNode.copy(child);
		}
	}
			 
	/**
	 * Return Recursive children XmlNodes for which the path is matching
	 * @param xpath - Path for searching
	 * @return List of XmlNode for which the given path is matching recursively
	 */
	public List<XmlNode> getChildrenByPath( String xpath ){

		List<XmlNode> outList = new LinkedList<XmlNode>();

		String[] xpathArr = xpath.split( "\\|");

		for( String xpath1 : xpathArr ) {

			xpath1 = xpath1.trim();

			if( ! xpath1.isEmpty() ) {

				if( xpath1.startsWith( "/")) {

					xpath1 = xpath1.substring( 1 );

					List<XmlNode> nodeList = new LinkedList<XmlNode>();

					nodeList.add( this );

					getChildrenByPath( nodeList, outList, xpath1.split("/"), 0);

				}else {

					getChildrenByPath( children, outList, xpath1.split("/"), 0);
				}
			}

		}

		return outList;
	}

	/**
	 * 
	 * @param nodeList - List of XmlNode for searching 
	 * @param outList - Return List of XmlNode for storing the results
	 * @param xpathArr - Search keys array
	 * @param index - Index of xpathArr for searching in nodeList
	 */
	public void getChildrenByPath( List<XmlNode> nodeList, 
			List<XmlNode> outList, String[] xpathArr, int index ) {

		boolean add = ( index == xpathArr.length - 1 );

		for( XmlNode xmlNode : nodeList ) {

			if( "*".equals( xpathArr[index] ) 
					|| xpathArr[index].equals( xmlNode.getTag() ) ) {

				if( add ) {

					outList.add(xmlNode);

				}else {

					getChildrenByPath( xmlNode.getChildren(), outList, xpathArr, index + 1);
				}
			}
		}
	}

}


