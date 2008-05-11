/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2008 Jan-Willem van den Broek
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jwbroek.cuelib;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>Class for serializing a {@link jwbroek.cuelib.CueSheet CueSheet} to an XML representation. The serialized
 * cue sheet will conform to the following XML Schema, which closely resembles the cue sheet syntax, except for
 * the fact that it is less restrictive with respect to allowed element values. This is necessary, as the
 * {@link jwbroek.cuelib.CueSheet CueSheet} structure is more lenient than the cue sheet standard.</p>
 * 
 * {@code
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:tns="http://jwbroek/cuelib/2008/cuesheet/1"
  targetNamespace="http://jwbroek/cuelib/2008/cuesheet/1"
  elementFormDefault="qualified"
  attributeFormDefault="unqualified"
  >
  
  <xsd:element name="cuesheet" type="tns:cuesheet"/>
  
  <xsd:complexType name="cuesheet">
    <xsd:sequence>
      <xsd:element name="file" type="tns:file" minOccurs="0" maxOccurs="unbounded"/>
    </xsd:sequence>
    <xsd:attribute name="genre" type="xsd:string" use="optional"/>
    <xsd:attribute name="date" type="xsd:integer" use="optional"/>
    <xsd:attribute name="discid" type="xsd:string" use="optional"/>
    <xsd:attribute name="comment" type="xsd:string" use="optional"/>
    <xsd:attribute name="catalog" type="xsd:string" use="optional"/>
    <xsd:attribute name="performer" type="xsd:string" use="optional"/>
    <xsd:attribute name="title" type="xsd:string" use="optional"/>
    <xsd:attribute name="songwriter" type="xsd:string" use="optional"/>
    <xsd:attribute name="cdtextfile" type="xsd:string" use="optional"/>
  </xsd:complexType>
  
  <xsd:complexType name="file">
    <xsd:sequence>
      <xsd:element name="track" type="tns:track" minOccurs="0" maxOccurs="unbounded"/>
    </xsd:sequence>
    <xsd:attribute name="file" type="xsd:string" use="optional"/>
    <xsd:attribute name="type" type="xsd:string" use="optional"/>
  </xsd:complexType>
  
  <xsd:complexType name="track">
    <xsd:sequence>
      <xsd:element name="pregap" type="tns:position" minOccurs="0"/>
      <xsd:element name="postgap" type="tns:position" minOccurs="0"/>
      <xsd:element name="flags" type="tns:flags" minOccurs="0"/>
      <xsd:element name="index" type="tns:index" minOccurs="0" maxOccurs="unbounded"/>
    </xsd:sequence>
    <xsd:attribute name="number" type="xsd:integer" use="optional"/>
    <xsd:attribute name="type" type="xsd:string" use="optional"/>
    <xsd:attribute name="isrc" type="xsd:string" use="optional"/>
    <xsd:attribute name="performer" type="xsd:string" use="optional"/>
    <xsd:attribute name="title" type="xsd:string" use="optional"/>
    <xsd:attribute name="songwriter" type="xsd:string" use="optional"/>
  </xsd:complexType>
  
  <xsd:complexType name="position">
    <xsd:attribute name="minutes" type="xsd:integer"/>
    <xsd:attribute name="seconds" type="xsd:integer"/>
    <xsd:attribute name="frames" type="xsd:integer"/>
  </xsd:complexType>
  
  <xsd:complexType name="flags">
    <xsd:sequence>
      <xsd:element name="flag" type="xsd:string" maxOccurs="unbounded"/>
    </xsd:sequence>
  </xsd:complexType>
  
  <xsd:complexType name="index">
    <xsd:annotation>
      <xsd:documentation>
        The attributes in this type will either all be present, or all absent. Unfortunately,
        I know of no way to capture this constraint in XML Schema version 1.0.
      </xsd:documentation>
    </xsd:annotation>
    <xsd:attribute name="minutes" type="xsd:integer" use="optional"/>
    <xsd:attribute name="seconds" type="xsd:integer" use="optional"/>
    <xsd:attribute name="frames" type="xsd:integer" use="optional"/>
    <xsd:attribute name="number" type="xsd:integer" use="optional"/>
  </xsd:complexType>
  
</xsd:schema>}
 * 
 * @author jwbroek
 */
public class CueSheetToXmlSerializer
{
  private DocumentBuilder docBuilder;
  private String namespace = "http://jwbroek/cuelib/2008/cuesheet/1";
  
  /**
   * Create a default CueSheetToXmlSerializer.
   * @throws ParserConfigurationException 
   */
  public CueSheetToXmlSerializer() throws ParserConfigurationException
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    docBuilderFactory.setNamespaceAware(true);
    this.docBuilder = docBuilderFactory.newDocumentBuilder();
  }
  
  /**
   * Write an XML representation of the cue sheet.
   * @param cueSheet The CueSheet to serialize.
   * @param writer The Writer to write the XML representation to. 
   * @throws TransformerException 
   */
  public void serializeCueSheet(CueSheet cueSheet, Writer writer) throws TransformerException
  {
    serializeCueSheet(cueSheet, new StreamResult(writer));
  }
  
  /**
   * Write an XML representation of the cue sheet.
   * @param cueSheet The CueSheet to serialize.
   * @param outputStream The OutputStream to write the XML representation to. 
   * @throws TransformerException 
   */
  public void serializeCueSheet(CueSheet cueSheet, OutputStream outputStream) throws TransformerException
  {
    serializeCueSheet(cueSheet, new StreamResult(outputStream));
  }

  /**
   * Write an XML representation of the cue sheet.
   * @param cueSheet The CueSheet to serialize.
   * @param file The File to write the XML representation to. 
   * @throws TransformerException 
   */
  public void serializeCueSheet(CueSheet cueSheet, File file) throws TransformerException
  {
    serializeCueSheet(cueSheet, new StreamResult(file));
  }

  /**
   * Write an XML representation of the cue sheet.
   * @param cueSheet The CueSheet to serialize.
   * @param result The Result to write the XML representation to. 
   * @throws TransformerException 
   */
  public void serializeCueSheet(CueSheet cueSheet, Result result) throws TransformerException
  {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer identityTransformer = transformerFactory.newTransformer();
    Source cueSheetSource = new DOMSource(serializeCueSheet(cueSheet));
    identityTransformer.transform(cueSheetSource, result);
  }

  /**
   * Get an XML DOM tree representation of the cue sheet.
   * @param cueSheet The CueSheet to serialize.
   * @return An XML DOM tree representation of the cue sheet. 
   */
  public Document serializeCueSheet(CueSheet cueSheet)
  {
    Document doc = docBuilder.newDocument();
    Element cueSheetElement = doc.createElementNS(this.namespace, "cuesheet");
    doc.appendChild(cueSheetElement);
    
    addAttribute(cueSheetElement, "genre", cueSheet.getGenre());
    addAttribute(cueSheetElement, "date", cueSheet.getYear());
    addAttribute(cueSheetElement, "discid", cueSheet.getDiscid());
    addAttribute(cueSheetElement, "comment", cueSheet.getComment());
    addAttribute(cueSheetElement, "catalog", cueSheet.getCatalog());
    addAttribute(cueSheetElement, "performer", cueSheet.getPerformer());
    addAttribute(cueSheetElement, "title", cueSheet.getTitle());
    addAttribute(cueSheetElement, "songwriter", cueSheet.getSongwriter());
    addAttribute(cueSheetElement, "cdtextfile", cueSheet.getCdTextFile());
    
    for (FileData fileData : cueSheet.getFileData())
    {
      serializeFileData(cueSheetElement, fileData);
    }
    
    return doc;
  }
  
  /**
   * Serialize the FileData.
   * @param parentElement The parent element for the FileData.
   * @param fileData The FileData to serialize.
   */
  private void serializeFileData(Element parentElement, FileData fileData)
  {
    Document doc = parentElement.getOwnerDocument();
    Element fileElement = doc.createElementNS(this.namespace, "file");
    parentElement.appendChild(fileElement);
    
    addAttribute(fileElement, "file", fileData.getFile());
    addAttribute(fileElement, "type", fileData.getFileType());
    
    for (TrackData trackData : fileData.getTrackData())
    {
      serializeTrackData(fileElement, trackData);
    }
  }

  /**
   * Serialize the TrackData.
   * @param parentElement The parent element for the TrackData.
   * @param trackData The TrackData to serialize.
   */
  private void serializeTrackData(Element parentElement, TrackData trackData)
  {
    Document doc = parentElement.getOwnerDocument();
    Element trackElement = doc.createElementNS(this.namespace, "track");
    parentElement.appendChild(trackElement);
    
    addAttribute(trackElement, "number", trackData.getNumber());
    addAttribute(trackElement, "type", trackData.getDataType());
    
    addAttribute(trackElement, "isrc", trackData.getIsrcCode());
    addAttribute(trackElement, "performer", trackData.getPerformer());
    addAttribute(trackElement, "title", trackData.getTitle());
    addAttribute(trackElement, "songwriter", trackData.getSongwriter());
    
    addElement(trackElement, "pregap", trackData.getPregap());
    addElement(trackElement, "postgap", trackData.getPostgap());
    
    if (trackData.getFlags().size() > 0)
    {
      serializeFlags(trackElement, trackData.getFlags());
    }
    
    for (Index index : trackData.getIndices())
    {
      serializeIndex(trackElement, index);
    }
  }
  
  /**
   * Serialize the flags.
   * @param parentElement The parent element for the TrackData.
   * @param flags The flags to serialize.
   */
  private void serializeFlags(Element parentElement, Set<String> flags)
  {
    Document doc = parentElement.getOwnerDocument();
    Element flagsElement = doc.createElementNS(this.namespace, "flags");
    parentElement.appendChild(flagsElement);

    for (String flag : flags)
    {
      addElement(flagsElement, "flag", flag);
    }
  }
  
  /**
   * Serialize the index.
   * @param parentElement The parent element for the TrackData.
   * @param index The Index to serialize.
   */
  private void serializeIndex(Element parentElement, Index index)
  {
    Element indexElement = addElement(parentElement, "index", index.getPosition(), true);
    
    addAttribute(indexElement, "number", index.getNumber());
  }
  
  /**
   * Add a position element. The element is only added if the position is != null.
   * In the latter case, the attributes with position data will still only be added if present.
   * @param parentElement The parent element for the position element.
   * @param elementName The name for the position element to add.
   * @param value The value to add.
   * @return The element that was created, or null if no element was created.
   */
  private Element addElement  ( Element parentElement
                              , String elementName
                              , Position position
                              )
  {
    return addElement(parentElement, elementName, position, false);
  }

  /**
   * Add a position element. The element is only added if the position is != null, or if creation is forced.
   * In the latter case, the attributes with position data will still only be added if present.
   * @param parentElement The parent element for the position element.
   * @param elementName The name for the position element to add.
   * @param forceElement Force creation of the element, but not necessarily of the attributes.
   * @param value The value to add.
   * @return The element that was created, or null if no element was created.
   */
  private Element addElement  ( Element parentElement
                              , String elementName
                              , Position position
                              , boolean forceElement
                              )
  {
    Element positionElement = null;
    
    if (position != null || forceElement)
    {
      positionElement = parentElement.getOwnerDocument().createElementNS(this.namespace, elementName);
      parentElement.appendChild(positionElement);
      
      if (position != null)
      {
        positionElement.setAttribute("minutes", ""+position.getMinutes());
        positionElement.setAttribute("seconds", ""+position.getSeconds());
        positionElement.setAttribute("frames", ""+position.getFrames());
      }
    }
    
    return positionElement;
  }

  /**
   * Add an element to the document. The element is only added if the value is != null.
   * @param cueBuilder
   * @param parentElement The parent element for this element.
   * @param elementName The name for the element.
   * @param value The value for the element.
   * @return The element that was created, or null if no element was created.
   */
  private Element addElement(Element parentElement, String elementName, String value)
  {
    Element newElement = null;
    
    if (value != null)
    {
      newElement = parentElement.getOwnerDocument().createElementNS(this.namespace, elementName);
      newElement.appendChild(parentElement.getOwnerDocument().createTextNode(value));
      parentElement.appendChild(newElement);
    }
    
    return newElement;
  }
  
  /**
   * Add an element to the document. The element is only added if the value is > -1.
   * @param cueBuilder
   * @param parentElement The parent element for this element.
   * @param elementName The name for the element.
   * @param value The value for the element.
   * @return The element that was created, or null if no element was created.
   */
  private Element addElement(Element parentElement, String elementName, int value)
  {
    Element newElement = null;
    
    if (value > -1)
    {
      newElement = parentElement.getOwnerDocument().createElementNS(this.namespace, elementName);
      newElement.appendChild(parentElement.getOwnerDocument().createTextNode("" + value));
      parentElement.appendChild(newElement);
    }
    
    return newElement;
  }

  /**
   * Add an attribute to the document. The attribute is only added if the value is != null.
   * @param cueBuilder
   * @param parentElement The parent element for this attribute.
   * @param attributeName The name for the attribute.
   * @param value The value for the attribute.
   */
  private void addAttribute(Element parentElement, String attributeName, String value)
  {
    if (value != null)
    {
      parentElement.setAttribute(attributeName, value);
    }
  }
  
  /**
   * Add an attribute to the document. The attribute is only added if the value is > -1.
   * @param cueBuilder
   * @param parentElement The parent element for this attribute.
   * @param attributeName The name for the attribute.
   * @param value The value for the attribute.
   */
  private void addAttribute(Element parentElement, String attributeName, int value)
  {
    if (value > -1)
    {
      parentElement.setAttribute(attributeName, "" + value);
    }
  }
}
