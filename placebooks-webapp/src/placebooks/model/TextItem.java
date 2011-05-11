package placebooks.model;

import java.net.URL;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Column;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Geometry;


@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
public class TextItem extends PlaceBookItem
{
	private String text; 
	
	public TextItem(User owner, Geometry geom, URL sourceURL, String text)
	{
		super(owner, geom, sourceURL);
		this.text = text;
	}

	public void deleteItemData() { }

	public String getEntityName()
	{
		return TextItem.class.getName();
	}

	public void appendConfiguration(Document config, Element root)
	{
		log.info("TextItem.appendConfiguration(), text=" + this.getText());
		Element item = getConfigurationHeader(config);
		Element text = config.createElement("text");
		text.appendChild(config.createTextNode(this.getText()));
		item.appendChild(text);
		root.appendChild(item);
	}

	@Persistent
	@Column(jdbcType = "CLOB")
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see placebooks.model.PlaceBookItem#GetHTML()
	 */
	@Override
	public String GetHTML()
	{
		StringBuilder output = new StringBuilder();
		output.append("<div class='placebook-item-text' id='");
		output.append(this.getPlaceBook().getKey());
		output.append("'>");
		output.append(this.text);
		output.append("'</div>");
		return output.toString();
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
