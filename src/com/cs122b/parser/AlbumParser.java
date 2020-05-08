package com.cs122b.parser;

import com.cs122b.model.Album;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

class AlbumParser extends BaseParser {
    LinkedList<Album> albums;

    AlbumParser() {
        super();
        this.albums = new LinkedList<Album>();
    }

    private Album getAlbum(Element albumElem) {

        Album album = new Album();

        // Set album attrs
        album.setId(this.getTextValue(albumElem, "id"));
        album.setName(this.getTextValue(albumElem, "name"));
        album.setAlbum_type(this.getTextValue(albumElem, "type"));
        album.setRelease_date(this.getTextValue(albumElem, "release_date"));

        NodeList images = albumElem.getElementsByTagName("images");
        if (images != null && images.getLength() > 0) {
            Element image = (Element) images.item(0);
            album.setImage(this.getTextValue(image, "url"));
        } else {
            album.setImage(getTextValue(albumElem, "https://picsum.photos/200"));
        }

        return album;
    }

    void parseAlbums(Element albumsElement) {
        NodeList nl = albumsElement.getElementsByTagName("item");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                // get the album element
                Element el = (Element) nl.item(i);

                // get the Album object
                Album album = getAlbum(el);

                // add it to list
                albums.add(album);
            }
        }
        return;
    }

    void commitAlbums() {
        return;
    }
}