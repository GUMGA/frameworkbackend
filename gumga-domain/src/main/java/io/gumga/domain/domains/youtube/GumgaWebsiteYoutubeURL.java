package io.gumga.domain.domains.youtube;

import com.google.common.base.Objects;
import io.gumga.domain.domains.GumgaYoutubeURL;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Tipo de dado para videos do Youtube
 */
public class GumgaWebsiteYoutubeURL implements GumgaYoutubeURL {

    private static final List<String> ACCEPTED_HOSTS = Arrays.asList("www.youtube.com");

    private URL url;

    public GumgaWebsiteYoutubeURL(URL url) {
        checkArgument(accept(url), "A URL deve pertencer ao formato do Youtube.");
        this.url = url;
    }

    public static Optional<String> getVideoIdFromUrl(URL url) {
        try {
            String[] queryParts = url.getQuery().split("&");
            for (String param : queryParts) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                if (key.equals("v")) {
                    return Optional.of(URLDecoder.decode(pair[1], "UTF-8"));
                }
            }

            return Optional.empty();

        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Não foi possivel decodificar a URL do video", e);
        }
    }

    @Override
    public String getVideoId() {
        return getVideoIdFromUrl(this.url).get();
    }

    public static boolean accept(URL url) {
        return (isFromAcceptedHost(url) && getVideoIdFromUrl(url).isPresent());
    }

    private static boolean isFromAcceptedHost(URL url) {
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

        GumgaWebsiteYoutubeURL that = (GumgaWebsiteYoutubeURL) o;

        return Objects.equal(this.url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
