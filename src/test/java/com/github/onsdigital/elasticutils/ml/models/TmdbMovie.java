package com.github.onsdigital.elasticutils.ml.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.client.http.response.sltr.models.SltrDocument;

import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class TmdbMovie extends SltrDocument {

    private int id;
    private String title;
    private boolean video;
    private String mlensId;
    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    private String tagline;
    private List<Director> directors;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("belongs_to_collection")
    private Map<String, Object> belongsToCollection;
    private int runtime;
    private float popularity;
    private String status;
    @JsonProperty("original_language")
    private String originalLanguage;
    private List<CastMember> cast;
    private long budget;
    @JsonProperty("production_countries")
    private List<Country> productionCountries;
    private List<Genre> genres;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("spoken_languages")
    private List<Language> spokenLanguages;
    @JsonProperty("original_title")
    private String originalTitle;
    private String overview;
    private boolean adult;
    @JsonProperty("poster_path")
    private String poster_path;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;
    @JsonProperty("imdb_id")
    private String imdbId;
    private long revenue;
    private String homepage;
    @JsonProperty("title_sent")
    private String titleSent;

    private TmdbMovie() {
        // For Jackson
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public String getMlensId() {
        return mlensId;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getTagline() {
        return tagline;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Map<String, Object> getBelongsToCollection() {
        return belongsToCollection;
    }

    public int getRuntime() {
        return runtime;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getStatus() {
        return status;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public List<CastMember> getCast() {
        return cast;
    }

    public long getBudget() {
        return budget;
    }

    public List<Country> getProductionCountries() {
        return productionCountries;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public List<Language> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public String getImdbId() {
        return imdbId;
    }

    public long getRevenue() {
        return revenue;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getTitleSent() {
        return titleSent;
    }
}
