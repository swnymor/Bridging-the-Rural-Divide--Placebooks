package org.placebooks.www;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;


@Root
public class Gpx {
		
		
		@Attribute(required=false)
		private String xmlns;

		@Attribute(required=false)
		private String xsi;

		@Attribute(required=false)
		public String creator;
		
		@Attribute(required=false)
		public String version;
		
		@Attribute(required=false)
		private String schemaLocation;
		
		@Element(required=false)
		public Metadata metadata;
		
		@Element
		public Trk trk;
		
		
		public String getXmlns(){
			return xmlns;
		}
		
		public String getSchemaLocation(){
			return schemaLocation;
		}
		
		public String getCreator(){
			return creator;
		}
		
		public String getVersion(){
			return version;
		}
		
		public Trk getTrk(){
			return trk;
		}
		
		public Metadata getMetadata() {
		      return metadata;           
		   }


	}

