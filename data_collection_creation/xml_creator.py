import os
import json
import random
import ssl
import shutil
import dicttoxml
from xml.dom.minidom import parseString


SONG_IDS_FILENAME = 'collected_data/song_ids.txt'
SONG_DETAILS_FILENAME = 'collected_data/track_details.json'
ARTIST_IDS_FILENAME = 'collected_data/artist_ids.txt'
TRACK_INFO_FILENAME = 'collected_data/track_info.json'
TRACK_DATA_FILENAME = 'collected_data/track_data.json'
ARTIST_DATA_FILENAME = 'collected_data/artist_data.json'
ALBUM_DATA_FILENAME = 'collected_data/album_data.json'

WRITE_PERM = 'w+'

def getAlbumOrElse(item, key):
    if key == "album":
        return {"id": item[key]["id"]}
    return item[key]

def create_xml(data_file_name, data_key, xml_name, keys):
    with open(data_file_name, 'r') as items_data:
        items = json.load(items_data)
        items = items[data_key]

        clean_items = []
        for item in items:
            if len(keys) == 0:
                clean_items.append(item)
            else:
                clean_items.append({key: getAlbumOrElse(item, key) for key in keys})

        xml = dicttoxml.dicttoxml(clean_items, custom_root=xml_name)
        dom = parseString(xml)
        with open('xml_creation/{}.xml'.format(xml_name), WRITE_PERM) as xml_file:
            xml_file.write(dom.toprettyxml())


if __name__ == '__main__':
    create_xml(ARTIST_DATA_FILENAME, 'artist_details', 'artists', ["id", "name", "images", "genres"])
    create_xml(ALBUM_DATA_FILENAME, 'album_details', 'albums', ["id", "name", "album_type", "release_date", "images", "artists"])
    create_xml(SONG_DETAILS_FILENAME, 'track_details', 'tracks', ["id", "name", "track_number", "album", "artists"])
    create_xml(TRACK_INFO_FILENAME, 'track_information', 'track_metas', [])
