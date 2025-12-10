package uz.pdp.kiyim_online_dokon.bot.session;

public enum UserState {
    MAIN_MENU,              // Asosiy menyu
    SELECTING_CATEGORY,     // Kategoriya tanlash
    SELECTING_PRODUCT,      // Mahsulot tanlash
    VIEWING_CART,           // Savatni ko'rish
    SEARCHING,              // Qidiruv holatida
    SEARCHING_RESULTS,      // Qidiruv natijalari
    CONFIRMING_ORDER,       // Buyurtmani tasdiqlash
    VIEWING_ORDERS          // Buyurtmalarni ko'rish
}