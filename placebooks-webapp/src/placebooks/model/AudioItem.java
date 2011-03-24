package placebooks.model;

import placebooks.controller.PropertiesSingleton;

import java.io.*;
import java.net.URL;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.commons.io.IOUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Geometry;

@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
public class AudioItem extends PlaceBookItem
{
	private File audio; 

	public AudioItem(User owner, Geometry geom, URL sourceURL, File audio)
	{
		super(owner, geom, sourceURL);
		this.audio = audio;
	}

	public String getEntityName()
	{
		return AudioItem.class.getName();
	}

	public void appendConfiguration(Document config, Element root)
	{
		Element item = getConfigurationHeader(config);

		// TODO: identical to VideoItem.appendConfiguration... might be some 
		// abstraction here to do.
		try
		{			
			FileInputStream fis = new FileInputStream(audio);
			File to = new File(
						PropertiesSingleton
							.get(this.getClass().getClassLoader())
							.getProperty(PropertiesSingleton.IDEN_PKG, "") 
							+ getPlaceBook().getKey() + "/" + audio.getName());

			FileOutputStream fos = new FileOutputStream(to);

			log.info("Copying audio file, from=" + audio.toString() + ", to=" 
					 + to.toString());
			IOUtils.copy(fis, fos);
			fis.close();
			fos.close();
			Element filename = config.createElement("filename");
			filename.appendChild(config.createTextNode(audio.getName()));
			item.appendChild(filename);
		}
		catch (IOException e)
		{
			log.error(e.toString());
		}

		root.appendChild(item);
	}

	@Persistent
	public String getAudio()
	{
		return audio.toString();
	}
	@Persistent
	public void setAudio(String filepath)
	{
		if (filepath != null)
			audio = new File(filepath);
	}

	/* (non-Javadoc)
	 * @see placebooks.model.PlaceBookItem#GetHTML()
	 */
	@Override
	public String GetHTML()
	{
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see placebooks.model.PlaceBookItem#GetCSS()
	 */
	@Override
	public String GetCSS()
	{
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see placebooks.model.PlaceBookItem#GetJavaScript()
	 */
	@Override
	public String GetJavaScript()
	{
		// TODO Auto-generated method stub
		return "";
	}
}