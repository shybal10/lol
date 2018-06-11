package com.dairam.android.fragments.activity.interfaces;

public interface listenerHideView {
	
	void hide();
	void showTopBar(String titleString);
	void showTab();
	void showcategory(String string);
	void defaultHeader();
	void setTopBarForCategory();
	void makeDefaultHeader();
	void setTopBarForBrands();
	void setTopBarForStyle();
	void setTopBarForShop();
	
	void showSortBy();
	void hideBottomBar();
	void showBottomBar();
	void promoItemHideTopbar();
	void reversePromoItemHideTopbar();
	
	
	void ShopmenuRed();
	void shopmenublack();
	
	
	void wishListCountUpdation(String countString);
	void cartListCountUpdation(String countString);
	
	void setTopBarForCart();
	void setTopBarForWishlist();
	
	void search();
	
	
	void dairamBar();
	
	void hidePopup();
	
	void hideSearchPopup();
	
	void loginlogout();
	
	void closeDrawer();
	
}
