package com.hulaa.backend.agoda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Deny Prasetyo,S.T
 * Java(Script) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 * @jasoet
 */

public class AgodaScrapper {

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("http://www.agoda.com/hotel-tentrem-yogyakarta/hotel/yogyakarta-id.html").get();
        Elements title = doc.select("#ctl00_ctl00_MainContent_ContentMain_HotelHeaderHD_lblHotelName");
        Elements address = doc.select("p.sblueboldunder");
        Elements loc = doc.select("#ctl00_ctl00_MainContent_ContentMain_HotelHeaderHD_lnkPopGoogleMap");
        Elements description = doc.select("#hotelDescription");
        Elements pictures = doc.select("#thumbs>table>tbody>tr img");
        Elements usefulInfos = doc.select("#ctl00_ctl00_MainContent_ContentMain_HotelInformation1_pnlUsefulInfo div.pspacer");
        Elements facilities = doc.select("#tbl_fac>tbody>tr");
        Elements totalScore = doc.select("#ctl00_ctl00_MainContent_ContentMain_HotelReview1_lblTotalScore");

        System.out.println("Name : " + title.text());
        System.out.println("Latitude : " + splitQuery(loc.attr("href")).get("latitude"));
        System.out.println("Longitude : " + splitQuery(loc.attr("href")).get("longitude"));
        System.out.println("Description : " + description.text());
        System.out.println("Address : " + address.text().substring(0, address.text().indexOf('(')));
        System.out.println("Total Score : " + totalScore.text());

        pictures.forEach(e -> {
            System.out.println("Image " + e.attr("alt") + " - " + e.attr("src").replace("_TMB", ""));
        });

        facilities.forEach(f -> {
            System.out.print(f.select("td").get(0).text() + " => ");
            f.select("td").get(1).select("p").forEach(p -> {
                System.out.print(p.text() + ", ");
            });
            System.out.println();
        });

        System.out.println("Usefull Info ====================");
        usefulInfos.forEach(e -> {
            System.out.println(e.select(".useful_left").text().trim() + " - " + e.select(".floatleft").text().trim());
        });
    }

    //Simple and Fast
    public static Map<String, List<String>> splitQuery(String url) throws UnsupportedEncodingException {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return new HashMap<>();
        }
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = uri.getQuery().split("&");
        for (String pair : pairs) {
            if (pair.trim().length() == 0) {
                continue;
            }
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!queryPairs.containsKey(key)) {
                queryPairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            queryPairs.get(key).add(value);
        }
        return queryPairs;
    }

    //Not Simple and Not So Fast
    public static Map<String, List<String>> extractQuery(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return new HashMap<>();
        }

        final List<String> pairs = Arrays.asList(uri.getQuery().split("&"));
        return pairs.stream().map(pair -> {
            try {
                if (pair.trim().length() == 0) {
                    return null;
                }
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                final String value = idx > 0 && pair.length() > (idx + 1) ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;

                Map<String, String> r = new HashMap<>();
                r.put(key, value);

                return r;
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }).collect(new MapToListCollector<>());
    }
}
