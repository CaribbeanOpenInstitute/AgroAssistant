package org.data.agroassistant;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class xmlParse {

	private static Document dom;
	private static ArrayList<CropObj>   cropList; 
	private static ArrayList<FarmObj>   farmList ;
	private static ArrayList<FarmerObj> farmerList; 
		
	
	public xmlParse(String xmlString) {
		farmerList = new ArrayList<FarmerObj>();
		farmList   = new ArrayList<FarmObj>();
		cropList   = new ArrayList<CropObj>();
		try {
		
			dom = loadXMLFromString( xmlString);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static Document loadXMLFromString(String xml) throws Exception {
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inStream = new InputSource();
		
		try {

			
			inStream.setCharacterStream(new StringReader(xml));
			
			doc = builder.parse(inStream);

		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc;
	}

	public int parseXML(String type){
		return parseDocument(type);
		
	}
	
	
	public ArrayList<CropObj> getCropList(){
		return cropList;
	}
	
	public ArrayList<FarmObj> getFarmList(){
		return farmList;
	}
	public ArrayList<FarmerObj> getFarmerList(){
		return farmerList;
	}
	
	public String getDom(){
		return dom.toString();
	}
	
	private int parseDocument(String objType) {
		// get the root element
		Element docEle = dom.getDocumentElement();


		//get a nodelist of elements
		String tag; 
		if (objType.equals("Farmer")){
			tag = "Farm";
		}else{
			tag = objType;
		}
			
		NodeList nl = docEle.getElementsByTagName(tag);
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the element
				Element el = (Element) nl.item(i);
				
				if (objType.equals("Farm")){
					FarmObj farm = getFarm(el);
					farmList.add(i,farm);
					
				}else if(objType.equals("Farmer")){
					FarmerObj farmer = getFarmer(el);
					farmerList.add(farmer);
					
				}else if(objType.equals("Crop")){
					CropObj crop = getCrop(el);
					
					cropList.add(crop);
					
				}else if(objType.equals("Price")){
					
				}else{
					
				}
				
				
			}
		}
		return nl.getLength();
	}
	
	private FarmObj getFarm(Element farmE){
		
		//get the attributes of each object from the xml tags
		int farmerid     = getIntValue  ( farmE , "FarmerID"     ) ;
		int farmid       = getIntValue  ( farmE , "PropertyID"   ) ;
		int propertySize = getIntValue  ( farmE , "Propertysize" ) ;
		int latitude     = getIntValue  ( farmE , "Ycoord"       ) ;
		int longitude    = getIntValue  ( farmE , "Xcoord"       );
		
		String parish    = getTextValue ( farmE , "Parish"    ) ;
		String extension = getTextValue ( farmE , "Extension" ) ;
		String district  = getTextValue ( farmE , "District"  ) ;
		
		//Create a new farm with the value read from the xml nodes
		FarmObj farm = new FarmObj(farmerid, farmid, propertySize, latitude, longitude, parish, extension, district);

		return farm;

	}
	
	private FarmerObj getFarmer(Element farmerE){
		
		int farmerid  = getIntValue  ( farmerE , "FarmerID"   ) ;
		String fName  = getTextValue ( farmerE , "firstname"  ) ;
		String lName  = getTextValue ( farmerE , "lastname"   ) ;
		String fSize  = getTextValue ( farmerE , "Farmersize" ) ;
		
		//Create a new farmer with the value read from the xml nodes
		//FarmerObj farmer = new FarmerObj(201001261, "Gebre", "Wallace", "SMALL");
		FarmerObj farmer = new FarmerObj(farmerid, fName, lName,fSize);
		return farmer;
	}

	private CropObj getCrop(Element CropE){
		Date date;
		//get the attributes of each object from the xml tags
		int farmid  = getIntValue  ( CropE , "PropertyID" ) ;
		int area    = getIntValue  ( CropE , "CropArea"   ) ;
		int count   = getIntValue  ( CropE , "CropCount"  ) ;
				
		String group    = getTextValue ( CropE , "CropGroup" ) ;
		String type 	= getTextValue ( CropE , "CropType"  ) ;
		String dateStr  = getTextValue ( CropE , "CropDate"  ) ;
		try {
			 date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateStr);
		}catch(ParseException e){
			date = new Date(0,0,0);
		}
		
		//Create a new crop with the value read from the xml nodes
		CropObj crop = new CropObj(farmid, group, type, area, count, date);

		return crop;
		
	}
	
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */

	private String getTextValue(Element ele, String tagName) {
		String textVal = "";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			try{
				textVal = el.getFirstChild().getNodeValue();
			}catch (DOMException e){
				textVal = e.toString();
			}
		}
		if (textVal.equals(null)){
			textVal = "";
		}
		
		return textVal;
	}
	
	/**
	 * Calls getTextValue and returns a int value
	 */
	private int getIntValue(Element ele, String tagName) {
		
		Integer value = 0;
		try{
			value = Integer.parseInt(getTextValue(ele,tagName).trim());
		}catch(NumberFormatException e){
			value = 1;
		}
		if (value == null){
			value = 0;
		}
		return value;
	}


}