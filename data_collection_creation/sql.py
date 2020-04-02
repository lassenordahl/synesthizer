import json

SONG_IDS_FILENAME = 'collected_data/song_ids.txt'
SONG_DETAILS_FILENAME = 'collected_data/track_details.json'
ARTIST_IDS_FILENAME = 'collected_data/artist_ids.txt'
TRACK_INFO_FILENAME = 'collected_data/track_info.json'
TRACK_DATA_FILENAME = 'collected_data/track_data.json'
ARTIST_DATA_FILENAME = 'collected_data/artist_data.json'
ALBUM_DATA_FILENAME = 'collected_data/album_data.json'


def insert(table, columns):
    def quote_str(col): return "\"{}\"".format(col.replace(
        '\"', '').replace('\\"', '')) if type(col) == str else str(col)
    return 'INSERT INTO {} VALUES({});\n'.format(table, ','.join(map(quote_str, columns)))


def create_table(data_file_name, data_key, table_name, get_columns):
    with open(data_file_name, 'r') as items_data:
        items = json.load(items_data)
        items = items[data_key]
        with open('sql_creation/{}.sql'.format(table_name), 'w') as table_file:
            for i in items:
                table_file.write(insert(table_name, get_columns(i)))


def get_track_columns(t):
    cols = []
    if t:
        cols.append(t['id'])
        cols.append(t['name'])
        cols.append(t['track_number'])
    return cols


def get_track_meta_columns(t):
    cols = []
    if t:
        cols.append(t['id'])
        cols.append(t['acousticness'])
        cols.append(t['analysis_url'])
        cols.append(t['danceability'])
        cols.append(t['duration_ms'])
        cols.append(t['energy'])
        cols.append(t['instrumentalness'])
        cols.append(t['key'])
        cols.append(t['liveness'])
        cols.append(t['loudness'])
        cols.append(t['mode'])
        cols.append(t['speechiness'])
        cols.append(t['tempo'])
        cols.append(t['time_signature'])
        cols.append(t['track_href'])
        cols.append(t['type'])
        cols.append(t['uri'])
        cols.append(t['valence'])
    return cols


def get_album_columns(a):
    cols = []
    if a:
        cols.append(a['id'])
        cols.append(a['album_type'])
        cols.append(a['images'][0]['url'])
        cols.append(a['release_date'])
    return cols


def get_artist_columns(a):
    cols = []
    if a:
        cols.append(a['id'])
        cols.append(a['name'])

        try:
            image = a['images'][0]['url']
        except:
            image = ""

        cols.append(image)
    return cols


def get_track_to_album_columns(t):
    cols = []
    if t:
        cols.append(t['id'])
        cols.append(t['album']['id'])
    return cols


def get_artist_to_track_columns(t):
    # TODO: Many to many
    pass


def get_artist_to_album_columns():
    # TODO: Many to many
    pass


def get_genre_to_album_columns():
    # TODO: Many to many
    pass


def get_genre_to_artist_columns():
    # TODO: Many to many
    pass


if __name__ == '__main__':
    tables = [
        (SONG_DETAILS_FILENAME, 'track_details', 'track', get_track_columns),
        (TRACK_INFO_FILENAME, 'track_information',
         'track_meta', get_track_meta_columns),
        (ALBUM_DATA_FILENAME, 'album_details', 'album', get_album_columns),
        (ARTIST_DATA_FILENAME, 'artist_details', 'artist', get_artist_columns),
        (SONG_DETAILS_FILENAME, 'track_details',
         'track_in_album', get_track_to_album_columns),
        # (SONG_DETAILS_FILENAME, 'track_details', 'artist_to_track', get_artist_to_track_columns)
    ]

    for table in tables:
        create_table(*table)
