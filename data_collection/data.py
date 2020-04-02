import sys
import spotipy
import spotipy.util as util
import json

scope = 'user-library-read'
username = 'lassenordahl'

SONG_IDS_FILENAME = 'song_ids.txt'
SONG_DETAILS_FILENAME = 'track_details.json'
ARTIST_IDS_FILENAME = 'artist_ids.txt'
TRACK_INFO_FILENAME = 'track_info.json'
TRACK_DATA_FILENAME = 'track_data.json'
ARTIST_DATA_FILENAME = 'artist_data.json'
ALBUM_DATA_FILENAME ='album_data.json'

SCRAPING_PLAYLIST_ID = '2n4Z5LcJPLWEMciZ40jLIC'

def get_track_ids(token):
  current_song = 0

  if token:
    with open(SONG_IDS_FILENAME, 'w') as song_id_file:
      with open(ARTIST_IDS_FILENAME, 'w') as artist_id_file:
        while True:
          sp = spotipy.Spotify(auth=token)
          response = sp.playlist_tracks(SCRAPING_PLAYLIST_ID, fields='items', offset=current_song)

          if len(response['items']) == 0:
            break

          for item in response['items']:
            track = item['track']
            if track['id'] and len(track['artists']) > 0:
              song_id_file.write(track['id'] + '\n')
              artist_id_file.write(track['artists'][0]['id'] + '\n')
          current_song += len(response['items'])
  else:
    print("Can't get token for", username)

def get_track_details(token):
  current_song = 0

  if token:
    with open(SONG_DETAILS_FILENAME, 'w') as song_details_file:
      song_details = {}
      song_details['track_details'] = []

      while True:
        sp = spotipy.Spotify(auth=token)
        response = sp.playlist_tracks(SCRAPING_PLAYLIST_ID, fields='items', offset=current_song)

        if len(response['items']) == 0:
          break

        

        for item in response['items']:
          track = item['track']
          if track['id'] and len(track['artists']) > 0:
            song_details['track_details'].append(track)
        current_song += len(response['items'])

      json.dump(song_details, song_details_file)
  else:
    print("Can't get token for", username)

def get_track_data(token):
  with open(SONG_IDS_FILENAME, 'r') as ids:

    sp = spotipy.Spotify(auth=token)

    song_ids = [row.rstrip('\n') for row in ids]
    grouped_ids = [song_ids[i: i + 50] for i in range(0, len(song_ids), 50)]   
    print(grouped_ids) 

    response_data = {}
    response_data['track_information'] = []

    for id_group in grouped_ids:
      response = sp.audio_features(tracks=id_group)
      response_data['track_information'].extend(response)

    with open(TRACK_INFO_FILENAME, 'w') as fp:
      json.dump(response_data, fp)

def get_artist_data(token):
    with open(ARTIST_IDS_FILENAME, 'r') as ids:
        sp = spotipy.Spotify(auth=token)

        artist_ids = [row.rstrip('\n') for row in set(ids)]
        grouped_ids = [artist_ids[i: i + 20] for i in range(0, len(grouped_ids), 20)]

        response_data = {}
        response_data['artist_details'] = []

        for id_group in grouped_ids:
            response = sp.artists(id_group)
            response_data['artist_data'].extend(response)

        with open(ARTIST_DATA_FILENAME, 'w') as df:
            json.dump(response_data, df)

def get_album_data(token):
    with open(TRACK_DATA_FILENAME, 'r') as tracks:
        sp = spotipy.Spotify(auth=token)

        for track in tracks['track_details']:
            print(track['album'])

def test_print():
  with open(TRACK_INFO_FILENAME, 'r') as fp:
    data = json.load(fp)
    print(len(data['track_information']))
    
def sync_data_sources():
  with open(TRACK_DATA_FILENAME, 'w') as track_data_file:
    with open(TRACK_INFO_FILENAME, 'r') as track_info_file:
      with open(SONG_IDS_FILENAME, 'r') as song_ids_file:

        song_ids = [row.rstrip('\n') for row in song_ids_file]
        track_info = json.load(track_info_file)['track_information']
        

    

if __name__ == '__main__':

  token = util.prompt_for_user_token(
    username, 
    scope, 
    client_id='de6cc87601c34c7fb31e1af2658215c6',
    client_secret='14f76c16de6e4734bd56e31aa2bfe83f',
    redirect_uri='http://localhost/'
  )

  # get_track_ids(token)
  # get_track_details(token)
  # get_track_data(token)
  get_artist_data(token)
  # test_print()


  