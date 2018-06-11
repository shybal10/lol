package com.dairam.android.fragments.activity.appconstants;

import android.app.Activity;



public class AppConstants {
	
	public static final String GCM_REG_URL = "http://dairam.com/push/android.php?dev_id=";
	/*
	 * Home page JSON details
	 */
	//public static String HOME_PAGE_URL 			    = 			 "http://dairam.com/index.php?route=api/home/list&language=";
	public static String HOME_PAGE_URL 			    	= 			 "http://dairam.com/index.php?route=api/homeupdated/list&language=";
	
	public static String HOME_PAGE_JSON_OBJECT	    =			 "banners";
	public static String HOME_PAGE_TAG_TITLE  	    =			 "title";
	public static String HOME_PAGE_TAG_LINK		    =			 "href";
	public static String HOME_PAGE_TAG_IMAGE	    =			 "image";

	/* 
	 * Landing screen sub item
	 */
	public static String HOME_SUB_JSON_OBJECT   	=  			 "products";
	public static String HOME_SUB_ID				=			 "id";
	public static String HOME_SUB_NAME    	   		=			 "name";
	public static String HOME_SUB_PRICE		    	=			 "price";
	public static String HOME_SUB_SPECIAL	    	=			 "special";
	public static String HOME_SUB_URL           	= 		     "details url";
	public static String HOME_SUB_IMAGE		    	=			 "image";
	public static String HOME_SUB_LANG    	    	=			 "language";
	public static String HOME_SUB_CURRENCY	    	=			 "currency";
	public static String HOME_SUB_OUT_OF_STOCK_FLAG =			 "quantity";
	
    /*
     * category details
     */
	public static String CATEGORY_URL 			    = 			 "http://dairam.com/index.php?route=api/category/list&language=";
    public static String CATEGORY_JSON_OBJECT       = 			 "categories";
    public static String CATEGORY_ID			    =			 "category_id";
    public static String CATEGORY_PARENT_ID			=			 "parent_id";
    public static String CATEGORY_NAME				=			 "name";
    public static String CATEGORY_IMAGE				=			 "image";
    public static String CATEGORY_HREF				=			 "href";
    public static String CATEGORY_PARAPERENT 		=			 "para_parent";
    public static String CATEGORY_lANGUAGE			=			 "para_lang";
    public static String CATEGORY_CURRENCY			=			 "para_currency";
    public static String CATEGORY_CAT				=			 "categories";
    
    /*
     * sub category variables 
     */
    public static String SUB_CATEGORY_JSON_OBJECT   = 			 "categories";
    public static String SUB_CATEGORY_ID			=			 "category_id";
    public static String SUB_CATEGORY_PARENT_ID		=			 "parent_id";
    public static String SUB_CATEGORY_NAME			=			 "name";
    public static String SUB_CATEGORY_IMAGE			=			 "image";
    public static String SUB_CATEGORY_HREF			=			 "href";
    public static String SUB_CATEGORY_PARAPERENT 	=			 "para_category";
    public static String SUB_CATEGORY_lANGUAGE		=			 "language";
    public static String SUB_CATEGORY_CURRENCY		=			 "para_currency";
    public static String SUB_CATEGORY_CAT			=			 "categories";
    
    /*
     * Sub category single item
     */
    public static String SUB_CATEGORY1_JSON_OBJECT   = 			 "products";
    public static String SUB_CATEGORY1_ID			 =			 "product_id";
    public static String SUB_CATEGORY1_NAME		     =			 "name";
    public static String SUB_CATEGORY1_PRICE		 =			 "price";
    public static String SUB_CATEGORY1_SPECIAL		 =			 "special";
    public static String SUB_CATEGORY1_IMAGE		 =			 "thumb";
    public static String SUB_CATEGORY1_HREF			 =			 "details url";
    public static String SUB_CATEGORY_THUMB         =	          "image";
    public static String SUB_CATEGORY1_ID1			 =			 "id";
    
    /* String single product parsing */
    public static String SINGLE_PRODUCT_URL           =    		  "http://dairam.com/index.php?route=api/product/get&id=42&language=1&currency=KWD";
    public static String SINGLE_PRODUCT_JSON_OBJECT   = 		  "product";
    public static String SINGLE_PRODUCT_ID			  =			  "id";
    public static String SINGLE_PRODUCT_NAME		  =			  "name";
    public static String SINGLE_PRODUCT_IMAGE         =			  "image";
    public static String SINGLE_PRODUCT_DESC		  =			  "description";
    public static String SINGLE_PRODUCT_SUB_JSON_OBJECT   =       "related products";
    public static String SINGLE_PRODUCT_SUB_ID            =       "product_id";
    public static String SINGLE_PRODUCT_SUB_IMAGE         =       "thumb";
    public static String SINGLE_PRODUCT_SUB_REF           =       "href";
    public static String SINGLE_PRODUCT_SUB_PRICE		  =       "price";
    
    /* Brand listing */
    public static String BRANDLISTING_URL			 =				"http://dairam.com/index.php?route=api/brandlisting/list&language=";
    public static String BRANDLISTING_JSON_OBJECT    =				"manufacturer";
    public static String BRANDLISTING_ID			 =				"id";
    public static String BRANDLISTING_NAME			 =				"name";
    public static String BRANDLISTING_IMAGE			 =				"image";
    public static String BRANDLISTING_HREF			 =				"href";
    
    /*
     * Styles 
     */
    public static String STYLE_URL			 		 =			  	"http://dairam.com/index.php?route=api/style/list&language=";
    public static String STYLE_JSON_OBJECT   		 =				"style";
    public static String STYLE_ID					 =				"id";
    public static String STYLE_TITLE				 =				"title";
    public static String STYLE_DESCRIPTION           =              "description";
    public static String STYLE_IMAGE				 =				"image";
    public static String STYLE_HREF					 =				"href";
    
	/*
	 * Terms and conditions
	 */
    public static String TERMS_CONDTN_URL			 =				"http://dairam.com/index.php?route=api/information/get&language=";
    public static String TERMS_CONDTN_JSON_OBJECT	 =				"information";
    public static String TERMS_CONDTN_TITLE			 =				"title";
    public static String TERMS_CONDTN_DESC			 =				"description";
    
    /*
     * Shipping policy
     */
    public static String SHIPPING_URL				 =				"http://dairam.com/index.php?route=api/information/get&language=";
    public static String SHIPPING_JSON_OBJECT		 =				"information";
    public static String SHIPPING_TITLE			 	 =				"title";
    public static String SHIPPING_DESC			 	 =				"description";
    /*
     *  Sale
     */
    public static String SALE_URL       			 =			   "http://dairam.com/index.php?route=api/saleslist/list&language=1&currency=KWD";
    public static String SALE_URL_AR       			 =			   "http://dairam.com/index.php?route=api/saleslist/list&language=2&currency=KWD";
    public static String SALE_JSONOBJECT       		 =             "banners";
    public static String SALE_IMAGE					 =			   "image";
    public static String SALE_TITLE					 =		       "title";
    public static String SALE_HREF					 =			   "href";
    
    /*
     * Fetch country from ip.....
     */
    public static String IP_FETCHING_URL             =			   "http://ip-api.com/json";
    public static String IP_FETCHING_COUNTRY		 =			   "country";
    
    /*
     * Register
     */
    public static String REGISTER_NEW_USER			 =				"http://dairam.com/index.php?route=api/register/get&language=";
    public static String REGISTER_URL 				 =				"http://dairam.com/index.php?route=api/register/post&language=1";
    
    /*
     * Login
     */
    public static String LOGIN_URLdummy					 =				"http://dairam.com/index.php?route=api/login/get&email=jaysonmk@yahoo.com&password=1q2w3e4r&language=1&currency=KWD";
    
    
    /*
     * Single item brand Listing
     */
    public static String BRAND_SINGLE_JSONOBJECT       		 =             "products";
    public static String BRAND_SINGLE_PRODUCT_ID      		 =             "id";
    public static String BRAND_SINGLE_IMAGE       		     =             "image";
    public static String BRAND_SINGLE_GENRE       		     =             "genre";
    public static String BRAND_SINGLE_NAME           		 =             "name";
    public static String BRAND_SINGLE_PRICE         		 =             "price";
    public static String BRAND_SINGLE_SPECIAL       		 =             "special";
    public static String BRAND_SINGLE_LINK					 = 			   "details url";
    
    /*
     * Sale Single Item
     */
    public static String SALES_SINGLE_JSONOBJECT       		 =             "products";
    public static String SALES_SINGLE_PRODUCT_ID      		 =             "id";
    public static String SALES_SINGLE_IMAGE       		     =             "image";
    public static String SALES_SINGLE_GENRE       		     =             "genre";
    public static String SALES_SINGLE_NAME           		 =             "name";
    public static String SALES_SINGLE_PRICE         		 =             "price";
    public static String SALES_SINGLE_SPECIAL       		 =             "special";
    public static String SALES_SINGLE_LINK					 = 			   "details url";
    
    
    /*
     * Cart Item  
     */
    public static String CART_URL							=			  "http://dairam.com/index.php?route=api/cart/get&cart=";
    
    public static String GOOGLE_LOGIN_URL					=			  "http://dairam.com/index.php?route=api/glogin/get&email=";
    
    
    /*
     * Currencies
     */
    public static String CURRENCIES_URL						=			  "http://dairam.com/index.php?route=api/currency/list&language=";
    public static String CURRENCIES_JSON_OBJ				=			  "currencies";
    public static String CURRENCIES_TITLE					=			  "title";
    public static String CURRENCIES_CODE					=			  "code";
    public static String CURRENCIES_NAME					=			  "name";
    public static String CURRENCIES_SYMLEFT					=			  "symbol_left";
    public static String CURRENCIES_SYMRYT					=			  "symbol_right";
    public static String CART_IT   							= 			   "cart";
    public static String WISHLIST_IT 						= 			   "wishlist";
    
    
    /*
     * Add To Cart
     */
    public static String CART_JSON_OBJ				        =			  "customer";
    public static String CART_ID				            =			  "id";
    public static String CART_COUNT							=			  "cart";
    public static String CART_FNAME							=			  "firstname";
    public static String CART_LNAME							=			  "lastname";
    public static String CART_EMAIL						    =             "email";
    public static String WISHLIST_COUNT 					=			  "wishlist";
    
    /*
     * Cart item listing...
     */
    public static String CART_ITEM_LIST_JSON_OBJ			=			  "products";
    public static String CART_ITEM_LIST_ID				    =			  "id";
    public static String CART_ITEM_LIST_NAME				=			  "name";
    public static String CART_ITEM_LIST_PRICE				=			  "price";
    public static String CART_ITEM_LIST_TOTAL				=			  "total";
    public static String CART_ITEM_LIST_REF				    =			  "href";
    public static String CART_ITEM_LIST_QUANTITY			=			  "quantity";
    public static String CART_ITEM_LIST_IMAGE				=			  "thumb";
    
    
    /*
     * Customer Care feed back 
     */
    public static String CUSTOMER_CARE_CONTACT_MAIL         =              "prasanth@webna.in";
    
     
    public static String FORGET_PASSWORD_URL  				=		    	"http://dairam.com/index.php?route=api/forgotpass/get&email=";
    
    
    public static String MY_ORDER_LIST_JSON_URL			    =			  "http://dairam.com/index.php?route=api/order/get&id=";
    public static String MY_ORDER_LIST_JSON_OBJ				=			  "order";
    public static String MY_ORDER_LIST_order_id				=			  "order_id";
    public static String MY_ORDER_LIST_status				=			  "status";
    public static String MY_ORDER_LIST_date_added			=			  "date_added";  
    public static String MY_ORDER_LIST_total				=			  "total";  
    
    
    public static String DEVICE_ID							=			null;
    public static String gcm_id								=			null;
    
    public static String LOGOUT_URL 						=          "http://dairam.com/index.php?route=api/logout/get&id=";
    
    //public static String MY_ORDER_ADDRESS_POST_JSON_URL		=		   "http://demo.dairam.com/index.php?route=api/address/post/format/json";
    public static String MY_ORDER_ADDRESS_POST_JSON_URL		=		   "http://dairam.com/index.php?route=api/address/post/format/json";

    public static String ADD_TO_CART_URL   					=			"http://dairam.com/index.php?route=api/cart/get&cart=";
    
    public static String WISHLIST_TO_CART_URL   			=			"http://dairam.com/index.php?route=api/cart/wishlisttocart&wishlist=";
    
    public static String UPDATE_CART_QTY_URL   				=			"http://dairam.com/index.php?route=api/cart/update&cart=";

    public static String SUB_CATEGORY_URL			        =			"http://dairam.com/index.php?route=api/subcategory/list&parent=";
   
    public static String LOGIN_URL							=			"http://dairam.com/index.php?route=api/login/get&email=";
    
    public static String CUSTOMER_CARE_URL					=			"";
    
    public static String EDIT_USER_ACCOUNT_URL				=			"http://dairam.com/index.php?route=api/account/get&firstname=";
    
    public static String PICK_ADDRESSBOOOK_URL				=			"http://dairam.com/index.php?route=api/address/list&id=";
    //public static String PICK_ADDRESSBOOOK_URL				=			"http://demo.dairam.com/index.php?route=api/address/list&id=";
    
    public static String PICK_COUNTRIES_URL					=			"http://dairam.com/index.php?route=api/country/list&language=";
    
    public static String UPDATE_ADDRESS_URL					=			"http://dairam.com/index.php?route=api/address/update/format/json";
    
    public static String UPLOAD_CONTACTUS_URL				=			"http://dairam.com/index.php?route=api/contact/post/format/json";
        
    public static String ZONE_URL							=			"http://dairam.com/index.php?route=api/zone/get&country_code=";
    
    //public static String ORDER_CONFIRM_KNET					=			"http://dairam.com/index.php?route=api/knetconfirm/get&id=";
    
    public static String ORDER_CONFIRM_KNET					=			"http://dairam.com/index.php?route=api/knetandroid/get&id=";
    
    public static String ORDER_CONFIRM_VISA					=			"http://dairam.com/index.php?route=api/visaconfirm/get&id=";
    
    public static String ORDER_CONFIRM_PAYPAL				=			"http://dairam.com/index.php?route=api/paypal/get&id=";
    
    public static String ORDER_CONFIRM_CASHU				=			"http://dairam.com/index.php?route=api/cashuconfirm/get&id=";
    
    public static String STYLE_DETAILS						=			"http://dairam.com/index.php?route=api/style/get&id=";
    
    //public static String POST_KNET_PAYMENT_RESULT			=			"http://dairam.com/index.php?route=api/knetconfirm/success&PaymentID=";
    
    public static String POST_KNET_PAYMENT_RESULT			=			"http://dairam.com/index.php?route=api/knetsuccess/success&PaymentID=";
    
    public static String POST_VISA_PAYMENT_RESULT			=			"http://dairam.com/index.php?route=api/visaconfirm/success&PaymentID=";
    
    public static String POST_PAYPAL_PAYMENT_RESULT			=			"http://dairam.com/index.php?route=api/paypal/success&id=";
  
    public static String Add_Device_Token = "http://dairamnotification.mawaqaademo.com/services/API.svc/insert_push_device_token";
	public static String device_Token = "push_device_token";
	public static String platform_Token = "push_platform";
	
	//To Login bundle args keys
	
	public static String FROM_PAGE = "page";
	public static String BANNER_TITLE = "bannertitle";
	public static String RETURN_URL = "returnurl";
	public static String PRODUCT_ID = "prod_id";
	public static String OPERATION = "operation";
	
	public static String BACKSELECTOR =	"backselecter";
}