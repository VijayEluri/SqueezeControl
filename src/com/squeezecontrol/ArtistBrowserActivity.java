/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 */

package com.squeezecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.squeezecontrol.model.Artist;
import com.squeezecontrol.view.BrowseableAdapter;

import java.io.IOException;

public class ArtistBrowserActivity extends AbstractMusicBrowserActivity<Artist> {

    public static final String EXTRA_GENRE_ID = "genre_id";
    private String mGenreId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "artist";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGenreId = extras.getString(EXTRA_GENRE_ID);
        }

        setContentView(R.layout.default_browser_list);
        super.init();
    }

    @Override
    protected BrowseableAdapter<Artist> createListAdapter() {
        return new BrowseableAdapter<Artist>(this, android.R.layout.simple_list_item_1);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position < 0)
            return;
        Artist selectedItem = (Artist) getListAdapter().getItem(position);
        if (selectedItem == null) return;
        Intent intent = new Intent(this, AlbumBrowserActivity.class);
        intent.putExtra(AlbumBrowserActivity.EXTRA_ARTIST_ID, selectedItem.getId());
        startActivity(intent);
    }

    @Override
    protected void addToPlaylist(Artist selectedItem) {
        getSqueezeService().getPlayer().sendCommand(
                "playlist addtracks contributor.id=" + selectedItem.getId());
        Toast.makeText(this, "Added to playlist:\n" + selectedItem.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void play(Artist selectedItem, int index) {
        getSqueezeService().getPlayer().sendCommand(
                "playlist loadtracks contributor.id=" + selectedItem.getId());
    }

    @Override
    protected BrowseLoadResult<Artist> loadItems(int startIndex, int count)
            throws IOException {
        return getMusicBrowser().getArtists(getQueryString(), mGenreId, startIndex, count);
    }
}
