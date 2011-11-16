package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.anddev.andengine.util.StreamUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:15 - 29.07.2011
 */
public class TexturePackLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mAssetBasePath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackLoader() {
		this("");
	}

	public TexturePackLoader(final String pAssetBasePath) {
		this.mAssetBasePath = pAssetBasePath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TexturePack loadFromAsset(final Context pContext, final String pAssetPath) throws TexturePackParseException {
		try {
			return this.load(pContext, pContext.getAssets().open(this.mAssetBasePath + pAssetPath));
		} catch (final IOException e) {
			throw new TexturePackParseException("Could not load " + this.getClass().getSimpleName() + " data from asset: " + pAssetPath, e);
		}
	}

	public TexturePack load(final Context pContext, final InputStream pInputStream) throws TexturePackParseException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final TexturePackParser texturePackerParser = new AssetTexturePackParser(pContext, this.mAssetBasePath);
			xr.setContentHandler(texturePackerParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return texturePackerParser.getTexturePack();
		} catch (final SAXException e) {
			throw new TexturePackParseException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new TexturePackParseException(e);
		} finally {
			StreamUtils.close(pInputStream);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
