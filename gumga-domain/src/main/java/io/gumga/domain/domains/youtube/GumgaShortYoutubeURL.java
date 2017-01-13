package io.gumga.domain.domains.youtube;

import com.google.common.base.Objects;
import io.gumga.domain.domains.GumgaYoutubeURL;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Tipo de dado para videos do Youtube
 */
public class GumgaShortYoutubeURL implements GumgaYoutubeURL {

    private static final List<String> ACCEPTED_HOSTS = Arrays.asList("youtu.be");

    private URL url;

    public GumgaShortYoutubeURL(URL url) {
        checkArgument(accept(url), "A URL deve pertencer ao formato abreviado do Youtube.");
        this.url = url;
    }

    @Override
    public String getVideoId() {
        return this.url.getPath().substring(1);
    }

    public static boolean accept(URL url) {
        return ACCEPTED_HOSTS.indexOf(url.getHost()) >= 0;
    }

    @Override
    public String toString() {
        return this.url.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GumgaShortYoutubeURL that = (GumgaShortYoutubeURL) o;

        return Objects.equal(this.url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
