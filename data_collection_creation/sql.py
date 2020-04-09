import os
import json
import random
import ssl
import shutil
from randomuser import RandomUser
from randomwordgenerator import randomwordgenerator

# Needed to comment the above out because I was getting an import error

SONG_IDS_FILENAME = 'collected_data/song_ids.txt'
SONG_DETAILS_FILENAME = 'collected_data/track_details.json'
ARTIST_IDS_FILENAME = 'collected_data/artist_ids.txt'
TRACK_INFO_FILENAME = 'collected_data/track_info.json'
TRACK_DATA_FILENAME = 'collected_data/track_data.json'
ARTIST_DATA_FILENAME = 'collected_data/artist_data.json'
ALBUM_DATA_FILENAME = 'collected_data/album_data.json'

CREATE_TABLES_FILE = '../createtable.sql'

# to create on .sql file, add a name below and change WRITE_PERM to 'a'
CREATION_INSERTION_FILE = 'CreationInsertion'
WRITE_PERM = 'a'


USER_COUNT = 100
PLAYLIST_COUNT = 1000
SONGS_IN_PLAYLIST = 8000


def insert(table, columns):
    def quote_str(col): return "\"{}\"".format(col.replace(
        '\"', '').replace('\\"', '')) if type(col) == str and col != 'DEFAULT' else str(col)
    return 'INSERT IGNORE INTO {} VALUES({});\n'.format(table, ','.join(map(quote_str, columns)))


def create_table(data_file_name, data_key, table_name, get_columns):
    with open(data_file_name, 'r') as items_data:
        items = json.load(items_data)
        items = items[data_key]
        with open('sql_creation/{}.sql'.format(CREATION_INSERTION_FILE or table_name), WRITE_PERM) as table_file:
            for i in items:
                table_file.write(insert(table_name, get_columns(i)))


def create_table_many_many(data_file_name, data_key, table_name, get_columns):
    with open(data_file_name, 'r') as items_data:
        items = json.load(items_data)
        items = items[data_key]
        with open('sql_creation/{}.sql'.format(CREATION_INSERTION_FILE or table_name), WRITE_PERM) as table_file:
            for i in items:
                rows_to_add = [list()]
                columns = get_columns(i)
                for col in columns:
                    # can't have more than one col that is list
                    if type(col) == list:
                        if len(col) <= 0:
                            continue
                        temp_rows_to_add = []
                        for c in col:
                            new_list = list(rows_to_add[0])
                            new_list.append(c)
                            temp_rows_to_add.append(new_list)
                        if len(temp_rows_to_add) > 0:
                            rows_to_add = temp_rows_to_add
                    else:
                        [row.append(col) for row in rows_to_add]
                for row in rows_to_add:
                    if len(row) == len(columns):
                        table_file.write(insert(table_name, row))


def create_fake_user_data(output_file_name, total_users, total_playlists, total_songs):
    with open('sql_creation/{}.sql'.format(CREATION_INSERTION_FILE or output_file_name), WRITE_PERM) as f:
        for id in range(0, total_users):
            f.write(insert('user', get_user_columns(id)))

        random_words = randomwordgenerator.generate_random_words(n=5000)
        for id in range(0, total_playlists):
            f.write(insert('playlist', get_playlist_columns(id, random_words)))

        for _ in range(0, total_playlists):
            f.write(insert('playlist_to_user', get_playlist_to_user_columns(
                total_users, total_playlists)))

        with open(SONG_DETAILS_FILENAME, 'r') as tracks_data:
            tracks = json.load(tracks_data)
            tracks = tracks['track_details']
            for _ in range(0, total_songs):
                f.write(insert('track_in_playlist', get_track_in_playlist_columns(
                    total_playlists, tracks)))


def get_user_columns(id):
    cols = []
    user = RandomUser()
    cols.append(id)
    cols.append(user.get_first_name())
    cols.append(user.get_last_name())
    cols.append('{} {}, {} {}'.format(user.get_street(), user.get_city(),
                                      user.get_state(), str(user.get_zipcode())))
    cols.append(user.get_email())
    cols.append(user.get_password())
    return cols


def get_playlist_columns(id, random_words):
    cols = []
    cols.append(id)

    rw1 = random_words[random.randint(0, len(random_words) - 1)]
    rw2 = random_words[random.randint(0, len(random_words) - 1)]
    cols.append('{} {}'.format(rw1, rw2))

    cols.append("https://picsum.photos/200")
    cols.append('DEFAULT')
    return cols


def get_playlist_to_user_columns(user_count, playlist_count):
    cols = []
    cols.append(random.randint(0, user_count))
    cols.append(random.randint(0, playlist_count))
    return cols


def get_track_in_playlist_columns(playlist_count, tracks):
    cols = []
    cols.append(random.randint(0, playlist_count))
    random_track = tracks[random.randint(0, len(tracks) - 1)]
    cols.append(random_track['id'])
    return cols


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
        cols.append(a['name'])
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
    #  Many to many
    cols = []
    if t:
        cols.append([artist['id'] for artist in t['artists']])
        cols.append(t['id'])
    return cols


def get_artist_to_album_columns(a):
    #  Many to many
    cols = []
    if a:
        cols.append([artist['id'] for artist in a['artists']])
        cols.append(a['id'])
    return cols


def get_genre_to_album_columns(a):
    #  Many to many
    cols = []
    if a:
        cols.append(a['id'])
        cols.append(a.get('genres', []))
    return cols


def get_genre_to_artist_columns(a):
    #  Many to many
    cols = []
    if a:
        cols.append(a['id'])
        cols.append(a.get('genres', []))
    return cols

def get_album_names():
    album_id_album_tuples = []
    with open('collected_data/track_details.json') as track_details_file:
        items = json.load(track_details_file)
        for track in items['track_details']:
            # print(track)
            # break
            album_id_album_tuples.append((track['album']['id'], track['album']['name']))
        print(album_id_album_tuples)
    return album_id_album_tuples


def add_album_names_to_sql_file(album_id_album_tuples):
    with open('sql_creation/CreationInsertionAlbumFix.sql', 'a') as sql_file:
        sql_file.write('\nALTER TABLE album ADD name VARCHAR(150);' + '\n')
        for album_id, album_name in album_id_album_tuples:
            sql_file.write('UPDATE album SET name = "' + album_name.replace('"', '')[:150] + '" WHERE id = "' + album_id + '";\n')

    

if __name__ == '__main__':

    # add_album_names_to_sql_file(get_album_names())

    if CREATION_INSERTION_FILE:
        shutil.copyfile(os.path.dirname(__file__) +
                        CREATE_TABLES_FILE, 'sql_creation/{}.sql'.format(CREATION_INSERTION_FILE))

    try:
        _create_unverified_https_context = ssl._create_unverified_context
    except AttributeError:
        pass
    else:
        ssl._create_default_https_context = _create_unverified_https_context

    one_to_one_tables = [
        (TRACK_INFO_FILENAME, 'track_information',
         'track_meta', get_track_meta_columns),
        (SONG_DETAILS_FILENAME, 'track_details', 'track', get_track_columns),
        (ALBUM_DATA_FILENAME, 'album_details', 'album', get_album_columns),
        (ARTIST_DATA_FILENAME, 'artist_details', 'artist', get_artist_columns),
        (SONG_DETAILS_FILENAME, 'track_details',
         'track_in_album', get_track_to_album_columns)
    ]

    many_to_many_tables = [
        (SONG_DETAILS_FILENAME, 'track_details',
         'artist_in_track', get_artist_to_track_columns),
        (ALBUM_DATA_FILENAME, 'album_details',
         'artist_in_album', get_artist_to_album_columns),
        (ALBUM_DATA_FILENAME, 'album_details',
         'album_in_genre', get_genre_to_album_columns),
        (ARTIST_DATA_FILENAME, 'artist_details',
         'artist_in_genre', get_genre_to_artist_columns)
    ]

    # create one to one tables
    for table in one_to_one_tables:
        create_table(*table)

    # create many to many tables
    for table in many_to_many_tables:
        create_table_many_many(*table)

    # create fake users
    create_fake_user_data('user_data', USER_COUNT,
                          PLAYLIST_COUNT, SONGS_IN_PLAYLIST)
