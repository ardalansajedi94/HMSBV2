package net.hotelairport.androidapp.Server;

import java.util.ArrayList;
import java.util.Map;

import net.hotelairport.androidapp.Models.Basket;
import net.hotelairport.androidapp.Models.BlogContent;
import net.hotelairport.androidapp.Models.CafeMenuItem;
import net.hotelairport.androidapp.Models.CafeRestaurant;
import net.hotelairport.androidapp.Models.Category;
import net.hotelairport.androidapp.Models.HotelSetting;
import net.hotelairport.androidapp.Models.Invoice;
import net.hotelairport.androidapp.Models.Language;
import net.hotelairport.androidapp.Models.LanguageKey;
import net.hotelairport.androidapp.Models.Position;
import net.hotelairport.androidapp.Models.Profile;
import net.hotelairport.androidapp.Models.RestaurantMenuItem;
import net.hotelairport.androidapp.Models.Service;
import net.hotelairport.androidapp.Models.TimeLine;
import net.hotelairport.androidapp.Models.Translation;
import net.hotelairport.androidapp.Models.WelcomeTabsItem;

/**
 * Created by Mohammad on 8/30/2017.
 */

public class ServerResponse {
    private BlogContent help;
    private ArrayList<BlogContent> helps;
    private String message, jwt;
    private ArrayList<BlogContent> Informations;
    private ArrayList<BlogContent> news;
    private BlogContent the_news;
    private BlogContent information;
    private Profile profile;
    private ArrayList<TimeLine> timeLine;
    private ArrayList<CafeRestaurant>restaurants;
    private ArrayList<CafeMenuItem>cafe_menu;
    private ArrayList<RestaurantMenuItem>restaurant_menu;
    private ArrayList<Invoice>invoices;
    private CafeMenuItem cafe_item;
    private RestaurantMenuItem restaurant_item;
    private ArrayList<Basket> your_basket;
    private ArrayList<Category>categories;
    private ArrayList<Language> languages;
    private ArrayList<LanguageKey> language_keys;
    private ArrayList<Translation> translations;
    private Position hotel_position;
    private ArrayList<HotelSetting> settings;
    private ArrayList<WelcomeTabsItem>slides;
    private ArrayList<Map<String,Service>>services;
    private ArrayList<CafeRestaurant> cafes;

    public ArrayList<WelcomeTabsItem> getSlides() {
        return slides;
    }

    public ArrayList<HotelSetting> getSettings() {
        return settings;
    }

    public Position getHotel_position() {
        return hotel_position;
    }

    public ArrayList<LanguageKey> getLanguage_keys() {
        return language_keys;
    }

    public ArrayList<Translation> getTranslations() {
        return translations;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public BlogContent getHelp() {
        return help;
    }

    public ArrayList<BlogContent> getHelps() {
        return helps;
    }

    public ArrayList<Basket> getYour_basket() {
        return your_basket;
    }

    public CafeMenuItem getCafe_item() {
        return cafe_item;
    }

    public RestaurantMenuItem getRestaurant_item() {
        return restaurant_item;
    }

    public ArrayList<TimeLine> getTimeLine() {
        return timeLine;
    }

    public ArrayList<BlogContent> getNews() {
        return news;
    }

    public BlogContent getThe_news() {
        return the_news;
    }

    public Profile getProfile() {
        return profile;
    }

    public BlogContent getInformation() {
        return information;
    }

    public String getMessage() {
        return message;
    }

    public String getJwt() {
        return jwt;
    }

    public ArrayList<BlogContent> getInformations() {
        return Informations;
    }

    public ArrayList<CafeRestaurant> getRestaurants() {
        return restaurants;
    }

    public ArrayList<CafeMenuItem> getCafe_menu() {
        return cafe_menu;
    }

    public ArrayList<RestaurantMenuItem> getRestaurant_menu() {
        return restaurant_menu;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public ArrayList<CafeRestaurant> getCafes() {
        return cafes;
    }
}
