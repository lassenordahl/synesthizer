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

def create_xml(data_file_name, data_key, xml_name):
    with open(data_file_name, 'r') as items_data:
        items = json.load(items_data)
        items = items[data_key]
        xml = dicttoxml.dicttoxml(items, custom_root=xml_name)
        dom = parseString(xml)
        with open('xml_creation/{}.xml'.format(xml_name), WRITE_PERM) as xml_file:
            xml_file.write(dom.toprettyxml())


if __name__ == '__main__':
    create_xml(ARTIST_DATA_FILENAME, 'artist_details', 'artists')
    create_xml(ALBUM_DATA_FILENAME, 'album_details', 'albums')
    create_xml(SONG_DETAILS_FILENAME, 'track_details', 'tracks')
    create_xml(TRACK_INFO_FILENAME, 'track_information', 'track_metas')
