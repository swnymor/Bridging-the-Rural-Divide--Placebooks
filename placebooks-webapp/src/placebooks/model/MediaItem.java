package placebooks.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.persistence.Entity;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import placebooks.controller.PropertiesSingleton;

import com.vividsolutions.jts.geom.Geometry;

@Entity
public abstract class MediaItem extends PlaceBookItem
{
	@JsonIgnore
	protected File file;

	MediaItem()
	{
	}

	public MediaItem(final User owner, final Geometry geom, final URL sourceURL, final File file)
	{
		this.file = file;
	}

	@Override
	public void appendConfiguration(final Document config, final Element root)
	{
		final Element item = getConfigurationHeader(config);

		try
		{
			copyDataToPackage(file);
			final Element filename = config.createElement("filename");
			filename.appendChild(config.createTextNode(file.getName()));
			item.appendChild(filename);
		}
		catch (final IOException e)
		{
			log.error(e.toString());
		}

		root.appendChild(item);
	}

	@Override
	public void deleteItemData()
	{
		if (!file.delete())
		{
			log.error("Problem deleting file " + file.toString());
		}
	}

	public File getFile()
	{
		return file;
	}

	public String getPath()
	{
		return file.toString();
	}

	public void setPath(final String filepath)
	{
		this.file = new File(filepath);
	}

	protected void copyDataToPackage(File dataFile) throws IOException
	{
		// Check package dir exists already
		final String path = 
			PropertiesSingleton
				.get(this.getClass().getClassLoader())
				.getProperty(PropertiesSingleton.IDEN_PKG, "") 
					+ getPlaceBook().getKey();

		if (new File(path).exists() || new File(path).mkdirs())
		{

			final FileInputStream fis = new FileInputStream(dataFile);
			final File to = new File(path + "/" + dataFile.getName());

			log.info("Copying file, from=" + dataFile.toString() 
					 + ", to=" + to.toString());

			final FileOutputStream fos = new FileOutputStream(to);
			IOUtils.copy(fis, fos);
			fis.close();
			fos.close();
		}
	}
	
	public void writeDataToDisk(String name, InputStream input) throws IOException
	{
		final String path = PropertiesSingleton.get(this.getClass().getClassLoader())
				.getProperty(PropertiesSingleton.IDEN_MEDIA, "");

		if (!new File(path).exists() && !new File(path).mkdirs()) { throw new IOException("Failed to write file"); }

		final int extIdx = name.lastIndexOf(".");
		final String ext = name.substring(extIdx + 1, name.length());

		setPath(path + "/" + getKey() + "." + ext);

		final OutputStream output = new FileOutputStream(file);
		int byte_;
		while ((byte_ = input.read()) != -1)
		{
			output.write(byte_);
		}
		output.close();
		input.close();

		log.info("Wrote " + name + " file " + file.getAbsolutePath());
	}
}