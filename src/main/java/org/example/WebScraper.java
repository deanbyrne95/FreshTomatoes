package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Scanner;

public class WebScraper {
    protected final static String URL = "https://www.rottentomatoes.com";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String type, name;
        do {
            System.out.print("Please name a category (Movie/TV/Celebrity): ");
            type = scan.nextLine();
            System.out.print("\n");
        } while (!isValidType(type));

        do {
            System.out.printf("Please name a %s: ", type.toLowerCase());
            name = scan.nextLine();
            name = name.replace(' ', '_');
        } while (name.isBlank() || name.isEmpty());


        // Here we create a document object and use JSoup to fetch the website
        switch (type.toLowerCase()) {
            case "movie":
                getMovieDetails(type, name);
                break;
            case "tv":
                getTvDetails(type, name);
                break;
            case "celebrity":
                getCelebrityDetails(type, name);
                break;
            default:
                System.out.printf("No such type: %s\n", type);
        }
    }

    private static boolean isValidType(String type) {
        if (type == null) {
            return false;
        } else if (type.isEmpty() || type.isBlank()) {
            return false;
        } else if (type.equalsIgnoreCase("Movie")) {
            return true;
        } else if (type.equalsIgnoreCase("TV")) {
            return true;
        } else return type.equalsIgnoreCase("Celebrity");
    }

    private static void getMovieDetails(String url) {
        try {
            Document doc = Jsoup.connect(URL.concat(url)).get();
            System.out.printf("Audience Score: %s%s\n", doc.select(".scoreboard").attr("audiencescore"), "%");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getMovieDetails(String type, String name) {
        String url = String.format("/%s/%s", getMediaType(type), name);
        getMovieDetails(url);
    }

    private static void getTvDetails(String url) {
        try {
            Document doc = Jsoup.connect(URL.concat(url)).get();
            System.out.printf("Audience Score: %s\n", doc.select(".audience-score .mop-ratings-wrap__percentage").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getTvDetails(String type, String name) {
        String url = String.format("/%s/%s", getMediaType(type), name);
        getTvDetails(url);
    }

    private static void getCelebrityDetails(String url) {
        try {
            Document doc = Jsoup.connect(URL.concat(url)).get();
            String highestRatedName = doc.select(".celebrity-bio__item[data-qa*=\"highest-rated\"] .label a").text().trim();
            System.out.printf("Highest Rated Movie: %s\n", highestRatedName);
            getMovieDetails(doc.select(".celebrity-bio__item[data-qa*=\"highest-rated\"] .label a").attr("href"));
            String lowestRatedName = doc.select(".celebrity-bio__item[data-qa*=\"lowest-rated\"] .label a").text().trim();
            System.out.printf("Lowest Rated Movie: %s\n", lowestRatedName);
            getMovieDetails(doc.select(".celebrity-bio__item[data-qa*=\"lowest-rated\"] .label a").attr("href"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getCelebrityDetails(String type, String name) {
        String url = String.format("/%s/%s", getMediaType(type), name);
        getCelebrityDetails(url);
    }

    private static String getMediaType(String type) {
        return type.equalsIgnoreCase("Movie") ? "m" : type.equalsIgnoreCase("TV") ? "tv" : "celebrity";
    }
}
